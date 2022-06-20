package com.example.projectoubi_41400;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class prof_criar_content extends AppCompatActivity {

    private EditText editText;
    Pacote novo;
    String subcatName;
    String catName;
    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_criar_content);

        editText = findViewById(R.id.content);
        text = findViewById(R.id.subcathegory);

        subcatName = getIntent().getExtras().getString("Subcathegory_Name");
        text.setText(subcatName);
        catName = getIntent().getExtras().getString("Cathegory_Name");

        Gson gson = new Gson();
        String pacote = getIntent().getExtras().getString("Pacote");
        novo = gson.fromJson(pacote,Pacote.class);

        SubCathegory current = novo.findSubCathegory(subcatName, catName);

        if(current.content != null && !current.content.isEmpty()) { // Verifica se há algum conteúdo
            editText.setText(Html.fromHtml(current.getContent(), Html.FROM_HTML_MODE_COMPACT));
        }
    }

    public void TextBold(View v){

        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);

        editText.setText(spannableString);

    }

    public void TextUnderline(View v){

        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new UnderlineSpan(),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);

        editText.setText(spannableString);
    }

    public void TextItallic(View v){

        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);

        editText.setText(spannableString);
    }

    public void TextNoFormat(View v){

        Spannable spannableString = new SpannableStringBuilder(editText.getText());

        StyleSpan[] spannable = spannableString.getSpans(editText.getSelectionStart(), editText.getSelectionEnd(), StyleSpan.class);
        if (spannable != null && spannable.length > 0) {
            for (StyleSpan styleSpan : spannable) {
                spannableString.removeSpan(styleSpan);
            }
        }

        editText.setText(spannableString);
    }

    public void AlignLeft(View v){

        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);
        editText.setText(spannableString);
    }

    public void AlignCenter(View v){

        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);
        editText.setText(spannableString);

    }

    public void AlignRight(View v){
        Spannable spannableString = new SpannableStringBuilder(editText.getText());
        spannableString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
                editText.getSelectionStart(),
                editText.getSelectionEnd(),
                0);
        editText.setText(spannableString);
    }

    public void Guardar(View v){
        endActivity(v);
    }

    public void endActivity ( View v) {

        Spannable content = new SpannableStringBuilder(editText.getText());
        String contentToHTML = Html.toHtml(content, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);

        novo.setContent(contentToHTML,subcatName,catName);

        Intent resultIntent = new Intent();
        Gson gson = new Gson();
        String pacote = gson.toJson(novo);
        resultIntent.putExtra("pacote", pacote);
        setResult(2, resultIntent);

        finish();
    }
}