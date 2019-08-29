package com.marvelapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager


class MarvelLoadingDialog(context: Context) : Dialog(context) {

    companion object {
        fun getInstance(context: Context) = MarvelLoadingDialog(context)
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.marvel_loading_dialog)
        setCancelable(false)
        window?.let {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT)
        }
    }
}

