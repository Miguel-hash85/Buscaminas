package com.example.buscaminas;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.gridlayout.widget.GridLayout;

public class Game extends AppCompatActivity{
    private GridLayout gridLayout;
    private Intent intentImplicito = null;
    private int dificultad;
    private int x, y;
    private CountDownTimer clock;
    private Button buttonClock;
    private ButtonXY button;
    private ButtonXY buttons[][];
    private int contador;
    private Intent chooser = null;
    //private ArrayList<ButtonXY> buttons = new ArrayList<>();
    private int cantidad = 0;
    private ImageButton buttonHelp;
    private int buttonSize = 2000;
    private int numeros[][];
    private MediaPlayer mediaPlayer;
    private ImageButton buttonDuke;
    private ImageView imageViewCount;
    private MediaRecorder recorder;
    private ImageView imageViewRec;
    private static final int GRABAR_VIDEO=1;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        buttonClock = (Button) findViewById(R.id.buttonClock);
        imageViewCount = (ImageView) findViewById(R.id.imageViewCount);
        imageViewRec = (ImageView) findViewById(R.id.imageViewRec);
        buttonDuke = (ImageButton) findViewById(R.id.buttonDuke);
        buttonHelp = (ImageButton) findViewById(R.id.buttonHelp);
        buttonDuke.setImageDrawable(getDrawable(R.drawable.shut));
        videoView = (VideoView)findViewById(R.id.videoView) ;
        videoView.setAlpha(0);
        buttonDuke.getBackground().setAlpha(25);
        buttonDuke.setTranslationX(110);
        buttonClock.setTranslationX(110);
        imageViewCount.setTranslationX(-60);
        buttonHelp.setTranslationX(-60);
        imageViewRec.setTranslationX(-60);
        buttonDuke.animate().alpha(1).translationX(60).setDuration(2500).start();
        buttonDuke.animate().alpha(1).translationX(-20).setDuration(1500).start();
        buttonClock.animate().alpha(1).translationX(60).setDuration(2500).start();
        buttonClock.animate().alpha(1).translationX(-20).setDuration(2500).start();
        imageViewCount.animate().alpha(1).translationX(30).setDuration(2500).start();
        imageViewRec.animate().alpha(1).translationX(30).setDuration(2500).start();
        buttonHelp.animate().alpha(1).translationX(30).setDuration(2500).start();
        //buttonCount.animate().alpha(1).translationX(-20).setDuration(2500).start();
        new CountDownTimer(3000000, 100) {

            public void onTick(long millisUntilFinished) {
                buttonClock.setText("" + millisUntilFinished / 100);
            }

            public void onFinish() {
                    destaparBombas();
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_lose), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Game.this, Result.class);
                    intent.putExtra("PARAM_1", "0");
                    startActivity(intent);
                    finish();
            }
        }.start();
        imageViewRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Creación del intent
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    // El vídeo se grabará en calidad baja (0)
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    // Limitamos la duración de la grabación a 5 segundos
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                    // Nos aseguramos de que haya una aplicación que pueda manejar el intent
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Lanzamos el intent
                        startActivityForResult(intent,GRABAR_VIDEO);
                    }


            }
        });
        Bundle extras = this.getIntent().getExtras();
        dificultad = extras.getInt("Level");
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game.this, VideoTutorial.class);
                startActivity(intent);
            }
        });
        buttonDuke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = (int) (Math.random() * 7 + 1);
                switch (num) {
                    case 1:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.talking_me);
                        talkingDuke();
                        break;
                    case 2:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.ugly);
                        talkingDuke();
                        break;
                    case 3:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.back_to_work);
                        talkingDuke();
                        break;
                    case 4:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.gonna_die);
                        talkingDuke();
                        break;
                    case 5:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.really);
                        talkingDuke();
                        break;
                    case 6:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.wasting_time);
                        talkingDuke();
                        break;
                    case 7:
                        mediaPlayer = MediaPlayer.create(Game.this, R.raw.youre_pissing);
                        talkingDuke();
                        break;
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        buttonDuke.setImageDrawable(getDrawable(R.drawable.shut));
                    }
                });
            }

        });

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.getBackground().setAlpha(80);
        switch (dificultad) {
            case 1:
                gridLayout.setColumnCount(5);
                gridLayout.setRowCount(5);
                x = 5;
                y = 5;
                contador = 22;
                imageViewCount.setBackground(getDrawable(R.drawable.three));
                buttonSize = buttonSize / (dificultad * 9);
                inicializarCasillas();
                break;
            case 2:
                gridLayout.setColumnCount(6);
                gridLayout.setRowCount(6);
                x = 6;
                y = 6;
                contador = 31;
                buttonSize = (int) (buttonSize / (dificultad * 5.5));
                imageViewCount.setBackground(getDrawable(R.drawable.five));
                inicializarCasillas();
                break;
            case 3:
                gridLayout.setColumnCount(7);
                gridLayout.setRowCount(7);
                x = 7;
                y = 7;
                contador = 42;
                buttonSize = (int) (buttonSize / (dificultad * 4.3));
                imageViewCount.setBackground(getDrawable(R.drawable.seven));
                inicializarCasillas();
                break;
            default:
                Toast.makeText(getApplicationContext(), getString(R.string.txt_fail), Toast.LENGTH_LONG).show();
                break;
        }
        // buttons = new ButtonXY[x][y];
        for (int j = 0; j < x; j++) {
            for (int k = 0; k < y; k++) {
                button = buttons[j][k];
                button.setHeight(buttonSize);
                button.setWidth(buttonSize);
                button.setxCoord(j);
                button.setyCoord(k);
                button.setGravity(Gravity.CENTER);
                button.setTextColor(Color.argb(0, 0, 0, 0));
                button.setTextSize(25);
                button.setBackground(getDrawable(R.drawable.border_button_black));
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
                            textColor(btn);
                            btn.setEnabled(false);
                            btn.setBackgroundColor(Color.argb(50, 1, 1, 1));
                            huecosEnBlanco(btn.getxCoord(), btn.getyCoord());
                            cantidad++;
                            contador--;
                            if (x == 5 && cantidad == 22) {
                                openResult();
                            }
                            if (x == 6 && cantidad == 31) {
                                openResult();
                            }
                            if (x == 7 && cantidad == 42) {
                                openResult();
                            }
                        } else {
                            textColor(btn);
                            btn.setEnabled(false);
                            btn.setBackgroundColor(Color.argb(50, 1, 1, 1));
                            //btn.getBackground().setAlpha(60);
                            cantidad++;
                            contador--;
                            if (x == 5 && cantidad == 22) {
                                openResult();
                            }
                            if (x == 6 && cantidad == 31) {
                                openResult();
                            }
                            if (x == 7 && cantidad == 42) {
                                openResult();
                            }
                        }
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ButtonXY btn = (ButtonXY) view;
                        if (!btn.getText().equals("\uD83C\uDFF4")) {
                            btn.setText("\uD83C\uDFF4");
                            btn.setTextColor(Color.BLACK);
                        } else {
                            btn.setText("" + btn.getNum());
                            btn.setTextColor(Color.argb(0, 0, 0, 0));
                        }
                        return false;
                    }
                });
                gridLayout.addView(button);
            }
            inicializarCasillasButton();
            //gridLayout.setBackground(getDrawable(R.drawable.bomba));
            //gridLayout.setMinimumHeight();
        }
        //colocarMinas();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GRABAR_VIDEO && resultCode == RESULT_OK) {
            videoView.setVideoURI(data.getData());
            videoView.setAlpha(1F);
            videoView.start();
        }
    }
    private void talkingDuke() {
        mediaPlayer.start();
        buttonDuke.setImageDrawable(getDrawable(R.drawable.loud));
    }

    private void inicializarCasillasButton() {
        numeros = new int[x][y];
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
        Toast.makeText(getApplicationContext(), getString(R.string.txt_win), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Game.this, Result.class);
        intent.putExtra("PARAM_1", buttonClock.getText());
        startActivity(intent);
        finish();
    }

    private void huecosEnBlanco(int coordX, int coordY) {

        //boolean salir=false;
        ButtonXY aux = null;
        if (contador > 0) {
            if (coordX - 1 < x && coordX - 1 >= 0) {
                if (!buttons[coordX - 1][coordY].getText().equals("0") && buttons[coordX - 1][coordY].isEnabled()) {//fila arriba
                    incrementarCantidad(buttons[coordX - 1][coordY]);
                }
            }
            if (coordX - 1 < x && coordY - 1 < y && coordX - 1 >= 0 && coordY - 1 >= 0) {
                if (!buttons[coordX - 1][coordY - 1].getText().equals("0") && buttons[coordX - 1][coordY - 1].isEnabled()) {//arriba izquierda
                    incrementarCantidad(buttons[coordX - 1][coordY - 1]);
                }
            }
            if (coordX - 1 < x && coordY + 1 < y && coordX - 1 >= 0) {
                if (!buttons[coordX - 1][coordY + 1].getText().equals("0") && buttons[coordX - 1][coordY + 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX - 1][coordY + 1]);
                }
            }
            if (coordY - 1 < y && coordY - 1 >= 0) {
                if (!buttons[coordX][coordY - 1].getText().equals("0") && buttons[coordX][coordY - 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX][coordY - 1]);
                }
            }
            if (coordY + 1 < y) {
                if (!buttons[coordX][coordY + 1].getText().equals("0") && buttons[coordX][coordY + 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX][coordY + 1]);
                }
            }
            if (coordX + 1 < x && coordY - 1 < y && coordY - 1 >= 0) {
                if (!buttons[coordX + 1][coordY - 1].getText().equals("0") && buttons[coordX + 1][coordY - 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX + 1][coordY - 1]);
                }
            }
            if (coordX + 1 < x) {
                if (!buttons[coordX + 1][coordY].getText().equals("0") && buttons[coordX + 1][coordY].isEnabled()) {
                    incrementarCantidad(buttons[coordX + 1][coordY]);
                }
            }
            if (coordX + 1 < x && coordY + 1 < y) {
                if (!buttons[coordX + 1][coordY + 1].getText().equals("0") && buttons[coordX + 1][coordY + 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX + 1][coordY + 1]);
                }
            }
            //Vacios 0
            if (coordX - 1 < x && coordX - 1 >= 0) {
                if (buttons[coordX - 1][coordY].getText().equals("0") && buttons[coordX - 1][coordY].isEnabled()) {
                    incrementarCantidad(buttons[coordX - 1][coordY]);
                    huecosEnBlanco(buttons[coordX - 1][coordY].getxCoord(), buttons[coordX - 1][coordY].getyCoord());
                }
            }
            //Arriba izquierda
            if (coordX - 1 < x && coordY - 1 < y && coordX - 1 >= 0 && coordY - 1 >= 0) {
                if (buttons[coordX - 1][coordY - 1].getText().equals("0") && buttons[coordX - 1][coordY - 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX - 1][coordY - 1]);
                    huecosEnBlanco(buttons[coordX - 1][coordY - 1].getxCoord(), buttons[coordX - 1][coordY - 1].getyCoord());
                }
            }
            //Arriba derecha
            if (coordX - 1 < x && coordY + 1 < y && coordX - 1 >= 0) {
                if (buttons[coordX - 1][coordY + 1].getText().equals("0") && buttons[coordX - 1][coordY + 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX - 1][coordY + 1]);
                    huecosEnBlanco(buttons[coordX - 1][coordY + 1].getxCoord(), buttons[coordX - 1][coordY + 1].getyCoord());
                }
            }

            //4 Izquierda
            if (coordY - 1 < y && coordY > 0) {
                if (buttons[coordX][coordY - 1].getText().equals("0") && buttons[coordX][coordY - 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX][coordY - 1]);
                    huecosEnBlanco(buttons[coordX][coordY - 1].getxCoord(), buttons[coordX][coordY - 1].getyCoord());
                }
            }
            //5

            if (coordY + 1 < y) {
                if (buttons[coordX][coordY + 1].getText().equals("0") && buttons[coordX][coordY + 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX][coordY + 1]);
                    huecosEnBlanco(buttons[coordX][coordY + 1].getxCoord(), buttons[coordX][coordY + 1].getyCoord());

                }
            }


            //6
            if (coordX + 1 < x && coordY - 1 < y && coordY - 1 >= 0) {
                if (buttons[coordX + 1][coordY - 1].getText().equals("0") && buttons[coordX + 1][coordY - 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX + 1][coordY - 1]);
                    huecosEnBlanco(buttons[coordX + 1][coordY - 1].getxCoord(), buttons[coordX + 1][coordY - 1].getyCoord());

                }
            }
            //7
            if (coordX + 1 < x) {
                if (buttons[coordX + 1][coordY].getText().equals("0") && buttons[coordX + 1][coordY].isEnabled()) {
                    incrementarCantidad(buttons[coordX + 1][coordY]);
                    huecosEnBlanco(buttons[coordX + 1][coordY].getxCoord(), buttons[coordX + 1][coordY].getyCoord());
                }
            }
            //8
            if (coordX + 1 < x && coordY + 1 < y) {
                if (buttons[coordX + 1][coordY + 1].getText().equals("0") && buttons[coordX + 1][coordY + 1].isEnabled()) {
                    incrementarCantidad(buttons[coordX + 1][coordY + 1]);
                    huecosEnBlanco(buttons[coordX + 1][coordY + 1].getxCoord(), buttons[coordX + 1][coordY + 1].getyCoord());
                }
            }


        }

    }

    private void incrementarCantidad(ButtonXY btn) {
        btn.setBackgroundColor(Color.argb(50, 1, 1, 1));
        textColor(btn);
        btn.setEnabled(false);
        if (contador > 0) {
            contador--;
        }
        cantidad++;

    }

    public void textColor(ButtonXY btn) {
        if (btn.getText().equals("1")) {
            btn.setTextColor(Color.WHITE);
        } else if (btn.getText().equals("2")) {
            btn.setTextColor(Color.GREEN);
        } else if (btn.getText().equals("3")) {
            btn.setTextColor(Color.YELLOW);
        } else if (btn.getText().equals("4")) {
            btn.setTextColor(Color.argb(100, 67, 16, 24));
        } else if (btn.getText().equals("5")) {
            btn.setTextColor(Color.MAGENTA);
        } else if (btn.getText().equals("6")) {
            btn.setTextColor(Color.argb(100, 50, 25, 0));
        } else if (btn.getText().equals("7")) {
            btn.setTextColor(Color.GRAY);
        } else if (btn.getText().equals("8")) {
            btn.setTextColor(Color.BLACK);
        }
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

    private void contarCoordenada(int fila, int columna, ButtonXY btn) {
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
        btn.setNum(cantidadDeBombas);
    }

    public void destaparBombas() {
        if (x == 5 && cantidad == 22) {
            winSound();
        } else if (x == 6 && cantidad == 31) {
            winSound();
        } else if (x == 7 && cantidad == 42) {
            winSound();
        } else {
            mediaPlayer = MediaPlayer.create(Game.this, R.raw.wasted);
            mediaPlayer.start();
        }
        try {
            sleep(1500);
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (buttons[i][j].getText().equals("9")) {
                        buttons[i][j].setText("💣");
                        buttons[i][j].setTextColor(Color.BLACK);
                        buttons[i][j].setEnabled(false);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void winSound() {
        mediaPlayer = MediaPlayer.create(Game.this, R.raw.damm);
        mediaPlayer.start();
    }
}