package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class gerirPacotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_linguas);
    }

    public void Leave(View v){
        endActivity(v);
    }

    public void endActivity ( View v) {
        Intent intent = new Intent();
        setResult(2,intent);

        finish();
    }
}
