package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.Toast;

public class Activity_creditedit extends AppCompatActivity {
    //接受intent数据
    String addoredit;
    String ctype;
    String cname;
    String cbalance;
    String cremark;
//    String select;

    //账户编辑页面
    ImageView back;
    ImageView ok;
    EditText type;
    EditText name;
    EditText balance;
    EditText remark;

    //账户选择页面
    AlertDialog creditTypeSelect;
    View popCreditType;
    LinearLayout type_cash;
    LinearLayout type_saving;
    LinearLayout type_credit;//指账户类别中的信用卡
    LinearLayout type_online;//指账户类别中的网络支付账户

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

        initAlertDialog();
        initClickListener();

}


    private void initAlertDialog() {
        creditTypeSelect = new AlertDialog.Builder(Activity_creditedit.this).create();
        popCreditType = LayoutInflater.from(Activity_creditedit.this).inflate(R.layout.pop_credittype,null);
        creditTypeSelect.setView(popCreditType);
        creditTypeSelect.setCanceledOnTouchOutside(true);

        type_cash = popCreditType.findViewById(R.id.type_cash);
        type_saving = popCreditType.findViewById(R.id.type_saving);
        type_credit = popCreditType.findViewById(R.id.type_credit);
        type_online = popCreditType.findViewById(R.id.type_online);

        type.setText(ctype.toCharArray(),0,ctype.length());

    }
    private void initClickListener() {
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initAlertDialog();
                creditTypeSelect.show();

                type_cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type.setText("现金");
                        creditTypeSelect.dismiss();
                    }

                });
                type_saving.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type.setText("储蓄卡");
                        creditTypeSelect.dismiss();
                    }
                });
                type_credit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type.setText("信用卡");
                        creditTypeSelect.dismiss();
                    }
                });
                type_online.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        type.setText("网络支付账户");
                        creditTypeSelect.dismiss();
                    }
                });

            }
        });

        final Intent intent = getIntent();
        addoredit = intent.getStringExtra("addoredit");
        ctype = intent.getStringExtra("ctype");

//        type.setText(select);

        switch (addoredit){
            case "add":
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cname = name.getText().toString();
                        cbalance = balance.getText().toString();
                        cremark = remark.getText().toString();

                        if(cname.isEmpty() || cbalance.isEmpty()){
                            Toast.makeText(Activity_creditedit.this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                        }else{
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
                    }
                });
                break;
            case "edit":
                final int id = intent.getIntExtra("id",0);
                cname = intent.getStringExtra("cname");
                cbalance = intent.getStringExtra("cbalance");
                cremark = intent.getStringExtra("cremark");

                cbalance = cbalance.replace("￥","");

                name.setText(cname);
                balance.setText(cbalance);
                remark.setText(cremark);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ctype = type.getText().toString();
                        cname = name.getText().toString();
                        cbalance = balance.getText().toString();
                        cremark = remark.getText().toString();

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ContentValues values = new ContentValues();
                                values.put("type",ctype);
                                values.put("name",cname);
                                values.put("balance",cbalance);
                                values.put("remarks",cremark);


                                db.update("credit",values,"id=?",new String[]{String.valueOf(id)});
                                Intent intent1 = new Intent();
                                intent1.putExtra("position_return",2);
                                setResult(RESULT_OK,intent1);
                                finish();
                            }
                        });


                    }
                });
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