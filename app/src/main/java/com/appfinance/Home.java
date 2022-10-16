package com.appfinance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        getSupportActionBar().hide();

        Button btnCalcJuros = (Button)findViewById(R.id.btnCalcJuros);

        btnCalcJuros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent it = new Intent(getApplicationContext(), CalcularJuros.class);
                startActivity(it);
                finish();

            }
        });
    }
}