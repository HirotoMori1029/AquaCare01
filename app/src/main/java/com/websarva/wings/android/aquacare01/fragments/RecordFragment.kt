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
import com.websarva.wings.android.aquacare01.*

class Recode : Fragment() {

    private val defVal = DefaultValues()
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
        val requireContext = requireContext()
        val sp = requireContext.getSharedPreferences(defVal.recSpFileName, Context.MODE_PRIVATE)

        var recordID = 0
        var fileName = sp.getString(defVal.recFileNameKey + recordID, "NoData") ?: "NoData"
        while (fileName != "NoData") {
            val date = sp.getString(defVal.recDateKey + recordID, "NoData") ?: "NoData"
            recordList.add(Diary(fileName, date))
            recordID++
            fileName = sp.getString(defVal.recFileNameKey + recordID, "NoData") ?: "NoData"
        }


        val adapter = RecordAdapter(recordList)

        //adapterのlisterを定義
        adapter.listener = object :RecordAdapter.Listener {
            override fun onClickBtn(index: Int) {
                adapter.recDeleteUpdate(index)
                var i = index
                var j = i + 1

                //ファイルを削除
                val dFileName = sp.getString(defVal.recFileNameKey + i, "NoData")
                requireContext.deleteFile(dFileName)

                //sharedPreferencesの該当データを削除
                sp.edit().remove(defVal.recFileNameKey + i).apply()
                sp.edit().remove(defVal.recDateKey + i).apply()
                Log.d("RecordFragment", "recordID$i has been deleted")

                //次のデータが埋まっていれば、今のデータに上書きするのを繰り返す
                var nextFileName = sp.getString(defVal.recFileNameKey + j, "NoData")
                while (nextFileName != "NoData") {
                    sp.edit().putString(defVal.recFileNameKey + i, sp.getString(defVal.recFileNameKey + j, "NoData")).apply()
                    sp.edit().putString(defVal.recDateKey + i, sp.getString(defVal.recDateKey + j, "NoData")).apply()
                    i++
                    j++
                    nextFileName = sp.getString(defVal.recFileNameKey + j, "NoData")
                    }

                //最後の番号のデータを削除する
                sp.edit().remove(defVal.recFileNameKey + i).apply()
                sp.edit().remove(defVal.recDateKey + i).apply()
                Log.d("RecordFragment", "recordID$i has been deleted")
                }
            }

        rvRecord.adapter = adapter
        val layoutManager = LinearLayoutManager(context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        rvRecord.layoutManager = layoutManager
    }
}