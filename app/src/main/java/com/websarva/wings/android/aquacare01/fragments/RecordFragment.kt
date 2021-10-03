package com.websarva.wings.android.aquacare01.fragments

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
import java.text.SimpleDateFormat
import java.util.*

class Recode : Fragment() {

    private val date = Date().time
    private val defaultValues = DefaultValues()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRecord: RecyclerView = view.findViewById(R.id.rvRecord)
        val dateStr = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date)

        val recordList = listOf(
            Diary(defaultValues.recFileName+".jpeg", dateStr),
        )

        rvRecord.adapter = RecordAdapter(recordList)
        rvRecord.layoutManager = LinearLayoutManager(requireContext())
    }

}