package com.websarva.wings.android.aquacare01

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.IOException

class ImageLoader {

    fun readImgFromFileName(fileName: String, context: Context): Bitmap? {
        return try {
            val bufferedInputStream = BufferedInputStream(context.openFileInput(fileName))
            BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}