package com.websarva.wings.android.aquacare01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AlarmViewAdapter(private var alarmListData: MutableList<Alarm>) :
    RecyclerView.Adapter<AlarmViewAdapter.AlarmListRecyclerViewHolder>() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_row, parent, false)
        return AlarmListRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmListRecyclerViewHolder, position: Int) {

        val alarm = alarmListData[position]
        if ((alarm.name != "NoData") && alarm.taskState) {
            holder.alarmIcon.setImageResource(R.drawable.task_waiting_round)
        } else if ((alarm.name != "NoData") && !alarm.taskState){
            holder.alarmIcon.setImageResource(R.drawable.task_remaining_round)
        } else {
            holder.alarmIcon.setImageResource(R.drawable.task_no_data)
        }

        holder.alarmIcon.setOnClickListener {
            listener?.onClickImage(position)
        }
        holder.taskName.text = alarm.name
        holder.taskName.setOnClickListener {
            listener?.onClickText(position)
        }
        holder.taskDateNext.text = alarm.nextDate
        holder.taskTimeNext.text = alarm.nextTime
        holder.taskDatePrev.text = alarm.prevDate
        holder.taskTimePrev.text = alarm.prevTime
        holder.taskRepeat.text = alarm.repeat
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

    fun deleteUpdate (index: Int) {
        alarmListData[index].name = "NoData"
        alarmListData[index].nextDate = "NoData"
        alarmListData[index].nextTime = "NoData"
        alarmListData[index].prevDate = "NoData"
        alarmListData[index].prevTime = "NoData"
        alarmListData[index].repeat = "NoRepeat"
        alarmListData[index].taskState = false
        notifyItemChanged(index)
    }

    fun stateUpdate (index: Int, nextDateStr: String, nextTimeStr: String, prevDateStr: String, prevTimeStr: String) {
        alarmListData[index].nextDate = nextDateStr
        alarmListData[index].nextTime = nextTimeStr
        alarmListData[index].prevDate = prevDateStr
        alarmListData[index].prevTime = prevTimeStr
        alarmListData[index].taskState = true
        notifyItemChanged(index)
    }

    interface Listener {
        fun onClickText(index: Int)
        fun onClickImage(index: Int)
    }

}