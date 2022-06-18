package com.example.projectoubi_41400;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class prof_criar_content extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_criar_content);

        editText = findViewById(R.id.content);
    }

    public void TextBold(View v){

    }

    public void TextUnderline(View v){

    }

    public void TextItallic(View v){

    }

    public void TextNoFormat(View v){

    }

}