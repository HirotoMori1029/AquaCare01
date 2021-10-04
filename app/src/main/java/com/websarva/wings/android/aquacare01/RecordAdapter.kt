package com.websarva.wings.android.aquacare01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(private val recordList: MutableList<Diary>): RecyclerView.Adapter<RecordAdapter.RecordRecyclerViewHolder>() {

    var listener: Listener? = null
    private val imageLoader = ImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_row, parent, false)
        return RecordRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordRecyclerViewHolder, index: Int) {
        val record = recordList[index]
        val bitmap = imageLoader.readImgFromFileName(record.fileName, holder.image.context)
        holder.image.setImageBitmap(bitmap)
        holder.date.text = record.date
        holder.recordDeleteBtn.setOnClickListener {
            listener?.onClickBtn(index)
        }
    }

    override fun getItemCount(): Int = recordList.size

    class RecordRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.recordDate)
        val image: ImageView = itemView.findViewById(R.id.recordImage)
        val recordDeleteBtn: Button = itemView.findViewById(R.id.recodeDeleteBtn)
    }

//todo あとで削除する関数を追加する
//    fun recDeleteUpdate(index: Int) {
//        recordList[index].fileName.
//    }

    interface Listener {
        fun onClickBtn(index: Int)
    }
}

