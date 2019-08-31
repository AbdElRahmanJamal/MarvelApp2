package com.marvelapp.marvelcharacterhome.presentation.view


import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.marvelapp.MarvelBaseFragment
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.frameworks.apiservice.MarvelApiService
import com.marvelapp.frameworks.apiservice.interceptor.ConnectivityInterceptor
import com.marvelapp.marvelcharacterhome.data.MarvelCharactersDataStore
import com.marvelapp.marvelcharacterhome.data.MarvelCharactersRepository
import com.marvelapp.marvelcharacterhome.domain.GetMarvelCharactersUseCase
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewIntents
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewStates
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchDialogViewStates
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchViewDialogIntents
import com.marvelapp.marvelcharacterhome.presentation.view.marvelCharactersRecView.EndlessOnScrollListener
import com.marvelapp.marvelcharacterhome.presentation.view.marvelCharactersRecView.MarvelCharactersAdapter
import com.marvelapp.marvelcharacterhome.presentation.view.searchresultview.SearchResultDialog
import com.marvelapp.marvelcharacterhome.presentation.view.searchresultview.SearchResultMarvelCharactersAdapter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_home.*


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
    private var searchResultMarvelCharactersAdapter: SearchResultMarvelCharactersAdapter? = null
    private lateinit var marvelCharactersViewModel: MarvelCharactersViewModel
    private lateinit var marvelCharactersViewModelFactory: MarvelCharactersViewModelFactory
    private var disposables: CompositeDisposable? = null
    private val loadMoreMarvelCharacters = BehaviorSubject.create<MarvelCharactersHomeViewIntents>()
    private val onSearchIconClicked = BehaviorSubject.create<MarvelCharactersSearchViewDialogIntents>()
    private var searchResultDialog: Dialog? = null
    private var disposeSearchDialogObservable: Disposable? = null

    override fun getLayoutId() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables = CompositeDisposable()
        setMarvelCharactersHomeTittle()
        setHasOptionsMenu(true)
        initRecView()

        marvelCharactersViewModelFactory = MarvelCharactersViewModelFactory(
            GetMarvelCharactersUseCase(
                MarvelCharactersRepository(
                    MarvelCharactersDataStore(
                        MarvelApiService(ConnectivityInterceptor(activity!!.applicationContext))
                    )
                )
            )
        )

        marvelCharactersViewModel = ViewModelProviders.of(this, marvelCharactersViewModelFactory)
            .get(MarvelCharactersViewModel::class.java)


        val dispose = marvelCharactersViewModel
            .getMarvelHomePageCharacters(getMarvelCharactersViewsIntents())
            .subscribe {
                renderHomeViewState(it)
            }
        disposables!!.add(dispose)
    }

    private fun renderHomeViewState(state: MarvelCharactersHomeViewStates) {
        when (state) {

            is MarvelCharactersHomeViewStates.ShowLoadingMarvelCharactersState -> showLoadingIndicator()
            is MarvelCharactersHomeViewStates.SuccessState -> handleHomePageMarvelCharactersOnSuccessState(state)
            is MarvelCharactersHomeViewStates.ErrorState -> hideLoadingIndicator()
            is MarvelCharactersHomeViewStates.ShowLoadMoreMarvelCharactersViewState -> showLoadMoreLoading()
            is MarvelCharactersHomeViewStates.HideLoadMoreMarvelCharactersViewState -> hideLoadMoreLoading()


        }
    }

    private fun getMarvelCharactersViewsIntents() = Observable.merge(
        onHomePageStart(), onLoadMoreMarvelCharacters()
    )

    private fun onHomePageStart() = Observable.just(MarvelCharactersHomeViewIntents.LoadingMarvelCharactersIntent)

    private fun onLoadMoreMarvelCharacters() = loadMoreMarvelCharacters

    private fun handleHomePageMarvelCharactersOnSuccessState(state: MarvelCharactersHomeViewStates.SuccessState) {
        marvelCharactersAdapter.setMarvelCharacters(state.marvelCharacters.data.results)
        hideLoadMoreLoading()
        hideLoadingIndicator()
    }

    private fun setLoadingSearchDialogVisibility(visibility: Int) {
        searchResultDialog!!.findViewById<LinearLayout>(R.id.search_dialog_loading_layout).visibility =
            visibility
    }

    private fun handleSearchForCharacterDialogViewOnSuccessState(results: List<Results>) {
        setLoadingSearchDialogVisibility(View.GONE)
        searchResultMarvelCharactersAdapter!!.setMarvelCharactersSearchResult(results)

    }

    private fun getSearchDialogIntents() = Observable.merge(
        onSearchIconClickedIntent(),
        onSearchFieldChangeOfSearchDialog(),
        onCloseButtonOfSearchDialogClicked()
    )

    private fun onSearchIconClickedIntent() = onSearchIconClicked

    private fun onSearchFieldChangeOfSearchDialog(): Observable<MarvelCharactersSearchViewDialogIntents> {
        val searchFieldOfSearchDialog: TextView = searchResultDialog!!.findViewById(R.id.search_edit_txt)
        return RxTextView.textChanges(searchFieldOfSearchDialog).map {
            MarvelCharactersSearchViewDialogIntents
                .SearchFieldChangeOfSearchDialogIntent(it.toString())
        }
    }

    private fun onCloseButtonOfSearchDialogClicked(): Observable<MarvelCharactersSearchViewDialogIntents> {
        val searchCloseButtonOfSearchDialog: ImageView = searchResultDialog!!.findViewById(R.id.search_close_btn)
        return RxView.clicks(searchCloseButtonOfSearchDialog).map {
            MarvelCharactersSearchViewDialogIntents.CloseButtonOfSearchDialogClickedIntent
        }
    }

    private fun initRecView() {
        marvelCharactersAdapter = MarvelCharactersAdapter()
        marvel_character_recView?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = marvelCharactersAdapter

            it.addOnScrollListener(object : EndlessOnScrollListener() {
                override fun onScrolledToEnd() {
                    loadMoreMarvelCharacters.onNext(
                        MarvelCharactersHomeViewIntents
                            .LoadingMoreMarvelCharactersIntent(offset = it.adapter!!.itemCount)
                    )
                }
            })
        }
    }


    private fun initRecViewForSearchResultDialog() {
        if (searchResultMarvelCharactersAdapter == null)
            searchResultMarvelCharactersAdapter = SearchResultMarvelCharactersAdapter()

        val itemDecoration = DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(context!!.resources.getDrawable(R.drawable.partial_white_divider, null))

        val marvelCharacterRecViewSearchResult =
            searchResultDialog!!.findViewById<RecyclerView>(R.id.marvel_character_recView_search_result)
        marvelCharacterRecViewSearchResult?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = searchResultMarvelCharactersAdapter
            it.addItemDecoration(itemDecoration)
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
        searchIcon.setOnClickListener {
            searchResultDialog = SearchResultDialog.getInstance(context!!)
            onSearchIconClicked.onNext(MarvelCharactersSearchViewDialogIntents.SearchIconClickedIntent)
            initRecViewForSearchResultDialog()
            handleSearchDialogViewStates()
        }
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
            is MarvelCharactersSearchDialogViewStates.ShowSearchResultDialogState -> showSearchResultDialog()
            is MarvelCharactersSearchDialogViewStates.ShowLoadingIndicatorSearchForCharacterState -> setLoadingSearchDialogVisibility(
                View.VISIBLE
            )
            is MarvelCharactersSearchDialogViewStates.HideLoadingIndicatorSearchForCharacterState -> setLoadingSearchDialogVisibility(
                View.GONE
            )
            is MarvelCharactersSearchDialogViewStates.CloseSearchResultDialogState -> hideSearchResultDialog()
            is MarvelCharactersSearchDialogViewStates.SuccessForSearchResultState ->
                handleSearchForCharacterDialogViewOnSuccessState(state.marvelCharacters.data.results)
            is MarvelCharactersSearchDialogViewStates.EmptyStateSearchResultDialog ->
                handleSearchForCharacterDialogViewOnSuccessState(emptyList())
        }

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

    private fun showSearchResultDialog() {
        searchResultDialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    private fun hideSearchResultDialog() {
        if (disposeSearchDialogObservable != null) disposeSearchDialogObservable!!.dispose()
        searchResultDialog?.let {
            it.dismiss()
            searchResultDialog = null
        }
    }
    override fun onStop() {
        super.onStop()
        if (!disposables!!.isDisposed)
            disposables!!.dispose()
    }
}
