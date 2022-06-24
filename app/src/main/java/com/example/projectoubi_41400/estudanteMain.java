package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class estudanteMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_estudante);
    }

    public void OpenGerirLinguas(View v) {
        Intent iActivity = new Intent(this, gerirLinguas.class);
        startActivityForResult(iActivity, 1);
    }

    public void OpenEstudar(View v) {
        Intent iActivity = new Intent(this, estudante_estudar_escolha.class);
        startActivityForResult(iActivity, 2);
    }

    public void OpenDefEstudante(View v) {
        Intent iActivity = new Intent(this, defEstudante.class);
        startActivityForResult(iActivity, 3);
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(1,intent);
        finish();
    }
}
