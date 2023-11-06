package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public class GameActivity extends AppCompatActivity {

    private EditText txtNumber;
    private Button btnCompare;
    private TextView lblResult;
    private ProgressBar pgbScore;
    private TextView lblHistory;

    private int searchedValue;
    private int score;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textView = findViewById(R.id.textView);
        SpannableStringBuilder texte = new SpannableStringBuilder(textView.getText().toString());
        texte.setSpan(new UnderlineSpan(), 0, texte.length(), 0);
        textView.setText(texte);

        txtNumber = (EditText) findViewById(R.id.txtNumber);
        btnCompare = (Button) findViewById(R.id.btnCompare);
        lblResult = (TextView) findViewById(R.id.lblResult);
        pgbScore = (ProgressBar) findViewById(R.id.pgbScore);
        lblHistory = (TextView) findViewById(R.id.lblHistory);

        btnCompare.setOnClickListener( btnCompareListener );

        init();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        double price = 1_000_000.01;
        Log.i( "DEBUG", formatter.format( price ) );

        DateFormat dataFormatter = DateFormat.getDateTimeInstance();
        Log.i( "DEBUG", dataFormatter.format( new Date() ) );
    }

    private void init() {
        score = 0;
        searchedValue = 1 + (int) (Math.random() * 100);
        Log.i( "DEBUG", "Searched value : " + searchedValue );

        txtNumber.setText( "" );
        pgbScore.setProgress( score );
        lblResult.setText( "" );
        lblHistory.setText( "" );

        txtNumber.requestFocus();
    }

    private void congratulations() {
        lblResult.setText( R.string.strCongratulations );

        AlertDialog retryAlert = new AlertDialog.Builder( this ).create();
        retryAlert.setTitle( R.string.app_name );
        retryAlert.setMessage( getString(R.string.strMessage, score ) );

        retryAlert.setButton( AlertDialog.BUTTON_POSITIVE, getString(R.string.strYes), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                init();
            }
        });

        retryAlert.setButton( AlertDialog.BUTTON_NEGATIVE, getString(R.string.strNo), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        retryAlert.setButton( AlertDialog.BUTTON_NEUTRAL, getString(R.string.tableauScore), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToScoreActivity();
            }
        });

        retryAlert.show();
    }

    private View.OnClickListener btnCompareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i( "DEBUG", "Button clicked" );

            String strNumber = txtNumber.getText().toString();
            if ( strNumber.equals( "" ) ) return;

            int enteredValue = Integer.parseInt( strNumber );
            lblHistory.append( strNumber + "\r\n" );
            pgbScore.incrementProgressBy( 1 );
            score++;

            if ( enteredValue == searchedValue ) {
                //TODO mémoriser score et save en BD le plus faible
                congratulations();
            } else if ( enteredValue < searchedValue ) {
                lblResult.setText( R.string.strGreater );
            } else {
                lblResult.setText( R.string.strLower );
            }

            txtNumber.setText( "" );
            txtNumber.requestFocus();

        }
    };

    private void goToScoreActivity(){
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
}