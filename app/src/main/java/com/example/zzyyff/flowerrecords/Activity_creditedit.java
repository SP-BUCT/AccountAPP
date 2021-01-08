package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class Activity_creditedit extends AppCompatActivity {
    //接受intent数据
    String addoredit;
    String ctype;


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
        setContentView(R.layout.activity_creditedit);
        setFullScreen();

        dbHelper = new tools_MyDatabaseHelper(Activity_creditedit.this, "credit.db", null, 1);
        db = dbHelper.getWritableDatabase();


        final Intent intent = getIntent();
        addoredit = intent.getStringExtra("addoredit");
        ctype = intent.getStringExtra("ctype");

        ok = (ImageView)findViewById(R.id.ok);
        back = findViewById(R.id.back);
        name = (EditText) findViewById(R.id.edit_name);
        type = (EditText) findViewById(R.id.edit_type);
        balance = (EditText) findViewById(R.id.edit_balance);
        remark = (EditText) findViewById(R.id.edit_remark);

        type.setText(ctype.toCharArray(),0,ctype.length());

        initClickListener();
}

    private void initClickListener() {
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                creditTypeSelect = new AlertDialog.Builder(Activity_creditedit.this).create();
                popCreditType = LayoutInflater.from(Activity_creditedit.this).inflate(R.layout.pop_credittype,null);
                creditTypeSelect.setView(popCreditType);
                creditTypeSelect.show();
            }
        });



        switch (addoredit){
            case "add":
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String cname = name.getText().toString();
                        String cbalance = balance.getText().toString();
                        String cremark = remark.getText().toString();

                        ContentValues values = new ContentValues();
                        values.put("type",ctype);
                        values.put("name",cname);
                        values.put("balance",cbalance);
                        values.put("remarks",cremark);

                        db.insert("credit",null,values);

                        Intent intent1 = new Intent();
                        intent1.putExtra("position_return",2);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                });
                break;
            case "edit":

                break;

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.putExtra("position_return",2);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });



    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }


    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}