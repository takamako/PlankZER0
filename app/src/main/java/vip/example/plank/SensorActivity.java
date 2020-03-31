package vip.example.plank;

//AndroidX

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.widget.ImageButton;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Handler;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View.OnClickListener;
import android.os.Build;
import android.os.Bundle;

public class SensorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);



        if (isFirstTime()) {
            SharedPreferences sp = getSharedPreferences("DataStore", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            int No1 = 0;
            int No2 = 0;
            int No3 = 0;
            editor.putInt("int_No1", No1); // int_1というキーに i の中身(2)を設定
            editor.putInt("int_No2", No2); // int_1というキーに i の中身(2)を設定
            editor.putInt("int_No3", No3); // int_1というキーに i の中身(2)を設定
            editor.commit(); // ここで実際にファイルに保存

            // show dialog
            FragmentManager fragmentManager = getFragmentManager();

            // DialogFragment を継承したAlertDialogFragmentのインスタンス
            AlertDialogFragment dialogFragment = new AlertDialogFragment();
            // DialogFragmentの表示
            dialogFragment.show(fragmentManager, "test alert dialog");

        }

        //初心者モード画面に遷移
        Button BiginnerModeButton = findViewById(R.id.biginnermode_button);
        BiginnerModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), BiginnerActivity.class);
                startActivity(intent);
            }
        });


        //中級者モード画面に遷移
        Button IntermediateModeButton = findViewById(R.id.intermediatemode_button);
        IntermediateModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), IntermediateActivity.class);
                startActivity(intent);
            }
        });


        //上級者モード画面に遷移
        Button AdvancedModeButton = findViewById(R.id.advancedmode_button);
        AdvancedModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AdvancedActivity.class);
                startActivity(intent);
            }
        });

        //日々の記録画面に遷移
        ImageButton ScoreCheckModeButton = findViewById(R.id.ScoreCheckButton);
        ScoreCheckModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ScoreCheckActivity.class);
                startActivity(intent);
            }
        });

        ImageButton helpButton = findViewById(R.id.helpButton);

        // ボタンタップでAlertを表示させる
        helpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();

                // DialogFragment を継承したAlertDialogFragmentのインスタンス
                AlertDialogFragment dialogFragment = new AlertDialogFragment();
                // DialogFragmentの表示
                dialogFragment.show(fragmentManager, "test alert dialog");
            }
        });
    }

    public void setTextView(String message){

    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }
    // DialogFragment を継承したクラス
    public static class AlertDialogFragment extends DialogFragment {
        // 選択肢のリスト
        private String[] menulist = {"選択(1)","選択(2)","選択(3)"};

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource( R.drawable.help1);
            // タイトル
            alert.setTitle("1人用の使い方！");
            alert.setView(  imageView );
            alert.setView(R.layout.activity_help1);
            alert.setPositiveButton( "OK", null );

            return alert.create();
        }

        private void setMassage(String message) {
            MainActivity mainActivity = (MainActivity) getActivity();
          // mainActivity.setTextView(message);
        }
    }
}