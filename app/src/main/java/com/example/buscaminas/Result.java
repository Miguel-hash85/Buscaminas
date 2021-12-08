package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

/**
 * This class shows the final score and let the player play again. You can aditionally check an historic rainking.
 * @author Miguel Sánchez
 */
public class Result extends AppCompatActivity {


    // The Implicit Intent we want to make
    private Intent intentImplicito = null;
    // The chooser
    private Intent chooser = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //private GifWebView score=null;
    private String puntuacion=null;
    private TextView resultado=null;
    private ImageView score=null;
    private ImageView historic=null;
    private ImageView tryAgain=null;
    private ImageView bestScore=null;
    private ImageView smile=null;
    private SQLiteDatabase dataBase=null;
    private Cursor cursor=null;
    private ImageView photo=null;
    static final int CAPTURA_IMAGEN = 1;



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
        score= (ImageView) findViewById(R.id.imageScore);
        historic= (ImageView) findViewById(R.id.imageHistoric);
        tryAgain= (ImageView) findViewById(R.id.imageTryAgain);
        bestScore= (ImageView) findViewById(R.id.imageBest);
        smile= (ImageView) findViewById(R.id.imageSmile);
        photo= (ImageView) findViewById(R.id.imagePhoto);
        smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
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
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Result.this, MainActivity.class);
                startActivity(intent);
                Animatoo.animateWindmill(Result.this);
                finish();

            }
        });
        bestScore=(ImageView) findViewById(R.id.imageBest);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }


}