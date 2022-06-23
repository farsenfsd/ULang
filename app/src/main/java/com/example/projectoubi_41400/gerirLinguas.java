package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class gerirLinguas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_linguas);
    }

    public void OpenDescarregarPacotes(View v){
        Intent iActivity = new Intent(this, estudante_descarregar.class);
        startActivityForResult(iActivity, 1);
    }
}
