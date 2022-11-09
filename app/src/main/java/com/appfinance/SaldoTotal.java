package com.appfinance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.List;

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
                }
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