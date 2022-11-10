package com.appfinance;
import static com.google.common.primitives.Ints.min;
import static java.lang.Math.max;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Lancamento extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        Intent it = new Intent(getApplicationContext(), SaldoTotal.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);
        getSupportActionBar().setTitle("Inserir Lançamento");

        EditText editTextValor = (EditText)findViewById(R.id.editTextValor);
        editTextValor.addTextChangedListener(new MoneyTextWatcher(editTextValor));

        Button btbLancamento = (Button)findViewById(R.id.btbLancamento);
        RadioButton rdbtnReceita = (RadioButton)findViewById(R.id.rdbtnReceita);
        RadioButton rdbtnDespesa = (RadioButton)findViewById(R.id.rdbtnDespesa);
        EditText editTextCategoria = (EditText)findViewById(R.id.editTextCategoria);
        EditText editTextData = (EditText)findViewById(R.id.editTextData);

        Lancamentos lanc_edit = (Lancamentos)getIntent().getSerializableExtra("EDIT");
        if(lanc_edit != null){
            rdbtnReceita.setChecked(lanc_edit.getReceita());
            rdbtnDespesa.setChecked(lanc_edit.getDespesa());
            editTextValor.setText(lanc_edit.getValor());
            editTextData.setText(lanc_edit.getData());
            editTextCategoria.setText(lanc_edit.getCategoria());
            getSupportActionBar().setTitle("Editar Lançamento");
        } else {
            getSupportActionBar().setTitle("Inserir Lançamento");
        }
    }


    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
        }
    };

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


    public void DataEditar(View view){
        new DatePickerDialog(Lancamento.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        EditText editTextData = (EditText)findViewById(R.id.editTextData);
        editTextData.setText(sdf.format(myCalendar.getTime()));
    }

    public void Adicionar(View view){
        RadioButton rdbtnReceita = (RadioButton)findViewById(R.id.rdbtnReceita);
        RadioButton rdbtnDespesa = (RadioButton)findViewById(R.id.rdbtnDespesa);
        EditText editTextValor = (EditText)findViewById(R.id.editTextValor);
        EditText editTextData = (EditText)findViewById(R.id.editTextData);
        EditText editTextCategoria = (EditText)findViewById(R.id.editTextCategoria);

        if(TextUtils.isEmpty(editTextValor.getText())) {
            Toast.makeText(Lancamento.this, "Preencha o valor! ", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(editTextData.getText())) {
            Toast.makeText(Lancamento.this, "Preencha a data!", Toast.LENGTH_SHORT).show();
            return;
        }if(TextUtils.isEmpty(editTextCategoria.getText())) {
            Toast.makeText(Lancamento.this, "Preencha a categoria!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();

        DAOLancamentos dao = new DAOLancamentos();
        Lancamentos lanc_edit = (Lancamentos)getIntent().getSerializableExtra("EDIT");

        Lancamentos lanc = new Lancamentos(email,rdbtnReceita.isChecked(),rdbtnDespesa.isChecked(),editTextValor.getText().toString(),editTextData.getText().toString(),editTextCategoria.getText().toString());
        if(lanc_edit==null){
        dao.add(lanc).addOnSuccessListener(suc->{
            Toast.makeText(Lancamento.this, "Lançamento inserido com sucesso!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(er->{
            Toast.makeText(Lancamento.this, "Erro: "+er.getMessage(), Toast.LENGTH_SHORT).show();
        });
        } else {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("receita", rdbtnReceita.isChecked());
            hashMap.put("despesa", rdbtnDespesa.isChecked());
            hashMap.put("valor", editTextValor.getText().toString());
            hashMap.put("data", editTextData.getText().toString());
            hashMap.put("categoria", editTextCategoria.getText().toString());

            dao.update(lanc_edit.getKey(), hashMap).addOnSuccessListener(suc->{

                Toast.makeText(Lancamento.this, "Lançamento editado com sucesso! ", Toast.LENGTH_SHORT).show();

            }).addOnFailureListener(er->{

                Toast.makeText(Lancamento.this, "Erro: "+er.getMessage(), Toast.LENGTH_SHORT).show();

            });
        }

        //aq man
        Intent it = new Intent(getApplicationContext(), SaldoTotal.class);
        startActivity(it);
        finish();

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
                HashMap<String, Double> hashMap = (HashMap<String, Double>)it1.getSerializableExtra("hashMap");
                it1.putExtra("hashMap",hashMap);
                startActivity(it1);
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
                Toast.makeText(Lancamento.this, "Você saiu da sua conta", Toast.LENGTH_SHORT).show();
                Intent it6 = new Intent(getApplicationContext(), Logar.class);
                startActivity(it6);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}