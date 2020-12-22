package com.mindinventory

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CenterItemFinder(
    private val context: Context,
    private val layoutManager: LinearLayoutManager,
    private val callback: CenterItemCallback,
    private val controlState: Int
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        callback.onScrolled(dx)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == controlState) {
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val itemsCount = lastVisible - firstVisible + 1
            val screenCenter = context.resources.displayMetrics.widthPixels / 2
            var minCenterOffset = Int.MAX_VALUE
            var middleItemIndex = 0
            for (index in 0 until itemsCount) {
                val listItem = layoutManager.getChildAt(index) ?: return
                val leftOffset = listItem.left
                val rightOffset = listItem.right
                val centerOffset = abs(leftOffset - screenCenter) + abs(rightOffset - screenCenter)
                if (minCenterOffset > centerOffset) {
                    minCenterOffset = centerOffset
                    middleItemIndex = index + firstVisible
                }
            }
            callback.onScrollFinished(middleItemIndex)
        }
    }
}