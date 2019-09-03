package com.marvelapp.marvelcharacterdetails.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvelapp.MarvelBaseFragment
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.frameworks.downloadImage
import com.marvelapp.marvelcharacterdetails.data.MarvelCharacterDetailsDataStore
import com.marvelapp.marvelcharacterdetails.data.MarvelCharacterDetailsRepository
import com.marvelapp.marvelcharacterdetails.domain.GetMarvelCharacterDetailsUseCase
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsPageViewIntents
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsViewStates
import com.marvelapp.marvelcharacterdetails.presentation.view.detailspageadapter.MarvelCharacterDetailsPageAdapter
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.marvel_character_details_fragment.*
import org.koin.android.ext.android.get

class MarvelCharacterDetails : MarvelBaseFragment() {

    override fun getLayoutId() = R.layout.marvel_character_details_fragment

    private lateinit var viewModel: MarvelCharacterDetailsViewModel
    private lateinit var marvelCharacterDetailsViewModelFactory: MarvelCharacterDetailsViewModelFactory
    private lateinit var marvelCharacterDetailsPageAdapter: MarvelCharacterDetailsPageAdapter
    private lateinit var marvelCharacterDetailsArgs: MarvelCharacterDetailsArgs
    private lateinit var marvelCharacterData: Results

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        arguments?.let {
            //this line to get data from safe args
            marvelCharacterDetailsArgs = MarvelCharacterDetailsArgs.fromBundle(it)
            marvelCharacterData = marvelCharacterDetailsArgs.marvelCharacterData
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        marvelCharacterDetailsViewModelFactory = MarvelCharacterDetailsViewModelFactory(
                GetMarvelCharacterDetailsUseCase(
                        MarvelCharacterDetailsRepository(MarvelCharacterDetailsDataStore(get()))
                )
        )

        viewModel = ViewModelProviders.of(this, marvelCharacterDetailsViewModelFactory)
                .get(MarvelCharacterDetailsViewModel::class.java)

        initRecView()


        character_image_details_page?.let {
            character_image_details_page.visibility = View.VISIBLE
            val charactersImage = marvelCharacterData.thumbnail
            val imageURL = charactersImage!!.path + "." + charactersImage.extension

            downloadImage(context!!, imageURL, character_image_details_page)
        }
        marvelCharacterData.description?.let { description ->
            character_name_title_page.visibility = View.VISIBLE
            character_name_content_page.visibility = View.VISIBLE
            if (description.isNotEmpty()) {
                character_desc_title_details_page.visibility = View.VISIBLE
                character_desc_content_details_page.visibility = View.VISIBLE
            }

            character_name_title_page.text = getString(R.string.name)
            character_name_content_page.text = marvelCharacterData.title
                    ?: marvelCharacterData.name

            character_desc_content_details_page.text = marvelCharacterData.description
            character_desc_title_details_page.text = getString(R.string.description)
        }

        viewModel.startMarvelCharacterDetailsPage(getMarvelCharacterDetailsIntent())
                .subscribe(Consumer {
                    renderMarvelCharacterDetailsPageView(it)
                })


    }

    private fun renderMarvelCharacterDetailsPageView(state: MarvelCharactersDetailsViewStates) {
        when (state) {

            is MarvelCharactersDetailsViewStates.OnLoadingPageDetailsState -> show_more_loading.visibility = View.VISIBLE
            is MarvelCharactersDetailsViewStates.OnSuccessPageDetailsState -> {
                marvelCharacterDetailsPageAdapter.setMarvelCharactersDetailsPageData(state.marvelCharacterDetailsModel.getMarvelDetailsModel())
                show_more_loading.visibility = View.GONE
            }
            is MarvelCharactersDetailsViewStates.OnErrorPageDetailsState -> show_more_loading.visibility = View.GONE

        }
    }

    private fun getMarvelCharacterDetailsIntent() = onDetailsPageStart()

    private fun initRecView() {
        marvelCharacterDetailsPageAdapter = MarvelCharacterDetailsPageAdapter()
        marvel_character_details_recView?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = marvelCharacterDetailsPageAdapter
        }

    }

    private fun onDetailsPageStart() = Observable.just(MarvelCharactersDetailsPageViewIntents.OnDetailsPageStartIntent(marvelCharacterData.id))

}
