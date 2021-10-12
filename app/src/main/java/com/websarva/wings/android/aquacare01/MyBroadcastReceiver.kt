package com.websarva.wings.android.aquacare01

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*


class MyBroadcastReceiver : BroadcastReceiver() {
    
    private val defVal = DefaultValues()

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPref = context.getSharedPreferences("savedTaskInAquariumCare", Context.MODE_MULTI_PROCESS)

        if (intent.action == "com.websarva.wings.android.aquacare01.NOTIFY_ALARM") {
            val alarmID :Int = intent.getIntExtra("RequestCode", 0)
            val nIntent = Intent(context, MainActivity::class.java)
            val pendingIntent :PendingIntent = PendingIntent.
                    getActivity(context, alarmID + defVal.nfMaxNum, nIntent, PendingIntent.FLAG_IMMUTABLE)
            setNotification(context, sharedPref, alarmID,pendingIntent)
            //タスク状態を保存
            sharedPref.edit().putBoolean(defVal.alarmBooleanKey + alarmID, false).apply()

        } else if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            //アラームの再設定を行う
            Log.d("MyBroadcastReceiver", "Booted!!")
            for (almID in 0..defVal.nfMaxNum) {
                bootedSetAlarm(context, sharedPref, almID)
            }

        }
    }
    private fun setNotification (context: Context, sp: SharedPreferences, alarmID: Int, pIntent: PendingIntent) {
        //Notificationに関する記述
        val channelId = "default"
        val title= context.getString(R.string.notification_title)
        val aMessage = sp.getString(defVal.alarmTaskNameKey + alarmID, context.getString(R.string.notification_message))
        val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.mipmap.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(aMessage)
            setContentIntent(pIntent)
            setWhen(System.currentTimeMillis())
            setAutoCancel(true)
        }
        //通知
        notificationManager.notify(R.string.app_name, builder.build())
    }


    //再起動時のアラームを設定する関数
    private fun bootedSetAlarm (context: Context, sp:SharedPreferences, almID: Int) {
        val alarmTaskName = sp.getString(defVal.alarmTaskNameKey + almID, null)
        val rpDays = sp.getInt(defVal.alarmRepeatDaysKey + almID, 0)
        if (alarmTaskName != null && rpDays != 0) {
            val nxtAlarmDate = sp.getLong(defVal.alarmNextLongKey + almID, 0)
            val calendar = Calendar.getInstance()
            if (calendar.time.time < nxtAlarmDate) {
                calendar.time = Date(nxtAlarmDate)
            } else {
                while (calendar.time.time > nxtAlarmDate) {
                    calendar.add(Calendar.DATE, rpDays)
                }
            }

            sp.edit().putLong(defVal.alarmNextLongKey + almID, calendar.time.time).apply()
            val almIntent = Intent(context, MyBroadcastReceiver::class.java).apply {
                action = "com.websarva.wings.android.aquacare01.NOTIFY_ALARM"
                putExtra("RequestCode", almID)
                putExtra("TaskName", alarmTaskName)
            }
            val pIntent = PendingIntent.getBroadcast(
                context,
                almID,
                almIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pIntent)
            Log.d("MyBroadcastReceiver", "Alarm has been set as alarmID$almID")
        }

    }
}