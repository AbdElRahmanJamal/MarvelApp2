package com.marvelapp.marvelcharacterdetails.presentation.view.marvelcharacterimagesdialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.entities.Thumbnail
import com.marvelapp.marvelapprecview.MarvelCharactersAdapter
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsPageViewIntents
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.marvel_character_images.*
import kotlinx.android.synthetic.main.search_result_dialog.search_close_btn


class MarvelCharacterImagesDialog(context: Context) : Dialog(context) {

    private val onImageScroll = PublishSubject.create<MarvelCharactersDetailsPageViewIntents>()
    private var marvelCharactersAdapter: MarvelCharactersAdapter =
        MarvelCharactersAdapter(R.layout.marvel_character_mages_ticket)

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

    fun showDialog() {
        this.apply {
            if (!isShowing) {
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

        }
        val marvelCharacters: MutableList<Results> = mutableListOf()
        marvelCharacters.add(
            Results(
                id = 0,
                title = "abdo",
                name = "",
                description = "",
                modified = "",
                thumbnail = Thumbnail(
                    "https://image.shutterstock.com/z/stock-photo-jordan-henderson-of-liverpool-celebrates-with-team-mates-and-the-trophy-tottenham-hotspur-v-1418830925",
                    "jpg"
                ),
                resourceURI = "",
                comics = null,
                series = null,
                stories = null,
                events = null,
                urls = null
            )
        )
        marvelCharacters.add(
            Results(
                id = 0,
                title = "abdo",
                name = "",
                description = "",
                modified = "",
                thumbnail = Thumbnail(
                    "https://image.shutterstock.com/z/stock-photo-jordan-henderson-of-liverpool-celebrates-with-team-mates-and-the-trophy-tottenham-hotspur-v-1418830925",
                    "jpg"
                ),
                resourceURI = "",
                comics = null,
                series = null,
                stories = null,
                events = null,
                urls = null
            )
        )
        marvelCharacters.add(
            Results(
                id = 0,
                title = "abdo",
                name = "",
                description = "",
                modified = "",
                thumbnail = Thumbnail(
                    "https://image.shutterstock.com/z/stock-photo-jordan-henderson-of-liverpool-celebrates-with-team-mates-and-the-trophy-tottenham-hotspur-v-1418830925",
                    "jpg"
                ),
                resourceURI = "",
                comics = null,
                series = null,
                stories = null,
                events = null,
                urls = null
            )
        )
        marvelCharacters.add(
            Results(
                id = 0,
                title = "abdo",
                name = "",
                description = "",
                modified = "",
                thumbnail = Thumbnail(
                    "https://image.shutterstock.com/z/stock-photo-jordan-henderson-of-liverpool-celebrates-with-team-mates-and-the-trophy-tottenham-hotspur-v-1418830925",
                    "jpg"
                ),
                resourceURI = "",
                comics = null,
                series = null,
                stories = null,
                events = null,
                urls = null
            )
        )

        marvelCharactersAdapter.setMarvelCharacters(marvelCharacters)
    }

    fun onCloseButtonClicked(): Observable<MarvelCharactersDetailsPageViewIntents.CloseButtonOfSearchDialogClickedIntent> =
        RxView.clicks(search_close_btn).map {
            MarvelCharactersDetailsPageViewIntents.CloseButtonOfSearchDialogClickedIntent
        }

    fun onScrollItem() = onImageScroll
}

