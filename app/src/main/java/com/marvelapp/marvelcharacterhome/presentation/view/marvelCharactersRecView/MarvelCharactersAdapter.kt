package com.marvelapp.marvelcharacterhome.presentation.view.marvelCharactersRecView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.marvelapp.R
import com.marvelapp.entities.Results
import com.marvelapp.frameworks.downloadImage
import com.marvelapp.marvelcharacterhome.presentation.view.MarvelCharactersDirections
import kotlinx.android.synthetic.main.marvel_home_ticket.view.*

class MarvelCharactersAdapter : RecyclerView.Adapter<MarvelCharactersAdapter.MarvelCharactersViewHolder>() {

    private var marvelCharacters: MutableList<Results> = mutableListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.marvel_home_ticket, parent, false)
        return MarvelCharactersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarvelCharactersViewHolder, position: Int) {
        val marvelCharacter = marvelCharacters[position]
        val imageURL = marvelCharacter.thumbnail!!.path + "." + marvelCharacter.thumbnail.extension
        downloadImage(context, imageURL, holder.charactersImage)
        holder.charactersName.text = marvelCharacter.name
        holder.itemView.setOnClickListener {

            val goToMarvelDetails
                    = MarvelCharactersDirections.actionFromHomeToMarvelCharacterDetails(marvelCharacter)
            Navigation.findNavController(it).navigate(goToMarvelDetails)
        }
    }

    internal fun setMarvelCharacters(marvelCharacters: List<Results>) {
        this.marvelCharacters.addAll(marvelCharacters)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return marvelCharacters.size
    }

    inner class MarvelCharactersViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val charactersImage: ImageView = itemView.character_image
        val charactersName: TextView = itemView.character_name

    }
}

