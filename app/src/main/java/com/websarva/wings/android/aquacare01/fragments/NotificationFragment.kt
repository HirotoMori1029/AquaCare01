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
        val alarmList = mutableListOf<Alarm>()
        createAlarmList(nfMaxNum, alarmList, sharedPreferences)

        //RecyclerViewにAdapterとLayoutManagerを設定
        val listener = object :AlarmViewAdapter.Listener {
            override fun onClickText(index: Int) {
                    val alarmMgr = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, AlarmNotification::class.java)
                    val pIntent = PendingIntent.getBroadcast(context, index, intent, 0)
                    alarmMgr.cancel(pIntent)
                    pIntent.cancel()
                    val sp = requireContext().getSharedPreferences("savedTaskInAquariumCare", Context.MODE_PRIVATE)
                    for (k in 0 until alarmStrKeysSize) {
                        sp.edit().remove(alarmStrKeys[k] + index).apply()
                    }
                    sp.edit().remove(alarmBooleanKey + index).apply()
                    fragmentRefresh()
                    Toast.makeText(context, "alarm ID$index was deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onClickImage(index: Int) {
                val taskState = sharedPreferences.getBoolean(alarmBooleanKey + index, true)
                if (!taskState) {
                    sharedPreferences.edit().putBoolean(alarmBooleanKey + index, true).apply()
                    fragmentRefresh()
                    Toast.makeText(context, "taskState $index was changed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        lvAlarm.adapter = AlarmViewAdapter(alarmList, listener)
        lvAlarm.layoutManager = linearLayoutManager
        lvAlarm.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        return view
    }


    //    alarmListを生成する関数
    private fun createAlarmList(rowNumber: Int, alarmList: MutableList<Alarm>, sp: SharedPreferences) {
        for (i in 0..rowNumber) {

            val alarmRowViews = arrayOfNulls<String?>(alarmStrKeysSize)
            for (j in 0 until alarmStrKeysSize) {
                alarmRowViews[j] = sp.getString(alarmStrKeys[j]+i,"NoData")
            }
            val taskState = sp.getBoolean(alarmBooleanKey + i, true)
            if (!alarmRowViews.contains(null)) {
                alarmList.add(Alarm(alarmRowViews[0], alarmRowViews[1], alarmRowViews[2], alarmRowViews[3], alarmRowViews[4], alarmRowViews[5], taskState))
            }
        }
    }

    private fun fragmentRefresh () {
        val tr = parentFragmentManager.beginTransaction()
        tr.replace(R.id.fragmentContainerView, NotificationFragment())
        tr.commit()
    }

}