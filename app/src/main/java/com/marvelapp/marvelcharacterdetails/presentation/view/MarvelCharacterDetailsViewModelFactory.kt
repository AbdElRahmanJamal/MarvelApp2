package com.marvelapp.marvelcharacterdetails.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marvelapp.marvelcharacterdetails.domain.GetMarvelCharacterDetailsUseCase

class MarvelCharacterDetailsViewModelFactory(
    private val getMarvelCharacterDetailsUseCase: GetMarvelCharacterDetailsUseCase
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MarvelCharacterDetailsViewModel(getMarvelCharacterDetailsUseCase) as T
    }
}