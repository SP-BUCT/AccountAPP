package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class adapter_credit extends RecyclerView.Adapter<adapter_credit.ViewHolder> {
    public tools_MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;
    List<class_Credit> list;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView credit_name;
        TextView credit_type;
        TextView credit_balance;
        ImageView credit_type_pic;
        CardView click_credit_item;
//        TextView credit_remarks;
        private ViewHolder(View view) {
            super(view);
            credit_name = view.findViewById(R.id.credit_name);
            credit_type = view.findViewById(R.id.credit_type);
            credit_balance = view.findViewById(R.id.credit_balance);
            credit_type_pic = view.findViewById(R.id.credit_type_pic);
            click_credit_item = view.findViewById(R.id.click_credit_item);
//            credit_remarks = view.findViewById(R.id.credit_remarks);
        }
    }

    public adapter_credit(List <class_Credit> objects, Context mcontext) {
        context = mcontext;
        list = objects;
        dbHelper = new tools_MyDatabaseHelper(mcontext, "credit.db",null,1);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final class_Credit card = list.get(position);
        holder.credit_name.setText(card.getName());
        holder.credit_type.setText(card.getType());
        holder.credit_balance.setText("￥"+card.getBalance());
        displayImage(holder.credit_type_pic,card.getType());
        holder.click_credit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Activity_creditedit.class);
                intent.putExtra("id", card.getId());
                intent.putExtra("addoredit","edit");
                intent.putExtra("cname",card.getName());
                intent.putExtra("ctype",card.getType());
                intent.putExtra("cbalance","￥"+card.getBalance());
                intent.putExtra("cimage_path",card.getImage_path());
                intent.putExtra("cremark",card.getRemarks());
                context.startActivity(intent);
            }
        });
        holder.click_credit_item.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setIcon(R.drawable.parttime)
                        .setTitle("警告")
                        .setMessage("确认删除嘛？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.delete("credit","id=?",new String[]{
                                        String.valueOf(card.getId())
                                });
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null).create().show();
                return  false;
            }
        });
    }

    public  static Bitmap getBitmap(String filePath, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        options.inSampleSize = inSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath,options);
    }

    public static int inSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width<=height){//竖屏拍照
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height
                        / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
        }else {//横屏
            if (height > reqWidth || width > reqHeight) {
                final int heightRatio = Math.round((float) height
                        / (float) reqWidth);
                final int widthRatio = Math.round((float) width / (float) reqHeight);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
        }
        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void displayImage(ImageView credit_image, String type) {
//        if(imagePath != null) {
//        credit_image.setImageBitmap(getBitmap(imagePath,360,640));
        switch (type)
        {
            case "现金":
                credit_image.setImageResource(R.drawable.cash);
                break;
            case "储蓄卡":
                credit_image.setImageResource(R.drawable.saving_card);
                credit_image.setPadding(7,7,7,7);
                break;
            case "信用卡":
                credit_image.setImageResource(R.drawable.credit_card);
                credit_image.setPadding(7,7,7,7);
                break;
            case "网络支付账户":
                credit_image.setImageResource(R.drawable.wifi);
                break;
        }

//        }
    }

    private void initTypeface(TextView textView) {
        Typeface typefaceModeran = Typeface.createFromAsset(context.getAssets(), "fonts/label.ttf");
        textView.setTypeface(typefaceModeran);
    }
}
