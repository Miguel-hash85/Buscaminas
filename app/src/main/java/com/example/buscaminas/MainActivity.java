package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    public static final int SECONDARY_ACTIVITY_1 = 1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Intent intent;
    private Button btnPlay;
    private RadioButton button;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        btnPlay = (Button) findViewById(R.id.playBtn);
        btnPlay.setEnabled(false);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    intent = new Intent(MainActivity.this, Game.class);
                    intent.putExtra("Level", level);
                    startActivity(intent);


            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.rdbGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioGroup.jumpDrawablesToCurrentState();
                btnPlay.setEnabled(true);
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(id);
                setFont(radioButton);
                if(radioButton.equals(findViewById(R.id.rdbEasy))){

                    level=1;

                }else if(radioButton.equals(findViewById(R.id.rdbMedium))){

                    level=2;

                }else{

                    level=3;

                }
                checkButtons();
            }
        });
    }





    private void setFont(RadioButton radioButton) {
        String s = radioButton.getText().toString();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, spannableString.length(), 0);
        radioButton.setText(spannableString);
    }

    private void checkButtons() {
        int count = radioGroup.getChildCount();
        ArrayList<RadioButton> rdbs = new ArrayList<RadioButton>();
        for (int it = 0; it < count; it++) {
            radioGroup.getChildAt(it);
            rdbs.add((RadioButton) radioGroup.getChildAt(it));
        }
        for (int j = 0; j < rdbs.size(); j++) {
            if (rdbs.get(j).getId() != radioButton.getId()) {
                restFont(rdbs.get(j));
            }
        }
    }

    private void restFont(RadioButton Button) {
        String s = Button.getText().toString();
        SpannableString spannableString = new SpannableString(s);
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, spannableString.length(), 0);
        Button.setText(spannableString);
    }

}