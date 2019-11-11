package com.example.plank

//追加

//ここまで

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_compare.*
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// IDEが自動インポートする場合もありますが
// それぞれ実装が異なる場合があるので、曖昧さを無くすためにここに列挙します

// パーミッションを要求するときのリクエストコード番号です
// 複数のContextからパーミッションが要求された時にどこから要求されたかを区別するために使います
private const val REQUEST_CODE_PERMISSIONS = 10

// 要求する全パーミッションの配列
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class CompareActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var imageView2: ImageView? = null
    private var cameraUri: Uri? = null//10/2追加
    private var cameraFile: File? = null//10/2追加
    private lateinit var viewFinder: TextureView

    private class LuminosityAnalyzer : ImageAnalysis.Analyzer {
        private var lastAnalyzedTimestamp = 0L

        /*
        * image plane bufferからbyte配列を抽出するHelper
        */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // バッファを０にする
            val data = ByteArray(remaining())
            get(data)   // Byte配列にバッファをコピー
            return data // Byte配列を返却
        }

        override fun analyze(image: ImageProxy, rotationDegrees: Int) {
            val currentTimestamp = System.currentTimeMillis()
            // 毎秒ごとに平均輝度を計算する
            if (currentTimestamp - lastAnalyzedTimestamp >=
                    TimeUnit.SECONDS.toMillis(1)) {
                // ImageAnalysisはYUV形式なのでimage.planes[0]にはY (輝度) planeが格納されている
                val buffer = image.planes[0].buffer
                // callback objectからimage dataの抽出
                val data = buffer.toByteArray()
                // pixel値の配列にデータを変換
                val pixels = data.map { it.toInt() and 0xFF }
                // imageの平均輝度の計算
                val luma = pixels.average()
                // 輝度のログ表示
                Log.d("CameraXApp", "Average luminosity: $luma")
                // 最後に分析したフレームのタイムスタンプに更新
                lastAnalyzedTimestamp = currentTimestamp
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        imageView = findViewById(R.id.image_view)
        imageView2 = findViewById(R.id.image_view2)
        viewFinder = findViewById(R.id.view_finder)

        // カメラパーミッションの要求
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // texture viewが変化した時にLayoutの再計算を行う
        viewFinder.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateTransform()
        }


        //11/11ポッキーの日
        timer_button.setOnClickListener { //画像班タイマー

            object : CountDownTimer(5000,100){
                override fun onFinish() {
                    //終了時の処理
                    count.text = "終了！！！"

                }

                override fun onTick(p0: Long) {
                    // 区切り（0.1秒毎）
                    count.text = "後 ${p0 /1000} 秒(デモ用)"
                }

            }.start()

        }


        //カメラボタン
        val cameraButton = findViewById<Button>(R.id.camera_button)
        cameraButton.setOnClickListener {
            //追加
            // Android 6, API 23以上でパーミッシンの確認
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermission()
            } else {
                cameraIntent()
            }
        }

        //写真を表示する処理
        val PhotoButton = findViewById<Button>(R.id.select_phote)
        PhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, RESULT_CAMERA)
        }

        val returnButton = findViewById<Button>(R.id.return_sub_com)
        returnButton.setOnClickListener { finish() }

    }
    private fun startCamera() {

        // viewfinder use caseのコンフィグレーションオブジェクトを生成
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(Size(640, 640))
        }.build()

        // viewfinder use caseの生成
        val preview = Preview(previewConfig)

        // viewfinderが更新されたらLayoutを再計算
        preview.setOnPreviewOutputUpdateListener {

            // SurfaceTextureの更新して再度親Viewに追加する
            val parent = viewFinder.parent as ViewGroup
            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // image capture use caseのコンフィグレーションオブジェクトを生成
        // Create configuration object for the image capture use case
        val imageCaptureConfig = ImageCaptureConfig.Builder()
                .apply {
                    setTargetAspectRatio(Rational(1, 1))
                    // イメージキャプチャの解像度は設定しない。
                    // 代わりに、アスペクト比と要求されたモードに基づいて
                    // 適切な解像度を推測するキャプチャモードを選択します。
                    setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                }.build()

        // image capture use caseの生成とボタンのClickリスナーの登録
        val imageCapture = ImageCapture(imageCaptureConfig)
        findViewById<ImageButton>(R.id.capture_button).setOnClickListener {
            val file = File(externalMediaDirs.first(),
                    "${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file,
                    object : ImageCapture.OnImageSavedListener {
                        override fun onError(error: ImageCapture.UseCaseError,
                                             message: String, exc: Throwable?) {
                            val msg = "Photo capture failed: $message"
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            Log.e("CameraXApp", msg)
                            exc?.printStackTrace()
                        }

                        override fun onImageSaved(file: File) {
                            val msg = "Photo capture succeeded: ${file.absolutePath}"
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            Log.d("CameraXApp", msg)
                        }
                    })
        }

        // 平均輝度を計算するimage analysis pipelineのセットアップ
        val analyzerConfig = ImageAnalysisConfig.Builder().apply {
            // 不具合を防ぐためにワーカースレッドを使う
            val analyzerThread = HandlerThread(
                    "LuminosityAnalysis").apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            // ここではすべての画像を分析するよりも、最新の画像を重視する
            setImageReaderMode(
                    ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
        }.build()

        // image analysis use caseの生成とanalyzerのインスタンス生成
        val analyzerUseCase = ImageAnalysis(analyzerConfig).apply {
            analyzer = LuminosityAnalyzer()
        }

        // use caseをlifecycleにバインドする
        CameraX.bindToLifecycle(this, preview, imageCapture, analyzerUseCase)
    }

    private fun updateTransform() {
        val matrix = Matrix()

        // view finderの中心の計算
        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        // 表示回転を考慮したプレビュー出力
        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // TextureViewへのセット
        viewFinder.setTransform(matrix)
    }

    /**
     * カメラのパーミッションリクエストダイアログ処理の結果を確認します
     * パーミッションが許可された場合はCameraを開始します
     * そうでない場合はToastを表示して終了します
     */
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(this,
                        "パーミッションが許可されませんでした",
                        Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    /**
     * 定義されたパーミッションをチェックします
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
                this, it) == PackageManager.PERMISSION_GRANTED
    }

    //10/2作成　カメラ保存
    private fun cameraIntent() {
        // 保存先のフォルダー
        val cFolder = getExternalFilesDir(Environment.DIRECTORY_DCIM)

        val fileDate = SimpleDateFormat(
                "ddHHmmss", Locale.US).format(Date())
        // ファイル名
        val fileName = String.format("CameraIntent_%s.jpg", fileDate)

        cameraFile = File(cFolder, fileName)

        cameraUri = FileProvider.getUriForFile(
                this@CompareActivity,
                applicationContext.packageName + ".fileprovider",
                cameraFile!!)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        startActivityForResult(intent, RESULT_CAMERA)

        Log.d("debug", "startActivityForResult()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //画像表示(撮影してすぐ)
        /**
         * if (requestCode == RESULT_CAMERA) {
         * Bitmap bitmap;
         * // cancelしたケースも含む
         * if( data.getExtras() == null){
         * Log.d("debug","cancel ?");
         * return;
         * }
         * else{
         * bitmap = (Bitmap) data.getExtras().get("data");
         * if(bitmap != null){
         * // 画像サイズを計測
         * int bmpWidth = bitmap.getWidth();
         * int bmpHeight = bitmap.getHeight();
         * Log.d("debug",String.format("w= %d",bmpWidth));
         * Log.d("debug",String.format("h= %d",bmpHeight));
         * }
         * }
         */


        //カメラの処理

        if (requestCode == RESULT_CAMERA) {

            if (cameraUri != null) {
                imageView2!!.setImageURI(cameraUri)

                registerDatabase(cameraFile!!)
            } else {
                Log.d("debug", "cameraUri == null")
            }
        }


        if (requestCode == RESULT_CAMERA) {

            if (data!!.data != null) {

                var pfDescriptor: ParcelFileDescriptor? = null
                try {
                    val uri = data.data

                    pfDescriptor = contentResolver.openFileDescriptor(uri!!, "r")
                    if (pfDescriptor != null) {
                        val fileDescriptor = pfDescriptor.fileDescriptor
                        val bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                        pfDescriptor.close()
                        imageView2!!.setImageBitmap(bmp)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        pfDescriptor?.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            }
        }
    }


    //以下の関数10/2追加
    // アンドロイドのデータベースへ登録する
    private fun registerDatabase(file: File) {
        val contentValues = ContentValues()
        val contentResolver = this@CompareActivity.contentResolver
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        contentValues.put("_data", file.absolutePath)
        contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }


    // Runtime Permission check
    private fun checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraIntent()
        } else {
            requestPermission()
        }// 拒否していた場合
    }

    // 許可を求める
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this@CompareActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION)

        } else {
            val toast = Toast.makeText(this,
                    "許可されないとアプリが実行できません",
                    Toast.LENGTH_SHORT)
            toast.show()

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION)
        }
    }

    companion object {

        private val RESULT_CAMERA = 1001
        private val REQUEST_PERMISSION = 1002//10/2追加
    }
    /*

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.d("debug","onRequestPermissionsResult()");

        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
 */

}
