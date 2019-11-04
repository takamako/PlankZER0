package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("debug","onCreate()");//10/2追加
        setContentView(R.layout.activity_main);

        //カメラモードに移動
        Button PhotoButton = findViewById(R.id.ViewImg);
        PhotoButton.setOnClickListener(new View.OnClickListener() {

                public void onClick (View v) {
                   Intent intent = new Intent(getApplication(), ImageActivity.class);
                   startActivity(intent);
                }

        });

        //センサーようの移動ボタン
        Button sendButton_sensor = findViewById(R.id.sensor_button);
        sendButton_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SensorActivity.class);
                startActivity(intent);
            }
        });
        }

}

