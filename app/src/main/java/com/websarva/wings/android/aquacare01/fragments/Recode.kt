package com.websarva.wings.android.aquacare01.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.websarva.wings.android.aquacare01.AlarmNotification
import com.websarva.wings.android.aquacare01.R

class Recode : Fragment() {

    var am: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recode, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val btn1 = view.findViewById<Button>(R.id.button)
//        val btn2 = view.findViewById<Button>(R.id.button2)
//        val intent = Intent(context, AlarmNotification::class.java)
//
//        btn1.setOnClickListener {
//            am = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
//
//            if (pendingIntent != null && am != null) {
//                am?.setExact()
//            }
//        }
//
//        btn2.setOnClickListener {
//            am = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
//            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
//
//            if (pendingIntent != null && am != null) {
//                am?.cancel(pendingIntent)
//            }
//        }
//    }

}