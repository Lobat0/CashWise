package com.appfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.auth.FirebaseAuth;

public class Dica extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter2 adapter;
    DAODicas dao;
    boolean isLoading = false;
    String key = null;
    FirebaseRecyclerAdapter fadapter;


    @Override
    public void onBackPressed(){
        Intent it = new Intent(getApplicationContext(), SaldoTotal.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas);
        getSupportActionBar().setTitle("Dicas");

        swipeRefreshLayout = findViewById(R.id.swip2);
        recyclerView = findViewById(R.id.rv2);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RVAdapter2(this);
        recyclerView.setAdapter(adapter);
        dao = new DAODicas();
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
                ArrayList<Dicas> dics = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Dicas dic = data.getValue(Dicas.class);
                    dic.setKey(data.getKey());
                    dics.add(dic);
                    key = data.getKey();
                }
                adapter.setItems(dics);
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
            case R.id.itemCalcJuros:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dica.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it1 = new Intent(getApplicationContext(), CalcularJuros.class);
                startActivity(it1);
                finish();
                return true;


            case R.id.itemLancamento:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dica.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it4 = new Intent(getApplicationContext(), Lancamento.class);
                startActivity(it4);
                finish();
                return true;

            case R.id.itemSaldoTotal:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dica.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it5 = new Intent(getApplicationContext(), SaldoTotal.class);
                startActivity(it5);
                finish();
                return true;

            case R.id.itemDeslogar:
                //troca de tela aq só ir repetindo
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(Dica.this, "Você saiu da sua conta", Toast.LENGTH_SHORT).show();
                Intent it6 = new Intent(getApplicationContext(), Logar.class);
                startActivity(it6);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}