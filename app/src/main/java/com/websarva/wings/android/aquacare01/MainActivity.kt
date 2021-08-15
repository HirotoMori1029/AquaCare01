package com.websarva.wings.android.aquacare01



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

//    EXTRAMESSAGE_test
const val EXTRA_MESSAGE ="testTestTest"

class MainActivity : AppCompatActivity() {

//    保存された画像のURI
    private var _imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ナビゲーションコントローラーを取得
        val navController = findNavController(R.id.fragmentContainerView)
        //ボトムナビゲーションをセット
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)

        }

    //ホームの画像をタップされたときの処理
    private val startForTookPhotoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            val aqImage = findViewById<ImageView>(R.id.aqImage)
            aqImage.setImageURI(_imageUri)
        }

    }

    fun onHomeImageClick(view: View) {
//        ファイル名を一意に作成する
//    Todo:Locale位置を考える
        val dataFormat = SimpleDateFormat("yyyyMMddhhmmss", Locale.JAPAN)
        val now = Date()
        val nowStr  = dataFormat.format(now)
        val fileName = "aqImagePhoto_{$nowStr}"

//        contentResolverを使ってUriオブジェクトを生成
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        _imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

//        intentオブジェクトを生成
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)
        startForTookPhotoResult.launch(intent)
    }

//    Alarmボタンが押されたときの処理
    fun onAlarmBtnClick (view: View){
        val message = "Add Task"
        val intent = Intent(this, AddTaskActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

}
