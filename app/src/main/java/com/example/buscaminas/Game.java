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

import java.util.Random;

public class Game extends AppCompatActivity {
    private GridLayout gridLayout;
    private Intent intentImplicito = null;
    private int dificultad;
    private int x, y;
    private CountDownTimer clock;
    private Button buttonClock;
    private Casilla casillas[][];
    private Button button;
    private boolean activo=true;
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
                inicializarCasillas();
                contarBombasDelPerimetro();
                break;
            case 2:
                gridLayout.setColumnCount(6);
                gridLayout.setRowCount(6);
                x=6;
                y=6;
                inicializarCasillas();
                contarBombasDelPerimetro();
                break;
            case 3:
                gridLayout.setColumnCount(7);
                gridLayout.setRowCount(7);
                x=7;
                y=7;
                inicializarCasillas();
                contarBombasDelPerimetro();
                break;
            default:
                Toast.makeText(getApplicationContext(),getString(R.string.txt_navegador), Toast.LENGTH_LONG).show();
                break;
        }

       for(int j=0;j<x;j++){
            for(int k=0;k<y;k++){
                button = new Button(this,null, R.style.Widget_AppCompat_Button_Small);
                button.setHeight(125);
                button.setWidth(125);
                button.setGravity(Gravity.CENTER);
                if(casillas[j][k].getContenido()==9){
                    button.setText("💣");
                }else{
                    button.setText(""+casillas[j][k].getContenido());
                }
                button.setTextColor(Color.BLACK);
                button.setBackground(getDrawable(R.drawable.border_button));
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int posX = x;
                        int posY = y;

                        if (activo){
                            for (int i = 0; i < x; i++){
                                for (int j = 0; j<y; j++){

                                    if (casillas[i][j].dentroDeLaCasilla(posX,posY)){
                                        if(button.isEnabled()) {
                                            if(!casillas[i][j].destapada) {
                                                casillas[i][j].banderita = !casillas[i][j].banderita;
                                            }
                                        }else{

                                            if(!casillas[i][j].banderita){
                                                casillas[i][j].destapada = true;

                                                if (casillas[i][j].contenido == 100) {
                                                    Toast.makeText(getApplicationContext(),getString(R.string.txt_lose), Toast.LENGTH_LONG).show();
                                                    destaparBombas();
                                                    activo = false;
                                                    Intent intent= new Intent(Game.this, Result.class);
                                                    intent.putExtra("PARAM_1", 0);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (casillas[i][j].contenido == 0) {
                                                    recorrer(i, j);
                                                }
                                            }
                                        }
                                        button.setEnabled(false);
                                    }
                                }
                            }

                            if (activo && heGanado()){
                                Toast.makeText(getApplicationContext(),getString(R.string.txt_win), Toast.LENGTH_LONG).show();
                                destaparBombas();
                                activo = false;
                                Intent intent= new Intent(Game.this, Result.class);
                                intent.putExtra("PARAM_1", 0);
                                startActivity(intent);
                                finish();
                            }

                        }



                    }
                });


                gridLayout.addView(button);


            }
        }
    }

    private void inicializarCasillas() {
        casillas= new Casilla[x][y];
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                casillas[i][j]=new Casilla();
            }
        }
        colocarMinas();
    }

    public void colocarMinas(){
        int cantidadDeMinasPorColocar=3;
        if(x==6) cantidadDeMinasPorColocar = 5;
        if(x==7) cantidadDeMinasPorColocar = 7;
        while (cantidadDeMinasPorColocar>0){
            int fila = (int) (Math.random()*x);
            int columna = (int) (Math.random()*x);
            if (casillas[fila][columna].contenido == 0){
                casillas[fila][columna].contenido = 9;
                cantidadDeMinasPorColocar --;
            }
        }
    }
    private void contarBombasDelPerimetro() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (casillas[i][j].getContenido() == 0) {
                    casillas[i][j].setContenido(contarCoordenada(i, j));
                }
            }
        }
    }

    private int contarCoordenada(int fila, int columna){
        int cantidadDeBombas = 0;

        if (fila - 1 >= 0 && columna - 1 >=0){
            if (casillas[fila-1][columna-1].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0){
            if (casillas[fila-1][columna].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if (fila - 1 >= 0 && columna + 1 <x){
            if (casillas[fila-1][columna+1].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if ( columna + 1 <x){
            if (casillas[fila][columna+1].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if (fila + 1 <x && columna + 1 <x){
            if (casillas[fila+1][columna+1].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if (fila + 1 <x ){
            if (casillas[fila+1][columna].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if (fila + 1 <x && columna - 1 >=0){
            if (casillas[fila+1][columna-1].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        if ( columna - 1 >=0){
            if (casillas[fila][columna-1].getContenido()==9){
                cantidadDeBombas++;
            }
        }

        return cantidadDeBombas;
    }

    private boolean heGanado(){
        int cantidad = 0;
        for (int i = 0; i< x; i++){
            for (int j = 0; j<y; j++){
                if (casillas[i][j].destapada){
                    cantidad++;
                }
            }
        }

        if (cantidad == 56){
            return true;
        } else {
            return false;
        }
    }

    public void destaparBombas(){
        for (int i = 0; i<x; i++){
            for (int j = 0; j < y; j++){
                casillas[i][j].banderita = false;
                if (casillas[i][j].getContenido()==9){
                    casillas[i][j].destapada = true;
                }
            }
        }
        button.setEnabled(false);
    }

    private void recorrer(int fila, int columna){

        if (fila>=0 && fila <x && columna>=0 && columna<y){
            if (casillas[fila][columna].contenido == 0 &&
                    !casillas[fila][columna].banderita){

                casillas[fila][columna].destapada = true;
                casillas[fila][columna].contenido = 50;

                recorrer(fila-1, columna-1);
                recorrer(fila-1,columna);
                recorrer(fila-1,columna+1);
                recorrer(fila,columna+1);
                recorrer(fila+1, columna+1);
                recorrer(fila+1, columna);
                recorrer(fila+1, columna-1);
                recorrer(fila, columna-1);

            } else if (casillas[fila][columna].contenido<=8 &&
                    !casillas[fila][columna].banderita){
                casillas[fila][columna].destapada = true;
            }
        }


    }
}