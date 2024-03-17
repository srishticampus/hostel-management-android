package com.project.hostelmate.preference

import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.project.hostelmate.R

fun Toast.showCustomToastError(message: String, activity: Activity)
{
    val layout = activity.layoutInflater.inflate (
        R.layout.activity_custom_toast,
        activity.findViewById(R.id.toast_container)
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    // use the application extension function
    this.apply {
        setGravity(Gravity.BOTTOM, 0, 40)
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}