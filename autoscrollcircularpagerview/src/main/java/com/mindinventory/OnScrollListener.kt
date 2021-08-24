package com.mindinventory

import androidx.recyclerview.widget.RecyclerView

class OnScrollListener(
    private val callback: CenterItemCallback,
    private val controlState: Int
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        callback.onScrolled(dx)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == controlState) {
            callback.onScrollFinished()
        }
    }
}