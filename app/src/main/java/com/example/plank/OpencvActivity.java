package com.example.plank;

//androidのモジュール
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

//opencvのモジュール
import org.opencv.android.OpenCVLoader;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Mat;
import org.opencv.core.Core;



public class OpencvActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener {

    private CameraBridgeViewBase m_cameraView;

    static {
        System.loadLibrary("opencv_java4");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv);

        //OpenCVLoader.initDebug();
        getPermissionCamera(this);
        Log.d("camera","get permission");

        // カメラビューのインスタンスを変数にバインド
        m_cameraView = findViewById(R.id.camera_view);

        // リスナーの設定 (後述)
        m_cameraView.setCvCameraViewListener(this);


        m_cameraView.enableView();
    }

    public static boolean getPermissionCamera(Activity activity) {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    0);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        // ここで何らかの画像処理を行う
        // 試しに、ネガポジ反転してみる
        Core.bitwise_not(inputFrame, inputFrame);
        return inputFrame;
    }


}



