package com.example.projectoubi_41400;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class estudante_estudar extends AppCompatActivity {

    LinearLayout container;
    FirebaseFirestore fcloud;
    TextView namePacote;
    String TAG = "estudante_estudar";
    Pacote pacote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudante_estudar);

        container = findViewById(R.id.container_scrollview);
        namePacote = findViewById(R.id.pacote_name);

        Gson gson = new Gson();
        String pacoteString = getIntent().getExtras().getString("Pacote");
        pacote = gson.fromJson(pacoteString, Pacote.class);

        namePacote.setText(pacote.getTitle());

        if (!pacote.cathegories.isEmpty()) {
            for (Cathegory cat : pacote.getCathegories()) {
                addCard(cat.getName());
            }
        }

    }

    private void addCard(String name) {
        View view = getLayoutInflater().inflate(R.layout.card_estudar, null);

        TextView nameView = view.findViewById(R.id.nameCard);
        LinearLayout layout = view.findViewById(R.id.layoutCard);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        nameView.setText(name);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iActivity = new Intent( view.getContext(), estudante_estudar_subcat.class);
                iActivity.putExtra("Cathegory_Name", name);
                Gson gson = new Gson();
                String pacoteString = gson.toJson(pacote);
                iActivity.putExtra("Pacote", pacoteString);
                startActivityForResult(iActivity, 2);
            }
        });

        container.addView(view);
    }

    public void Return(View v) {
        endActivity(v);
    }

    public void endActivity(View v) {
        Intent intent = new Intent();
        setResult(2, intent);

        finish();
    }
}