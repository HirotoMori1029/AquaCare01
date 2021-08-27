package com.websarva.wings.android.aquacare01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AlarmViewAdapter(private val alarmListData: MutableList<Alarm>) :
    RecyclerView.Adapter<AlarmViewAdapter.AlarmListRecyclerViewHolder>() {

    private lateinit var listener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_row, parent, false)
        return AlarmListRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmListRecyclerViewHolder, position: Int) {

        val alarm = alarmListData[position]
        holder.alarmIcon.setImageResource(R.drawable.task_waiting_round)
        holder.alarmIcon.setOnClickListener {
            listener.onClick(it)
        }
        holder.taskName.text = alarm.name
        holder.taskName.setOnClickListener {
            listener.onClick(it)
        }
        holder.taskDateNext.text = alarm.nextDate
        holder.taskTimeNext.text = alarm.nextTime
        holder.taskDatePrev.text = alarm.prevDate
        holder.taskTimePrev.text = alarm.prevTime
        holder.taskRepeat.text = alarm.repeat
    }

    fun setOnItemClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int = alarmListData.size

    inner class AlarmListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val alarmIcon: ImageView = itemView.findViewById(R.id.alarmIcon)
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val taskTimeNext: TextView = itemView.findViewById(R.id.taskTimeNext)
        val taskDateNext: TextView = itemView.findViewById(R.id.taskDateNext)
        val taskTimePrev: TextView = itemView.findViewById(R.id.taskTimePrev)
        val taskDatePrev: TextView = itemView.findViewById(R.id.taskDatePrev)
        val taskRepeat: TextView = itemView.findViewById(R.id.taskRepeat)

    }

}