package vip.example.plank;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.view.animation.RotateAnimation;
import android.os.Handler;
import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import java.util.*;
import java.lang.*;
import java.io.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class IntermediateActivity extends AppCompatActivity implements SensorEventListener {


    private TestOpenHelper helper;
    private SQLiteDatabase db;
    private DateFormat df = new SimpleDateFormat("yyyy/MM/dd ");
    private SensorManager sensorManager;
    private TextView timerText;//タイマーの表示ぶん
    private TextView timerText＿trainig;
    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("mm:ss", Locale.US);
    //"mm:ss.SSS", Locale.US
    private TextView textView, textInfo;
    private SoundPool soundPool;
    private int soundOne, soundTwo, soundThree,soundFour;
    float nextX =0;
    float nextY =0;
    float nextZ =0;
    private  long time = 10000;
    private int first = 0;
    private float FirstX,FirstY,FirstZ =0;
    private int frag = 0;
    private int timing = 0;
    private int stop_count = 0;
    private double all_count = 0;
    private int move_frag = 0;
    final Handler handler = new Handler();
    private int set_frag = 1;
    private TextView setCount;

    private int totalscore=0;
    private double totalmil=0;

    private Runnable delay;
    private Runnable delayStartCountDown;
    private Runnable enablestart;

    // 3分= 3x60x1000 = 180000 msec
    long countNumber = 30000;
    //スタート前
    long countbefore = 10000;
    // インターバル msec
    long interval = 10;
    final IntermediateActivity.CountDown countDown = new IntermediateActivity.CountDown(countNumber, interval);
    Button startButton;
    Button stopButton;
    Button setCountButton;
    //private Runnable;

    private Sensor accel;
    private TextView textGraph;
    private LineChart mChart;
    private String[] labels = new String[]{
            "姿勢の揺れ度",
            "Y軸の揺れ",
            "Z軸の揺れ"};
    private int[] colors = new int[]{
            Color.BLUE,
            Color.GRAY,
            Color.MAGENTA};
    private boolean lineardata = true;


    private int No1;
    private int No2;
    private int No3;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        SharedPreferences sp = getSharedPreferences("DataStore", MODE_PRIVATE);
        Editor editor = sp.edit();
        No1 = sp.getInt("int_No1", 0);
        No2 = sp.getInt("int_No2", 0);
        No3 = sp.getInt("int_No3", 0);

        //レイアウト関連
        startButton = findViewById(R.id.start_button);//タイマーのボタン
        stopButton = findViewById(R.id.stop_button);//タイマーのボタン
        timerText = findViewById(R.id.timer);
        setCountButton = findViewById(R.id.set_button);
        timerText＿trainig = findViewById(R.id.timer_training);
        timerText.setText(dataFormat.format(10000));
        timerText＿trainig.setText(dataFormat.format(countNumber));
        textView = findViewById(R.id.text_view);
        textView.setText("トレーニングスコア：" + 0);
        textGraph = findViewById(R.id.text_view);
        setCount = findViewById(R.id.settime);
        setCount.setText("×" + set_frag +"セット");

        final IntermediateActivity.CountDown countDown_before = new IntermediateActivity.CountDown(countbefore, interval);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mChart = findViewById(R.id.chart);
        // インスタンス生成
        mChart.setData(new LineData());
        // no description text
        mChart.getDescription().setEnabled(false);
        // Grid背景色
        mChart.setDrawGridBackground(true);
        // 右側の目盛り
        mChart.getAxisRight().setEnabled(false);

        // Grid縦軸を破線 x軸に関する処理

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(false);//x軸のラベル消す

        //Y軸関連
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaxValue(4.0f);
        leftAxis.setAxisMinValue(-4.0f);
        leftAxis.setDrawGridLines(true);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // textInfo = findViewById(R.id.text_info);

        //スタートボタンの処理
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(delayStartCountDown);
                handler.removeCallbacks(delay);

                totalscore=0;
                totalmil=0;

                FragmentManager fragmentManager2 = getFragmentManager();
                BiginnerActivity.AlertDialogFragment_setpoketto dialogFragment_setpoketto = new BiginnerActivity.AlertDialogFragment_setpoketto();
                // DialogFragmentの表示
                dialogFragment_setpoketto.show(fragmentManager2, "setpoketto alert dialog");
                startButton.setEnabled(false);
                countDown_before.start();
                //countDown_beforeで終わるときにスタートボタンが押せるの防ぐ

                delayStartCountDown =  new Runnable(){//遅延定義 10/31
                    @Override
                    public void run() {
                        timing =1;
                        if(timing==1) {
                            soundPool.play(soundFour, 1.0f, 1.0f, 0, 0, 1);
                        }
                    }
                };

                enablestart = new Runnable() {//遅延定義 10/31
                    @Override
                    public void run() {
                        startButton.setEnabled(false);
                    }
                };

                delay =  new Runnable(){//遅延定義 10/31
                    @Override
                    public void run() {
                        mChart.setData(new LineData());
                        soundPool.play(soundFour, 1.0f, 1.0f, 0, 0, 1);
                        first =1;
                        frag=1;
                        timing = 1;
                        countDown.start();
                        timing = 0;
                        startButton.setEnabled(false);
                    }
                };
                handler.postDelayed(delayStartCountDown, 7000);//遅延実行
                handler.postDelayed(delayStartCountDown, 8000);//遅延実行
                handler.postDelayed(delayStartCountDown, 9000);//遅延実行
                handler.postDelayed(delay, 10001);//遅延実行
                int set_frag_c=set_frag;
                for(int xx=0;set_frag>1;set_frag--) {
                    handler.postDelayed(enablestart, (set_frag-1)*40000+100);
                    handler.postDelayed(delayStartCountDown, (set_frag-1)*40000+7000);//遅延実行
                    handler.postDelayed(delayStartCountDown, (set_frag-1)*40000+8000);//遅延実行
                    handler.postDelayed(delayStartCountDown, (set_frag-1)*40000+9000);//遅延実行
                    handler.postDelayed(delay, (set_frag-1)*40000+10001);//遅延実行
                }
                set_frag=set_frag_c;
                setCount.setText("×" + set_frag +"セット");
            }
        });
        //ストップボタンの処理
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startButton.setEnabled(true);
                if(frag==1){
                    countDown.cancel();}
                if(frag ==0){
                    countDown_before.cancel();}
                handler.removeCallbacks(delay);
                frag=0;
                timing =0;
                handler.removeCallbacks(delayStartCountDown);
                handler.removeCallbacks(delay);
                timerText.setText(dataFormat.format(10000));
                timerText＿trainig.setText(dataFormat.format(countNumber));

                stop_count=0;
                all_count=0;
                setCountButton.setEnabled(true);
                set_frag=1;
                setCount.setText("×" + set_frag +"セット");
            }
        });

        Button returnButton_sensor = findViewById(R.id.return_button_sensor);
        returnButton_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frag==1){
                    countDown.cancel();}
                if(frag ==0){
                    countDown_before.cancel();}
                handler.removeCallbacks(delay);
                frag=0;
                timing =0;
                handler.removeCallbacks(delayStartCountDown);
                handler.removeCallbacks(delay);
                finish();
            }
        });

        setCountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                set_frag+=1;
                setCount.setText("×" + set_frag +"セット");
                if(set_frag==9){
                    setCountButton.setEnabled(false);
                }
            }
        });

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        soundOne = soundPool.load(this, R.raw.one, 1);
        soundTwo = soundPool.load(this, R.raw.two, 1);
        soundThree = soundPool.load(this, R.raw.three, 1);
        soundFour = soundPool.load(this, R.raw.four, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Listenerの登録
        Sensor accel = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed(){
        // 行いたい処理
        frag=0;
        timing =2;
        handler.removeCallbacks(delayStartCountDown);
        handler.removeCallbacks(delay);
        finish();
    }

    // 解除するコードも入れる!
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorX, sensorY, sensorZ;
        float gravity[] = new float[3];
        float linear_acceleration[] = new float[1];
        //final float alpha = 0.5f;
        if(first==1){
            FirstX = event.values[0];
            FirstY = event.values[1];
            FirstZ = event.values[2];
            first=0;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];


            if(frag==1) {
                if (FirstZ - nextZ < -1 || FirstZ - nextZ > 1) {
                    soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                    move_frag=1;
                } else if (FirstX - nextX < -1 || FirstX - nextX > 1) {
                    soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                    move_frag=1;
                } else if (FirstY - nextY < -1 || FirstY - nextY > 1) {
                    soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                    move_frag=1;
                }else{
                    move_frag=0;
                }

                gravity[0] = (FirstX - nextX);
                gravity[1] = (FirstY - nextY);
                gravity[2] = (FirstZ - nextZ);

                float x = Math.max(gravity[0], gravity[1]);
                float y = Math.max(x, gravity[2]);

                float x2 = Math.min(gravity[0], gravity[1]);
                float y2 = Math.min(gravity[0], gravity[1]);
                if(Math.abs(y)>Math.abs(y2)){
                    linear_acceleration[0] =y;
                }else{
                    linear_acceleration[0] =y2;
                }

                String accelero;

                LineData data = mChart.getLineData();

                if (data != null) {
                    for (int i = 0; i < 1; i++) {
                        ILineDataSet set3 = data.getDataSetByIndex(i);
                        if (set3 == null) {
                            LineDataSet set = new LineDataSet(null, labels[i]);
                            set.setLineWidth(2.0f);
                            set.setColor(colors[i]);
                            // liner line
                            set.setDrawCircles(false);
                            // no values on the chart
                            set.setDrawValues(false);
                            set3 = set;
                            data.addDataSet(set3);
                        }

                        // data update
                        if (!lineardata) {
                            data.addEntry(new Entry(set3.getEntryCount(), event.values[i]), i);
                        } else {
                            data.addEntry(new Entry(set3.getEntryCount(), linear_acceleration[i]), i);
                        }

                        data.notifyDataChanged();
                    }

                    mChart.notifyDataSetChanged(); // 表示の更新のために変更を通知する
                    mChart.setVisibleXRangeMaximum(180); // 表示の幅を決定する
                    mChart.moveViewToX(data.getEntryCount()); // 最新のデータまで表示を移動させる
                }
            }
            nextX = sensorX;
            nextY = sensorY;
            nextZ = sensorZ;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class CountDown extends CountDownTimer {

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了

            timerText.setText(dataFormat.format(10000));
            timerText＿trainig.setText(dataFormat.format(countNumber));
            frag =0;
            double x=100*stop_count/all_count;
            x=Math.floor(x);
            double mil =all_count*1000/countNumber;
            double mil_count = stop_count/mil;

            if(timing ==1) {
                startButton.setEnabled(true);
            }else{
                textView.setTextColor(Color.RED);

                if(set_frag >1){

                    totalmil+=mil_count;
                    totalscore+=stop_count*2;
                    textView.setText("合計スコア：" + totalscore + "\n" + String.valueOf((int)totalmil)+ "秒キープできたよ！");
                }else{


                    if(No1< stop_count*2){
                        SharedPreferences sp = getSharedPreferences("DataStore", MODE_PRIVATE);
                        Editor editor = sp.edit();
                        No3 = No2;
                        No2 = No1;
                        No1 = stop_count*2;

                        editor.putInt("int_No1", No1); // int_1というキーに i の中身(2)を設定
                        editor.putInt("int_No2", No2); // int_1というキーに i の中身(2)を設定
                        editor.putInt("int_No3", No3); // int_1というキーに i の中身(2)を設定
                        editor.commit(); // ここで実際にファイルに保存
                    }else if(No2 < stop_count*2){
                        SharedPreferences sp = getSharedPreferences("DataStore", MODE_PRIVATE);
                        Editor editor = sp.edit();
                        No3 = No2;
                        No2 = stop_count*2;

                        editor.putInt("int_No2", No2); // int_1というキーに i の中身(2)を設定
                        editor.putInt("int_No3", No3); // int_1というキーに i の中身(2)を設定
                        editor.commit();
                    }else if(No3 < stop_count*2){
                        SharedPreferences sp = getSharedPreferences("DataStore", MODE_PRIVATE);
                        Editor editor = sp.edit();
                        No3 = stop_count*2;
                        editor.putInt("int_No3", No3); // int_1というキーに i の中身(2)を設定
                        editor.commit();
                    }

                    textView.setText("ランキング！\n 1位:"+No1+"\n 2位:"+No2+"\n 3位:"+No3+"\n\nトレーニングスコア：" + stop_count*2 + "\n今回は" + String.valueOf((int)mil_count)+ "秒キープできたよ！");
                }
                if(helper == null){
                    helper = new TestOpenHelper(getApplicationContext());
                }

                if(db == null){
                    db = helper.getWritableDatabase();
                }

                //現在の時刻
                String date = getNowDate();

                insertData(db, date,stop_count*2,mil_count);
            }
            stop_count=0;
            all_count=0;

            if(mil_count>28){
                FragmentManager fragmentManager = getFragmentManager();
                AlertDialogFragment dialogFragment = new AlertDialogFragment();
                // DialogFragmentの表示
                dialogFragment.show(fragmentManager, "test alert dialog");
            }
            stop_count=0;
            all_count=0;

            if(timing ==2){}else{   soundPool.play(soundFour, 1.0f, 1.0f, 0, 0, 1);}
        }

        public  String getNowDate(){
            final Date date = new Date(System.currentTimeMillis());
            return df.format(date);
        }

        //追加 dbにかきこみ
        private void insertData(SQLiteDatabase db, String date, int score,double sec){

            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("score", score);
            values.put("level", "中級者");
            values.put("sec", sec);
            db.insert("testdb", null, values);
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            //long mm = millisUntilFinished / 1000 / 60;
            //long ss = millisUntilFinished / 1000 % 60;
            //long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            //timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));
            if(millisUntilFinished>10000){
                frag=1;
            }

            if(frag==0){
                timerText.setText(dataFormat.format(millisUntilFinished));
            }
            if(frag ==1){
                timerText＿trainig.setText(dataFormat.format(millisUntilFinished));
                double x=100*stop_count/all_count;
                x=Math.floor(x);
                textView.setTextColor(Color.BLUE);
                textView.setText( "トレーニングスコア：" +stop_count*2 );

                all_count++;
                if(move_frag==0){
                    stop_count++;
                }
            }
        }
    }

    public static class AlertDialogFragment extends DialogFragment {
        // 選択肢のリスト
        private String[] menulist = {"選択(1)","選択(2)","選択(3)"};

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource( R.drawable.level_up2);
            // タイトル
            alert.setTitle("上の難易度を目指しましょう！");
            alert.setView(  imageView );
            alert.setPositiveButton( "OK", null );

            return alert.create();
        }

        private void setMassage(String message) {
            MainActivity mainActivity = (MainActivity) getActivity();
            // mainActivity.setTextView(message);
        }
    }

    public static class AlertDialogFragment_setpoketto extends DialogFragment {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource( R.drawable.poket);
            // タイトル
            alert.setTitle("ポケットか背中にスマホを入れましょう！");
            alert.setView(  imageView );
            alert.setPositiveButton( "OK", null );

            return alert.create();
        }

        private void setMassage(String message) {
            MainActivity mainActivity = (MainActivity) getActivity();
            // mainActivity.setTextView(message);
        }
    }
}
