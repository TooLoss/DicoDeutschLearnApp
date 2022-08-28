package com.example.projectdicodeutsch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.projectdicodeutsch.adapter.WordAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Exercice_frenchtodeutsch extends AppCompatActivity {

    String[] ExercicesWords = new String[3];
    InputStream inputStream;
    AnimationDrawable ColorAnimation;
    String failWordFile = "fail_word_file";
    ArrayList<String> recentFailWord = new ArrayList<String>();
    ArrayList<String> recentFailWordReversed = new ArrayList<String>();

    int lineExercice = 1;

    private boolean useVerbeFort = false;
    private boolean useVerbeConjugue = false;
    private boolean useReste = true;

    private RecyclerView wordRecyclerView;
    private WordAdapter wordAdapter;
    List<String> failFrenchWordList = new ArrayList<String>();
    List<String> failGermanWordList = new ArrayList<String>();
    List<String> failWordTypeList = new ArrayList<String>();

    public Activity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_frenchtodeutsch);

        // VARIABLES
        TextView WordFrench = findViewById(R.id.SetWordFrench);
        Button FindBtn = findViewById(R.id.WordSearchBtn);
        Button Giveup = findViewById(R.id.Giveup);
        RelativeLayout FrenchBox = findViewById(R.id.WordFrench);
        EditText FindContent = findViewById(R.id.EditTextWord);

        FrenchBox.setBackgroundResource(R.drawable.color_animation);
        ColorAnimation = (AnimationDrawable) FrenchBox.getBackground();

        // Recycler View Init
        wordRecyclerView = findViewById(R.id.fail_word);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            ExercicesWords = RefindWord(WordFrench, "wordlist_dicodeutsch");
        } catch (IOException e) {
            e.printStackTrace();
        }

        FindBtn.setOnClickListener(new View.OnClickListener() {
            //
            //  ON CLICK
            //
            @Override
            public void onClick(View view) {

                // CHEAT
                //Toast.makeText(getApplicationContext(), stripAccents(ExercicesWords[3].toLowerCase()), Toast.LENGTH_LONG).show();
                String ContentValue = FindContent.getText().toString();
                ContentValue = stripAccents(ContentValue).toLowerCase(Locale.ROOT);
                boolean ContainDet = ContentValue.contains("Die ".toLowerCase()) || ContentValue.contains("Der ".toLowerCase()) || ContentValue.contains("Das ".toLowerCase());
                String BaseValue = ExercicesWords[1].toLowerCase();

                if(ExercicesWords[2].contains("N")) {
                    if(BaseValue.equalsIgnoreCase(ContentValue) && ContainDet) {
                        ColorAnimation.stop();
                        Win(FindContent,WordFrench, "wordlist_dicodeutsch");
                    }
                }else if(BaseValue.equalsIgnoreCase(ContentValue)){
                    ColorAnimation.stop();
                    Win(FindContent,WordFrench, "wordlist_dicodeutsch");
                }
            }
        });

        Giveup.setOnClickListener(new View.OnClickListener() {
            //
            //  ON CLICK
            //
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), ExercicesWords[1] , Toast.LENGTH_LONG).show();
                try {
                    writeToFile(ExercicesWords);
                    recentFailWord.add(String.join(" > ", ExercicesWords));

                    failFrenchWordList.add(ExercicesWords[0]);
                    failGermanWordList.add(ExercicesWords[1]);
                    failWordTypeList.add(ExercicesWords[2]);

                    List<String> recentFailWordFrenchReversed = failFrenchWordList;
                    List<String> recentFailWordGermanReversed = failGermanWordList;
                    Collections.reverse(recentFailWordFrenchReversed);
                    Collections.reverse(recentFailWordGermanReversed);

                    wordAdapter = new WordAdapter(thisActivity, recentFailWordFrenchReversed, recentFailWordGermanReversed);
                    wordRecyclerView.setAdapter(wordAdapter);

                    ExercicesWords = RefindWord(WordFrench, "fail_word_file");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });;

        // Toggle Verbes Forts

        ToggleButton toggleVerbeFort = (ToggleButton) findViewById(R.id.toggleVerbesForts);
        toggleVerbeFort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useVerbeFort = toggleVerbeFort.isChecked();
                toggleButtonSwitchColor(toggleVerbeFort);
                try {
                    ExercicesWords = RefindWord(WordFrench, "fail_word_file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });;

        // Toggle Verbe Conjugues

        ToggleButton toggleVerbeConjug = (ToggleButton) findViewById(R.id.toggleVerbesConjugues);
        toggleVerbeConjug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useVerbeConjugue = toggleVerbeConjug.isChecked();
                toggleButtonSwitchColor(toggleVerbeConjug);
                try {
                    ExercicesWords = RefindWord(WordFrench, "fail_word_file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });;


        ToggleButton toggleWordRest = (ToggleButton) findViewById(R.id.toggleWordRest);
        toggleWordRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useReste = toggleWordRest.isChecked();
                toggleButtonSwitchColor(toggleWordRest);
                try {
                    ExercicesWords = RefindWord(WordFrench, "fail_word_file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });;
    }

    private void toggleButtonSwitchColor(ToggleButton button) {
        if(button.isChecked()) {
            int colorInt = getResources().getColor(R.color.green);
            ColorStateList csl = ColorStateList.valueOf(colorInt);
            button.setBackgroundTintList(csl);
        } else {
            int colorInt = getResources().getColor(R.color.red);
            ColorStateList csl = ColorStateList.valueOf(colorInt);
            button.setBackgroundTintList(csl);
        }
    }

    private void writeToFile(String[] data) {
        try {
            File file = new File(failWordFile +".csv");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Arrays.toString(data));
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] GetRandomFrenchWord(String csvFileName) throws IOException {

        List<String> FrenchLine = new ArrayList<>();
        List<String> DeutschLine = new ArrayList<>();
        List<String> Wordtype = new ArrayList<>();
        List<String> WordConjugate = new ArrayList<>();

        String line = null;
        InputStream InputStream = getResources().openRawResource(R.raw.wordlist_trainmode);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(InputStream, StandardCharsets.UTF_8)
        );
        int NumLine = 0;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",", 4);

            switch (values[0]) {
                case "VCF":
                    if (useVerbeFort) {
                        FrenchLine.add(values[1]);
                        DeutschLine.add(values[2]);
                        Wordtype.add(values[0]);
                        ++NumLine;
                    }
                    break;
                case "VC":
                    if (useVerbeConjugue) {
                        FrenchLine.add(values[1]);
                        DeutschLine.add(values[2]);
                        Wordtype.add(values[0]);
                        ++NumLine;
                    }
                    break;
                default:
                    if (useReste) {
                        FrenchLine.add(values[1]);
                        DeutschLine.add(values[2]);
                        Wordtype.add(values[0]);
                        ++NumLine;
                    }
                    break;
            }
        }
        int RandomLineInRange = getRandomNumber(0,NumLine);

        String[] ReturnWords = new String[3];

        if(NumLine > 0) {
            ReturnWords[0] = stripAccents(FrenchLine.get(RandomLineInRange));
            ReturnWords[1] = stripAccents(DeutschLine.get(RandomLineInRange));
            ReturnWords[2] = Wordtype.get(RandomLineInRange);
        } else {
            ReturnWords[0] = "Aucun mot ne correspond, activez une nature";
            ReturnWords[1] = "ㅤ";
            ReturnWords[2] = "E";
        }

        if(getRandomNumber(0,4) == 0 && failFrenchWordList.size() > 3) {
            // Remettre les mots raté avec une plus grande probabilité.
            int indexRecentFailWordRandom = getRandomNumber(0, failFrenchWordList.size() - 1);

            if ((!useVerbeFort && failWordTypeList.get(indexRecentFailWordRandom) != "VF") || (!useVerbeConjugue && failWordTypeList.get(indexRecentFailWordRandom) != "VC")) {
                switch (failWordTypeList.get(indexRecentFailWordRandom)) {
                    case "VCF":
                        if (useVerbeFort) {
                            ReturnWords[0] = failFrenchWordList.get(indexRecentFailWordRandom);
                            ReturnWords[1] = failGermanWordList.get(indexRecentFailWordRandom);
                            ReturnWords[2] = failWordTypeList.get(indexRecentFailWordRandom);

                            failFrenchWordList.remove(indexRecentFailWordRandom);
                            failGermanWordList.remove(indexRecentFailWordRandom);
                            failWordTypeList.remove(indexRecentFailWordRandom);
                        }
                        break;
                    case "VC":
                        if (useVerbeConjugue) {
                            ReturnWords[0] = failFrenchWordList.get(indexRecentFailWordRandom);
                            ReturnWords[1] = failGermanWordList.get(indexRecentFailWordRandom);
                            ReturnWords[2] = failWordTypeList.get(indexRecentFailWordRandom);

                            failFrenchWordList.remove(indexRecentFailWordRandom);
                            failGermanWordList.remove(indexRecentFailWordRandom);
                            failWordTypeList.remove(indexRecentFailWordRandom);
                        }
                        break;
                    default:
                        if (useReste) {
                            ReturnWords[0] = failFrenchWordList.get(indexRecentFailWordRandom);
                            ReturnWords[1] = failGermanWordList.get(indexRecentFailWordRandom);
                            ReturnWords[2] = failWordTypeList.get(indexRecentFailWordRandom);

                            failFrenchWordList.remove(indexRecentFailWordRandom);
                            failGermanWordList.remove(indexRecentFailWordRandom);
                            failWordTypeList.remove(indexRecentFailWordRandom);
                        }
                        break;
                }
            }
        }

        return ReturnWords;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        if(s.contains("ß")) {
            s.replaceAll("ß","ss");
        }
        return s;
    }

    private String[] RefindWord(TextView TextViewObject, String csvFileName) throws IOException {
        String[] ExercicesWords = GetRandomFrenchWord(csvFileName);
        TextViewObject.setText(ExercicesWords[0]);
        return ExercicesWords;
    }
   private void Win(EditText FindContent, TextView WordFrench, String csvFileName) {
       FindContent.setText("");
       ColorAnimation.start();
       try {
           ExercicesWords = RefindWord(WordFrench, csvFileName);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    public void disableWordVerbeFort(View view) {

        ToggleButton toggleButton = findViewById(R.id.toggleVerbesForts);
        int tag = (Integer) view.getTag();
        boolean isButtonEnable = toggleButton.isChecked();

        if(isButtonEnable) {
            toggleButton.setChecked(!toggleButton.isChecked());
        }
   }

    public void goToHomePage(View view) {
        finish();
    }
}