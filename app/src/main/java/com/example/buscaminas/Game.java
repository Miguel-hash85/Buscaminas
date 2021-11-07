package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {
    private GridLayout gridLayout;
    private Intent intentImplicito = null;
    private int dificultad;
    private int x, y;
    private CountDownTimer clock;
    private Button buttonClock;
    private Button button;
    private Casilla casillas[][];
    private ArrayList<Button> buttons = new ArrayList<>();
    private static int cantidad = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        buttonClock = (Button) findViewById(R.id.buttonClock);
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                buttonClock.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                buttonClock.setText("done!");
            }
        }.start();

        Bundle extras = this.getIntent().getExtras();
        dificultad = extras.getInt("Level");

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        switch (dificultad) {
            case 1:
                gridLayout.setColumnCount(5);
                gridLayout.setRowCount(5);
                x = 5;
                y = 5;
                inicializarCasillas();
                break;
            case 2:
                gridLayout.setColumnCount(6);
                gridLayout.setRowCount(6);
                x = 6;
                y = 6;
                inicializarCasillas();
                break;
            case 3:
                gridLayout.setColumnCount(7);
                gridLayout.setRowCount(7);
                x = 7;
                y = 7;
                inicializarCasillas();
                break;
            default:
                Toast.makeText(getApplicationContext(), getString(R.string.txt_navegador), Toast.LENGTH_LONG).show();
                break;
        }

        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                button = new Button(this, null, R.style.Widget_AppCompat_Button_Small);
                button.setHeight(125);
                button.setWidth(125);
                button.setGravity(Gravity.CENTER);
                button.setTextColor(Color.BLACK);
                button.setBackground(getDrawable(R.drawable.border_button));
                button.setTextSize(0);
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button btn = (Button) view;
                        if (btn.getText().equals("9")) {
                            destaparBombas();
                            Toast.makeText(getApplicationContext(), getString(R.string.txt_lose), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Game.this, Result.class);
                            intent.putExtra("PARAM_1", "0");
                            startActivity(intent);
                            finish();
                        } else if(btn.getText().equals("0")){
                            huecosEnBlanco();
                            heGanado(btn);
                            if (cantidad == 22||cantidad==31||cantidad==42){
                              openResult();
                            }
                        }else{
                            btn.setTextSize(25);
                            btn.setEnabled(false);
                            heGanado(btn);
                            if (cantidad == 22||cantidad==31||cantidad==42){
                                openResult();
                            }
                        }
                    }
                });
                gridLayout.addView(button);
            }
        }
        inicializarCasillasButton();
    }

    private void openResult() {
        destaparBombas();
        Toast.makeText(getApplicationContext(), getString(R.string.txt_win), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Game.this, Result.class);
        intent.putExtra("PARAM_1", buttonClock.getText());
        startActivity(intent);
        finish();
    }

    private void huecosEnBlanco() {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getText().equals("0")) {
                    buttons.get(i).setTextSize(25);
                    buttons.get(i).setEnabled(false);
                    cantidad++;
            }
        }
    }

    private void inicializarCasillasButton() {
        Button btn;

        int cont = 0;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            btn = (Button) gridLayout.getChildAt(i);
            buttons.add(btn);
        }
        for (int k = 0; k < x; k++) {
            for (int j = 0; j < y; j++) {
                if (casillas[k][j].getContenido() == 0 && cont < buttons.size()) {
                    contarCoordenada(k, j, buttons.get(cont));
                    cont++;
                } else if (casillas[k][j].getContenido() == 9 && cont < buttons.size()) {
                    buttons.get(cont).setText("" + 9);
                    cont++;
                }

            }
        }


    }

    private void inicializarCasillas() {
        casillas = new Casilla[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                casillas[i][j] = new Casilla();
            }
        }
        colocarMinas();
    }

    private void colocarMinas() {
        int cantidadDeMinasPorColocar = 3;
        if (x == 6) cantidadDeMinasPorColocar = 5;
        if (x == 7) cantidadDeMinasPorColocar = 7;
        while (cantidadDeMinasPorColocar > 0) {
            int fila = (int) (Math.random() * x);
            int columna = (int) (Math.random() * x);
            if (casillas[fila][columna].getContenido() == 0) {
                casillas[fila][columna].setContenido(9);
                cantidadDeMinasPorColocar--;
            }
        }
    }

    private void contarCoordenada(int fila, int columna, Button btn) {
        int cantidadDeBombas = 0;

        if (fila - 1 >= 0 && columna - 1 >= 0) {
            if (casillas[fila - 1][columna - 1].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0) {
            if (casillas[fila - 1][columna].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0 && columna + 1 < x) {
            if (casillas[fila - 1][columna + 1].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (columna + 1 < x) {
            if (casillas[fila][columna + 1].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (fila + 1 < x && columna + 1 < x) {
            if (casillas[fila + 1][columna + 1].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (fila + 1 < x) {
            if (casillas[fila + 1][columna].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (fila + 1 < x && columna - 1 >= 0) {
            if (casillas[fila + 1][columna - 1].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }

        if (columna - 1 >= 0) {
            if (casillas[fila][columna - 1].getContenido() == 9) {
                cantidadDeBombas++;
            }
        }
        btn.setText("" + cantidadDeBombas);
    }

    private void heGanado(Button btn) {
        if(x==5){
            for(int i=0;i<buttons.size();i++){
                if(buttons.get(i).equals(btn)&&!buttons.get(i).getText().equals("0")){
                    cantidad++;
                    i=buttons.size();
                }
            }
        }
        if(x==6){
            for(int i=0;i<buttons.size();i++){
                if(buttons.get(i).equals(btn)){
                    cantidad++;
                    i=buttons.size();
                }
            }
        }
        if(x==7){
            for(int i=0;i<buttons.size();i++){
                if(buttons.get(i).equals(btn)){
                    cantidad++;
                    i=buttons.size();
                }
            }
        }
    }

    public void destaparBombas() {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getText().equals("9")) {
                buttons.get(i).setText("💣");
                buttons.get(i).setTextSize(20);
                buttons.get(i).setEnabled(false);
            }
        }
    }

}