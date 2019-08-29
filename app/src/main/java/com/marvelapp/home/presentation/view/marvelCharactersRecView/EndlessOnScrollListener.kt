package com.marvelapp.home.presentation.view.marvelCharactersRecView

import androidx.recyclerview.widget.RecyclerView


abstract class EndlessOnScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!recyclerView.canScrollVertically(1)) {
            onScrolledToEnd()
        }
    }

    abstract fun onScrolledToEnd()

}