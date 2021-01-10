package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.effect.EffectFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_credit extends Fragment {
    RecyclerView rec;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    tools_MyDatabaseHelper dbHelper2;
    SQLiteDatabase db2;

    View view;
    List<class_Credit>credits = new ArrayList();
    adapter_credit adapter_credit;

    ImageView add_credit;//添加账户，源代码记账用了account,无奈账户只能用credit表示

    View popCreditType;
    AlertDialog creditTypeSelect;
    LinearLayout type_cash;
    LinearLayout type_saving;
    LinearLayout type_credit;//指账户类别中的信用卡
    LinearLayout type_online;//指账户类别中的网络支付账户

    TextView assets;
    TextView debt;
    TextView sum;

    HashMap accredits;

//    账户结算
    float casset = 0;
    float cdebt = 0;
    float csum = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new tools_MyDatabaseHelper(getContext(), "credit.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper2 = new tools_MyDatabaseHelper(getContext(), "record.db", null, 1);
        db2 = dbHelper2.getWritableDatabase();
        view = inflater.inflate(R.layout.layout_credit, container, false);
        rec = view.findViewById(R.id.rec_credit);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));

        casset = 0;
        cdebt = 0;
        csum = 0;

        initClickListener();
        initAlertDialog();
        initSum();

        return view;
    }

    private void initSum(){

        assets = view.findViewById(R.id.assets);
        debt = view.findViewById(R.id.debt);
        sum = view.findViewById(R.id.sum);


        //净资产结算

//        数据库账户原始资产结算
        Cursor cursor = db.query("credit",new String[]{"balance"},null,null,null,null,null);
//        if (cursor.moveToFirst()) {
//            do
//            {
//                float num =cursor.getFloat(cursor.getColumnIndex("balance"));
//                if(num>0){
//                    casset = casset + num;
//                }
//                else {
//                    cdebt= cdebt + num;
//                }
//            }while (cursor.moveToNext());
//        }

        //        根据各账户净收支动态显示账户资产结算

        csum = casset + cdebt;
        assets.setText(String.valueOf(casset));
        debt.setText(String.valueOf(cdebt));
        sum.setText(String.valueOf(csum));
        if(csum<0){
            sum.setTextColor(0xfff87070);
        }



    }


    private void initClickListener() {
        add_credit = view.findViewById(R.id.add_credit);
        add_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creditTypeSelect.show();
                type_cash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Activity_creditedit.class);
                        intent.putExtra("addoredit","add");
                        intent.putExtra("ctype","现金");
                        startActivityForResult(intent, 1);
//                        startActivity(intent);
                        creditTypeSelect.dismiss();
                    }
                });
                type_saving.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Activity_creditedit.class);
                        intent.putExtra("addoredit","add");
                        intent.putExtra("ctype","储蓄卡");
                        startActivityForResult(intent, 1);
//                        startActivity(intent);
                        creditTypeSelect.dismiss();
                    }
                });
                type_credit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Activity_creditedit.class);
                        intent.putExtra("addoredit","add");
                        intent.putExtra("ctype","信用卡");
//                        startActivity(intent);
                        startActivityForResult(intent, 1);
                        creditTypeSelect.dismiss();
                    }
                });
                type_online.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Activity_creditedit.class);
                        intent.putExtra("addoredit","add");
                        intent.putExtra("ctype","网络支付账户");
                        startActivityForResult(intent, 1);
//                        startActivity(intent);
                        creditTypeSelect.dismiss();
                    }
                });
            }
        });

    }

    private void initAlertDialog() {
        creditTypeSelect = new AlertDialog.Builder(getActivity()).create();
        popCreditType = LayoutInflater.from(getActivity()).inflate(R.layout.pop_credittype,null);
        creditTypeSelect.setView(popCreditType);
        creditTypeSelect.setCanceledOnTouchOutside(true);

        type_cash = popCreditType.findViewById(R.id.type_cash);
        type_saving = popCreditType.findViewById(R.id.type_saving);
        type_credit = popCreditType.findViewById(R.id.type_credit);
        type_online = popCreditType.findViewById(R.id.type_online);
    }

//    根据各账户净收支动态显示账户余额
    protected void initList()
    {
        credits.clear();
        accredits = new HashMap();
        //结算各账单净收支
//        select sum(cost),sum(income),paymethod from record group by paymethod
        Cursor cursor2 = db2.rawQuery("select sum(cost) as accost,sum(income) as acincome,paymethod from record group by paymethod",
                null);

        float acsum = 0;

        if (cursor2.moveToFirst()) {
            do
            {
                float accost = 0;
                float acincome = 0;
                acsum = 0;
                String acname = new String();

                acname = cursor2.getString(cursor2.getColumnIndex("paymethod"));
                accost = cursor2.getFloat(cursor2.getColumnIndex("accost"));
                acincome = cursor2.getFloat(cursor2.getColumnIndex("acincome"));
                acsum = acincome - accost;
                accredits.put(acname,acsum);
            }while (cursor2.moveToNext());
        }


        //显示账户
        Cursor cursor = db.query("credit", null, null, null, null, null, "id");
        if (cursor.moveToFirst()) {
            do
            {
                String acname = new String();
                float acbalance = 0;
                Object osum = 0;
                acsum = 0;
                acname = cursor.getString(cursor.getColumnIndex("name"));
                acbalance = cursor.getFloat(cursor.getColumnIndex("balance"));
                osum = accredits.get(""+acname);
                if(osum == null){
                    acsum = acbalance;
                }
                else{
                    acsum = (float)osum + acbalance;
                }

                if(acsum>0){
                    casset = casset + acsum;
                }else{
                    cdebt = cdebt + acsum;
                }


                credits.add(new class_Credit(cursor.getInt(cursor.getColumnIndex("id")),
                        acname,
                        cursor.getString(cursor.getColumnIndex("type")),
                        acsum,
                        cursor.getString(cursor.getColumnIndex("image_path"))
               ));
            }while (cursor.moveToNext());
        }
    }

//    显示数据库中账户原始余额
//    protected void initList()
//    {
//        credits.clear();
//
//        Cursor cursor = db.query("credit", null, null, null, null, null, "id");
//        if (cursor.moveToFirst()) {
//            do
//            {
//                credits.add(new class_Credit(cursor.getInt(cursor.getColumnIndex("id")),
//                        cursor.getString(cursor.getColumnIndex("name")),
//                        cursor.getString(cursor.getColumnIndex("type")),
//                        cursor.getFloat(cursor.getColumnIndex("balance")),
//                        cursor.getString(cursor.getColumnIndex("image_path"))
//                ));
//            }while (cursor.moveToNext());
//        }
//    }

    public void onResume() {
        super.onResume();
        initList();
        adapter_credit = new adapter_credit(credits, getContext());
        rec.setAdapter(adapter_credit);
        initSum();
    }


}
