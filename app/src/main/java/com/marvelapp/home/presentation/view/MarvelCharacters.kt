package com.marvelapp.home.presentation.view


import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvelapp.R
import com.marvelapp.frameworks.apiservice.MarvelApiService
import com.marvelapp.frameworks.apiservice.interceptor.ConnectivityInterceptor
import com.marvelapp.home.data.MarvelCharactersDataStore
import com.marvelapp.home.data.MarvelCharactersRepository
import com.marvelapp.home.domain.GetMarvelCharactersUseCase
import com.marvelapp.home.presentation.mvilogic.MarvelCharactersViewIntents
import com.marvelapp.home.presentation.mvilogic.MarvelCharactersViewStates
import com.marvelapp.home.presentation.view.marvelCharactersRecView.EndlessOnScrollListener
import com.marvelapp.home.presentation.view.marvelCharactersRecView.MarvelCharactersAdapter
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
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
class MarvelCharacters : Fragment() {

    lateinit var marvelCharactersAdapter: MarvelCharactersAdapter
    private lateinit var marvelCharactersViewModel: MarvelCharactersViewModel
    private lateinit var marvelCharactersViewModelFactory: MarvelCharactersViewModelFactory
    private val disposables = CompositeDisposable()
    private val playerViewsIntent = BehaviorSubject.create<MarvelCharactersViewIntents>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                renderViewState(it)
            }
        disposables.add(dispose)
    }

    private fun renderViewState(state: MarvelCharactersViewStates) {
        when (state) {
            is MarvelCharactersViewStates.LoadingState -> {
                // handleLoadingState()
            }
            is MarvelCharactersViewStates.SuccessState -> {
                handleOnSuccessState(state)
            }
            is MarvelCharactersViewStates.ErrorState -> {
                //handleOnErrorState(state)
            }

            is MarvelCharactersViewStates.LoadMoreMarvelCharactersViewState -> {
                loading_layout.visibility = View.VISIBLE
            }
            is MarvelCharactersViewStates.HideLoadMoreViewState -> {
                loading_layout.visibility = View.GONE
            }
        }
    }

    private fun handleOnSuccessState(state: MarvelCharactersViewStates.SuccessState) {
        loading_layout.visibility = View.GONE
        marvelCharactersAdapter.setMarvelCharacters(state.marvelCharacters.data.results)
    }

    private fun getMarvelCharactersViewsIntents() = Observable.merge(onHomePageStart(), onLoadMoreMarvelCharacters())


    private fun onHomePageStart() = Observable.just(MarvelCharactersViewIntents.GetMarvelCharactersIntent)

    private fun onLoadMoreMarvelCharacters() = playerViewsIntent

    private fun initRecView() {
        marvelCharactersAdapter = MarvelCharactersAdapter()
        marvel_character_recView?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = marvelCharactersAdapter

            it.addOnScrollListener(object : EndlessOnScrollListener() {
                override fun onScrolledToEnd() {
                    playerViewsIntent.onNext(
                        MarvelCharactersViewIntents
                            .GetMoreMarvelCharactersIntent(offset = it.adapter!!.itemCount)
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
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }

        if (searchView != null) {
            searchForMovie(searchView, searchManager)
            changeSearchIconOfSearchView(searchView)
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

    private fun searchForMovie(
        searchView: SearchView,
        searchManager: SearchManager
    ) {
        val queryTextListener: SearchView.OnQueryTextListener
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

        queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("onQueryTextChange", newText)

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.i("onQueryTextSubmit", query)

                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onStop() {
        super.onStop()
        if (!disposables.isDisposed)
            disposables.dispose()
    }
}
