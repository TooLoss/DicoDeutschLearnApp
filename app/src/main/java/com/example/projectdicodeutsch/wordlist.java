package com.example.projectdicodeutsch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.projectdicodeutsch.adapter.WordAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class wordlist extends AppCompatActivity {

    public List<String> frenchListComplete = new ArrayList<String>();
    public List<String> germanListComplete = new ArrayList<String>();

    private RecyclerView wordRecyclerView;
    private WordAdapter wordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);

        try {
            generateCsvList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordRecyclerView = findViewById(R.id.wordListComplete);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        wordAdapter = new WordAdapter(this, frenchListComplete, germanListComplete, true);
        wordRecyclerView.setAdapter(wordAdapter);
    }

    private void generateCsvList() throws IOException {

        // Open CSV
        InputStream InputStream = getResources().openRawResource(R.raw.wordlist_two_line);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(InputStream, StandardCharsets.UTF_8)
        );

        String line;
        while ( (line = br.readLine()) != null ) {
            String[] values = line.split(",");
            frenchListComplete.add(values[1]);
            germanListComplete.add(values[2]);
        }
    }

    public void goToHomePage(View view) {
        finish();
    }
}