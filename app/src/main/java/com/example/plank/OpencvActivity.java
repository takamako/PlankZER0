package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;

public class OpencvActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv);

        TextView textView = findViewById(R.id.text1);
        textView.setText(OpenCVLoader.OPENCV_VERSION);  // ★OpenCVのバージョンを設定
    }
}
