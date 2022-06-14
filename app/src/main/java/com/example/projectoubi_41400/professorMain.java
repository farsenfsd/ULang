package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class professorMain extends AppCompatActivity {

    TextView bemvindo;
    FirebaseFirestore fcloud;
    FirebaseAuth mAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);

        bemvindo = findViewById(R.id.mainText2);

        mAuth = FirebaseAuth.getInstance();
        fcloud = FirebaseFirestore.getInstance();
        try
        {
            // Procura perceber se o user está a null
            userID = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = fcloud.collection("Users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    bemvindo.setText("Bem-vindo/a " + value.getString("username"));
                }
            });
        }
        catch(NullPointerException e)
        {
            System.out.print("User a null");
            bemvindo.setText("Bem-vindo/a " + "professor/a");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main_professor);

        bemvindo = findViewById(R.id.mainText2);

        mAuth = FirebaseAuth.getInstance();
        fcloud = FirebaseFirestore.getInstance();

        try
        {
            // Procura perceber se o user está a null
            userID = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = fcloud.collection("Users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    bemvindo.setText("Bem-vindo/a " + value.getString("username"));
                }
            });
        }
        catch(NullPointerException e)
        {
            System.out.print("User a null");
            bemvindo.setText("Bem-vindo/a " + "professor/a");
        }
    }

    public void OpenIniciarSessao(View v) {
        Intent iActivity = new Intent(this, iniciarSessao.class);
        startActivityForResult(iActivity, 1);

    }

    public void OpenRegistar(View v) {
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