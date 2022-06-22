package com.example.projectoubi_41400;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class prof_criar_subcat
 extends AppCompatActivity {

    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    TextView text;
    Pacote novo;
    String CatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_criar_subcat);

        add = findViewById(R.id.addCategoria);
        layout = findViewById(R.id.container);
        text = findViewById(R.id.subcathegory);
        buildDialog();

        CatName = getIntent().getExtras().getString("Cathegory_Name");
        text.setText(CatName);
        Gson gson = new Gson();
        String pacote = getIntent().getExtras().getString("Pacote");
        novo = gson.fromJson(pacote,Pacote.class);

        Cathegory current = novo.findCathegory(CatName);
        if (!current.subCathegories.isEmpty()) {
            for (SubCathegory subcat : current.getSubCathegories()) {
                addCard(subcat.getName(), 1);
            }
        }

    }

    public void OpenAbrirDialogo(View v) {
        dialog.show();
    }

    public void OpenGuardarPacote(View v) {
        endActivity(v);
    }

    public void Abrir(View v){
        View view = getLayoutInflater().inflate(R.layout.card, null);
        TextView nameView = view.findViewById(R.id.name);
        nameView.setText("Olá");

    }

    private void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_short,null);

        EditText name = view.findViewById(R.id.catEdit);

        builder.setView(view);
        builder.setTitle("Introduza o nome da sub-categoria")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addCard(name.getText().toString(),0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog = builder.create();
    }

    private void addCard(String name, int flag) { // Flag = 0 -> adiciona ao objecto pacote; Flag = 1 -> Não adiciona ao objecto pacote.
        View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.deleteCat);
        Button edit = view.findViewById(R.id.editCatButton);

        nameView.setText(name);

        if(flag == 0) {
            SubCathegory aux = new SubCathegory();
            aux.setName(name);
            novo.addSubCathegory(aux, CatName);
            novo.increasePageNumber();
        }

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                novo.remSubCathegory(name, CatName);
                layout.removeView(view);
                novo.decreasePageNumber();
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){ // Não pronto
            @Override
            public void onClick(View v) {
                Intent iActivity = new Intent( v.getContext(), prof_criar_content.class);
                iActivity.putExtra("Subcathegory_Name", name);
                iActivity.putExtra("Cathegory_Name",CatName);
                Gson gson = new Gson();
                String pacote = gson.toJson(novo);
                iActivity.putExtra("Pacote", pacote);
                startActivityForResult(iActivity, 2);
            }
        });

        layout.addView(view);
    }

    public void endActivity ( View v) {

        Intent resultIntent = new Intent();
        Gson gson = new Gson();
        String pacote = gson.toJson(novo);
        resultIntent.putExtra("pacote", pacote);
        setResult(2, resultIntent);

        finish();
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

}