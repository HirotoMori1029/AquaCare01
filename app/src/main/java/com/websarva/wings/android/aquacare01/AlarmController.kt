package com.websarva.wings.android.aquacare01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class AlarmController {

    fun setAlarm (context: Context, requestCode: Int, fireTime:Long) {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        intent.action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
        intent.putExtra("RequestCode", requestCode)
        val pending = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as? AlarmManager
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, fireTime, pending)
    }
}