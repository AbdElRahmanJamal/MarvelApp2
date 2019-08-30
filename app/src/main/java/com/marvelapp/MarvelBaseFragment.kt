package com.marvelapp


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_marvel_base.*
import kotlinx.android.synthetic.main.fragment_marvel_base.view.*


abstract class MarvelBaseFragment : Fragment() {

    private var loadingIndicator: Dialog? = null
    private var fragmentView: ViewGroup? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_marvel_base, container, false)
        fragmentView = inflater.inflate(getLayoutId(), null, false) as ViewGroup
        inflate.base_fragment_container.addView(fragmentView, 0)
        return inflate
    }

    protected abstract fun getLayoutId(): Int

    fun showLoadingIndicator() {
        if (loadingIndicator == null) {
            loadingIndicator = MarvelLoadingDialog.getInstance(context!!)
        }
        loadingIndicator?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    fun hideLoadingIndicator() {

        loadingIndicator?.let {
            it.dismiss()
        }

    }

    fun showLoadMoreLoading() {
        loading_layout.visibility = View.VISIBLE
    }

    fun hideLoadMoreLoading() {
        loading_layout.visibility = View.GONE
    }
}
