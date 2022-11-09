package com.appfinance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;


public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Lancamentos> list = new ArrayList<>();

    public RVAdapter(Context ctx){
        this.context = ctx;
    }

    public void setItems(ArrayList<Lancamentos> lanc){
        list.addAll(lanc);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items,parent,false);
        return new LancamentosVH(view);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Lancamentos e = null;
        this.onBindViewHolder(holder,position,e);
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Lancamentos e) {
        LancamentosVH vh = (LancamentosVH) holder;
        Lancamentos lanc = e==null? list.get(position):e;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String comp1="",comp2="";
        comp1=mAuth.getCurrentUser().getEmail();
        comp2=lanc.getEmail();
        if(comp1.equals(comp2)) {
            vh.txt_email.setText(lanc.getEmail());
            vh.txt_receita.setText(lanc.getReceita().toString());
            vh.txt_despesa.setText(lanc.getDespesa().toString());
            vh.txt_valor.setText(lanc.getValor());

            if(lanc.getReceita()){
                vh.txt_valor.setTextColor(Color.GREEN);
            } else {
                vh.txt_valor.setTextColor(Color.RED);
            }

            vh.txt_data.setText(lanc.getData());
            vh.txt_categoria.setText(lanc.getCategoria());
        } else {
            vh.layoutAll.setVisibility(View.GONE);
        }
        vh.txt_option.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(context,vh.txt_option);
            popupMenu.inflate(R.menu.options_menu);

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.menu_edit:
                        Intent intent = new Intent(context,Lancamento.class);
                        intent.putExtra("EDIT",lanc);
                        context.startActivity(intent);
                        ((Activity)(context)).finish();
                        break;

                    case R.id.menu_remove:

                        DAOLancamentos dao = new DAOLancamentos();
                        dao.remove(lanc.getKey()).addOnSuccessListener(suc->
                        {
                            Toast.makeText(context, "Lançamento removido com sucesso!", Toast.LENGTH_SHORT).show();
                            // tava bugando ai coloquei pra dar finish msm, dps tento arrumar o codigo debaixo era o certo mas n funciona
                            // notifyItemRemoved(position);
                            // list.remove(lanc);
                            Intent intent2 = new Intent(context,SaldoTotal.class);
                            context.startActivity(intent2);
                            ((Activity)(context)).finish();
                        }).addOnFailureListener(er->{
                            Toast.makeText(context, "Erro: "+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                        break;

                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
