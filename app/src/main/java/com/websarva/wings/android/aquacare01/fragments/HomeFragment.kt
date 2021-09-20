package com.websarva.wings.android.aquacare01.fragments



import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
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
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class HomeFragment : Fragment() {

    private val fileName = "aquarium_home" + ".jpeg"
    private var displayBmp: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //各ビューを取得
        val aqImage = view.findViewById<ImageView>(R.id.aqImage)
        val addAlarmBtn = view.findViewById<Button>(R.id.addAlarmBtn)
        val addRecordBtn = view.findViewById<Button>(R.id.addRecordBtn)

        //fileがあればHome画面に表示させる
        displayBmp = readHomeImg(requireContext())
        if (displayBmp != null) {
            aqImage.setImageBitmap(displayBmp)
        }

        //共有ファイルの選択から返ってきた後の処理
        //選択された画像をリサイズした上で固有の内部ストレージに保存し、表示させる
        val startForSetBitmapResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    result.data?.data?.also { uri ->
                        val inputStream = requireContext().contentResolver.openInputStream(uri)
                        var gotBitmap = BitmapFactory.decodeStream(inputStream)
                        //縦が長ければ回転させる
                        if (gotBitmap.width <= gotBitmap.height) {
                            gotBitmap = rotateBitmap(gotBitmap)
                        }
                        val resizedBitmap = resizeBitmap(gotBitmap, aqImage)
                        saveImgFromBmp(resizedBitmap, requireContext())
                        displayBmp = readHomeImg(requireContext())
                        aqImage.setImageBitmap(displayBmp)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        //        HomeImageが押されたときの処理
        aqImage.setOnClickListener {
            val recIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startForSetBitmapResult.launch(recIntent)
        }

//        AddAlarmBtnが押されたときの処理
        addAlarmBtn.setOnClickListener {
            val message = "Add Task"
            val tskIntent = Intent(context, AddTaskActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(tskIntent)
        }

        //        AddRecodeBtnが押されたときの処理
        addRecordBtn.setOnClickListener {

        }
    }

    private fun saveImgFromBmp (bmp: Bitmap, context: Context) {
        try {
            val byteArrOutputStream = ByteArrayOutputStream()
            val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrOutputStream)
            fileOutputStream.write(byteArrOutputStream.toByteArray())
            fileOutputStream.close()
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
    }

    private fun readHomeImg(context: Context): Bitmap? {
        return try {
            val bufferedInputStream = BufferedInputStream(context.openFileInput(fileName))
            BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {

            e.printStackTrace()
            null
        }
    }

    private fun resizeBitmap (beforeResizeBmp: Bitmap, imageView: ImageView) :Bitmap {
        val resizeScale = beforeResizeBmp.width.toDouble() / imageView.width.toDouble()
        val resizedWidth = (beforeResizeBmp.width / resizeScale).toInt()
        val resizedHeight = (beforeResizeBmp.height / resizeScale).toInt()
        return Bitmap.createScaledBitmap(beforeResizeBmp, resizedWidth, resizedHeight, true)
    }

    private fun rotateBitmap (bitmap: Bitmap) :Bitmap {
        val matrix = Matrix()
        matrix.postRotate(-90F)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}