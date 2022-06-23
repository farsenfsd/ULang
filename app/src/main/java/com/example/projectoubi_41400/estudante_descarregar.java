package com.example.projectoubi_41400;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class estudante_descarregar extends AppCompatActivity {

    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudante_descarregar);

        container = findViewById(R.id.container_scrollview);

        addCard("olá","exemplo 1");
        addCard("adeus","exemplo 2");
        addCard("será?","exemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\nexemplo 3\n");
    }


    private void addCard(String name, String details) {
        View view = getLayoutInflater().inflate(R.layout.card_descarregar, null);

        TextView nameView = findViewById(R.id.nameCard);
        TextView detailsView = findViewById(R.id.detailsCard);
        LinearLayout layout = findViewById(R.id.layoutCard);
        //layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ImageView download = view.findViewById(R.id.downloadCard);

        nameView.setText(name);
        detailsView.setText(details);

        download.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(estudante_descarregar.this, "A iniciar download", Toast.LENGTH_LONG).show();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (detailsView.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout, new AutoTransition());
                detailsView.setVisibility(v);
            }
        });

        container.addView(view);
    }
}