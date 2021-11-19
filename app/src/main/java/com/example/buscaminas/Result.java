package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class shows the final score and let the player play again. You can aditionally check an historic rainking.
 * @author Miguel Sánchez
 */
public class Result extends AppCompatActivity {


    // The Implicit Intent we want to make
    private Intent intentImplicito = null;
    // The chooser
    private Intent chooser = null;
    private static final int SECONDARY_ACTIVITY_1 = 1;
    //private GifWebView score=null;
    private String puntuacion=null;
    private TextView resultado=null;
    private ImageButton historic=null;
    private Button tryAgain=null;
    private Button bestScore=null;
    private SQLiteDatabase dataBase=null;
    private Cursor cursor=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras=this.getIntent().getExtras();
        puntuacion=extras.getString("PARAM_1");
        dataBase=openOrCreateDatabase("Score", Context.MODE_PRIVATE, null);
        dataBase.execSQL("CREATE TABLE IF NOT EXISTS t_minitabla (SCORE VARCHAR)");
        //score=(TextView)findViewById(R.id.textScore);
        if(Integer.valueOf(puntuacion)>0) {
            dataBase.execSQL("INSERT INTO t_minitabla VALUES ("+puntuacion+")");
        }
        resultado=(TextView) findViewById(R.id.textPoints);
        resultado.setText(puntuacion);
        historic=(ImageButton)findViewById(R.id.imageButtonH);
        //historic.setBackgroundResource(R.drawable.historic);
        //bestScore=(Button)findViewById(R.id.buttonBestScore);
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
                    Toast.makeText(getApplicationContext(),getString(R.string.txt_nav), Toast.LENGTH_LONG).show();
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
                cursor= dataBase.rawQuery("SELECT MAX(SCORE) FROM t_minitabla", null);
                if(cursor.getCount()==0){
                    Toast.makeText(getApplicationContext(),getString(R.string.txt_no_score), Toast.LENGTH_SHORT).show();
                }else if(cursor.moveToNext()){
                    Toast.makeText(getApplicationContext(),""+cursor.getString(0), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}