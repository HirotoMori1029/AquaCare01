package com.websarva.wings.android.aquacare01

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*


class MyBroadcastReceiver : BroadcastReceiver() {
    
    private val defaultValues = DefaultValues()

    override fun onReceive(context: Context, intent: Intent) {
        val sharedPref = context.getSharedPreferences("savedTaskInAquariumCare", Context.MODE_MULTI_PROCESS)

        if (intent.action == "com.websarva.wings.android.aquacare01.NOTIFY_ALARM") {
            val alarmID :Int = intent.getIntExtra("RequestCode", 0)
            val nIntent = Intent(context, MainActivity::class.java)
            val pendingIntent :PendingIntent = PendingIntent.
                    getActivity(context, alarmID + defaultValues.nfMaxNum, nIntent, PendingIntent.FLAG_IMMUTABLE)

            //Notificationに関する記述
            val channelId = "default"
            val title= context.getString(R.string.app_name)
            val aMessage = sharedPref.getString(defaultValues.alarmTaskNameKey + alarmID, context.getString(R.string.notification_message))
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(context, channelId).apply {
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setContentTitle(title)
                setContentText(aMessage)
                setContentIntent(pendingIntent)
                setWhen(System.currentTimeMillis())
                setAutoCancel(true)
            }

            //通知
            notificationManager.notify(R.string.app_name, builder.build())

            //タスク状態を保存
            val key = defaultValues.alarmBooleanKey + alarmID
            val taskState = sharedPref.getBoolean(key, false)
            if (taskState) {
                sharedPref.edit().putBoolean(key, false).apply()
            }

        } else if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            //アラームの再設定を行う
            Log.d("MyBroadcastReceiver", "Booted!!")
            for (alarmID in 0..defaultValues.nfMaxNum) {
                val alarmTaskName = sharedPref.getString(defaultValues.alarmTaskNameKey + alarmID, "NoData")
                if (alarmTaskName != "NoData") {
                    val almIntent = Intent(context, MyBroadcastReceiver::class.java).apply {
                        action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
                        putExtra("RequestCode", alarmID)
                        putExtra("TaskName", alarmTaskName)
                    }

                    val pIntent = PendingIntent.getBroadcast(context, alarmID, almIntent, PendingIntent.FLAG_IMMUTABLE)
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmType = AlarmManager.RTC_WAKEUP
                    val alarmNextLong = sharedPref.getLong(defaultValues.alarmNextLongKey + alarmID, 0)
                    val calendar = Calendar.getInstance()
                    //NextTimeがリブートした時間よりも遅かったら、taskStateをfalseにする
                    if (calendar.time.time >= alarmNextLong) {
                        sharedPref.edit().putBoolean(defaultValues.alarmBooleanKey + alarmID, false).apply()
                        Log.d("MyBroadcastReceiver", "Task state for alarmID$alarmID has changed to false")
                    }
                    calendar.time = Date(alarmNextLong)
                    val alarmRepeatDays = sharedPref.getInt(defaultValues.alarmRepeatDaysKey + alarmID, 0)
                    if (alarmRepeatDays != 0) {
                        alarmManager.setRepeating(alarmType,calendar.timeInMillis,AlarmManager.INTERVAL_DAY * alarmRepeatDays, pIntent)
                        Log.d("MyBroadcastReceiver", "Repeat alarm has been set as alarmID$alarmID")
                    } else {
                        alarmManager.setExact(alarmType, calendar.timeInMillis, pIntent)
                        Log.d("MyBroadcastReceiver", "Normal alarm has been set as alarmID$alarmID")
                    }
                } else {
                    break
                }
            }

        }
    }
}