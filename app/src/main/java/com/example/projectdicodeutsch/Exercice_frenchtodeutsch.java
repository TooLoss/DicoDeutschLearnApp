package com.example.projectdicodeutsch;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Exercice_frenchtodeutsch extends AppCompatActivity {

    String[] ExercicesWords = new String[3];
    InputStream inputStream;
    AnimationDrawable ColorAnimation;

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

        try {
            ExercicesWords = RefindWord(WordFrench);
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
                        Win(FindContent,WordFrench);
                    }
                }else if(BaseValue.equalsIgnoreCase(ContentValue)){
                    ColorAnimation.stop();
                    Win(FindContent,WordFrench);
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
                    ExercicesWords = RefindWord(WordFrench);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });;
    }

    private String[] GetRandomFrenchWord() throws IOException {

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

    private String[] RefindWord(TextView TextViewObject) throws IOException {
        String[] ExercicesWords = GetRandomFrenchWord();
        TextViewObject.setText(ExercicesWords[0]);
        return ExercicesWords;
    }
   private void Win(EditText FindContent, TextView WordFrench) {
       FindContent.setText("");
       ColorAnimation.start();
       try {
           ExercicesWords = RefindWord(WordFrench);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}