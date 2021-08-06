package com.websarva.wings.android.aquacare01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AddTaskActivity : AppCompatActivity(), TimePickerFragment.OnTimeSetListener, DatePickerFragment.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val addTaskTitle = findViewById<TextView>(R.id.addTaskTitle)
        val addTaskNameEdit = findViewById<EditText>(R.id.addTaskNameEdit)
        val addTaskDate = findViewById<TextView>(R.id.addTaskDate)
        val addTaskTime = findViewById<TextView>(R.id.addTaskTime)
        val addTaskSaveBtn = findViewById<Button>(R.id.addTaskSaveBtn)

        addTaskTitle.text = message

        addTaskDate.setOnClickListener{
            val fragmentD = DatePickerFragment()
            fragmentD.show(supportFragmentManager, "datePicker")
        }

        addTaskTime.setOnClickListener{
            val fragmentT = TimePickerFragment()
            fragmentT.show(supportFragmentManager, "timePicker")
        }

        addTaskSaveBtn.setOnClickListener{
            finish()
        }
    }

    //    DatePickerがsetされた後の処理
    override fun onDateSelected(date: String) {
        val addTaskDate = findViewById<TextView>(R.id.addTaskDate)
        addTaskDate.text = date
    }

    //    TimePickerがsetされた後の処理
    override fun onTimeSelected(time: String) {
        val addTaskTime = findViewById<TextView>(R.id.addTaskTime)
        addTaskTime.text = time
    }

}