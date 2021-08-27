package com.websarva.wings.android.aquacare01.fragments


import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.websarva.wings.android.aquacare01.AddTaskActivity
import com.websarva.wings.android.aquacare01.EXTRA_MESSAGE
import com.websarva.wings.android.aquacare01.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    //    保存された画像のURI
    private var _imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //カメラから帰ってきたときの処理
        val startForTookPhotoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                val aqImage = view.findViewById<ImageView>(R.id.aqImage)
                aqImage.setImageURI(_imageUri)
            }
        }

        //        Homeボタンが押されたときの処理
        val aqImage = view.findViewById<ImageView>(R.id.aqImage)
        aqImage.setOnClickListener {
            //        ファイル名を一意に作成する
            val dataFormat = SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault())
            val now = Date()
            val nowStr  = dataFormat.format(now)
            val fileName = "aqImagePhoto_{$nowStr}"

//        contentResolverを使ってUriオブジェクトを生成
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            _imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

//        intentオブジェクトを生成
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)
            startForTookPhotoResult.launch(intent)
        }

//        AddAlarmが押されたときの処理
        val addAlarmBtn = view.findViewById<Button>(R.id.addAlarmBtn)
        addAlarmBtn.setOnClickListener {
            //    Alarmボタンが押されたときの処理
            val message = "Add Task"
            val intent = Intent(context, AddTaskActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }

//        addRecodeBtnが押されたときの処理
        val addRecodeBtn = view.findViewById<Button>(R.id.addRecodeBtn)
        addRecodeBtn.setOnClickListener {
            val sp = requireActivity().getSharedPreferences("savedTaskInAquariumCare", Context.MODE_PRIVATE)
            sp.edit().clear().apply()
        }

    }
}