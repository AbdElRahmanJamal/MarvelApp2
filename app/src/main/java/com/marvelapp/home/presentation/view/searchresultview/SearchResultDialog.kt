package com.marvelapp.home.presentation.view.searchresultview


import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.marvelapp.R


class SearchResultDialog(context: Context) : Dialog(context) {

    companion object {
        fun getInstance(context: Context) = SearchResultDialog(context)
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.search_result_dialog)
        setCancelable(false)
        window?.let {
            window!!.setBackgroundDrawableResource(R.color.black_75_opacity)
            window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            )
        }
    }
}

