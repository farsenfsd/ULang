package com.example.projectoubi_41400;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OpenEstudante(View v) {
        Intent iActivity = new Intent(this, estudanteMain.class);
        startActivityForResult(iActivity,1);
    }

    public void OpenProfessor(View v) {
        Intent iActivity = new Intent(this, professorMain.class);
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
}