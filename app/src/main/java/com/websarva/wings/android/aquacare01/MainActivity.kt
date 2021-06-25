package com.websarva.wings.android.aquacare01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
//アクションバーを使う場合
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController

import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //ツールバーをセット
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //ナビゲーションコントローラーを取得
        val navController = findNavController(R.id.fragmentContainerView)
        //ボトムナビゲーション用を取得
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        //アプリケーションバー確認用の変数を用意
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.recode, R.id.notificationFragment))
        //アクションバーとナビゲーションコントローラーを連携
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //ボトムナビゲーションをセット
        bottomNavigationView.setupWithNavController(navController)

    }
}

