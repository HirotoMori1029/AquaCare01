package com.websarva.wings.android.aquacare01

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    override fun onBindViewHolder(holder: AlarmListRecyclerViewHolder, index: Int) {

        val alarm = alarmListData[index]

        if (alarm.name != holder.taskName.context.getString(R.string.no_data_str)) {
            holder.apply {
                taskName.setTextColor(Color.BLUE)
                taskDateNext.setTextColor(Color.BLACK)
                taskTimeNext.setTextColor(Color.BLACK)
                taskNextStr.setTextColor(Color.BLACK)
                taskDateNextStr.setTextColor(Color.BLACK)
                taskTimeNextStr.setTextColor(Color.BLACK)
                taskDeleteBtn.setTextColor(Color.BLACK)
                taskDeleteBtn.isEnabled = true
            }
            if (alarm.taskState) {
                holder.alarmIcon.setImageResource(R.drawable.task_waiting_80px)
            } else if (!alarm.taskState) {
                holder.alarmIcon.setImageResource(R.drawable.task_remaining_80px)
                holder.alarmIcon.setOnClickListener {
                    listener?.onClickImage(index)
                }
            }
        } else {
            holder.alarmIcon.setImageResource(R.drawable.task_no_data_80px)
        }
        holder.apply {
            taskName.text = alarm.name
            taskDeleteBtn.setOnClickListener {
                listener?.onClickBtn(index)
            }
            taskDateNext.text = alarm.nextDate
            taskTimeNext.text = alarm.nextTime
            taskDatePrev.text = alarm.prevDate
            taskTimePrev.text = alarm.prevTime
            taskRepeat.text = alarm.repeat
        }
    }

    override fun getItemCount(): Int = alarmListData.size

    inner class AlarmListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val alarmIcon: ImageView = itemView.findViewById(R.id.alarmIcon)
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val taskNextStr: TextView = itemView.findViewById(R.id.taskNextStr)
        val taskDateNextStr: TextView = itemView.findViewById(R.id.taskDateNextStr)
        val taskTimeNextStr: TextView = itemView.findViewById(R.id.taskTimeNextStr)
        val taskTimeNext: TextView = itemView.findViewById(R.id.taskTimeNext)
        val taskDateNext: TextView = itemView.findViewById(R.id.taskDateNext)
        val taskTimePrev: TextView = itemView.findViewById(R.id.taskTimePrev)
        val taskDatePrev: TextView = itemView.findViewById(R.id.taskDatePrev)
        val taskRepeat: TextView = itemView.findViewById(R.id.taskRepeat)
        val taskDeleteBtn: Button = itemView.findViewById(R.id.taskDeleteBtn)

    }

    fun deleteUpdate (index: Int, noData: String) {
        alarmListData[index].apply {
            name = noData
            nextDate = noData
            nextTime = noData
            prevDate = noData
            prevTime = noData
            repeat = noData
            taskState = false
            notifyItemChanged(index)
        }
    }

    fun stateUpdate (updateDate:UpdateDate) {
        alarmListData[updateDate.index].apply {
            nextDate = updateDate.nextDateStr
            nextTime = updateDate.nextTimeStr
            prevDate = updateDate.prevDateStr
            prevTime = updateDate.prevTimeStr
            taskState = true
        }
        notifyItemChanged(updateDate.index)
    }

    interface Listener {
        fun onClickBtn(index: Int)
        fun onClickImage(index: Int)
    }

}