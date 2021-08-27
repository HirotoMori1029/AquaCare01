package com.websarva.wings.android.aquacare01.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.aquacare01.Alarm
import com.websarva.wings.android.aquacare01.AlarmNotification
import com.websarva.wings.android.aquacare01.AlarmViewAdapter
import com.websarva.wings.android.aquacare01.R

class NotificationFragment : Fragment() {
//    表示最大数
    var nfMaxNum = 5
//    sharePreferencesにあるalarmデータの数
    private var listCountNum = 0
//    保存するkeyの配列
    val alarmStrKeys = arrayOf(
        "taskName",
        "taskDateNext",
        "taskTimeNext",
        "taskDatePrev",
        "taskTimePrev",
        "taskRepeat"
    )
    private val alarmStrKeysSize = alarmStrKeys.size
    val alarmBooleanKey = "taskState"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        val lvAlarm = view.findViewById<RecyclerView>(R.id.lvAlarm)
        val linearLayoutManager = LinearLayoutManager(view.context)
        //    sharedPreferencesを用意
        val sharedPreferences = requireActivity().getSharedPreferences("savedTaskInAquariumCare", Context.MODE_PRIVATE)

        listCountNum = listCount(sharedPreferences)
        val alarmList = mutableListOf<Alarm>()
        createAlarmList(listCountNum, alarmList, sharedPreferences)

        //RecyclerViewにAdapterとLayoutManagerを設定
        lvAlarm.adapter = AlarmViewAdapter(alarmList)
        lvAlarm.layoutManager = linearLayoutManager
        lvAlarm.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        return view
    }


    //    リストデータの個数を数える関数
    private fun listCount(sp: SharedPreferences): Int {

        var rtNum = 0
        for (j in 0..nfMaxNum) {
            //todo 配列の中全てをチェックすべきか？
            val str = sp.getString(alarmStrKeys[0]+j, "NoData")
            if (str != "NoData") {
                rtNum = j
            } else {
                break
            }
        }

        return rtNum
    }

    //    listを生成する関数
    private fun createAlarmList(listCountNum: Int, alarmList: MutableList<Alarm>, sp: SharedPreferences) {
        for (i in 0..listCountNum) {

            val alarmRowViews = arrayOfNulls<String?>(alarmStrKeysSize)
            for (j in 0 until alarmStrKeysSize) {
                alarmRowViews[j] = sp.getString(alarmStrKeys[j]+i,"NoData")
            }


            if (!alarmRowViews.contains(null)) {
                alarmList.add(Alarm(alarmRowViews[0], alarmRowViews[1], alarmRowViews[2], alarmRowViews[3], alarmRowViews[4], alarmRowViews[5]))
            }
        }
    }


//    override fun onClick(position: Int) {
//        val alarmMgr = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, AlarmNotification::class.java)
//        val pIntent = PendingIntent.getBroadcast(context, position, intent, 0)
//        alarmMgr.cancel(pIntent)
//        val sp = requireContext().getSharedPreferences("savedTaskInAquariumCare", Context.MODE_PRIVATE)
//        for (k in 0 until alarmStrKeysSize) {
//            sp.edit().remove(alarmStrKeys[k] + position).apply()
//        }
//        val tr = parentFragmentManager.beginTransaction()
//        tr.replace(R.id.fragmentContainerView, NotificationFragment())
//        tr.commit()
//        Toast.makeText(context, "alarm ID$position was deleted", Toast.LENGTH_SHORT).show()
//
//    }

}