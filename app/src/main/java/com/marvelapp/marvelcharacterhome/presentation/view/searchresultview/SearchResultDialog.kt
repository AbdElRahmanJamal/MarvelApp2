package com.marvelapp.marvelcharacterhome.presentation.view.searchresultview


import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.marvelapprecview.MarvelCharactersAdapter
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewIntents
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchViewDialogIntents
import io.reactivex.Observable
import kotlinx.android.synthetic.main.search_result_dialog.*


class SearchResultDialog(context: Context) : Dialog(context) {

    private var searchResultMarvelCharactersAdapter: MarvelCharactersAdapter? = null

    companion object {
        private var INSTANCE: SearchResultDialog? = null
        fun getInstance(context: Context) = if (INSTANCE == null) SearchResultDialog(context) else INSTANCE
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

            initRecViewForSearchResultDialog()
        }

    }

    fun showSearchResultDialog() {
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

    fun getAdapter() = searchResultMarvelCharactersAdapter

    private fun initRecViewForSearchResultDialog() {
        if (searchResultMarvelCharactersAdapter == null)
            searchResultMarvelCharactersAdapter = MarvelCharactersAdapter(R.layout.marvel_search_result_ticket)

        val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(context.resources.getDrawable(R.drawable.partial_white_divider, null))

        marvel_character_recView_search_result.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultMarvelCharactersAdapter
            addItemDecoration(itemDecoration)
        }
    }

    fun setLoadingSearchDialogVisibility(visibility: Int) {
        search_dialog_loading_layout.visibility = visibility
    }

    fun handleSearchForCharacterDialogViewOnSuccessState(results: List<Results>) {
        setLoadingSearchDialogVisibility(View.GONE)
        searchResultMarvelCharactersAdapter!!.setMarvelCharacters(results)

    }

    fun onSearchFieldChanged(): Observable<MarvelCharactersSearchViewDialogIntents.SearchFieldChangeOfSearchDialogIntent> =
        RxTextView.textChanges(search_edit_txt).map {
            MarvelCharactersSearchViewDialogIntents
                .SearchFieldChangeOfSearchDialogIntent(it.toString())
        }

    fun onCloseButtonClicked(): Observable<MarvelCharactersHomeViewIntents.CloseButtonOfSearchDialogClickedIntent> =
        RxView.clicks(search_close_btn).map {
            MarvelCharactersHomeViewIntents.CloseButtonOfSearchDialogClickedIntent
        }
}

