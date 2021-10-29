package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.gridlayout.widget.GridLayout;

import java.util.Random;

public class Game extends AppCompatActivity {
    private GridLayout gridLayout;
    private  Intent intent;
    private Button button;
    private boolean finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        button=(Button)findViewById(R.id.buttonClock);
        intent= new Intent(Game.this, Result.class);
        for (int i = 1; i <= 18; i++) {
            Button button = new Button(this);
            button.setId(i);
            button.setBackgroundColor(Color.BLACK);
            button.setBackground(getDrawable(R.drawable.border_button));
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
            gridLayout.addView(button);
        }
    }
}