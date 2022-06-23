package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class prof_remover extends AppCompatActivity {

    FirebaseFirestore fcloud;
    private FirebaseAuth mAuth;
    List<Pacote> listaPacotes;
    String TAG = "Gerir Pacotes";
    Pacote selecionado;
    String username;
    ArrayAdapter<Pacote> adapter;

    Spinner spinner;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_removerpacotes);

        spinner = findViewById(R.id.spinner);
        desc = findViewById(R.id.description);

        listaPacotes = new ArrayList<>();
        username = getIntent().getExtras().getString("Username");

        adapter = new ArrayAdapter<Pacote>(this,
                R.layout.spinner_item, listaPacotes);
        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecionado = (Pacote) adapterView.getSelectedItem();
                displayDescription(selecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getPackages();

    }

    public void Leave(View v){
        endActivity(v);
    }

    public void getSelectedPackage(View v){
        selecionado = (Pacote) spinner.getSelectedItem();
        displayDescription(selecionado);
    }

    private void displayDescription(Pacote pacote){
        String description = pacote.getDescription();

        String data = "Descrição" + ":\n\n" + description;
        desc.setText(data);
    }

    public void OpenRemove(View v){
        selecionado = (Pacote) spinner.getSelectedItem();
        if(selecionado != null){
            FirebaseAuth.getInstance();
            CollectionReference collectionReference = fcloud.collection("Packages");
            collectionReference.document(selecionado.packageID).delete();
            listaPacotes.remove(selecionado);
            adapter.notifyDataSetChanged();
        }
    }

    public void getPackages(){

        //mAuth = FirebaseAuth.getInstance();
        fcloud = FirebaseFirestore.getInstance();
        //String userID = mAuth.getCurrentUser().getUid();
        Gson gson = new Gson();

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        CollectionReference collectionReference = fcloud.collection("Packages");
        collectionReference.whereEqualTo("AuthorID", userID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for( QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String aux = documentSnapshot.getString("PackageGSON");
                    Pacote aux2 = gson.fromJson(aux,Pacote.class);
                    listaPacotes.add(aux2);
                    adapter.notifyDataSetChanged();

                }
                Log.d(TAG,"dentro da lista: " + listaPacotes.toString());
            }
        });
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        finish();
    }
}