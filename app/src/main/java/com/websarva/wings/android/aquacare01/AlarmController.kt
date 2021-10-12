package com.websarva.wings.android.aquacare01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AlarmController {

    fun setAlarm (context: Context, requestCode: Int, fireTime:Long) {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        intent.action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
        intent.putExtra("RequestCode", requestCode)
        val pending = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as? AlarmManager
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, fireTime, pending)
        Log.d("setAlarm", "Alarm has been set as requestCode$requestCode")

    }

    fun cancelAlarm (context: Context, requestCode: Int) {
        val alarmMgr =context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        intent.action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
        val pIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmMgr.cancel(pIntent)
        pIntent.cancel()
        Log.d("cancelAlarm", "Alarm has been canceled, requestCode=$requestCode")
    }
}