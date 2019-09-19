package com.marvelapp.marvelcharacterdetails.presentation.view.marvelcharacterimagesdialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.jakewharton.rxbinding2.view.RxView
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.marvelapprecview.MarvelCharactersAdapter
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsPageViewIntents
import io.reactivex.Observable
import kotlinx.android.synthetic.main.marvel_character_images.*
import kotlinx.android.synthetic.main.search_result_dialog.search_close_btn


class MarvelCharacterImagesDialog(context: Context) : Dialog(context) {

    private var marvelCharactersAdapter: MarvelCharactersAdapter =
        MarvelCharactersAdapter(R.layout.marvel_character_mages_ticket)
    private var marvelCharacters: MutableMap<String, List<Results>>? = null
    private var position: Int = 0

    companion object {
        private var INSTANCE: MarvelCharacterImagesDialog? = null
        fun getInstance(context: Context) = if (INSTANCE == null) MarvelCharacterImagesDialog(context) else INSTANCE
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.marvel_character_images)
        setCancelable(false)
        window?.let {
            window!!.setBackgroundDrawableResource(R.color.black_75_opacity)
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

            initViewPager()
        }

    }

    fun showDialog(marvelCharacters: MutableMap<String, List<Results>>?, position: Int) {
        this.marvelCharacters = marvelCharacters
        this.position = position
        this.apply {
            if (!isShowing) {
                marvelCharactersAdapter.setMarvelCharacters(marvelCharacters!!.values.toList()[position])
                show()
            }
        }
    }

    fun hideSearchResultDialog() {
        this.apply {
            dismiss()
        }
    }


    private fun initViewPager() {

        images_rec_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = marvelCharactersAdapter
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    fun onCloseButtonClicked(): Observable<MarvelCharactersDetailsPageViewIntents.CloseButtonOfSearchDialogClickedIntent> =
        RxView.clicks(search_close_btn).map {
            MarvelCharactersDetailsPageViewIntents.CloseButtonOfSearchDialogClickedIntent
        }

}

