package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class estudante_classificar extends AppCompatActivity {

    FirebaseFirestore fcloud;
    private FirebaseAuth mAuth;
    List<Pacote> listaPacotes;
    String TAG = "estudante_classificar";
    Pacote selecionado;
    String username;
    ArrayAdapter<Pacote> adapter;
    double total = 0.0;
    double combined = 0.0;
    boolean scoreExists = false;
    double oldRating = 0.0;

    Spinner spinner;
    TextView desc;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudante_classificar);

        spinner = findViewById(R.id.spinner);
        desc = findViewById(R.id.description);
        ratingBar = findViewById(R.id.ratingBarClassify);

        listaPacotes = new ArrayList<>();

        adapter = new ArrayAdapter<Pacote>(this,
                R.layout.spinner_item, listaPacotes);
        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecionado = (Pacote) adapterView.getSelectedItem();
                if(selecionado != null)
                    displayDescription(selecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getPackages();

        mAuth = FirebaseAuth.getInstance();

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

        String data = "Descrição:\n\n" + description;
        desc.setText(data);
    }

    public void OpenClassify(View v){

        double rating = ratingBar.getRating();
        oldRating = 0.0;

        fcloud = FirebaseFirestore.getInstance();

        selecionado = (Pacote) spinner.getSelectedItem();
        if(selecionado != null) {
            String userID = mAuth.getCurrentUser().getUid();

            DocumentReference docUserRatings = fcloud.collection("Packages").document(selecionado.packageID).collection("Ratings").document(userID);

            docUserRatings.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        if(task.getResult().exists() ) {
                            scoreExists = true;
                            oldRating = task.getResult().getDouble("Rating");
                        }

                        Map<String, Object> packageM = new HashMap<>();

                        packageM.put("Rating", rating);
                        docUserRatings.set(packageM).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(estudante_classificar.this, "Pontuação dada: " + rating, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.d("TAG", "(prof_criar) onFailure: " + e.toString());
                                Toast.makeText(estudante_classificar.this, "Houve um erro ao dar a pontuação", Toast.LENGTH_LONG).show();
                            }
                        });
                        //Log.d(TAG, "Final rating: " + selecionado.getRating());
                        DocumentReference pacoteDoc = fcloud.collection("Packages").document(selecionado.packageID);
                        pacoteDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                Map<String, Object> packageM = new HashMap<>();

                                double total = 0;
                                int nclassificacoes = 0;

                                if (documentSnapshot.get("Nclassificacoes", int.class) != null && documentSnapshot.get("TotalClassificacoes", double.class) != null) {
                                    total = documentSnapshot.getDouble("TotalClassificacoes");
                                    nclassificacoes = documentSnapshot.get("Nclassificacoes", int.class);
                                }

                                if(!scoreExists) {
                                    nclassificacoes += 1;
                                }

                                total -= oldRating;
                                total +=  rating;

                                selecionado.setRating(total / nclassificacoes);

                                packageM.put("Nclassificacoes", nclassificacoes);
                                packageM.put("TotalClassificacoes", total);
                                packageM.put("Rating", selecionado.getRating());
                                pacoteDoc.set(packageM, SetOptions.merge());

                            }
                        });
                    }
                }
            });
        }
    }

    public List<String> ReadAllLocalPackages(){
        List<String> filenames = new ArrayList<>();

        File path = getApplicationContext().getFilesDir();
        File Packages = new File(path +"/Packages");
        File[] listOfFiles = Packages.listFiles();

        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                filenames.add(listOfFiles[i].getName());
            }
        }
        return filenames;
    }

    public void getPackages() {

        fcloud = FirebaseFirestore.getInstance();
        Gson gson = new Gson();

        List<String> filenames = ReadAllLocalPackages();
        listaPacotes = new ArrayList<>();

        for (String file : filenames) {
            String[] aux;
            aux = file.split("\\.");

            DocumentReference auxDocs = fcloud.collection("Packages").document(aux[0]);

            auxDocs.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String aux = document.getString("PackageGSON");
                            Pacote aux2 = gson.fromJson(aux, Pacote.class);
                            listaPacotes.add(aux2);
                        }
                        adapter.clear();
                        adapter.addAll(listaPacotes);
                        adapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);
        finish();
    }
}
