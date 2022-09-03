package com.example.projectdicodeutsch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.projectdicodeutsch.adapter.WordAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class wordlist extends AppCompatActivity {

    public List<String> frenchListComplete = new ArrayList<String>();
    public List<String> germanListComplete = new ArrayList<String>();

    private RecyclerView wordRecyclerView;
    private WordAdapter wordAdapter;

    private ToggleButton hideTranslationSwitch;
    boolean hideTranslaiton = false;

    Activity thisActvity = this;

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

        hideTranslationSwitch = findViewById(R.id.hideTranslationSwitch);
        hideTranslationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int colorInt;
                ColorStateList csl;
                if(isChecked)  {
                    colorInt = getResources().getColor(R.color.white);
                    csl = ColorStateList.valueOf(colorInt);
                    hideTranslationSwitch.setBackgroundTintList(csl);

                    List<String> emptyList = Arrays.asList(new String[frenchListComplete.size() - 1]);
                    wordAdapter = new WordAdapter(thisActvity, frenchListComplete, emptyList, true);
                    wordRecyclerView.setAdapter(wordAdapter);
                } else {
                    colorInt = getResources().getColor(R.color.primary);
                    csl = ColorStateList.valueOf(colorInt);
                    hideTranslationSwitch.setBackgroundTintList(csl);

                    wordAdapter = new WordAdapter(thisActvity, frenchListComplete, germanListComplete, true);
                    wordRecyclerView.setAdapter(wordAdapter);
                }
            }
        });
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