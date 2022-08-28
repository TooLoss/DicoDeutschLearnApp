package com.example.projectdicodeutsch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.projectdicodeutsch.adapter.WordAdapter;
import com.example.projectdicodeutsch.model.WordModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class translate_search extends AppCompatActivity {

    private RecyclerView wordRecyclerView;
    private WordAdapter wordAdapter;

    private List<WordModel> wordsList;
    public EditText FindContent;
    private List<String> frenchWordList;
    private List<String> germanWordList;
    int rowFrenchWord = 1;

    public translate_search thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_search);

        ImageView firstTextView = (ImageView)findViewById(R.id.imageView2);
        firstTextView.bringToFront();

        Button FindBtn = findViewById(R.id.WordSearchBtn);
        FindContent = findViewById(R.id.WordSearchBar);

        thisActivity = this;
        rowFrenchWord = 1;

        // Liste des mots affichées
        wordRecyclerView = findViewById(R.id.recyclerView);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        wordsList = new ArrayList<>();

        FindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String WordSearch = FindContent.getText().toString();
                try {
                    if(WordSearch.length() > 2) {
                        int numOfWord = searchCsvLine(rowFrenchWord, WordSearch);
                        if(numOfWord > 0)  {
                            wordAdapter = new WordAdapter(thisActivity, frenchWordList, germanWordList);
                            wordRecyclerView.setAdapter(wordAdapter);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int searchCsvLine(int searchColumnIndex, String searchString) throws IOException {

        // Variables
        WordModel wordLine = new WordModel();
        List<WordModel> functionWordList = new ArrayList<>();

        List<String> functionGermanList = new ArrayList<>();
        List<String> functionFrenchList = new ArrayList<>();

        // Ouverture du fichier csv
        InputStream InputStream = getResources().openRawResource(R.raw.wordlist_two_line);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(InputStream, StandardCharsets.UTF_8)
        );

        // Simplification du texte
        searchString = searchString.toLowerCase();
        searchString = stripAccents(searchString);

        String line;
        boolean Found = false;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(",");

            Log.e("errorvalue", Integer.toString(searchColumnIndex));
            Log.e("finderror", values[1]);

            String CompareValue = values[searchColumnIndex].toLowerCase();
            CompareValue = stripAccents(CompareValue);

            if(CompareValue.indexOf(searchString) != -1) {

                functionFrenchList.add(values[1]);
                functionGermanList.add(values[2]);
            }
        }

        germanWordList = functionGermanList;
        frenchWordList = functionFrenchList;

        br.close();
        return functionFrenchList.size();
    }


    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public void goToHomePage(View view) {
        finish();
    }
}