package com.websarva.wings.android.aquacare01.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class NotificationFragment : Fragment() {

    private var lvAlarm: RecyclerView? = null
    private val alarmList = mutableListOf<Alarm>()
//    表示最大数
    var nfMaxNum = 5
//    各種キーを設定
    val alarmTaskNameKey = "taskName"
    val alarmNextLongKey = "taskNext"
    val alarmPrevLongKey = "taskPrev"
    val alarmRepeatDaysKey = "taskRepeat"
    val alarmBooleanKey = "taskState"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo: モードを後で戻す
        val sharedPreferences = requireContext().getSharedPreferences("savedTaskInAquariumCare", Context.MODE_MULTI_PROCESS)

//        click時のListenerを設定
        val listener = object :AlarmViewAdapter.Listener {
            override fun onClickText(index: Int) {
                val alarmMgr =requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, AlarmNotification::class.java)
                val pIntent = PendingIntent.getBroadcast(context, index, intent, 0)
                alarmMgr.cancel(pIntent)
                pIntent.cancel()
                removeKeyData(sharedPreferences, alarmTaskNameKey + index)
                removeKeyData(sharedPreferences, alarmNextLongKey + index)
                removeKeyData(sharedPreferences, alarmPrevLongKey + index)
                removeKeyData(sharedPreferences, alarmRepeatDaysKey + index)
                removeKeyData(sharedPreferences, alarmBooleanKey + index)
                Toast.makeText(context, "alarm ID$index was deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onClickImage(index: Int) {
//                アイコンを変更する処理
                val taskState = sharedPreferences.getBoolean(alarmBooleanKey + index, true)
                if (!taskState) {
                    sharedPreferences.edit().putBoolean(alarmBooleanKey + index, true).apply()
                    Toast.makeText(context, "taskState $index was changed to true", Toast.LENGTH_SHORT).show()
                    //PrevTimeを変更する処理
                    val cal = Calendar.getInstance()
                    sharedPreferences.edit().putLong(alarmPrevLongKey + index, cal.time.time).apply()
                    //NextTimeを変更する処理

                    val rpDays = sharedPreferences.getInt(alarmRepeatDaysKey + index, 0)
                    if (rpDays != 0) {
                        val nDate = Date(sharedPreferences.getLong(alarmNextLongKey + index, 0))
                        cal.time = nDate
                        cal.add(Calendar.DATE, rpDays)
                        sharedPreferences.edit().putLong(alarmNextLongKey + index, cal.time.time).apply()
                    }
                }
            }
        }

//        RecyclerViewに設定をする
        lvAlarm = view.findViewById(R.id.lvAlarm)
        val linearLayoutManager = LinearLayoutManager(view.context)
        lvAlarm?.setHasFixedSize(true)
        lvAlarm?.adapter= AlarmViewAdapter(createAlarmList(sharedPreferences),listener)
        lvAlarm?.layoutManager = linearLayoutManager
        lvAlarm?.addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
    }

    //    alarmListを生成する関数
    private fun createAlarmList(sp: SharedPreferences) :MutableList<Alarm> {

        for (i in 0..nfMaxNum) {
            val name = sp.getString(alarmTaskNameKey + i, "NoData")
            val nextDateLong = sp.getLong(alarmNextLongKey + i, 0)
            val nextDate = getDataOrNoData(nextDateLong, "MM / dd")
            val nextTime = getDataOrNoData(nextDateLong, "HH : mm")
            val prevDateLong = sp.getLong(alarmPrevLongKey + i, 0)
            val prevDate = getDataOrNoData(prevDateLong, "MM / dd")
            val prevTime = getDataOrNoData(prevDateLong, "HH : mm")
            val repeatDays = sp.getInt(alarmRepeatDaysKey + i, 0)
            val repeatDaysStr = if (repeatDays == 0) {"NoRepeat"} else {"Repeat $repeatDays days"}
            val taskState = sp.getBoolean(alarmBooleanKey + i, false)
                alarmList.add(Alarm(name, nextDate, nextTime, prevDate, prevTime, repeatDaysStr, taskState))
        }

        return alarmList
    }

    private fun getDataOrNoData (date: Long, format: String) :String {
        return if (date != 0L) {SimpleDateFormat(format, Locale.getDefault()).format(Date(date))} else {"NoData"}
    }

    private fun removeKeyData (sp: SharedPreferences, key: String) {
        sp.edit().remove(key).apply()
    }

}