package com.example.plank

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.OpenCVLoader
import android.widget.TextView

//OpenCVのセットアップは以下のリンク参照
//https://algorithm.joho.info/programming/kotlin/opencv-install-kt/

class OpencvActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opencv)

        val returnButton = findViewById<Button>(R.id.return_sub_open)
        returnButton.setOnClickListener { finish() }

        //OpenCVの動作確認用にバージョンを表示する
        val txtVersion : TextView = findViewById(R.id.version)
        if (OpenCVLoader.initDebug()) {
            txtVersion.text = "OpenCV Version: " + OpenCVLoader.OPENCV_VERSION
        } else {
            txtVersion.text = "OpenCV Version: Not found."
        }
    }

}
