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

            //toDo 編集の余地あり
//            builder.setItems(R.array.image_selected_option_list/*, DialogButtonClickLister()*/) {
//                dialog, which ->
//                Toast.makeText(context, "${which}が入っている", Toast.LENGTH_SHORT).show()
//            }
            builder.setNegativeButton(R.string.dialog_cancel, DialogButtonClickLister())
            builder.create()
        }
        return dialog ?: throw IllegalStateException("activity is null!")
    }

    private inner class DialogButtonClickLister : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            var msg = ""
//            タップされたアクションで分岐
            when(which) {
                DialogInterface.BUTTON_NEGATIVE ->
                    msg = getString(R.string.dialog_clicked)
            }

            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}