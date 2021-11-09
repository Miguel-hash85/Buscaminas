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
    private ButtonXY button;
    private ButtonXY buttons[][];
    //private ArrayList<ButtonXY> buttons = new ArrayList<>();
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
        // buttons = new ButtonXY[x][y];
        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                button=buttons[j][k];
                button.setHeight(125);
                button.setWidth(125);
                button.setGravity(Gravity.CENTER);
                button.setTextColor(Color.BLACK);
                button.setBackground(getDrawable(R.drawable.border_button));
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ButtonXY btn = (ButtonXY) view;
                        if (btn.getText().equals("9")) {
                            destaparBombas();
                            Toast.makeText(getApplicationContext(), getString(R.string.txt_lose), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Game.this, Result.class);
                            intent.putExtra("PARAM_1", "0");
                            startActivity(intent);
                            finish();
                        } else if (btn.getText().equals("0")) {
                            btn.setTextSize(25);
                            huecosEnBlanco(btn.getxCoord(), btn.getyCoord());
                            heGanado(btn);
                            if (cantidad == 22 || cantidad == 31 || cantidad == 42) {
                                openResult();
                            }
                        } else {
                            btn.setTextSize(25);
                            btn.setEnabled(false);
                            heGanado(btn);
                            if (cantidad == 22 || cantidad == 31 || cantidad == 42) {
                                openResult();
                            }
                        }
                    }
                });
                gridLayout.addView(button);
            }
            inicializarCasillasButton();
        }
        //colocarMinas();

    }
    private void inicializarCasillasButton() {
        for (int k = 0; k < x; k++) {
            for (int j = 0; j < y; j++) {
                if (buttons[k][j].getText().equals("0")) {
                    contarCoordenada(k, j, buttons[k][j]);
                }
            }
        }


    }

    private void openResult() {
        destaparBombas();
        Toast.makeText(getApplicationContext(), getString(R.string.txt_win), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Game.this, Result.class);
        intent.putExtra("PARAM_1", buttonClock.getText());
        startActivity(intent);
        finish();
    }

    private void huecosEnBlanco(int coordX, int coordY) {
            if (coordX - 1 < x && coordX - 1 > 0) {
                if (!buttons[coordX - 1][coordY].getText().equals("0") && buttons[coordX - 1][coordY].getTextSize() != 25) {//fila arriba
                    incrementarCantidad(buttons[coordX - 1][coordY]);
                }
            }
            if (coordX - 1 < x && coordY - 1 < y && coordX - 1 > 0 && coordY -1> 0) {
                if (!buttons[coordX - 1][coordY - 1].getText().equals("0") && buttons[coordX - 1][coordY - 1].getTextSize() != 25) {//arriba izquierda
                    incrementarCantidad(buttons[coordX - 1][coordY - 1]);
                }
            }
            if (coordX - 1 < x && coordY + 1 < y && coordX - 1 > 0) {
                if (!buttons[coordX - 1][coordY + 1].getText().equals("0") && buttons[coordX - 1][coordY + 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX - 1][coordY + 1]);
                }
            }
            if (coordY - 1 < y && coordY -1> 0) {
                if (!buttons[coordX][coordY - 1].getText().equals("0") && buttons[coordX][coordY - 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX][coordY - 1]);
                }
            }
            if (coordY + 1 < y) {
                if (!buttons[coordX][coordY + 1].getText().equals("0") && buttons[coordX][coordY + 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX][coordY + 1]);
                }
            }
            if (coordX + 1 < x && coordY - 1 < y && coordY-1 > 0) {
                if (!buttons[coordX + 1][coordY - 1].getText().equals("0") && buttons[coordX + 1][coordY - 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX + 1][coordY - 1]);
                }
            }
            if (coordX + 1 < x) {
                if (!buttons[coordX + 1][coordY].getText().equals("0") && buttons[coordX + 1][coordY].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX + 1][coordY]);
                }
            }
            if (coordX + 1 < x && coordY + 1 < y) {
                if (!buttons[coordX + 1][coordY + 1].getText().equals("0") && buttons[coordX + 1][coordY + 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX + 1][coordY + 1]);
                }
            }
            //Vacios
            if (coordX - 1 < x && coordX - 1 > 0) {
                if (buttons[coordX - 1][coordY].getText().equals("0") && buttons[coordX - 1][coordY].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX - 1][coordY]);
                    huecosEnBlanco(buttons[coordX - 1][coordY].getxCoord(), buttons[coordX - 1][coordY].getyCoord());
                }
            }
            //Arriba izquierda
            if (coordX - 1 < x && coordY - 1 < y && coordX - 1 > 0 && coordY -1> 0) {
                if (buttons[coordX - 1][coordY - 1].getText().equals("0") && buttons[coordX - 1][coordY - 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX - 1][coordY - 1]);
                    huecosEnBlanco(buttons[coordX - 1][coordY - 1].getxCoord(), buttons[coordX - 1][coordY - 1].getyCoord());
                }
            }
            //Arriba derecha
            if (coordX - 1 < x && coordY + 1 < y && coordX - 1 > 0) {
                if (buttons[coordX - 1][coordY + 1].getText().equals("0") && buttons[coordX - 1][coordY + 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX - 1][coordY + 1]);
                    huecosEnBlanco(buttons[coordX - 1][coordY + 1].getxCoord(), buttons[coordX - 1][coordY + 1].getyCoord());
                }
            }

            //4 Izquierda
            if (coordY - 1 < y && coordY > 0) {
                if (buttons[coordX][coordY - 1].getText().equals("0") && buttons[coordX][coordY - 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX][coordY - 1]);
                    huecosEnBlanco(buttons[coordX][coordY - 1].getxCoord(), buttons[coordX][coordY - 1].getyCoord());
                }
            }
            //5
            if (coordY + 1 < y) {
                if (buttons[coordX][coordY + 1].getText().equals("0") && buttons[coordX][coordY + 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX][coordY + 1]);
                    huecosEnBlanco(buttons[coordX][coordY + 1].getxCoord(), buttons[coordX][coordY + 1].getyCoord());
                }
            }/*
            //6
            if (coordX + 1 < x && coordY - 1 < y && coordY -1> 0) {
                if (buttons[coordX + 1][coordY - 1].getText().equals("0") && buttons[coordX + 1][coordY - 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX + 1][coordY - 1]);
                    huecosEnBlanco(buttons[coordX + 1][coordY - 1].getxCoord(), buttons[coordX + 1][coordY - 1].getyCoord());
                }
            }
            //7
            if (coordX + 1 < x) {
                if (buttons[coordX + 1][coordY].getText().equals("0") && buttons[coordX + 1][coordY].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX + 1][coordY]);
                    huecosEnBlanco(buttons[coordX + 1][coordY].getxCoord(), buttons[coordX + 1][coordY].getyCoord());
                }
            }
            //8
            if (coordX + 1 < x && coordY + 1 < y) {
                if (buttons[coordX + 1][coordY + 1].getText().equals("0") && buttons[coordX + 1][coordY + 1].getTextSize() != 25) {
                    incrementarCantidad(buttons[coordX + 1][coordY + 1]);
                    huecosEnBlanco(buttons[coordX + 1][coordY + 1].getxCoord(), buttons[coordX + 1][coordY + 1].getyCoord());
                }
            }*/
    }

    private void incrementarCantidad(ButtonXY btn) {
        btn.setTextSize(25);
        btn.setEnabled(false);
        cantidad++;
    }


    /*private void inicializarCasillasButton() {
        ButtonXY btn;

        int cont = 0;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            btn = (ButtonXY) gridLayout.getChildAt(i);
            buttons.add(btn);
        }
        for (int k = 0; k < x; k++) {
            for (int j = 0; j < y; j++) {
                if (ButtonXY[k][j].getText() == 0 && cont < buttons.size()) {
                    contarCoordenada(k, j, buttons.get(cont));
                    cont++;
                } else if (casillas[k][j].getContenido() == 9 && cont < buttons.size()) {
                    buttons.get(cont).setText("" + 9);
                    cont++;
                }

            }
        }


    }*/

    private void inicializarCasillas() {
        buttons = new ButtonXY[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                buttons[i][j] = new ButtonXY(this, null, R.style.Widget_AppCompat_Button_Small);
                buttons[i][j].setText("0");
                buttons[i][j].setxCoord(i);
                buttons[i][j].setyCoord(j);
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
            int columna = (int) (Math.random() * y);
            if (buttons[fila][columna].getText().equals("0")) {
                buttons[fila][columna].setText("9");
                cantidadDeMinasPorColocar--;
            }
        }
    }

    private void contarCoordenada(int fila, int columna,Button btn) {
        int cantidadDeBombas = 0;

        if (fila - 1 >= 0 && columna - 1 >= 0) {//si tengo una fila a la izquierda y una columna encima(diagonal a la izquierda arriba)
            if (buttons[fila - 1][columna - 1].getText().equals("9")) {//analizamos si es una bomba
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0) {//analizamos la casilla de la izquierda
            if (buttons[fila - 1][columna].getText().equals("9")) {//analizamos si es una bomba
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0 && columna + 1 < x) {//diagonal de abajo a la izquierda
            if (buttons[fila - 1][columna + 1].getText().equals("9")) {//analizamos si es bomba
                cantidadDeBombas++;
            }
        }

        if (columna + 1 < x) {//casilla de abajo
            if (buttons[fila][columna + 1].getText().equals("9")) {
                cantidadDeBombas++;
            }
        }

        if (fila + 1 < x && columna + 1 < x) {//diagonal de abajo a la derecha
            if (buttons[fila + 1][columna + 1].getText().equals("9")) {
                cantidadDeBombas++;
            }
        }

        if (fila + 1 < x) {//casilla de la derecha
            if (buttons[fila + 1][columna].getText().equals("9")) {
                cantidadDeBombas++;
            }
        }

        if (fila + 1 < x && columna - 1 >= 0) {//diagonal arriba a la derecha
            if (buttons[fila + 1][columna - 1].getText().equals("9")) {
                cantidadDeBombas++;
            }
        }

        if (columna - 1 >= 0) {//casilla de arriba
            if (buttons[fila][columna - 1].getText().equals("9")) {
                cantidadDeBombas++;
            }
        }
        btn.setText("" + cantidadDeBombas);
    }

    private void heGanado(ButtonXY btn) {
        if (x == 5) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (buttons[i][j].equals(btn) && !buttons[i][j].getText().equals("0")) {
                        cantidad++;
                        i = x;
                        j = y;
                    }

                }

            }
        }
        if (x == 6) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (buttons[i][j].equals(btn) && !buttons[i][j].getText().equals("0")) {
                        cantidad++;
                        i = x;
                        j = y;
                    }

                }

            }
        }
        if (x == 7) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (buttons[i][j].equals(btn) && !buttons[i][j].getText().equals("0")) {
                        cantidad++;
                        i = x;
                        j = y;
                    }

                }

            }
        }
    }

    public void destaparBombas() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (buttons[i][j].getText().equals("9")) {
                    buttons[i][j].setText("💣");
                    buttons[i][j].setTextSize(20);
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

}