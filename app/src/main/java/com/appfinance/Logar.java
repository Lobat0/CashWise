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

public class Logar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar);
    }

    public void Entrar(View view){
        EditText editTextTextSenha = (EditText)findViewById(R.id.editTextTextSenha);
        EditText editTextTextEmail = (EditText)findViewById(R.id.editTextTextEmail);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        try{
        String email = editTextTextEmail.getText().toString();
        String password = editTextTextSenha.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Logar.this, "Usuário logado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(getApplicationContext(), SaldoTotal.class);
                    startActivity(it);
                    finish();
                }else{
                    Toast.makeText(Logar.this, "Erro de Login: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        } catch (Exception e){
            Toast.makeText(Logar.this, "Erro:"+e, Toast.LENGTH_SHORT).show();
        }
    }

    public void EsqSenha(View view){
        EditText editTextTextEmail = (EditText)findViewById(R.id.editTextTextEmail);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        try{
        String email = editTextTextEmail.getText().toString();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Logar.this, "Email enviado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Logar.this, "Erro de Login: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        } catch (Exception e){
            Toast.makeText(Logar.this, "Erro:"+e, Toast.LENGTH_SHORT).show();
        }
    }

    public void Cadastrar(View view){
        Intent it = new Intent(getApplicationContext(), Cadastrar.class);
        startActivity(it);
        finish();
    }
}