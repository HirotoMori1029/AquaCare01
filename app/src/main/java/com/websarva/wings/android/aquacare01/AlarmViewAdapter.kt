package com.websarva.wings.android.aquacare01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AlarmViewAdapter(private val alarmListData: MutableList<Alarm>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<AlarmViewAdapter.AlarmListRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_row, parent, false)
        return AlarmListRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmListRecyclerViewHolder, position: Int) {

        val alarm = alarmListData[position]
        holder.alarmIconImageView.setImageResource(R.mipmap.ic_launcher)
        holder.taskNameTextView.text = alarm.name
        holder.dateTextView.text = alarm.date
        holder.timeTextView.text = alarm.time
    }

    override fun getItemCount(): Int = alarmListData.size

    inner class AlarmListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val alarmIconImageView: ImageView = itemView.findViewById(R.id.alarmIcon)
        val taskNameTextView: TextView = itemView.findViewById(R.id.taskName)
        val timeTextView: TextView = itemView.findViewById(R.id.taskTime)
        val dateTextView: TextView = itemView.findViewById(R.id.taskDate)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}