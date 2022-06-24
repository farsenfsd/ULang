package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registar extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText RegEmail;
    EditText RegPw;
    EditText RegUsername;
    TextView Aviso;
    FirebaseFirestore fcloud;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        mAuth = FirebaseAuth.getInstance();
        RegEmail = findViewById(R.id.regEmail);
        RegPw = findViewById(R.id.regPassword);
        Aviso = findViewById(R.id.reg_Aviso);
        RegUsername = findViewById(R.id.regUsername);
        fcloud = FirebaseFirestore.getInstance();

    }

    public void Registar(View v) {
        String email = (RegEmail.getText().toString().trim());
        String pw = (RegPw.getText().toString().trim());
        String username = (RegUsername.getText().toString().trim());

        if(username.isEmpty()){
            RegUsername.setError("É necessário um nome de utilizador.");
            RegUsername.requestFocus();
            return;
        }

        if(email.isEmpty()){
            RegEmail.setError("É necessário um e-mail.");
            RegEmail.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            RegEmail.setError("Por favor introduza um e-mail válido.");
            RegEmail.requestFocus();
            return;
        }

        if(pw.isEmpty()){
            RegPw.setError("É necessária uma palavra-chave.");
            RegPw.requestFocus();
            return;
        }

        else if(pw.length() < 6){
            RegPw.setError("O tamanho minímo para palavras-chave é 6 caracteres.");
            RegPw.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,pw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fcloud.collection("Users").document(userID);
                            Map<String,Object> userM = new HashMap<>();
                            userM.put("ID",userID);
                            userM.put("username",username);
                            documentReference.set(userM).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(registar.this, "O utilizador foi registado com sucesso", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Log.d("TAG","(REGISTAR) onFailure: " + e.toString());
                                    Toast.makeText(registar.this, "O registro falhou", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
