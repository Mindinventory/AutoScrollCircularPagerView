package com.mindinventory

interface CenterItemCallback {
    fun onScrollFinished(visibleItemPosition: Int)
    fun onScrolled(dx: Int)
}