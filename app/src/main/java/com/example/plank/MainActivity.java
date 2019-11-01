package com.example.plank;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private final static int RESULT_CAMERA = 1001;
    private final static int REQUEST_PERMISSION = 1002;//10/2追加
    private ImageView imageView;
    private ImageView imageView2;
    private Uri cameraUri;//10/2追加
    private File cameraFile;//10/2追加

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("debug","onCreate()");//10/2追加
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        imageView2 = findViewById(R.id.image_view2);

        /*
        Button sendButton = findViewById(R.id.movie_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MovieActivity.class);
                startActivity(intent);
            }
        });


        //カメラボタン
        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //追加
                // Android 6, API 23以上でパーミッシンの確認
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
                else {
                    cameraIntent();
                }

            }
        });

         */

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

