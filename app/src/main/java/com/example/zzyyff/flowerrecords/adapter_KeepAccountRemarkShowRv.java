package com.example.zzyyff.flowerrecords;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class adapter_KeepAccountRemarkShowRv extends RecyclerView.Adapter< adapter_KeepAccountRemarkShowRv.ViewHolder> {
    private List<class_credititem> remarkList;
    Context context;
    private int lastpos = -1;
    static ViewHolder lastholder;
    static class ViewHolder extends RecyclerView.ViewHolder{
       LinearLayout remarkLayout;
       TextView tvRemarkShow;
        public ViewHolder(View view){
            super(view);
            remarkLayout = (LinearLayout)view.findViewById(R.id.remarkLayout);
            tvRemarkShow = (TextView)view.findViewById(R.id.tvremark);
//            lastholder = new ViewHolder(view);
        }
    }
    public adapter_KeepAccountRemarkShowRv(List<class_credititem> List){
        remarkList = List;

    }
    public adapter_KeepAccountRemarkShowRv(List<class_credititem> List, int i){
        remarkList = List;
        lastpos = i;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keep_account_remark,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        context = parent.getContext();
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        holder.remarkLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(!remarkList.get(holder.getAdapterPosition()).isIsclick()){
//                    final ViewHolder lastholder = holder;
                    if (lastpos == -1) {
                        lastholder = holder;
                    }
                    else {
                        lastholder.remarkLayout.setBackgroundResource(R.drawable.ic_key_back);
                        lastholder.tvRemarkShow.setTextColor(parent.getContext().getResources().getColor(typedValue.resourceId));
                        remarkList.get(lastholder.getAdapterPosition()).setIsclick(false);

                        lastholder = holder;

                    }

                    lastpos = holder.getAdapterPosition();
                    holder.remarkLayout.setBackgroundResource(R.drawable.ic_remark_click);
                    holder.tvRemarkShow.setTextColor(parent.getContext().getResources().getColor(R.color.color_gold));
                    remarkList.get(holder.getAdapterPosition()).setIsclick(true);
                }
                else {
                    lastpos = -1;
                    holder.remarkLayout.setBackgroundResource(R.drawable.ic_key_back);
                    holder.tvRemarkShow.setTextColor(parent.getContext().getResources().getColor(typedValue.resourceId));
                    remarkList.get(holder.getAdapterPosition()).setIsclick(false);
                }
            }
        });

        holder.remarkLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        return holder;
    }

    public String getRemark(){
        StringBuilder returnRemark = new StringBuilder();
        for(int i=0;i<remarkList.size();i++){
            if(remarkList.get(i).isIsclick()){
                returnRemark.append(remarkList.get(i).getName());
            }else {
                returnRemark.delete(0, returnRemark.length());
            }
        }
        return returnRemark.toString();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvRemarkShow.setText(remarkList.get(position).getName());
        //Log.e("ADPTER", remarkList.get(position).toString());
        if(remarkList.get(position).isIsclick()){
            holder.remarkLayout.setBackgroundResource(R.drawable.ic_remark_click);
            holder.tvRemarkShow.setTextColor(context.getResources().getColor(R.color.color_gold));
            lastholder = holder;
            lastpos = position;
        }
    }

    @Override
    public int getItemCount() {
        return remarkList.size();
    }


}
