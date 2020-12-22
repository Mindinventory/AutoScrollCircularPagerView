package com.mindinventory

interface CenterItemCallback {
    fun onScrollFinished(middleElement: Int)
    fun onScrolled(dx: Int)
}