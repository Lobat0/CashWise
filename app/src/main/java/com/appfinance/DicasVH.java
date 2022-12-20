package com.appfinance;

import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DicasVH extends RecyclerView.ViewHolder {

    public TextView txt_dicas;
    public View layoutAll2;

    public DicasVH(@NonNull View itemView) {
        super(itemView);
        txt_dicas = itemView.findViewById(R.id.txt_dicas);
        layoutAll2 = itemView.findViewById(R.id.layoutAll2);
    }
}
