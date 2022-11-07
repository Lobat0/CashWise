package com.appfinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastrar extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        Intent it = new Intent(getApplicationContext(), Logar.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
    }

    public void Cadastro(View view){

        EditText editTextTextSenha = (EditText)findViewById(R.id.editTextSenhaCad);
        EditText editTextTextEmail = (EditText)findViewById(R.id.editTextTextEmailCad);
        String email = editTextTextEmail.getText().toString();
        String password = editTextTextSenha.getText().toString();


        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        try{
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Cadastrar.this, "Usuário registrado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Cadastrar.this, "Erro de Registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        } catch (Exception e){
            Toast.makeText(Cadastrar.this, "Erro:"+e, Toast.LENGTH_SHORT).show();
        }

    }
    }
