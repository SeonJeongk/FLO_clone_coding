package com.example.flo.ui.song

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.flo.R

class CustomToast(context: Context, message: String) : Toast(context) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.toast_like, null)
        view.findViewById<TextView>(R.id.toast_tv).apply {
            text = message
        }
        setView(view)
        setGravity(Gravity.FILL_HORIZONTAL, 0, 650)
        duration = LENGTH_SHORT
    }
}