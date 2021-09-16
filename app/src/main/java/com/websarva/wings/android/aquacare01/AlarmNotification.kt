package com.websarva.wings.android.aquacare01

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.websarva.wings.android.aquacare01.fragments.NotificationFragment


class AlarmNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val requestCode :Int = intent.getIntExtra("RequestCode", 0)
        val taskName :String? = intent.getStringExtra("TaskName")
        val nIntent = Intent(context, MainActivity::class.java)
        val pendingIntent :PendingIntent = PendingIntent.getActivity(context, requestCode + NotificationFragment().nfMaxNum, nIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //        Notificationに関する記述
        val channelId = "default"
        val title= context.getString(R.string.app_name)
        val aMessage ="$taskName"
        val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
        builder.setContentTitle(title)
        builder.setContentText(aMessage)
        builder.setContentIntent(pendingIntent)
        builder.setWhen(System.currentTimeMillis())
        builder.setAutoCancel(true)
//        通知
        notificationManager.notify(R.string.app_name, builder.build())

//        タスク状態を保存
        val sharedPref = context.getSharedPreferences("savedTaskInAquariumCare", Context.MODE_MULTI_PROCESS)
        val key = NotificationFragment().alarmBooleanKey + requestCode
        val taskState = sharedPref.getBoolean(key, false)
        if (taskState) {
            sharedPref.edit().putBoolean(key, false).apply()
        }



    }
}