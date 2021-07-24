package com.websarva.wings.android.aquacare01.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.websarva.wings.android.aquacare01.R

class SettingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        Todo:後で編集する
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val intent = activity?.intent
        val extras = intent?.extras

//        設定タイトルと設定文を表示
        val settingsTitleT = extras?.getString("settingsTitle")
        val settingsDescT = extras?.getString("settingsDesc")

        val tvSettingsTitle = view.findViewById<TextView>(R.id.settings_row)
        val tvSettingsDescT = view.findViewById<TextView>(R.id.settings_description)

        return view
    }

}