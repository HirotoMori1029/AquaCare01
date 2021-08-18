package com.websarva.wings.android.aquacare01.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.aquacare01.Alarm
import com.websarva.wings.android.aquacare01.AlarmViewAdapter
import com.websarva.wings.android.aquacare01.R

class NotificationFragment : Fragment() {

    private var nfMaxNum = 10
    private var listCountNum = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //        sharedPreferencesから情報を取得
        val sharedPreferences = requireActivity().getSharedPreferences("savedTaskInAquariumCare", Context.MODE_PRIVATE)
        //        生成するリストデータの個数を数える関数
        fun countListNum () {
            for (j in 1..nfMaxNum) {
                val str = sharedPreferences.getString("taskNameKey$listCountNum","NoData")
                if (str != "NoData") {
                    listCountNum++
                } else {
                    break
                }
            }
            if (listCountNum != 1)
            listCountNum--
        }

        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        val lvAlarm = view.findViewById<RecyclerView>(R.id.lvAlarm)
        val linearLayoutManager = LinearLayoutManager(view.context)

//        ▼テストデータの生成
        val alarmList = mutableListOf<Alarm>()
        countListNum()
        for (i in 1..listCountNum) {
            val taskName = sharedPreferences.getString("taskNameKey$i", "NoData")
            val taskDate = sharedPreferences.getString("taskDateKey$i", "NoData")
            val taskTime = sharedPreferences.getString("taskTimeKey$i", "NoData")

//        nullでないならlistに情報を追加
            if ((taskName != null) && (taskDate != null) && (taskTime != null)) {
                alarmList.add(Alarm(taskName, taskDate, taskTime))
            }
        }

        //RecyclerViewにAdapterとLayoutManagerを設定
        lvAlarm.adapter = AlarmViewAdapter(alarmList)
        lvAlarm.layoutManager = linearLayoutManager
        lvAlarm.addItemDecoration(DividerItemDecoration(view.context, linearLayoutManager.orientation))

        return view
    }

}