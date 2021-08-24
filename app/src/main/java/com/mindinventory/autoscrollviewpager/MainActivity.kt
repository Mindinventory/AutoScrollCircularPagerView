package com.mindinventory.autoscrollviewpager

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val images = ArrayList<Int>().apply {
        add(R.drawable.image1)
        add(R.drawable.image2)
        add(R.drawable.image3)
        add(R.drawable.image4)
        add(R.drawable.image5)
    }
    private val myAdapter by lazy { MyAdapter() }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        autoScrollContainer.setAdapter(myAdapter)
        autoScrollContainer.setItems(images)
    }
}