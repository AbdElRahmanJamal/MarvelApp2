package com.marvelapp.marvelcharacterdetails.entities

import com.marvelapp.entities.Results

class MarvelCharacterDetailsModel(
    private val comics: Pair<String, MutableList<Results>>,
    private val series: Pair<String, MutableList<Results>>,
    private val stories: Pair<String, MutableList<Results>>,
    private val events: Pair<String, MutableList<Results>>
) {
    fun getMarvelDetailsModel(): MutableMap<String, List<Results>> {
        val mutableMapOf = mutableMapOf<String, List<Results>>()
        mutableMapOf[comics.first] = comics.second
        mutableMapOf[series.first] = series.second
        mutableMapOf[stories.first] = stories.second
        mutableMapOf[events.first] = events.second
        return mutableMapOf
    }
}