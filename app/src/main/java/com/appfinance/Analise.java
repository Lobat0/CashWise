package com.appfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Analise extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        Intent it = new Intent(getApplicationContext(), SaldoTotal.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analise);
        getSupportActionBar().setTitle("Análise de Valores");

        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> valores = new ArrayList<>();

        Intent intent = getIntent();
        HashMap<String, Double> hashMap = (HashMap<String, Double>)intent.getSerializableExtra("hashMap");

        for(String i  :  hashMap.keySet() ) {
            String tempString = Double.toString(hashMap.get(i));
            valores.add(new PieEntry(Float.parseFloat(tempString), i));
        }

        PieDataSet pieDataSet = new PieDataSet(valores, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setFormSize(20f);

        PieData pieData = new PieData(pieDataSet);
        Legend l = pieChart.getLegend();

        l.setTextColor(Color.WHITE);
        l.setTextSize(20f);

        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setHoleColor(Color.parseColor("#1F1717"));
        pieData.setValueTextSize(20f);
        pieChart.setCenterTextSize(20f);
        pieChart.setData(pieData);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Valores em porcentagem e suas categorias");
        pieChart.animate();
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

            case R.id.itemDicas:
                //troca de tela aq só ir repetindo
                //Toast.makeText(Dica.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it3 = new Intent(getApplicationContext(), Dica.class);
                startActivity(it3);
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
                Toast.makeText(Analise.this, "Você saiu da sua conta", Toast.LENGTH_SHORT).show();
                Intent it6 = new Intent(getApplicationContext(), Logar.class);
                startActivity(it6);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}