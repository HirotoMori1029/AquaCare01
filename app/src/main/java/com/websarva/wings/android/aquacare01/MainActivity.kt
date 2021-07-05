package com.websarva.wings.android.aquacare01

import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
//非推奨startActivityForResult解決のため
//import androidx.activity.result.contract.ActivityResultContracts

import androidx.navigation.findNavController

import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

//よくわからない
//import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ナビゲーションコントローラーを取得
        val navController = findNavController(R.id.fragmentContainerView)
        //ボトムナビゲーション用を取得
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)

        val aqImage = findViewById<ImageView>(R.id.aqImage)
        val imageSelectedLister = ImageSelectedLister()
        aqImage.setOnClickListener(imageSelectedLister)
    }

    private inner class ImageSelectedLister : View.OnClickListener {
        override fun onClick(view: View) {
            val dialogFragment = ImageSelectedDialogFragment()
            dialogFragment.show(supportFragmentManager, "ImageSelectedDialogFragment")
        }


    //  ここから画像を押されたときの処理

//    //保存された画像のURIを定義
//    private var _imageUri: Uri? = null
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode == 200 && resultCode == RESULT_OK) {
//            val aqImage = findViewById<ImageView>(R.id.aqImage)
//            aqImage.setImageURI(_imageUri)
//        }
//    }
////    画像が押されたときにカメラを起動する。非推奨のためあとで変更する
//    fun onCameraImageClick(view: View) {
//        //日時データを形成するフォーマッタを設定 Localeを設定する？
//        val dataFormat = SimpleDateFormat("yyyyMMddHHmmss")
////        日付を取得
//        val now = Date()
////        取得した日時データを文字列に変換し取得
//        val nowStr = dataFormat.format(now)
////        ストレージに格納する画像のファイル名を生成。ファイル名を一意に確保するためにタイムスタンプの値を使用
//        val fileName = "aquariumImagePhoto_${nowStr}.jpg"
////        ContentValueオブジェクトを生成
//        val values = ContentValues()
////        画像ファイル名を設定
//        values.put(MediaStore.Images.Media.TITLE, fileName)
////        画像ファイルの種類を設定
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
////        ContentResolverを使ってURIオブジェクト生成
//        _imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
////        Intentオブジェクトを生成
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////        Extra情報として、_imageUriを設定
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)
////        アクティビティを起動
//        startActivityForResult(intent, 200)
//    }

}



}


