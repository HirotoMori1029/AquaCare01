package com.websarva.wings.android.aquacare01

import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import java.lang.IllegalStateException


class ImageSelectedDialogFragment : DialogFragment() {

    override fun onCreateDialog (savedInstanceStore: Bundle?): Dialog {
//        アクティビティがnullでないならばダイアログオブジェクトを生成
        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.image_clicked_option_title)
            builder.setItems(R.array.image_selected_option_list, DialogButtonClickListener())
            builder.setNegativeButton(R.string.dialog_cancel, DialogButtonClickListener())
            builder.create()
        }
        return dialog ?: throw IllegalStateException("activity is null!")
    }

    private inner class DialogButtonClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            var msg = ""
//            タップされたアクションで分岐
            when(which) {
                DialogInterface.BUTTON_NEGATIVE ->
                    msg = "negativeボタンが選択されました"
                0 ->
                    msg = "0が選択されました"
//                <カメラを起動する処理はここでいいですか？>

                1 ->
                    msg = "1が選択されました"
            }

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}