package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class prof_gestor extends AppCompatActivity {
    FirebaseFirestore fcloud;
    FirebaseAuth mAuth;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor_gerir);
        username = getIntent().getExtras().getString("Username");

    }

    public void OpenCriarPacote(View v) {
        Intent iActivity = new Intent(this, prof_criar.class);
        iActivity.putExtra("Username",username);
        iActivity.putExtra("modo",0);
        startActivityForResult(iActivity, 1);
    }

    public void OpenEditarPacote(View v) {
        Intent iActivity = new Intent(this, gerirPacotes.class);
        iActivity.putExtra("Username",username);
        startActivityForResult(iActivity, 1);
    }

    public void OpenRemoverPacote(View v) {
        Intent iActivity = new Intent(this, iniciarSessao.class);
        startActivityForResult(iActivity, 1);
    }

    public void OpenGerirConta(View v) {
        Intent iActivity = new Intent(this, registar.class);
        startActivityForResult(iActivity, 2);
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        finish();
    }
}
