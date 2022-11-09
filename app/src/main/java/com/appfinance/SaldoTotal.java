package com.appfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class SaldoTotal extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter adapter;
    DAOLancamentos dao;
    boolean isLoading = false;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_total);
        getSupportActionBar().setTitle("Saldo Total");

        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        dao = new DAOLancamentos();
        loadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading=true;
                        loadData();
                    }
                }
            }
        });


    }

    double saldototal=0;
    private void loadData() {

        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Lancamentos> lanc = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Lancamentos lan = data.getValue(Lancamentos.class);
                    lan.setKey(data.getKey());
                    lanc.add(lan);
                    key = data.getKey();

                    //teste de pegar valores das variaveis de valor para somar dps
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    String temp="",comp1="",comp2="";
                    comp1=mAuth.getCurrentUser().getEmail();
                    comp2=lan.getEmail();
                    if(comp1.equals(comp2)) {
                        temp = lan.getValor();
                        temp = temp.replace(".", "");
                        temp = temp.replace(",", ".");
                        if (lan.getDespesa()) {
                            saldototal += Double.valueOf(temp)*-1;
                        } else {
                            saldototal += Double.valueOf(temp);
                        }
                    }
                }

                String temp = "R$: "+String.format("%.2f",saldototal);
                temp = temp.replace(".", ",");
                getSupportActionBar().setTitle("Saldo Total "+temp);
                adapter.setItems(lanc);
                adapter.notifyDataSetChanged();
                isLoading=false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        MenuItem itemUsuario = menu.findItem(R.id.itemUsuario);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        itemUsuario.setTitle((mAuth.getCurrentUser().getEmail()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemHome:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dicas.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it1 = new Intent(getApplicationContext(), Home.class);
                startActivity(it1);
                finish();
                return true;

            case R.id.itemAnalise:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dicas.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it2 = new Intent(getApplicationContext(), Analise.class);
                startActivity(it2);
                finish();
                return true;

            case R.id.itemDicas:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dicas.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it3 = new Intent(getApplicationContext(), Dicas.class);
                startActivity(it3);
                finish();
                return true;

            case R.id.itemLancamento:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dicas.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it4 = new Intent(getApplicationContext(), Lancamento.class);
                startActivity(it4);
                finish();
                return true;

            case R.id.itemSaldoTotal:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dicas.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it5 = new Intent(getApplicationContext(), SaldoTotal.class);
                startActivity(it5);
                finish();
                return true;

            case R.id.itemDeslogar:
                //troca de tela aq só ir repetindo
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(SaldoTotal.this, "Você saiu da sua conta", Toast.LENGTH_SHORT).show();
                Intent it6 = new Intent(getApplicationContext(), Logar.class);
                startActivity(it6);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}