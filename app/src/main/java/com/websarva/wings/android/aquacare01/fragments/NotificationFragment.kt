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
import com.websarva.wings.android.aquacare01.*
import java.text.SimpleDateFormat
import java.util.*

//todo: sharedPreferences変更の可能性あり

class NotificationFragment : Fragment() {

    private var lvAlarm: RecyclerView? = null
    private var alarmList = mutableListOf<Alarm>()
    private val defaultValues = DefaultValues()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        RecyclerViewに設定をする
        lvAlarm = view.findViewById(R.id.lvAlarm)
        val sharedPreferences = requireContext().getSharedPreferences(defaultValues.taskSaveFileName, Context.MODE_MULTI_PROCESS)
        alarmList = createAlarmList(sharedPreferences)
        val adapter= AlarmViewAdapter(alarmList)

        //        click時のListenerを設定
        adapter.listener = object :AlarmViewAdapter.Listener {
            override fun onClickBtn(index: Int) {
                cancelPIntent(index)
                removeIndexData(sharedPreferences, index)
                adapter.deleteUpdate(index)
                Toast.makeText(context, "alarm ID$index was deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onClickImage(index: Int) {
//                アイコンを変更する処理
                val taskState = sharedPreferences.getBoolean(defaultValues.alarmBooleanKey + index, true)
                if (!taskState) {
                    sharedPreferences.edit().putBoolean(defaultValues.alarmBooleanKey + index, true).apply()
                    val rpDays = sharedPreferences.getInt(defaultValues.alarmRepeatDaysKey + index, 0)
                    if (rpDays != 0) {
                        //PrevTimeを変更する処理
                        val cal = Calendar.getInstance()
                        sharedPreferences.edit().putLong(defaultValues.alarmPrevLongKey + index, cal.time.time).apply()
                        val pDate = cal.time
                        val prevDateStr = SimpleDateFormat("MM / dd", Locale.getDefault()).format(pDate)
                        val prevTimeStr = SimpleDateFormat("HH : mm", Locale.getDefault()).format(pDate)
                        //NextTimeを変更する処理
                        val nDate = Date(sharedPreferences.getLong(defaultValues.alarmNextLongKey + index, 0))
                        cal.time = nDate
                        cal.add(Calendar.DATE, rpDays)
                        val nDateDisplay = cal.time
                        Log.d("nextDate","nDate = $nDate, nDateDisplay = $nDateDisplay")
                        sharedPreferences.edit().putLong(defaultValues.alarmNextLongKey + index, nDateDisplay.time).apply()
                        val nextDateStr = SimpleDateFormat("MM / dd", Locale.getDefault()).format(nDateDisplay)
                        val nextTimeStr = SimpleDateFormat("HH : mm", Locale.getDefault()).format(nDateDisplay)

                        adapter.stateUpdate(index, nextDateStr, nextTimeStr, prevDateStr, prevTimeStr)
                    } else {
                        removeIndexData(sharedPreferences, index)
                        adapter.deleteUpdate(index)
                    }

                }
            }
        }

        val linearLayoutManager = LinearLayoutManager(view.context)
        lvAlarm?.setHasFixedSize(true)
        lvAlarm?.adapter= adapter
        lvAlarm?.layoutManager = linearLayoutManager
        lvAlarm?.addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
    }

    //    alarmListを生成する関数
    private fun createAlarmList(sp: SharedPreferences) :MutableList<Alarm> {

        for (i in 0..defaultValues.nfMaxNum) {
            val name = sp.getString(defaultValues.alarmTaskNameKey + i, "NoData")
            val nextDateLong = sp.getLong(defaultValues.alarmNextLongKey + i, 0)
            val nextDate = getDataOrNoData(nextDateLong, "MM / dd")
            val nextTime = getDataOrNoData(nextDateLong, "HH : mm")
            val prevDateLong = sp.getLong(defaultValues.alarmPrevLongKey + i, 0)
            val prevDate = getDataOrNoData(prevDateLong, "MM / dd")
            val prevTime = getDataOrNoData(prevDateLong, "HH : mm")
            val repeatDays = sp.getInt(defaultValues.alarmRepeatDaysKey + i, 0)
            val repeatDaysStr = if (repeatDays == 0) {"NoRepeat"} else {"Repeat $repeatDays days"}
            val taskState = sp.getBoolean(defaultValues.alarmBooleanKey + i, false)
                alarmList.add(Alarm(name, nextDate, nextTime, prevDate, prevTime, repeatDaysStr, taskState))
        }

        return alarmList
    }

    private fun getDataOrNoData (date: Long, format: String) :String {
        return if (date != 0L) {SimpleDateFormat(format, Locale.getDefault()).format(Date(date))} else {"NoData"}
    }

    private fun removeIndexData (sp: SharedPreferences, index:Int) {
        sp.edit().remove(defaultValues.alarmTaskNameKey + index).apply()
        sp.edit().remove(defaultValues.alarmNextLongKey + index).apply()
        sp.edit().remove(defaultValues.alarmPrevLongKey + index).apply()
        sp.edit().remove(defaultValues.alarmRepeatDaysKey + index).apply()
        sp.edit().remove(defaultValues.alarmBooleanKey + index).apply()
    }

    private fun cancelPIntent (index: Int) {
        val alarmMgr =requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), MyBroadcastReceiver::class.java)
        intent.action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
        val pIntent = PendingIntent.getBroadcast(requireContext(), index, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmMgr.cancel(pIntent)
        Log.d("cancelPIntent", "requestCode is $index")
        pIntent.cancel()
    }

}