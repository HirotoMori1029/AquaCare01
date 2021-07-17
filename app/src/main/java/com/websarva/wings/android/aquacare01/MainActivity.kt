package com.websarva.wings.android.aquacare01


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ナビゲーションコントローラーを取得
        val navController = findNavController(R.id.fragmentContainerView)
        //ボトムナビゲーションをセット
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)

//        HomeのImageViewにリスナを設定
        val aqImage = findViewById<ImageView>(R.id.aqImage)
        val imageSelectedLister = ImageSelectedLister()
        aqImage.setOnClickListener(imageSelectedLister)


    }
//画像がタップされたときのリスナ
    private inner class ImageSelectedLister : View.OnClickListener {
        override fun onClick(view: View) {
            val dialogFragment = ImageSelectedDialogFragment()
            dialogFragment.show(supportFragmentManager, "ImageSelectedDialogFragment")
        }
}

}




