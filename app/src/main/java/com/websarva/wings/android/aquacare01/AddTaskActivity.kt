package com.websarva.wings.android.aquacare01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.websarva.wings.android.aquacare01.fragments.NotificationFragment
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener, DatePickerFragment.OnDateSetListener {

    //    Notificationに使用する定数を定義
    private var alarmManager: AlarmManager? = null
    private var pending: PendingIntent? = null
    private var requestCode = 0
    private val calendar = Calendar.getInstance()
    private val titleLengthLimit = 100
    private val repeatDaysLimit = 365

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
        val cDateStr = SimpleDateFormat("MM / dd", Locale.getDefault()).format(calendar.time)
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
            val rpInt = addTaskRepeatInt.text.toString().toIntOrNull() ?: 0
            when {
                tskName.length >= titleLengthLimit -> {
                    Toast.makeText(applicationContext, R.string.character_limit, Toast.LENGTH_LONG).show()
                }
                rpInt >= repeatDaysLimit -> {
                    Toast.makeText(applicationContext, R.string.repeat_period_limit, Toast.LENGTH_LONG).show()
                }
                else -> {
        //              チェック状態を取得
                    val rpCBisChecked = rpCheckBox.isChecked
                    //        sharedPreferencesを準備
                    val sharedPref =
                        getSharedPreferences(NotificationFragment().taskSaveFileName, Context.MODE_MULTI_PROCESS)
        //              すでにIDが存在する場合、+1する
                    requestCode = setReqCode(sharedPref)

        //              intentを生成
                    val intent = Intent(applicationContext, MyBroadcastReceiver::class.java)
                    //todo 怪しい
                    intent.action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
                    intent.putExtra("RequestCode", requestCode)
                    intent.putExtra("TaskName", tskName)
                    pending = PendingIntent.getBroadcast(applicationContext, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        //              sharedPrefに保存
                    savePreferences(
                        NotificationFragment().alarmTaskNameKey + requestCode,
                        tskName,
                        sharedPref
                    )
                    sharedPref.edit()
                        .putBoolean(NotificationFragment().alarmBooleanKey + requestCode, true).apply()
                    sharedPref.edit().putLong(
                        NotificationFragment().alarmNextLongKey + requestCode,
                        calendar.time.time
                    ).apply()
                    sharedPref.edit()
                        .putInt(NotificationFragment().alarmRepeatDaysKey + requestCode, rpInt).apply()

        //              アラームに使用する定数を用意
                    alarmManager = getSystemService(ALARM_SERVICE) as? AlarmManager
                    val alarmType = AlarmManager.RTC_WAKEUP
                    Log.d("AddTaskActivity", "task saved with RequestCode$requestCode")

        //              rpCheckBoxが入っていればリピートで設定
                    if (rpCBisChecked) {
                        if (alarmManager != null) {
                            alarmManager?.setRepeating(
                                alarmType,
                                calendar.timeInMillis,
                                AlarmManager.INTERVAL_FIFTEEN_MINUTES * rpInt,
                                pending
                            )
        //                      トーストで設定されたことを表示する
                            alarmStartToast()
                        }

                    } else {
                        if (alarmManager != null) {
                            alarmManager?.setExact(alarmType, calendar.timeInMillis, pending)
        //                トーストで設定されたことを表示する
                            alarmStartToast()
                        }
                    }
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
//    alarmセットされたときにトーストする関数
    private fun alarmStartToast () {
        Toast.makeText(applicationContext, R.string.alarm_start, Toast.LENGTH_SHORT).show()
    }

//    sharedPrefに保存する関数
    private fun savePreferences(key :String, saveStr :String, sp: SharedPreferences) {
        sp.edit().putString(key, saveStr).apply()
    }
//    現存するrequestCode+1を生成する関数
    private fun setReqCode(sp:SharedPreferences) : Int {
        var reqCode = 0
        var gotDataCheckStr :String?
        for (i in 0..NotificationFragment().nfMaxNum) {
            gotDataCheckStr = sp.getString(NotificationFragment().alarmTaskNameKey + i, "noData")
            if (gotDataCheckStr != "noData") {
                reqCode = i + 1
            } else {
                break
            }
        }
        return reqCode
    }

}