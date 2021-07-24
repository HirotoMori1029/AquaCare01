package com.websarva.wings.android.aquacare01



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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

class MainActivity : AppCompatActivity() {

//    保存された画像のURI
    private var _imageUri: Uri? = null

//    Notificationに使用する定数を定義
    private var alarmManager: AlarmManager? = null
    private var pending: PendingIntent? = null
    private val requestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ナビゲーションコントローラーを取得
        val navController = findNavController(R.id.fragmentContainerView)
        //ボトムナビゲーションをセット
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)


    }

    private val startForTookPhotoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == RESULT_OK) {
            val aqImage = findViewById<ImageView>(R.id.aqImage)
            aqImage.setImageURI(_imageUri)
        }

    }

//ホームの画像をタップされたときの処理
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

//    AddAlertButtonが押されたときの処理
    fun onAlertBtnClick(view: View) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
//        10sec足す
        calendar.add(Calendar.SECOND, 10)
//        intentを生成
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        intent.putExtra("RequestCode", requestCode)
        pending = PendingIntent.getBroadcast(applicationContext, requestCode, intent,0)

//        アラームをセット
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(alarmManager != null) {
            alarmManager?.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
            //            トーストで設定されたことを表示する
            Toast.makeText(applicationContext, "alarm start", Toast.LENGTH_SHORT).show()
        }

    }

}
