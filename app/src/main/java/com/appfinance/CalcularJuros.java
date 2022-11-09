package com.appfinance;

import static com.google.common.primitives.Ints.min;
import static java.lang.Math.max;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.Locale;

public class CalcularJuros extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        Intent it = new Intent(getApplicationContext(), SaldoTotal.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_juros);
        getSupportActionBar().setTitle("Calcular Juros");
        EditText txt_valorPag = (EditText)findViewById(R.id.txt_valorPag);
        txt_valorPag.addTextChangedListener(new CalcularJuros.MoneyTextWatcher(txt_valorPag));

    }

    public void Calcular(View view){
        RadioButton simples = (RadioButton)findViewById(R.id.rdbtnSimp);
        EditText txt_valorPag = (EditText)findViewById(R.id.txt_valorPag);
        EditText txt_qtdParc = (EditText)findViewById(R.id.txt_qtdParc);
        EditText txt_porcJuros = (EditText)findViewById(R.id.txt_porcJuros);

        if(TextUtils.isEmpty(txt_valorPag.getText())) {
            Toast.makeText(CalcularJuros.this, "Preencha o valor a ser pago! ", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(txt_qtdParc.getText()) || 0 == Double.parseDouble(txt_qtdParc.getText().toString())) {
            Toast.makeText(CalcularJuros.this, "Preencha a porcentagem de juros!", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(txt_porcJuros.getText()) || 0 == Double.parseDouble(txt_porcJuros.getText().toString())){
            Toast.makeText(CalcularJuros.this, "Preencha a quantidade de parcelas!", Toast.LENGTH_SHORT).show();
            return;
        }
        String temp = "";
        temp = txt_valorPag.getText().toString();
        temp = temp.replace(".", "");
        temp = temp.replace(",", ".");

        Double valPag= null,result = null,juros=null;

        valPag = Double.valueOf(temp);
        int parc=0;

        parc = Integer.valueOf(txt_qtdParc.getText().toString());
        juros = Double.valueOf(txt_porcJuros.getText().toString());

        juros = juros/100;

        if(simples.isChecked()){
            result =   valPag*juros;
            result =  result+valPag;
        } else {
            for (int i=0; i<=parc; i++)
            {
                    result = valPag * Math.pow((1 + juros), i);
            }

        }


        AlertDialog.Builder resultado = new AlertDialog.Builder(CalcularJuros.this);
        resultado.setTitle("Valor total a ser pago:");
        resultado.setMessage("   R$: "+String.format("%.2f",result));
        resultado.setPositiveButton("OK",null);
        resultado.create().show();
    }

    public class MoneyTextWatcher implements TextWatcher {

        private EditText editText;

        private String lastAmount = "";

        private int lastCursorPosition = -1;

        public MoneyTextWatcher(EditText editText) {
            super();
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence amount, int start, int before, int count) {

            if (!amount.toString().equals(lastAmount)) {

                String cleanString = clearCurrencyToNumber(amount.toString());

                try {

                    String formattedAmount = transformToCurrency(cleanString);
                    editText.removeTextChangedListener(this);
                    editText.setText(formattedAmount);
                    editText.setSelection(formattedAmount.length());
                    editText.addTextChangedListener(this);

                    if (lastCursorPosition != lastAmount.length() && lastCursorPosition != -1) {
                        int lengthDelta = formattedAmount.length() - lastAmount.length();
                        int newCursorOffset = max(0, min(formattedAmount.length(), lastCursorPosition + lengthDelta));
                        editText.setSelection(newCursorOffset);
                    }
                } catch (Exception e) {
                    //log something
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String value = s.toString();
            if(!value.equals("")){
                String cleanString = clearCurrencyToNumber(value);
                String formattedAmount = transformToCurrency(cleanString);
                lastAmount = formattedAmount;
                lastCursorPosition = editText.getSelectionStart();
            }
        }

        public String clearCurrencyToNumber(String currencyValue) {
            String result = null;

            if (currencyValue == null) {
                result = "";
            } else {
                result = currencyValue.replaceAll("[(a-z)|(A-Z)|($,. )]", "");
            }
            return result;
        }

        public boolean isCurrencyValue(String currencyValue, boolean podeSerZero) {
            boolean result;

            if (currencyValue == null || currencyValue.length() == 0) {
                result = false;
            } else {
                if (!podeSerZero && currencyValue.equals("0,00")) {
                    result = false;
                } else {
                    result = true;
                }
            }
            return result;
        }

        public String transformToCurrency(String value) {
            double parsed = Double.parseDouble(value);
            String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format((parsed / 100));
            formatted = formatted.replaceAll("[^(0-9)(.,)]", "");
            return formatted;
        }
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
                //Toast.makeText(Dicas.this, "Teste do item 1", Toast.LENGTH_SHORT).show();
                Intent it1 = new Intent(getApplicationContext(), CalcularJuros.class);
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
                Toast.makeText(CalcularJuros.this, "Você saiu da sua conta", Toast.LENGTH_SHORT).show();
                Intent it6 = new Intent(getApplicationContext(), Logar.class);
                startActivity(it6);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}