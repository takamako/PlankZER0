package com.example.plank;

import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout;

public class ScoreCheckActivity extends AppCompatActivity{

    private TextView textView;
    private EditText editTextKey, editTextValue;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    private int cap[];
    private int caption[];
    private int haiten[];
    private int categoryPoint[];
    private int averagePoint[];
    private int categoryNum = 5;
    private final int MP = TableLayout.LayoutParams.MATCH_PARENT;
    private final int WC = TableLayout.LayoutParams.WRAP_CONTENT;

    public ScoreCheckActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecheck);

        textView = findViewById(R.id.text_view);

        Button readButton = findViewById(R.id.button_read);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(helper == null){
                    helper = new TestOpenHelper(getApplicationContext());
                }

                if(db == null){
                    db = helper.getWritableDatabase();
                }
                insertData(db, "test", 1);
                readData();
            }
        });
    }


    private void readData(){
        if(helper == null){
            helper = new TestOpenHelper(getApplicationContext());
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
       // Log.d("debug","**********Cursor");

        Cursor cursor = db.query(
                "testdb",
                new String[] { "date", "score" },
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append(cursor.getString(0));
            sbuilder.append(": ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("\n");
            cursor.moveToNext();
        }

        // 忘れずに！
        cursor.close();

        Log.d("debug","**********"+sbuilder.toString());
        textView.setText(sbuilder.toString());
    }

    private void insertData(SQLiteDatabase db, String com, int price){

        ContentValues values = new ContentValues();
        values.put("date", com);
        values.put("score", price);

        db.insert("testdb", null, values);
    }

    TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);
    for(int i=0; i<categoryNum; i++)

    {
        TableRow tableRow = (TableRow)getLayoutInflater().inflate(R.layout.table_row, null);
        TextView name = (TextView)tableRow.findViewById(R.id.rowtext1);
        
        name.setText(cap[i]+caption[i]);
        TextView point = (TextView)tableRow.findViewById(R.id.rowtext2);
        
        point.setText(Integer.toString(haiten[i]));
        TextView score = (TextView)tableRow.findViewById(R.id.rowtext3);
        score.setText(Integer.toString(categoryPoint[i]));
        TextView ave = (TextView)tableRow.findViewById(R.id.rowtext4);
        ave.setText(Integer.toString(averagePoint[i]));

        if((i+1)%2 == 0){
            int color = getResources().getColor(R.color.colorPrimary);
            name.setBackgroundColor(color);
            point.setBackgroundColor(color);
            score.setBackgroundColor(color);
            ave.setBackgroundColor(color);
        }

        tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC));
    }

}
