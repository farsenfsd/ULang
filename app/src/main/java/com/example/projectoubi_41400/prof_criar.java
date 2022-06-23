package com.example.projectoubi_41400;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import java.util.Date;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class prof_criar extends AppCompatActivity {

    String description;
    Button add;
    AlertDialog dialogAdd;
    AlertDialog dialogTitle;
    AlertDialog dialogEdit;
    AlertDialog dialogDescription;
    LinearLayout layout;
    String username;
    Pacote novo;
    FirebaseFirestore fcloud;
    private FirebaseAuth mAuth;
    String TAG = "prof_criar";
    boolean cancelled = false;
    int modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_criar);

        add = findViewById(R.id.addCategoria);
        layout = findViewById(R.id.container);
        buildDialog();
        buildDialogGuardarTitle();
        buildDialogGuardarDescription();
        buildDialogEdit();

        username = getIntent().getExtras().getString("Username");
        modo = getIntent().getExtras().getInt("modo");

        if(modo != 0){
            Gson gson = new Gson();
            String pacote = getIntent().getExtras().getString("Pacote");
            novo = gson.fromJson(pacote,Pacote.class);

            if (!novo.cathegories.isEmpty()) {
                for (Cathegory cat : novo.getCathegories()) {
                    addCard(cat.getName(), 1);
                }
            }
        }
        else {
            novo = new Pacote();
            novo.setAuthor(username);
        }
    }

    public void OpenAbrirDialogo(View v) {
        dialogAdd.show();
    }

    public void OpenDialogoGuardar(View v){
        if(modo == 1)
            dialogEdit.show();
        else{
            dialogTitle.show();
        }
    }

    public void UploadPackage(View v) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
        Date createdDate = new Date();
        mAuth = FirebaseAuth.getInstance();
        fcloud = FirebaseFirestore.getInstance();
        // Procura perceber se o user está a null
        String userID = mAuth.getCurrentUser().getUid();

        Gson gson = new Gson();
        novo.setAuthorID(userID);
        novo.setUpdateDate(createdDate);
        DocumentReference docRef;

        if(modo == 1)
            docRef = fcloud.collection("Packages").document(novo.packageID);
        else {
            docRef = fcloud.collection("Packages").document();
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> packageM = new HashMap<>();

                    novo.setPackageID(docRef.getId());
                    String pacote = gson.toJson(novo);
                    packageM.put("AuthorID", userID);
                    packageM.put("AuthorUsername", username);
                    packageM.put("PackageGSON", pacote);
                    packageM.put("Title", novo.getTitle());
                    packageM.put("Description", novo.getDescription());
                    packageM.put("Date", createdDate);
                    packageM.put("NPages", novo.getNumberPages());
                    packageM.put("NDownloads", novo.getNumberDownloads());
                    packageM.put("Rating", novo.getRating());
                    docRef.set(packageM).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(prof_criar.this, "O pacote foi criado com sucesso", Toast.LENGTH_LONG).show();
                            endActivity(v);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "(prof_criar) onFailure: " + e.toString());
                            Toast.makeText(prof_criar.this, "A criação do pacote falhou", Toast.LENGTH_LONG).show();
                            endActivity(v);
                        }
                    });
                }
            }
        });
    }

    public void Abrir(View v){
        View view = getLayoutInflater().inflate(R.layout.card, null);
        TextView nameView = view.findViewById(R.id.name);
        nameView.setText("Teste");
    }

    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_short,null);

        EditText name = view.findViewById(R.id.catEdit);

        builder.setView(view);
        builder.setTitle("Introduza o nome da categoria")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!name.getText().toString().isEmpty())
                            addCard(name.getText().toString(),0);
                        else{
                            Toast.makeText(prof_criar.this, "O nome da categoria não pode estar vazio.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialogAdd = builder.create();
    }

    private void buildDialogEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_short,null);

        builder.setTitle("Pretende alterar o nome e descrição do pacote?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogTitle.show();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UploadPackage(view);
                    }
                });
        dialogEdit = builder.create();
    }

    private void buildDialogGuardarTitle(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_short,null);

        EditText name = view.findViewById(R.id.catEdit);

        builder.setView(view);
        builder.setTitle("Introduza o nome do pacote")
                .setPositiveButton("Próximo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!name.getText().toString().isEmpty()) {
                            novo.setTitle(name.getText().toString());
                            dialogDescription.show();
                        }
                        else{
                            Toast.makeText(prof_criar.this, "O nome não pode estar vazio", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelled = true;
                    }
                });
        dialogTitle = builder.create();
    }

    private void buildDialogGuardarDescription(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_long,null);

        EditText descriptionEdit = view.findViewById(R.id.descriptionEdit);

        builder.setView(view);
        builder.setTitle("Introduza a descrição do pacote")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!descriptionEdit.getText().toString().isEmpty()) {
                            description = descriptionEdit.getText().toString();
                            novo.setDescription(description);
                            UploadPackage(view);
                        }
                        else{
                            Toast.makeText(prof_criar.this, "A descrição não pode estar vazia", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelled = true;
                    }
                });
        dialogDescription = builder.create();
    }

    private void addCard(String name, int flag) {
        View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.deleteCat);
        Button edit = view.findViewById(R.id.editCatButton);

        nameView.setText(name);

        if(flag == 0) {
            Cathegory aux = new Cathegory();
            aux.setName(name);
            novo.addCathegory(aux);
        }

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                novo.remCathegory(name);
                layout.removeView(view);
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent iActivity = new Intent( v.getContext(), prof_criar_subcat.class);
                iActivity.putExtra("Cathegory_Name", name);
                Gson gson = new Gson();
                String pacote = gson.toJson(novo);
                iActivity.putExtra("Pacote", pacote);
                startActivityForResult(iActivity, 2);
            }
        });

        layout.addView(view);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 2) && (data != null)){
            Gson gson = new Gson();

            String pacote = data.getExtras().getString("pacote");
            novo = gson.fromJson(pacote, Pacote.class);
        }
    }

    public void OpenCancel(View v){
        endActivity(v);
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        if(modo == 1)
            setResult(3,intent);
        else {
            setResult(2, intent);
        }
        finish();
    }

}