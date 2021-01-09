package com.example.zzyyff.flowerrecords;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_quota extends AppCompatActivity {
    ImageView back;
    ImageView btnSave;
    tools_MyDatabaseHelper dbHelper_mine;
    SQLiteDatabase db_mine;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    EditText editQuota;
    float quota;
    float balance;
    float percent;
    TextView currentBalance;
    TextView currentPercent;
    String date_month;
    String date_year;
    float sum_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this, "theme", "AppTheme")));
        setContentView(R.layout.activity_quota);

        back = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        currentBalance = findViewById(R.id.currentBalance);
        currentPercent = findViewById(R.id.currentPercent);
        editQuota = findViewById(R.id.editQuota);

        dbHelper_mine = new tools_MyDatabaseHelper(Activity_quota.this, "mine.db", null, 1);
        db_mine = dbHelper_mine.getWritableDatabase();
        dbHelper = new tools_MyDatabaseHelper(Activity_quota.this, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        initData();

        //返回+保存（单用户）
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if(sum_cost < quota) {
                    values.put("date_signed", "no");
                } else {
                    values.put("date_signed", "yes");
                }
                quota = Float.valueOf(editQuota.getText().toString());
                values.put("quota", quota);
                db_mine.update("mine",values,"name=?",new String[]{"QJ-QWG-JHC"});
                finish();
                }
        });

    }

    private void initData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String time = sdf.format(new Date());
        String date = time.split(" ")[0];
        date_year = date.substring(0,4);
        date_month = date.substring(5,7);

        //求当前月总和
        Cursor cursor = db.rawQuery("select sum(cost) from record" +
                        " Where inorout=? and date_year =? and date_month =?",
                new String[]{"out", date_year, date_month});
        if (cursor.moveToFirst()) {
            sum_cost = cursor.getFloat(0);
        } else {
            sum_cost = 0;
        }

        //找设定额度
        Cursor cursor1 = db_mine.rawQuery("select quota from mine" +
                        " Where name=?",
                new String[]{"QJ-QWG-JHC"});
        if(cursor1.moveToFirst()) {
            quota = cursor1.getFloat(0);
        } else {
            quota = 0;
        }

        //求余额，设置超额提醒
        balance = quota - sum_cost;
        if(sum_cost < quota) {
            percent = sum_cost / quota * 100;
        } else {
            percent = 100;
            currentPercent.setTextColor(Color.parseColor("#F87070"));
            ContentValues values = new ContentValues();
            values.put("date_signed", "yes");
            db_mine.update("mine",values,"name=?",new String[]{"QJ-QWG-JHC"});
        }

        currentBalance.setText(Float.toString(balance));
        currentPercent.setText(percent + "%");
        editQuota.setText(Float.toString(quota));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
