package com.websarva.wings.android.aquacare01

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: OnDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateSetListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + "must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    private fun getDateString(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return SimpleDateFormat("yyyy / MM / dd ").format(calendar.time)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val date = getDateString(year, month, day)
        listener?.onDateSelected(date, year, month, day)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    interface OnDateSetListener {
        fun onDateSelected(date: String, year: Int, month: Int, day: Int)
    }

}
