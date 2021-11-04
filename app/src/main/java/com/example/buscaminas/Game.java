package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import java.util.Random;

public class Game extends AppCompatActivity {
    private GridLayout gridLayout;
    private Intent intentImplicito = null;
    private int dificultad;
    private int x, y;
    private CountDownTimer clock;
    private Button buttonClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttonClock=(Button)findViewById(R.id.buttonClock);
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                buttonClock.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                buttonClock.setText("done!");
            }
        }.start();

        Bundle extras=this.getIntent().getExtras();
        dificultad=extras.getInt("Level");

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        switch(dificultad){
            case 1:
                gridLayout.setColumnCount(5);
                gridLayout.setRowCount(5);
                x=5;
                y=5;
                break;
            case 2:
                gridLayout.setColumnCount(6);
                gridLayout.setRowCount(6);
                x=6;
                y=6;
                break;
            case 3:
                gridLayout.setColumnCount(7);
                gridLayout.setRowCount(7);
                x=7;
                y=7;
                break;
            default:
                Toast.makeText(getApplicationContext(),getString(R.string.txt_navegador), Toast.LENGTH_LONG).show();
                break;
        }
        for (int i = 1; i <= x*y; i++) {
            Button button = new Button(this);
            button.setId(i);
            button.setBackgroundColor(Color.BLACK);
            button.setBackground(getDrawable(R.drawable.border_button));
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent= new Intent(Game.this, Result.class);
                    intent.putExtra("PARAM_1", 0);
                    startActivity(intent);
                    finish();

                }
            });
            gridLayout.addView(button);

        }
    }
}