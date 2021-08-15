package com.websarva.wings.android.aquacare01

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var listener: OnTimeSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTimeSetListener) {
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
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    private fun getTimeString(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        return SimpleDateFormat("HH : mm").format(calendar.time)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

        val time = getTimeString(hourOfDay, minute)
        listener?.onTimeSelected(time, hourOfDay, minute)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    interface OnTimeSetListener {
        fun onTimeSelected(time: String, hourOfDay: Int, minute: Int)
    }

}

