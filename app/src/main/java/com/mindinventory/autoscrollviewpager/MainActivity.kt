package com.mindinventory.autoscrollviewpager

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val images = ArrayList<Int>().apply {
        add(R.drawable.number0)
        add(R.drawable.number1)
        add(R.drawable.number2)
        add(R.drawable.number3)
        add(R.drawable.number4)
        add(R.drawable.number5)
        add(R.drawable.number6)
        add(R.drawable.number7)
        add(R.drawable.number8)
        add(R.drawable.number9)
        add(R.drawable.number10)
        add(R.drawable.number11)
        add(R.drawable.number12)
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