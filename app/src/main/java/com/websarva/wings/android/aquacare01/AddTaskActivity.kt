package com.websarva.wings.android.aquacare01


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener, DatePickerFragment.OnDateSetListener {

    //    Notificationに使用する定数を定義
    private var requestCode = 0
    private val calendar = Calendar.getInstance()
    private val titleLengthLimit = 100
    private val repeatDaysLimit = 365
    private val defaultValues = DefaultValues()
    private val alarmController = AlarmController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

//        viewを取得
        val addTaskNameEdit = findViewById<EditText>(R.id.addTaskNameEdit)
        val addTaskDate = findViewById<TextView>(R.id.addTaskDate)
        val addTaskTime = findViewById<TextView>(R.id.addTaskTime)
        val addTaskSaveBtn = findViewById<Button>(R.id.addTaskSaveBtn)
        val rpCheckBox = findViewById<CheckBox>(R.id.rpCheckBox)
        val addTaskRepeatInt = findViewById<EditText>(R.id.addTaskRepeatInt)

//      textViewに初期値を表示
        val cDateStr = SimpleDateFormat("MM / dd", Locale.getDefault()).format(calendar.time)
        val cTimeStr = SimpleDateFormat("HH : mm", Locale.getDefault()).format(calendar.time)
        addTaskDate.text = cDateStr
        addTaskTime.text = cTimeStr

        //        Listenerをセット
        rpCheckBox.setOnCheckedChangeListener { _, _ ->
            addTaskRepeatInt.isEnabled = !addTaskRepeatInt.isEnabled
        }

        addTaskDate.setOnClickListener {
            DatePickerFragment().show(supportFragmentManager, "datePicker")
        }
        addTaskTime.setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, "timePicker")
        }

//        SAVEボタンを押されたときの処理
        addTaskSaveBtn.setOnClickListener {

//        viewの文字列を取得
            val tskName = addTaskNameEdit.text.toString()
            val rpInt = addTaskRepeatInt.text.toString().toIntOrNull() ?: 0
            val sp = getSharedPreferences(defaultValues.taskSaveFileName, Context.MODE_MULTI_PROCESS)
            requestCode = setReqCode(sp)
            when {
                tskName.length >= titleLengthLimit -> {
                    Toast.makeText(applicationContext, R.string.character_limit, Toast.LENGTH_LONG).show()
                }
                rpInt >= repeatDaysLimit -> {
                    Toast.makeText(applicationContext, R.string.repeat_period_limit, Toast.LENGTH_LONG).show()
                }
                requestCode > defaultValues.nfMaxNum -> {
                    Toast.makeText(applicationContext, R.string.request_code_limit, Toast.LENGTH_LONG).show()
                }
                else -> {
                    saveToSharedPref(sp, requestCode, tskName, calendar.time.time, rpInt)
                    alarmController.setAlarm(applicationContext, requestCode, calendar.timeInMillis)
                    Toast.makeText(applicationContext, R.string.alarm_start, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }

    //    DatePickerがsetされた後の処理
    override fun onDateSelected(date: String, year: Int, month: Int, day:Int) {
        val addTaskDate = findViewById<TextView>(R.id.addTaskDate)
        addTaskDate.text = date
        calendar.set(year, month, day)
    }

    //    TimePickerがsetされた後の処理
    override fun onTimeSelected(time: String, hourOfDay: Int, minute: Int) {
        val addTaskTime = findViewById<TextView>(R.id.addTaskTime)
        addTaskTime.text = time
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
    }

    private fun saveToSharedPref (sp: SharedPreferences, requestCode: Int, tskName: String, nextTime:Long, repeatDays:Int) {
        sp.edit().putString(defaultValues.alarmTaskNameKey + requestCode, tskName).apply()
        sp.edit().putBoolean(defaultValues.alarmBooleanKey + requestCode, true).apply()
        sp.edit().putLong(defaultValues.alarmNextLongKey + requestCode, nextTime).apply()
        sp.edit().putInt(defaultValues.alarmRepeatDaysKey + requestCode, repeatDays).apply()
        Log.d("AddTaskActivity","task saved as $tskName and requestCode is $requestCode")
    }

    //    現存するrequestCode+1を生成する関数
    private fun setReqCode(sp:SharedPreferences) : Int {
        var reqCode = 0
        var gotDataCheckStr = sp.getString(defaultValues.alarmTaskNameKey + reqCode, null)
        while (gotDataCheckStr != null) {
            reqCode++
            gotDataCheckStr = sp.getString(defaultValues.alarmTaskNameKey + reqCode, null)
        }
        return reqCode
    }

}