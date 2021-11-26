package com.example.buscaminas;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int SECONDARY_ACTIVITY_1 = 1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Intent intent;
    private Button btnPlay;
    private int level;
    private MediaPlayer mediaPlayer;
    private TypeWriter tw;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();
        btnPlay = (Button) findViewById(R.id.playBtn);
        tw = (TypeWriter) findViewById(R.id.welcome);
        tw.setText("");
        tw.setCharacterDelay(130);
        tw.animateText(getString(R.string.welcome));
        tw = (TypeWriter) findViewById(R.id.txtSelect);
        tw.setText("");
        tw.setCharacterDelay(120);
        tw.animateText(getString(R.string.welcome));
        btnPlay.setActivated(false);
        btnPlay.setTextColor(Color.WHITE);
        btnPlay.setBackgroundColor(Color.TRANSPARENT);
        progress = new ProgressDialog(MainActivity.this);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnPlay.isActivated()) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.annoying);
                    mediaPlayer.start();
                } else {
                    progress.setMessage("Please Wait Loading..."); // creating message

                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // style of indicator
                    progress.setIndeterminate(true);
                    progress.show();
                    new Thread() {

                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            intent = new Intent(MainActivity.this, Game.class);
                            intent.putExtra("Level", level);
                            startActivity(intent);
                            progress.dismiss();
                        }
                    }.start();
                }
            }
        });


        radioGroup = (RadioGroup) findViewById(R.id.rdbGroup);
        radioGroup.setBackgroundColor(Color.argb(70, 1, 1, 1));
        radioGroup.clearAnimation();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioGroup.jumpDrawablesToCurrentState();
                btnPlay.setActivated(true);
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(id);
                radioButton.setSoundEffectsEnabled(false);
                setFont(radioButton);
                if (radioButton.equals(findViewById(R.id.rdbEasy))) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.tobeyou);
                    mediaPlayer.start();
                    level = 1;
                } else if (radioButton.equals(findViewById(R.id.rdbMedium))) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.slacker);
                    mediaPlayer.start();
                    level = 2;
                } else {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.makemyday);
                    mediaPlayer.start();
                    level = 3;
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