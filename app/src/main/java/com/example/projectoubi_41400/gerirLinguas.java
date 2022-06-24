package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class gerirLinguas extends AppCompatActivity {

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_linguas);
    }

    public void OpenDescarregarPacotes(View v){
        Intent iActivity = new Intent(this, estudante_descarregar.class);
        startActivityForResult(iActivity, 1);
    }

    public void OpenClassificarPacotes(View v){
        Intent iActivity = new Intent(this, estudante_classificar.class);
        startActivityForResult(iActivity, 1);
    }

    public void OpenRemoverPacote(View v){
        Intent iActivity = new Intent(this, estudante_remover.class);
        startActivityForResult(iActivity, 1);
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        finish();
    }
}
