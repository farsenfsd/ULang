package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class estudante_estudar_escolha extends AppCompatActivity {

    FirebaseFirestore fcloud;
    private FirebaseAuth mAuth;
    List<Pacote> listaPacotes;
    String TAG = "estudante_remover";
    Pacote selecionado;
    String username;
    ArrayAdapter<Pacote> adapter;

    Spinner spinner;
    TextView desc;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudante_estudar_escolha);

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

    public void OpenStudy(View v){

        selecionado = (Pacote) spinner.getSelectedItem();
        if(selecionado != null){
            Intent iActivity = new Intent(this, estudante_estudar.class);
            iActivity.putExtra("Username",username);
            iActivity.putExtra("modo",1);
            Gson gson = new Gson();
            String pacote = gson.toJson(selecionado);
            iActivity.putExtra("Pacote", pacote);
            startActivityForResult(iActivity, 3);
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

    public Pacote readFile(String filename){

        File path = getApplicationContext().getFilesDir();
        File Packages = new File(path +"/Packages");

        Pacote aux;
        Gson gson = new Gson();

        String ret = "";

        try {
            Reader reader = Files.newBufferedReader( Paths.get(Packages + "/" + filename));
            //FileInputStream reader = new FileInputStream(new File(Packages, filename));
            aux = gson.fromJson(reader, Pacote.class);
            reader.close();
            Log.d(TAG,aux.toString());
            return aux;
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return null;
    }

    public void getPackages() {

        fcloud = FirebaseFirestore.getInstance();
        Gson gson = new Gson();

        List<String> filenames = ReadAllLocalPackages();
        listaPacotes = new ArrayList<>();

        for (String file : filenames) {
            String[] aux;
            aux = file.split("\\.");
            listaPacotes.add(readFile(file));
        }
        adapter.clear();
        adapter.addAll(listaPacotes);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);
        finish();
    }
}
