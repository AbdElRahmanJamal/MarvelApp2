package com.marvelapp.marvelapprecview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.frameworks.downloadImage
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewIntents
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchViewDialogIntents
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.marvel_character_details_single_row_ticket.view.*
import kotlinx.android.synthetic.main.marvel_home_ticket.view.*
import kotlinx.android.synthetic.main.marvel_search_result_ticket.view.*

class MarvelCharactersAdapter(private val layout: Int) :
    RecyclerView.Adapter<MarvelCharactersAdapter.MarvelCharactersViewHolder>() {

    private var marvelCharacters: MutableList<Results> = mutableListOf()
    private lateinit var context: Context
    private val goToMarvelDetailsPage = PublishSubject.create<MarvelCharactersHomeViewIntents>()
    private val goToMarvelDetailsPageFromSearchDialog =
        PublishSubject.create<MarvelCharactersSearchViewDialogIntents>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(layout, parent, false)
        return MarvelCharactersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarvelCharactersViewHolder, position: Int) {
        val marvelCharacter = marvelCharacters[position]
        marvelCharacter.thumbnail?.let {
            val imageURL = marvelCharacter.thumbnail.path + "." + marvelCharacter.thumbnail.extension
            downloadImage(context, imageURL, holder.charactersImage)
            holder.charactersName.text = marvelCharacter.name ?: marvelCharacter.title
            holder.itemView.setOnClickListener {
                if (layout == R.layout.marvel_home_ticket) {
                    goToMarvelDetailsPage.onNext(
                        MarvelCharactersHomeViewIntents.GoToMarvelCharacterDetailsPageIntent(
                            marvelCharacter
                        )
                    )
                } else {
                    goToMarvelDetailsPageFromSearchDialog.onNext(
                        MarvelCharactersSearchViewDialogIntents.GoToMarvelCharacterDetailsPageIntent(
                            marvelCharacter
                        )
                    )
                }
            }

        }

    }

    fun getGoToMarvelDetailsPageIntent() = goToMarvelDetailsPage
    fun getGoToMarvelDetailsPageIntentFromSearchDialog() = goToMarvelDetailsPageFromSearchDialog

    internal fun setMarvelCharacters(marvelCharacters: List<Results>) {
        if (layout == R.layout.marvel_search_result_ticket ||
            layout == R.layout.marvel_character_details_ticket
        ) this.marvelCharacters.clear()
        this.marvelCharacters.addAll(marvelCharacters)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return marvelCharacters.size
    }

    inner class MarvelCharactersViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val charactersImage: ImageView = when (layout) {
            R.layout.marvel_home_ticket -> itemView.character_image
            R.layout.marvel_search_result_ticket -> itemView.character_image_search_result
            else -> itemView.character_image_details_page
        }
        val charactersName: TextView =
            if (layout == R.layout.marvel_home_ticket) itemView.character_name
            else if (layout == R.layout.marvel_search_result_ticket) itemView.character_name_search_result
            else itemView.character_name_details_page

    }
}

