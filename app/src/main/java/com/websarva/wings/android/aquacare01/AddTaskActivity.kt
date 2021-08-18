package com.websarva.wings.android.aquacare01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener, DatePickerFragment.OnDateSetListener {

    //    Notificationに使用する定数を定義
    private var alarmManager: AlarmManager? = null
    private var pending: PendingIntent? = null
    private var requestCode = 1
    private val calendar = Calendar.getInstance()
    private var gotDataCheckStr :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
//        viewを取得
        val addTaskTitle = findViewById<TextView>(R.id.addTaskTitle)
        val addTaskNameEdit = findViewById<EditText>(R.id.addTaskNameEdit)
        val addTaskDate = findViewById<TextView>(R.id.addTaskDate)
        val addTaskTime = findViewById<TextView>(R.id.addTaskTime)
        val addTaskSaveBtn = findViewById<Button>(R.id.addTaskSaveBtn)
        val rpCheckBox = findViewById<CheckBox>(R.id.rpCheckBox)
        val addTaskRepeatInt = findViewById<EditText>(R.id.addTaskRepeatInt)

//        初期表示に使う現在時刻を文字列で取得する
        val cDateStr = SimpleDateFormat("yyyy / MM / dd", Locale.getDefault()).format(calendar.time)
        val cTimeStr = SimpleDateFormat("HH : mm", Locale.getDefault()).format(calendar.time)

//        各ビューにテキストを設定
        addTaskTitle.text = message
        addTaskDate.text = cDateStr
        addTaskTime.text = cTimeStr

        //        Listenerをセット
        rpCheckBox.setOnCheckedChangeListener { _, _ ->
            addTaskRepeatInt.isEnabled = !addTaskRepeatInt.isEnabled
        }

        addTaskDate.setOnClickListener {
            val fragmentD = DatePickerFragment()
            fragmentD.show(supportFragmentManager, "datePicker")
        }
        addTaskTime.setOnClickListener {
            val fragmentT = TimePickerFragment()
            fragmentT.show(supportFragmentManager, "timePicker")
        }

//        SAVEボタンを押されたときの処理
        addTaskSaveBtn.setOnClickListener {

//        viewの文字列を取得
            val tskName = addTaskNameEdit.text.toString()
            val tskDate = addTaskDate.text.toString()
            val tskTime = addTaskTime.text.toString()
            val rpLong = addTaskRepeatInt.text.toString().toLongOrNull() ?: 0
//        チェック状態を取得
            val rpCBisChecked = rpCheckBox.isChecked
            //        sharedPreferencesを準備
            val sharedPref = getSharedPreferences("savedTaskInAquariumCare", Context.MODE_PRIVATE)
//            当該requestCodeがある場合+1する
            for (i in 1..10) {
                gotDataCheckStr = sharedPref.getString("taskNameKey$requestCode", "noData")
                if ( gotDataCheckStr != "noData") {
                    requestCode++
                } else {
                    break
                }
            }
//        intentを生成
            val intent = Intent(applicationContext, AlarmNotification::class.java)
            intent.putExtra("RequestCode", requestCode)
            pending = PendingIntent.getBroadcast(applicationContext, requestCode, intent, 0)

//            sharedPrefに保存する関数を用意
            fun savePreferences(key :String, saveStr :String) {
                sharedPref.edit().putString(key, saveStr).apply()
            }

//            sharedPrefに保存
            savePreferences("taskNameKey$requestCode", tskName)
            savePreferences("taskDateKey$requestCode", tskDate)
            savePreferences("taskTimeKey$requestCode", tskTime)

//            アラームに使用する定数を用意
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val alarmType = AlarmManager.RTC_WAKEUP
            val triggerTime = calendar.timeInMillis

//            rpCheckBoxが入っていればリピートで設定
            if (rpCBisChecked) {
                if (alarmManager != null) {
                    alarmManager?.setInexactRepeating(alarmType, triggerTime, millisToDay(rpLong), pending)
//                トーストで設定されたことを表示する
                    alarmStartToast ()
                }

                } else {
                if (alarmManager != null) {
                    alarmManager?.setExact(alarmType, triggerTime, pending)
//                トーストで設定されたことを表示する
                    alarmStartToast ()
                }
            }
            finish()
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

    private fun alarmStartToast () {
        Toast.makeText(applicationContext, R.string.alarm_start, Toast.LENGTH_SHORT).show()
    }

    private fun millisToDay (millis: Long) :Long {
        return millis * 1000 * 60 * 60 * 24
    }

}