package com.websarva.wings.android.aquacare01.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.aquacare01.DefaultValues
import com.websarva.wings.android.aquacare01.Diary
import com.websarva.wings.android.aquacare01.R
import com.websarva.wings.android.aquacare01.RecordAdapter

class Recode : Fragment() {

    private val defaultValues = DefaultValues()
    private val recordList = mutableListOf<Diary>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRecord: RecyclerView = view.findViewById(R.id.rvRecord)
        val sp = requireContext().getSharedPreferences(defaultValues.recSpFileName, Context.MODE_PRIVATE)

        for (recordID in 0..defaultValues.recMaxNum) {
            val fileName = sp.getString(defaultValues.recFileNameKey + recordID, "NoData") ?: "NoData"
            if (fileName == "NoData") {
                break
            } else {
                val date = sp.getString(defaultValues.recDateKey + recordID, "NoData") ?: "NoData"
                recordList.add(Diary(fileName, date))
            }
        }
        rvRecord.adapter = RecordAdapter(recordList.reversed())
        rvRecord.layoutManager = LinearLayoutManager(requireContext())
    }
}