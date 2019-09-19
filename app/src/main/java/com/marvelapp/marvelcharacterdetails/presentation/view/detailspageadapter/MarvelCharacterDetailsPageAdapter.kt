package com.marvelapp.marvelcharacterdetails.presentation.view.detailspageadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.marvelapprecview.MarvelCharactersAdapter
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsPageViewIntents
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.marvel_character_details_ticket.view.*


class MarvelCharacterDetailsPageAdapter :
    RecyclerView.Adapter<MarvelCharacterDetailsPageAdapter.MarvelCharactersViewHolder>() {


    private var marvelCharactersDetailsPageData: MutableMap<String, List<Results>> = mutableMapOf()
    private lateinit var context: Context
    private val showMarvelCharacterImages =
        PublishSubject.create<MarvelCharactersDetailsPageViewIntents>()
    private var disposable: Disposable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context

        val view = inflater.inflate(R.layout.marvel_character_details_ticket, parent, false)
        return MarvelCharactersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarvelCharactersViewHolder, position: Int) {
        val marvelCharacterDetailsTitle = marvelCharactersDetailsPageData.keys.toList()[position]
        val marvelCharacterDetailsResultsList = marvelCharactersDetailsPageData.values.toList()[position]
        val marvelCharactersAdapter = MarvelCharactersAdapter(R.layout.marvel_character_details_single_row_ticket)

        if (marvelCharacterDetailsResultsList.isNotEmpty()) {
            holder.detailsPageTitle.text = marvelCharacterDetailsTitle


            marvelCharactersAdapter.apply {
                setMarvelCharacters(marvelCharacterDetailsResultsList)
            }
            holder.detailsPageRecView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = marvelCharactersAdapter
            }
            val subscribe = marvelCharactersAdapter.getShowMarvelCharacterImages().subscribe {
                showMarvelCharacterImages.onNext(
                    MarvelCharactersDetailsPageViewIntents.OnDetailItemClickedIntent(
                        marvelCharactersDetailsPageData.values.toList()[position],
                        position
                    )
                )
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (disposable!!.isDisposed)
            disposable!!.dispose()

    }

    fun getShowMarvelCharacterImages() =
        showMarvelCharacterImages

    internal fun setMarvelCharactersDetailsPageData(marvelCharacters: MutableMap<String, List<Results>>) {

        this.marvelCharactersDetailsPageData.clear()
        this.marvelCharactersDetailsPageData.putAll(marvelCharacters)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return marvelCharactersDetailsPageData.size
    }

    inner class MarvelCharactersViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val detailsPageTitle: TextView = itemView.recView_title
        val detailsPageRecView: RecyclerView = itemView.marvel_character_recView

    }
}

