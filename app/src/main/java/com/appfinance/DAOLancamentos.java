package com.appfinance;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class DAOLancamentos {

    private DatabaseReference databaseReference;

    public DAOLancamentos(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Lancamentos.class.getSimpleName());
    }

    public Task<Void> add(Lancamentos lanc){

        return databaseReference.push().setValue(lanc);

    }


}
