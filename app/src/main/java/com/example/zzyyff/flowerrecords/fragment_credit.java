package com.example.zzyyff.flowerrecords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class fragment_credit extends Fragment {
    RecyclerView rec;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    View view;
    List<class_Credit>credits = new ArrayList();
    com.example.zzyyff.flowerrecords.adapter_credit adapter_credit;

    ImageView add_credit;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new tools_MyDatabaseHelper(getContext(), "credit.db", null, 1);
        db = dbHelper.getWritableDatabase();
        view = inflater.inflate(R.layout.layout_credit, container, false);
        rec = view.findViewById(R.id.rec_credit);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));


        add_credit = view.findViewById(R.id.add_credit);

        add_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),  Activity_creditedit.class);
                startActivity(intent);
            }
        });
        return view;
    }
    protected void initList()
    {
        credits.clear();
        Cursor cursor = db.query("credit", null, null, null, null, null, "id");
        if (cursor.moveToFirst()) {
            do
            {
               credits.add(new class_Credit(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("type")),
                       cursor.getFloat(cursor.getColumnIndex("balance")),
                        cursor.getString(cursor.getColumnIndex("image_path"))
               ));
            }while (cursor.moveToNext());
        }
    }

    public void onResume() {
        super.onResume();
        initList();
        adapter_credit = new adapter_credit(credits, getContext());
        rec.setAdapter(adapter_credit);
    }


}
