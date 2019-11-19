package com.example.plank;

//androidのモジュール
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


//opencvのモジュール
import org.opencv.android.OpenCVLoader;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class OpencvActivity extends Activity {


    // CameraBridgeViewBase は JavaCameraView/NativeCameraView のスーパークラス
    private CameraBridgeViewBase mCameraView;

    // ライブラリ初期化完了後に呼ばれるコールバック (onManagerConnected)
    // public abstract class BaseLoaderCallback implements LoaderCallbackInterface
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                // 読み込みが成功したらカメラプレビューを開始
                case LoaderCallbackInterface.SUCCESS:
                    mCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv);

        TextView textView = findViewById(R.id.text1);
        textView.setText(OpenCVLoader.OPENCV_VERSION);  // ★OpenCVのバージョンを設定
    }
}
