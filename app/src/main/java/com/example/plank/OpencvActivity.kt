package com.example.plank

import android.os.Bundle
import android.view.View
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity

class OpencvActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opencv)

        val returnButton = findViewById<Button>(R.id.return_sub_open)
        returnButton.setOnClickListener { finish() }


    }

}
