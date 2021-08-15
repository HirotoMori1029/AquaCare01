package com.websarva.wings.android.aquacare01

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat

class AlarmNotification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val requestCode :Int = intent.getIntExtra("RequestCode", 0)
        val pendingIntent :PendingIntent = PendingIntent.getActivity(context, requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "default"
        val title= context.getString(R.string.app_name)
        val currentTime: Long = System.currentTimeMillis()
        val dataFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val cTime = dataFormat.format(currentTime)
        val message ="This is notification at {$cTime}"

//        NotificationChannelを設定
        val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)

//        NotificationManagerを設定
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
        builder.setContentTitle(title)
        builder.setContentText(message)
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        builder.setWhen(System.currentTimeMillis())
//        通知
        notificationManager.notify(R.string.app_name, builder.build())

    }
}