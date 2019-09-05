package com.marvelapp.marvelcharacterhome.presentation.view


import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.marvelapp.MarvelBaseFragment
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.frameworks.apiservice.apiFactory
import com.marvelapp.marvelapprecview.EndlessOnScrollListener
import com.marvelapp.marvelapprecview.MarvelCharactersAdapter
import com.marvelapp.marvelcharacterhome.data.MarvelCharactersDataStore
import com.marvelapp.marvelcharacterhome.data.MarvelCharactersRepository
import com.marvelapp.marvelcharacterhome.domain.GetMarvelCharactersUseCase
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewIntents
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewStates
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchDialogViewStates
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchViewDialogIntents
import com.marvelapp.marvelcharacterhome.presentation.view.searchresultview.SearchResultDialog
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin
import org.koin.standalone.StandAloneContext.stopKoin


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MarvelCharacters.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MarvelCharacters.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MarvelCharacters : MarvelBaseFragment() {


    private lateinit var marvelCharactersAdapter: MarvelCharactersAdapter

    private lateinit var marvelCharactersViewModel: MarvelCharactersViewModel
    private lateinit var marvelCharactersViewModelFactory: MarvelCharactersViewModelFactory
    private var disposables: CompositeDisposable? = null
    private val loadMoreMarvelCharacters = PublishSubject.create<MarvelCharactersHomeViewIntents>()
    private val onSearchIconClicked = PublishSubject.create<MarvelCharactersHomeViewIntents>()
    private var searchResultDialog: SearchResultDialog? = null
    private var disposeSearchDialogObservable: Disposable? = null

    override fun getLayoutId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables = CompositeDisposable()
        setMarvelCharactersHomeTittle()
        setHasOptionsMenu(true)
        initRecView()
        searchResultDialog = SearchResultDialog.getInstance(context!!)
        startKoin(activity!!.applicationContext, listOf(apiFactory))

        marvelCharactersViewModelFactory = MarvelCharactersViewModelFactory(
            GetMarvelCharactersUseCase(
                MarvelCharactersRepository(
                    MarvelCharactersDataStore(
                        get()
                    )
                )
            )
        )

        marvelCharactersViewModel = ViewModelProviders.of(this, marvelCharactersViewModelFactory)
            .get(MarvelCharactersViewModel::class.java)

        disposables!!.add(marvelCharactersViewModel
            .getMarvelHomePageCharacters(getMarvelCharactersViewsIntents())
            .subscribe {
                renderHomeViewState(it)
            })
    }

    private fun renderHomeViewState(state: MarvelCharactersHomeViewStates) {
        when (state) {
            is MarvelCharactersHomeViewStates.ShowSearchResultDialogState -> handleOnSearchIconShown()
            is MarvelCharactersHomeViewStates.CloseSearchResultDialogState -> handleCloseButtonSearchDialogActions()
            is MarvelCharactersHomeViewStates.ShowLoadingMarvelCharactersState -> showLoadingIndicator()
            is MarvelCharactersHomeViewStates.SuccessState -> handleHomePageMarvelCharactersOnSuccessState(state)
            is MarvelCharactersHomeViewStates.ErrorState -> hideLoadingIndicator()
            is MarvelCharactersHomeViewStates.ShowLoadMoreMarvelCharactersViewState -> showLoadMoreLoading()
            is MarvelCharactersHomeViewStates.HideLoadMoreMarvelCharactersViewState -> hideLoadMoreLoading()
            is MarvelCharactersHomeViewStates.GoToMarvelCharacterDetailsPageState -> goToMarvelDetailsPage(state.marvelCharacter)

        }
    }

    private fun handleOnSearchIconShown() {
        searchResultDialog!!.showSearchResultDialog()
        handleSearchDialogViewStates()
    }

    private fun goToMarvelDetailsPage(marvelCharacter: Results) {
        val goToMarvelDetails = MarvelCharactersDirections.actionFromHomeToMarvelCharacterDetails(marvelCharacter)
        Navigation.findNavController(view!!).navigate(goToMarvelDetails)
    }

    private fun getMarvelCharactersViewsIntents() = Observable.merge(
        onHomePageStart(),
        Observable.merge(onSearchIconClickedIntent(), onCloseButtonOfSearchDialogClicked()),
        onLoadMoreMarvelCharacters(),
        onRecViewItemClicked()
    )

    private fun onHomePageStart() = Observable.just(MarvelCharactersHomeViewIntents.LoadingMarvelCharactersIntent)

    private fun onSearchIconClickedIntent() = onSearchIconClicked

    private fun onCloseButtonOfSearchDialogClicked() = searchResultDialog!!.onCloseButtonClicked()

    private fun onLoadMoreMarvelCharacters() = loadMoreMarvelCharacters

    private fun onRecViewItemClicked() = marvelCharactersAdapter.getGoToMarvelDetailsPageIntent()

    private fun handleHomePageMarvelCharactersOnSuccessState(state: MarvelCharactersHomeViewStates.SuccessState) {
        marvelCharactersAdapter.setMarvelCharacters(state.marvelCharacters.data.results)
        hideLoadMoreLoading()
        hideLoadingIndicator()
    }

    private fun getSearchDialogIntents() = Observable.merge(
        onRecViewItemClickedSearchDialog(),
        onSearchFieldChangeOfSearchDialog()
    )

    private fun onRecViewItemClickedSearchDialog() =
        searchResultDialog!!.getAdapter()!!.getGoToMarvelDetailsPageIntentFromSearchDialog()


    private fun onSearchFieldChangeOfSearchDialog(): Observable<MarvelCharactersSearchViewDialogIntents.SearchFieldChangeOfSearchDialogIntent> =
        searchResultDialog!!.onSearchFieldChanged()

    private fun initRecView() {
        marvelCharactersAdapter = MarvelCharactersAdapter(R.layout.marvel_home_ticket)
        marvel_character_recView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = marvelCharactersAdapter

            addOnScrollListener(object : EndlessOnScrollListener() {
                override fun onScrolledToEnd() {
                    loadMoreMarvelCharacters.onNext(
                        MarvelCharactersHomeViewIntents
                            .LoadingMoreMarvelCharactersIntent(offset = adapter!!.itemCount)
                    )
                }
            })
        }

    }

    private fun setMarvelCharactersHomeTittle() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = null
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_menu, menu)

        var searchView: SearchView? = null
        val searchItem = menu?.findItem(R.id.search)

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }

        searchView?.let {
            changeSearchIconOfSearchView(it)
            changeTextViewBackGroundOfSearchView(it)
            handleOnSearchIconClicked(it)

        }

    }


    private fun handleOnSearchIconClicked(searchView: SearchView) {
        val searchIcon: ImageView = searchView.findViewById(R.id.search_button)
        RxView.clicks(searchIcon).map {
            MarvelCharactersHomeViewIntents.SearchIconClickedIntent
        }.subscribe(onSearchIconClicked)
    }

    private fun handleSearchDialogViewStates() {
        disposeSearchDialogObservable = marvelCharactersViewModel
            .getMarvelCharactersSearchDialogResult(getSearchDialogIntents())
            .subscribe {
                renderSearchDialogViewState(it)
            }
    }

    private fun renderSearchDialogViewState(state: MarvelCharactersSearchDialogViewStates) {
        when (state) {
            is MarvelCharactersSearchDialogViewStates.ShowLoadingIndicatorSearchForCharacterState ->
                searchResultDialog!!.setLoadingSearchDialogVisibility(View.VISIBLE)
            is MarvelCharactersSearchDialogViewStates.HideLoadingIndicatorSearchForCharacterState ->
                searchResultDialog!!.setLoadingSearchDialogVisibility(View.GONE)
            is MarvelCharactersSearchDialogViewStates.SuccessForSearchResultState ->
                searchResultDialog!!.handleSearchForCharacterDialogViewOnSuccessState(state.marvelCharacters.data.results)
            is MarvelCharactersSearchDialogViewStates.EmptyStateSearchResultDialog ->
                searchResultDialog!!.handleSearchForCharacterDialogViewOnSuccessState(emptyList())
            is MarvelCharactersSearchDialogViewStates.GoToMarvelCharacterDetailsPageState -> {
                handleGoToDetailsPageSearchDialog(state)
            }
        }

    }

    private fun handleGoToDetailsPageSearchDialog(state: MarvelCharactersSearchDialogViewStates.GoToMarvelCharacterDetailsPageState) {
        disposeSearchDialogObservable!!.dispose()
        goToMarvelDetailsPage(state.marvelCharacter)
        searchResultDialog!!.hideSearchResultDialog()
    }

    private fun handleCloseButtonSearchDialogActions() {
        disposeSearchDialogObservable!!.dispose()
        searchResultDialog!!.hideSearchResultDialog()

    }

    private fun changeSearchIconOfSearchView(searchView: SearchView) {
        val searchIcon: ImageView = searchView.findViewById(R.id.search_button)
        searchIcon.setImageDrawable(
            ContextCompat.getDrawable(
                activity!!,
                R.drawable.ic_search_magnifier_interface_symbol
            )
        )
    }

    private fun changeTextViewBackGroundOfSearchView(searchView: SearchView) {
        val searchTextView: TextView = searchView.findViewById(R.id.search_src_text)
        searchTextView.setHint(R.string.search_world)
        searchTextView.setTextColor(Color.parseColor("#000000"))
        searchTextView.setBackgroundResource(R.drawable.rectangle)
    }

    override fun onStop() {
        super.onStop()
        stopKoin()
        searchResultDialog = null
        if (!disposables!!.isDisposed) {
            disposables!!.dispose()
            disposables!!.clear()
        }
    }
}
