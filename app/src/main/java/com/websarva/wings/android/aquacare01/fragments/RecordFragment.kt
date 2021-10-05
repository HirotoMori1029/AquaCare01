package com.websarva.wings.android.aquacare01.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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

        var recordID = 0
        var fileName = sp.getString(defaultValues.recFileNameKey + recordID, "NoData") ?: "NoData"
        while (fileName != "NoData") {
            val date = sp.getString(defaultValues.recDateKey + recordID, "NoData") ?: "NoData"
            recordList.add(Diary(fileName, date))
            recordID++
            fileName = sp.getString(defaultValues.recFileNameKey + recordID, "NoData") ?: "NoData"
        }


        val adapter = RecordAdapter(recordList)

        //adapterのlisterを定義
        adapter.listener = object :RecordAdapter.Listener {
            override fun onClickBtn(index: Int) {
                adapter.recDeleteUpdate(index)
                var i = index
                var j = i + 1

                //ファイルを削除
                val dFileName = sp.getString(defaultValues.recFileNameKey + i, "NoData")
                requireContext().deleteFile(dFileName)

                //sharedPreferencesの該当データを削除
                sp.edit().remove(defaultValues.recFileNameKey + i).apply()
                sp.edit().remove(defaultValues.recDateKey + i).apply()
                Log.d("RecordFragment", "recordID$i has been deleted")

                //次のデータが埋まっていれば、今のデータに上書きするのを繰り返す
                var nextFileName = sp.getString(defaultValues.recFileNameKey + j, "NoData")
                while (nextFileName != "NoData") {
                    sp.edit().putString(defaultValues.recFileNameKey + i, sp.getString(defaultValues.recFileNameKey + j, "NoData")).apply()
                    sp.edit().putString(defaultValues.recDateKey + i, sp.getString(defaultValues.recDateKey + j, "NoData")).apply()
                    i++
                    j++
                    nextFileName = sp.getString(defaultValues.recFileNameKey + j, "NoData")
                    }

                //最後の番号のデータを削除する
                sp.edit().remove(defaultValues.recFileNameKey + i).apply()
                sp.edit().remove(defaultValues.recDateKey + i).apply()
                Log.d("RecordFragment", "recordID$i has been deleted")
                }
            }

        rvRecord.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        rvRecord.layoutManager = layoutManager
    }
}