package com.appfinance;

import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LancamentosVH extends RecyclerView.ViewHolder {

    public TextView txt_email,txt_receita,txt_despesa,txt_valor,txt_data,txt_categoria,txt_option;
    public View layoutAll;

    public LancamentosVH(@NonNull View itemView) {
        super(itemView);
        txt_email = itemView.findViewById(R.id.txt_email);
        txt_receita = itemView.findViewById(R.id.txt_receita);
        txt_despesa = itemView.findViewById(R.id.txt_despesa);
        txt_valor = itemView.findViewById(R.id.txt_valor);
        txt_data = itemView.findViewById(R.id.txt_data);
        txt_categoria = itemView.findViewById(R.id.txt_categoria);
        txt_option = itemView.findViewById(R.id.txt_option);
        layoutAll = itemView.findViewById(R.id.layoutAll);
    }
}
