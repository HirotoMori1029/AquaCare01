package com.websarva.wings.android.aquacare01.fragments

import android.content.Context
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
    private val alarmController = AlarmController()
    private val dateSDF = SimpleDateFormat(defaultValues.datePtn, Locale.getDefault())
    private val timeSDF = SimpleDateFormat(defaultValues.timePtn, Locale.getDefault())


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
                alarmController.cancelAlarm(requireContext(), index)
                removeIndexData(sharedPreferences, index)
                adapter.deleteUpdate(index)
                Toast.makeText(context, "alarm ID$index was deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onClickImage(index: Int) {
//                アイコンを変更する処理
                val taskState = sharedPreferences.getBoolean(defaultValues.alarmBooleanKey + index, false)
                if (!taskState) {
                    sharedPreferences.edit().putBoolean(defaultValues.alarmBooleanKey + index, true).apply()
                    val rpDays = sharedPreferences.getInt(defaultValues.alarmRepeatDaysKey + index, 0)
                    if (rpDays != 0) {
                        val updateDate = updateAlarmInfo(sharedPreferences, index, rpDays)
                        adapter.stateUpdate(updateDate)
                        alarmController.setAlarm(requireContext(), index, updateDate.fireTime)
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
            val name = sp.getString(defaultValues.alarmTaskNameKey + i, getString(R.string.no_data_str))
            val lNextDate = sp.getLong(defaultValues.alarmNextLongKey + i, 0)
            val nextDate = getStrFromDate(lNextDate, dateSDF)
            val nextTime = getStrFromDate(lNextDate, timeSDF)
            val lPrevDate = sp.getLong(defaultValues.alarmPrevLongKey + i, 0)
            val prevDate = getStrFromDate(lPrevDate, dateSDF)
            val prevTime = getStrFromDate(lPrevDate,timeSDF)
            val repeatDays = sp.getInt(defaultValues.alarmRepeatDaysKey + i, 0)
            val repeatDaysStr = if (repeatDays == 0) {"NoRepeat"} else {"Repeat $repeatDays days"}
            val taskState = sp.getBoolean(defaultValues.alarmBooleanKey + i, false)
                alarmList.add(Alarm(name, nextDate, nextTime, prevDate, prevTime, repeatDaysStr, taskState))
        }

        return alarmList
    }

    private fun getStrFromDate (date: Long, sdf:SimpleDateFormat) :String {
        return if (date != 0L) {sdf.format(Date(date))} else {getString(R.string.no_data_str)}
    }

    private fun removeIndexData (sp: SharedPreferences, index:Int) {
        sp.edit().remove(defaultValues.alarmTaskNameKey + index).apply()
        sp.edit().remove(defaultValues.alarmNextLongKey + index).apply()
        sp.edit().remove(defaultValues.alarmPrevLongKey + index).apply()
        sp.edit().remove(defaultValues.alarmRepeatDaysKey + index).apply()
        sp.edit().remove(defaultValues.alarmBooleanKey + index).apply()
    }

    private fun updateAlarmInfo (sp: SharedPreferences, index: Int, rpDays:Int) :UpdateDate {

        //PrevTimeを変更する処理
        val pCal = Calendar.getInstance()
        sp.edit().putLong(defaultValues.alarmPrevLongKey + index, pCal.time.time).apply()
        val prevDateStr = dateSDF.format(pCal.time)
        val prevTimeStr = timeSDF.format(pCal.time)
        //NextTimeを変更する処理
        val nCal = Calendar.getInstance()
        nCal.time = Date(sp.getLong(defaultValues.alarmNextLongKey + index, 0))
        while (pCal > nCal) {
            nCal.add(Calendar.DATE, rpDays)
        }
        sp.edit().putLong(defaultValues.alarmNextLongKey + index, nCal.time.time).apply()
        val nextDateStr = dateSDF.format(nCal.time)
        val nextTimeStr = timeSDF.format(nCal.time)

        return UpdateDate(index, prevDateStr, prevTimeStr, nextDateStr, nextTimeStr, nCal.timeInMillis)
    }

}
