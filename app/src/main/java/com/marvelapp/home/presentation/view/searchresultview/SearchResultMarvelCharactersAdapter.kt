package com.marvelapp.home.presentation.view.searchresultview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marvelapp.R
import com.marvelapp.frameworks.downloadImage
import com.marvelapp.home.entities.Results
import kotlinx.android.synthetic.main.marvel_search_result_ticket.view.*

class SearchResultMarvelCharactersAdapter :
    RecyclerView.Adapter<SearchResultMarvelCharactersAdapter.MarvelCharactersViewHolder>() {

    private var marvelCharacters: MutableList<Results> = mutableListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.marvel_search_result_ticket, parent, false)
        return MarvelCharactersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarvelCharactersViewHolder, position: Int) {
        val marvelCharacter = marvelCharacters[position]
        val imageURL = marvelCharacter.thumbnail.path + "." + marvelCharacter.thumbnail.extension
        downloadImage(context, imageURL, holder.charactersImage)
        holder.charactersName.text = marvelCharacter.name
    }

    fun setMarvelCharactersSearchResult(marvelCharacters: List<Results>) {
        this.marvelCharacters.clear()
        this.marvelCharacters.addAll(marvelCharacters)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return marvelCharacters.size
    }

    inner class MarvelCharactersViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val charactersImage: ImageView = itemView.character_image_search_result
        val charactersName: TextView = itemView.character_name_search_result

    }
}

