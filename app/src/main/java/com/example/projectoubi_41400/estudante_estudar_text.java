package com.example.projectoubi_41400;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class estudante_estudar_text extends AppCompatActivity {

    private TextView content;
    Pacote novo;
    String subcatName;
    String catName;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudante_estudar_text);

        content = findViewById(R.id.content);
        text = findViewById(R.id.subcathegory);

        subcatName = getIntent().getExtras().getString("Subcathegory_Name");
        text.setText(subcatName);
        catName = getIntent().getExtras().getString("Cathegory_Name");

        Gson gson = new Gson();
        String pacote = getIntent().getExtras().getString("Pacote");
        novo = gson.fromJson(pacote, Pacote.class);

        SubCathegory current = novo.findSubCathegory(subcatName, catName);

        if(current.content != null && !current.content.isEmpty()) { // Verifica se há algum conteúdo
            Spanned text = Html.fromHtml(current.getContent(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH);
            content.setText(text);
            if(current.alignment != null && !current.alignment.isEmpty()){
                switch (current.alignment) {
                    case "left":
                        content.setGravity(Gravity.LEFT);
                        break;
                    case "center":
                        content.setGravity(Gravity.CENTER);
                        break;
                    case "right":
                        content.setGravity(Gravity.RIGHT);
                        break;
                }
            }
        }
    }

    public void endActivity ( View v) {
        Intent resultIntent = new Intent();
        setResult(2, resultIntent);

        finish();
    }
}