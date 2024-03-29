package com.marvelapp.marvelcharacterhome.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marvelapp.marvelcharacterhome.domain.GetMarvelCharactersUseCase

class MarvelCharactersViewModelFactory(
        private val getMarvelCharactersUseCase: GetMarvelCharactersUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MarvelCharactersViewModel(getMarvelCharactersUseCase) as T
    }
}