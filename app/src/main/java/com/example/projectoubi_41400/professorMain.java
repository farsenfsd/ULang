package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class professorMain extends AppCompatActivity {

    String username;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);
        username = getIntent().getExtras().getString("Username");
    }

    public void OpenEstudante(View v) {
        Intent iActivity = new Intent(this, estudanteMain.class);
        startActivityForResult(iActivity,1);
    }

    public void OpenProfessor(View v) {
        Intent iActivity = new Intent(this, prof_gestor.class);
        iActivity.putExtra("Username", username);
        startActivityForResult(iActivity, 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            // Termina Sessão
        }
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();

        finish();
    }
}