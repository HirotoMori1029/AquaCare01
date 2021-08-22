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

class NotificationFragment : Fragment(), AlarmViewAdapter.OnItemClickListener {
//    表示最大数
    private var nfMaxNum = 10
//    sharePreferencesにあるalarmデータの数
    private var listCountNum = 1

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
        lvAlarm.adapter = AlarmViewAdapter(alarmList, this)
        lvAlarm.layoutManager = linearLayoutManager
        lvAlarm.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        return view
    }

    override fun onItemClick(position: Int) {
        val alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, AlarmNotification::class.java)
        val pIntent = PendingIntent.getService(context, 1, intent, 0)
        pIntent.cancel()
        alarmMgr?.cancel(pIntent)
        Toast.makeText(context, "$position was clicked and requestCode1 was deleted", Toast.LENGTH_SHORT).show()
    }

//    リストデータの個数を数える関数
    private fun listCount(sp: SharedPreferences): Int {

        var rtNum = 1
        for (j in 1..nfMaxNum) {
            val str = sp.getString("taskNameKey$j", "NoData")
            if (str != "NoData") {
                rtNum = j
            } else {
                break
            }
        }
        if (rtNum != 1) {
            rtNum--
        }
        return rtNum
    }

//    listを生成する関数
    private fun createAlarmList(listCountNum: Int, alarmList: MutableList<Alarm>, sp: SharedPreferences) {

        for (i in 1..listCountNum) {
            val taskName = sp.getString("taskNameKey$i", "NoData")
            val taskDate = sp.getString("taskDateKey$i", "NoData")
            val taskTime = sp.getString("taskTimeKey$i", "NoData")

//        nullでないならlistに情報を追加
            if ((taskName != null) && (taskDate != null) && (taskTime != null)) {
                alarmList.add(Alarm(taskName, taskDate, taskTime))
            }
        }
    }

}