package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class Activity_creditedit extends AppCompatActivity {

    ImageView back;
    ImageView ok;
    EditText type;
    EditText name;
    EditText balance;
    EditText remark;

    AlertDialog creditTypeSelect;
    View popCreditType;

    final ContentValues values = new ContentValues();

    static tools_MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;

    String takenPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.test);
        setFullScreen();

//        dbHelper = new tools_MyDatabaseHelper(Activity_creditedit.this, "credit.db", null, 1);
//        db = dbHelper.getWritableDatabase();
//
//        name = (EditText) findViewById(R.id.name);
//        type = (EditText) findViewById(R.id.type);
//        balance = (EditText) findViewById(R.id.balance);
//        remark = (EditText) findViewById(R.id.remark);
//
//        initClickListener();

    }

//    private void initClickListener() {
//        type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                creditTypeSelect = new AlertDialog.Builder(Activity_creditedit.this).create();
//                popCreditType = LayoutInflater.from(Activity_creditedit.this).inflate(R.layout.pop_credittype,null);
//                creditTypeSelect.setView(popCreditType);
//                creditTypeSelect.show();
//            }
//        });
//    }


    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}