package com.example.projectdicodeutsch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Exercice_frenchtodeutsch extends AppCompatActivity {

    String[] ExercicesWords = new String[3];
    InputStream inputStream;
    AnimationDrawable ColorAnimation;
    String failWordFile = "fail_word_file";
    ArrayList<String> recentFailWord = new ArrayList<String>();
    ArrayList<String> recentFailWordReversed = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_frenchtodeutsch);

        // VARIABLES
        TextView WordFrench = findViewById(R.id.SetWordFrench);
        ListView recentFailWordList = findViewById(R.id.fail_word);
        Button FindBtn = findViewById(R.id.WordSearchBtn);
        Button Giveup = findViewById(R.id.Giveup);
        RelativeLayout FrenchBox = findViewById(R.id.WordFrench);
        EditText FindContent = findViewById(R.id.EditTextWord);

        FrenchBox.setBackgroundResource(R.drawable.color_animation);
        ColorAnimation = (AnimationDrawable) FrenchBox.getBackground();

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
                    if(recentFailWord.size() > 20) {
                        recentFailWord.remove(0);
                    }
                    if(!recentFailWord.isEmpty()) {
                        recentFailWordReversed = recentFailWord;
                        Collections.reverse(recentFailWordReversed);
                        ArrayAdapter<String> ArrayAdapter = new ArrayAdapter<String>(Exercice_frenchtodeutsch.this, android.R.layout.simple_list_item_1, recentFailWordReversed);
                        recentFailWordList.setAdapter(ArrayAdapter);
                    }
                    ExercicesWords = RefindWord(WordFrench, "fail_word_file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });;
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

    private String[] GetFailedWord() throws IOException {

        List<String> FrenchLine = new ArrayList<>();
        List<String> DeutschLine = new ArrayList<>();
        List<String> Wordtype = new ArrayList<>();
        List<String> WordConjugate = new ArrayList<>();

        String line = null;
        InputStream InputStream = getResources().openRawResource(R.raw.fail_word_file);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(InputStream, StandardCharsets.UTF_8)
        );
        int NumLine = 0;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",", 4);
            ++NumLine;

            FrenchLine.add(values[1]);
            DeutschLine.add(values[2]);
            Wordtype.add(values[0]);
        }
        int RandomLineInRange = getRandomNumber(0,NumLine);

        String[] ReturnWords = new String[3];

        ReturnWords[0] = stripAccents(FrenchLine.get(RandomLineInRange));
        ReturnWords[1] = stripAccents(DeutschLine.get(RandomLineInRange));
        ReturnWords[2] = Wordtype.get(RandomLineInRange);

        return ReturnWords;
    }

    private String[] GetRandomFrenchWord(String csvFileName) throws IOException {

        List<String> FrenchLine = new ArrayList<>();
        List<String> DeutschLine = new ArrayList<>();
        List<String> Wordtype = new ArrayList<>();
        List<String> WordConjugate = new ArrayList<>();

        String line = null;
        InputStream InputStream = getResources().openRawResource(R.raw.wordlist_dicodeutsch);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(InputStream, StandardCharsets.UTF_8)
        );
        int NumLine = 0;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",", 4);
            ++NumLine;

            FrenchLine.add(values[1]);
            DeutschLine.add(values[2]);
            Wordtype.add(values[0]);
        }
        int RandomLineInRange = getRandomNumber(0,NumLine);

        String[] ReturnWords = new String[3];

        ReturnWords[0] = stripAccents(FrenchLine.get(RandomLineInRange));
        ReturnWords[1] = stripAccents(DeutschLine.get(RandomLineInRange));
        ReturnWords[2] = Wordtype.get(RandomLineInRange);

        if(getRandomNumber(0,3) == 0 && recentFailWord.size() > 3) {
            // Remettre les mots raté avec une plus grande probabilité.
            int indexRecentFailWordRandom = getRandomNumber(0, recentFailWord.size() - 1);
            List<String> FailedList = new ArrayList<String>(Arrays.asList(recentFailWord.get(indexRecentFailWordRandom).split(" > ")));
            ReturnWords[0] = FailedList.get(0);
            ReturnWords[1] = FailedList.get(1);
            ReturnWords[2] = FailedList.get(2);
            recentFailWord.remove(indexRecentFailWordRandom);
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
}