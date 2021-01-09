package com.example.zzyyff.flowerrecords;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class fragment_mine extends Fragment implements View.OnClickListener{
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView daysentence;
    LinearLayout changeskin,usehelp,feedback,setting,quotaAlert;
    private static final String Url = "http://api.testvip.club/index/index/dailySentence";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mine, container, false);
        sp = getContext().getSharedPreferences("mine", Context.MODE_PRIVATE);
        editor = sp.edit();

        changeskin = view.findViewById(R.id.changeskin);
        usehelp = view.findViewById(R.id.usehelp);
        feedback = view.findViewById(R.id.feedback);
        setting = view.findViewById(R.id.setting);
        quotaAlert = view.findViewById(R.id.quotaAlert);

        changeskin.setOnClickListener(this);
        usehelp.setOnClickListener(this);
        feedback.setOnClickListener(this);
        setting.setOnClickListener(this);
        sendRequsetWithOkHttp();
        showQuota();
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeskin://换肤
                SPUtils.put(getContext(),"choose","F");
                Intent intent3 = new Intent(getContext(),Activity_StyleChanged.class);
                startActivityForResult(intent3,1);
                break;
            case R.id.usehelp://使用帮助
                Intent intent23 = new Intent(getContext(),Activity_usehelp.class);
                startActivity(intent23);
                break;
            case R.id.feedback://关于
                Intent intent4 = new Intent(getContext(),Activity_About.class);
                startActivity(intent4);
                break;
            case R.id.setting://关于
                Intent intent5 = new Intent(getContext(),Activity_quota.class);
                startActivity(intent5);
                break;
        }
    }

    private void sendRequsetWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(Url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    parseJSONObject(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }
    private void parseJSONObject(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

                final String dailysentence = jsonObject.getString("content");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        daysentence.setText(dailysentence);
                    }
                });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    if(data.getStringExtra("resetTheme").equals("ture")){
                        getActivity().recreate();
                    }
                }
        }
    }

    void showQuota() {
        tools_MyDatabaseHelper dbHelper_mine = new tools_MyDatabaseHelper(getActivity(), "mine.db", null, 1);
        SQLiteDatabase db_mine = dbHelper_mine.getWritableDatabase();
        String date_signed;
        String jud = "yes";
        Cursor cursor1 = db_mine.rawQuery("select date_signed from mine" +
                        " Where name=?",
                new String[]{"QJ-QWG-JHC"});
        if(cursor1.moveToFirst()) {
            date_signed = cursor1.getString(0);
        } else {
            date_signed = null;
        }
        //Log.e("是否显示额度警告：",date_signed);
        if(date_signed.equals(jud)) {
            quotaAlert.setVisibility(View.VISIBLE);
        } else {
            quotaAlert.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showQuota();
    }

}
