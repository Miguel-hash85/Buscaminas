package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
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
    private int puntuacion=0;
    private TextView resultado=null;
    private Button historic=null;
    private Button tryAgain=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle extras=this.getIntent().getExtras();
        puntuacion=extras.getInt("PARAM_1");
        score=(TextView)findViewById(R.id.textScore);
        resultado=(TextView) findViewById(R.id.textPoints);
        resultado.setText(resultado.getText());
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
                finish();

            }
        });

    }
}