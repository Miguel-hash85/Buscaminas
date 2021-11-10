package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class shows the final score and let the player play again. You can aditionally check an historic rainking.
 * @author Miguel Sánchez
 */
public class Result extends AppCompatActivity {


    // The Implicit Intent we want to make
    Intent intentImplicito = null;
    // The chooser
    Intent chooser = null;
    private static final int SECONDARY_ACTIVITY_1 = 1;
    private TextView score=null;
    private String puntuacion=null;
    private TextView resultado=null;
    private Button historic=null;
    private Button tryAgain=null;
    private Button bestScore=null;
    private SQLiteDatabase dataBase=null;
    private Cursor cursor=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        dataBase=openOrCreateDatabase("Score", Context.MODE_PRIVATE, null);
        dataBase.execSQL("CREATE TABLE IF NOT EXISTS t_score(Score VARCHAR)");
        Bundle extras=this.getIntent().getExtras();
        puntuacion=extras.getString("PARAM_1");
        score=(TextView)findViewById(R.id.textScore);
        if(Integer.valueOf(puntuacion)>0)
            dataBase.execSQL("INSERT INTO t_score values(puntuacion)");
        resultado=(TextView) findViewById(R.id.textPoints);
        resultado.setText(puntuacion);
        historic=(Button)findViewById(R.id.buttonHistoric);
        historic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentImplicito= new Intent();
                intentImplicito.setAction(Intent.ACTION_WEB_SEARCH);
                intentImplicito.putExtra(SearchManager.QUERY,getString(R.string.urlRanking));
                chooser=Intent.createChooser(intentImplicito,getString(R.string.txt_intent_app));
                if(chooser.resolveActivity(getPackageManager())!=null){

                    startActivity(chooser);

                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.txt_navegador), Toast.LENGTH_LONG).show();
                }

            }
        });
        tryAgain=(Button) findViewById(R.id.buttonTryAgain);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Result.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        bestScore=(Button) findViewById(R.id.buttonBestScore);
        bestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor= dataBase.rawQuery("SELECT MAX("+Score+") FROM "+t_score);
            }
        });

    }
}