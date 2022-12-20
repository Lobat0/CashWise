package com.appfinance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;


public class RVAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Dicas> list = new ArrayList<>();

    public RVAdapter2(Context ctx){
        this.context = ctx;
    }

    public void setItems(ArrayList<Dicas> dic){
        list.addAll(dic);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items2,parent,false);
        return new DicasVH(view);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Dicas e = null;
        this.onBindViewHolder(holder,position,e);
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Dicas e) {
        DicasVH vh = (DicasVH) holder;
        Dicas dic = e==null? list.get(position):e;

        vh.txt_dicas.setText(dic.getDicas());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

