package com.websarva.wings.android.aquacare01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.aquacare01.fragments.HomeFragment

class RecordAdapter(private val recordList: List<Diary>): RecyclerView.Adapter<RecordAdapter.RecordRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_row, parent, false)
        return RecordRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordRecyclerViewHolder, position: Int) {
        val record = recordList[position]
        holder.image.setImageBitmap(record.bitmap)
        holder.date.text = record.date
    }

    override fun getItemCount(): Int = recordList.size

    class RecordRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.recordDate)
        val image: ImageView = itemView.findViewById(R.id.recordImage)
    }
}