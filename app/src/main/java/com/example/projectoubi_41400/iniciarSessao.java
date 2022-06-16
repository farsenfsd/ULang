package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class iniciarSessao extends AppCompatActivity {

    FirebaseFirestore fcloud;
    private FirebaseAuth mAuth;
    EditText InicEmail;
    EditText InicPw;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sessao);

        mAuth = FirebaseAuth.getInstance();
        InicEmail = findViewById(R.id.inicEmail);
        InicPw = findViewById(R.id.inicPassword);

        Intent i = getIntent();
        username = getIntent().getExtras().getString("Username");

    }

    public void IniciarSessao(View v) {
        String email = (InicEmail.getText().toString());
        String pw = (InicPw.getText().toString());

        if(email.isEmpty()){
            InicEmail.setError("É necessário um e-mail.");
            InicEmail.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            InicEmail.setError("Por favor introduza um e-mail válido.");
            InicEmail.requestFocus();
            return;
        }

        if(pw.isEmpty()){
            InicPw.setError("É necessária uma palavra-chave.");
            InicPw.requestFocus();
            return;
        }

        else if(pw.length() < 6){
            InicPw.setError("O tamanho minímo para palavras-chave é 6 caracteres.");
            InicPw.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(iniciarSessao.this, "Sessão Iniciada com sucesso", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            openGestor(v);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(iniciarSessao.this, "As credenciais estão erradas.", Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void openGestor ( View v) {

        mAuth = FirebaseAuth.getInstance();
        fcloud = FirebaseFirestore.getInstance();

        try
        {
            // Procura perceber se o user está a null
            String userID = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = fcloud.collection("Users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    username = value.getString("username");
                }
            });
        }
        catch(NullPointerException e)
        {
            System.out.print("User a null");
        }

        Intent iActivity = new Intent(this, prof_gestor.class);
        iActivity.putExtra("Username",username);
        startActivityForResult(iActivity, 1);
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);
        finish();
    }
}
