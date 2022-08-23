package com.example.projectdicodeutsch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;


public class translate_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_search);

        ImageView firstTextView = (ImageView)findViewById(R.id.imageView2);
        firstTextView.bringToFront();

        Button FindBtn = findViewById(R.id.WordSearchBtn);
        EditText FindContent = findViewById(R.id.WordSearchBar);
        ListView ItemList = findViewById(R.id.spinner);
        ImageView ReturnBtn = findViewById(R.id.TranslateGotoMain);
        //  PLACE DES MOTS DANS LE CSV
        int ColumnFrenchWord = 1;

        FindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String WordSearch = FindContent.getText().toString();

                try {
                    if(WordSearch.length() > 2) {
                        ArrayList<String> ArrayListResult = new ArrayList<String>(searchCsvLine(ColumnFrenchWord, WordSearch));
                        if(!ArrayListResult.isEmpty())  {
                            int ArraySize = ArrayListResult.size();
                            ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(translate_search.this, android.R.layout.simple_list_item_1, ArrayListResult);
                            ItemList.setAdapter(ArrayAdapter);
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Par Trouvé", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private ArrayList<String> searchCsvLine(int searchColumnIndex, String searchString) throws IOException {
        ArrayList<String> resultRow = new ArrayList<String>();

        InputStream InputStream = getResources().openRawResource(R.raw.wordlist_dicodeutsch);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(InputStream, StandardCharsets.UTF_8)
        );

        searchString = searchString.toLowerCase();
        searchString = stripAccents(searchString);

        String line;
        boolean Found = false;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(",");

            String CompareValue = values[searchColumnIndex].toLowerCase();
            CompareValue = stripAccents(CompareValue);

            if(CompareValue.indexOf(searchString) != -1) {
                String AppendReturnLine = "";
                if(values[3].equals(" ")) {
                    AppendReturnLine = values[1] + " > " + values[2];
                }
                else if(values[4].equals(" ")) {
                    AppendReturnLine = values[1] + " > " + values[2] + " : " + values[3];
                }
                else {
                    AppendReturnLine = values[1] + " > " + values[2] + " : " + values[3] + values[4];
                }
                resultRow.add(AppendReturnLine);
            }
        }
        br.close();
        return resultRow;
    }

    private void SwitchHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}