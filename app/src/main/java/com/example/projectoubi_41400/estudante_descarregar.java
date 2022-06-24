package com.example.projectoubi_41400;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class estudante_descarregar extends AppCompatActivity {

    LinearLayout container;
    FirebaseFirestore fcloud;
    List<Pacote> listaPacotes;
    EditText searchView;
    String TAG = "estudante_descarregar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudante_descarregar);

        listaPacotes = new ArrayList<>();
        container = findViewById(R.id.container_scrollview);
        searchView = findViewById(R.id.searchText);


        getPackages();

        //addCard("olá","exemplo 1");
        //addCard("adeus","exemplo 2");
        //addCard("será?","exemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\n");
    }


    private void addCard(Pacote pacote) {
        View view = getLayoutInflater().inflate(R.layout.card_descarregar, null);

        TextView nameView = view.findViewById(R.id.nameCard);
        TextView detailsView = view.findViewById(R.id.detailsCard);
        TextView statisticsView = view.findViewById(R.id.statisticsCard);
        RatingBar ratingBarView = view.findViewById(R.id.ratingBarCard);
        LinearLayout layout = view.findViewById(R.id.layoutCard);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ImageView download = view.findViewById(R.id.downloadCard);

        nameView.setText(pacote.getTitle());
        detailsView.setText(pacote.getDescription());
        String statistics = "Author: " + pacote.getAuthor() + " | Last Updated: " + pacote.getDateUpdated() + " | P: " + pacote.getNumberPages() + " |  "+ pacote.getNumberDownloads() + " ⬇";
        statisticsView.setText(statistics);
        ratingBarView.setRating( (float) pacote.getRating() );

        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(estudante_descarregar.this, "A iniciar descarga...", Toast.LENGTH_SHORT).show();
                try {
                    DescarregarPacote(pacote);
                } catch (FileNotFoundException e) {
                    Toast.makeText(estudante_descarregar.this, "Houve um erro ao descarregar!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (detailsView.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout, new AutoTransition());
                detailsView.setVisibility(v);
            }
        });

        container.addView(view);
    }

    public void getPackages(){
        fcloud = FirebaseFirestore.getInstance();
        //String userID = mAuth.getCurrentUser().getUid();
        Gson gson = new Gson();

        CollectionReference collectionReference = fcloud.collection("Packages");
        collectionReference.orderBy("Rating").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                for( QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String aux = documentSnapshot.getString("PackageGSON");
                    Pacote aux2 = gson.fromJson(aux,Pacote.class);
                    int nDownloads = documentSnapshot.get("NDownloads", int.class);
                    double nRating = documentSnapshot.getDouble("Rating");
                    aux2.setNumberDownloads(nDownloads);
                    aux2.setRating(nRating);
                    listaPacotes.add(aux2);
                }
                orderByRating(listaPacotes);
                //Log.d(TAG,"dentro da lista: " + listaPacotes.toString());
            }
        });
    }

    public void Search(View v){

        String queryText = "";
        queryText += searchView.getText().toString();
        List<Pacote> aux = new ArrayList<>();
        if(!queryText.isEmpty()) {
            for(Pacote p : listaPacotes){
                if(p.getTitle().contains(queryText) || p.getDescription().contains(queryText) || p.getAuthor().contains(queryText))
                    aux.add(p);
            }
            orderByRating(aux);
        }
        else{
            orderByRating(listaPacotes);
        }
    }

    public void DescarregarPacote(Pacote p) throws FileNotFoundException {

        DocumentReference documentReference = fcloud.collection("Packages").document(p.packageID);

        documentReference.update(
                "NDownloads", FieldValue.increment(1));

        String content = "";
        Gson gson = new Gson();
        content = gson.toJson(p);
        savePackage(p.packageID + ".json", content);
    }

    public void savePackage(String filename, String content) throws FileNotFoundException {
        File path = getApplicationContext().getFilesDir();
        File Packages = new File(path +"/Packages");

        if(!Packages.exists()) {
            Packages.mkdir();
            //Log.d(TAG, "Creating package...");
        }
        try {
            FileOutputStream writer = new FileOutputStream(new File(Packages, filename));
            writer.write(content.getBytes());
            writer.close();
            Toast.makeText(getApplicationContext(), "Package downloaded sucessfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Return(View v){
        endActivity(v);
    }

    public void orderByRating(List<Pacote> original){
        container.removeAllViews();
        List<Pacote> aux = original;
        aux.sort(Comparator.comparingDouble(Pacote::getRating));
        Collections.reverse(aux);
        for(Pacote p : original){
            addCard(p);
        }
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        finish();
    }

}