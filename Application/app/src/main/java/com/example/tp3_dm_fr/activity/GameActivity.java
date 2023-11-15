package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.database.DatabaseManager;
import com.example.tp3_dm_fr.database.Score;
import com.example.tp3_dm_fr.database.User;

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
    private TextView gameTextView;
    private DatabaseManager databaseManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");

        gameTextView = findViewById(R.id.textView);

        //Souligne le texte du "gameTextView"
        SpannableStringBuilder texte = new SpannableStringBuilder(gameTextView.getText().toString());
        texte.setSpan(new UnderlineSpan(), 0, texte.length(), 0);
        gameTextView.setText(texte);

        txtNumber = (EditText) findViewById(R.id.txtNumber);
        btnCompare = (Button) findViewById(R.id.btnCompare);
        lblResult = (TextView) findViewById(R.id.lblResult);
        pgbScore = (ProgressBar) findViewById(R.id.pgbScore);
        lblHistory = (TextView) findViewById(R.id.lblHistory);

        btnCompare.setOnClickListener( btnCompareListener );

        init();

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        double price = 1_000_000.01;

        DateFormat dataFormatter = DateFormat.getDateTimeInstance();

        databaseManager = new DatabaseManager(this);

    }

    private void init() {
        score = 0;
        searchedValue = 80;
        //searchedValue = 1 + (int) (Math.random() * 100);
        Log.i( "DEBUG", "Searched value : " + searchedValue );

        txtNumber.setText( "" );
        pgbScore.setProgress( score );
        lblResult.setText( "" );
        lblHistory.setText( "" );

        txtNumber.requestFocus();
    }

    /**
     *
     * Affiche un message de félicitations dans une étiquette et crée une boîte de dialogue pour
     * informer l'utilisateur du score obtenu. La boîte de dialogue propose trois options :
     * recommencer le jeu, quitter l'application, ou afficher le tableau des scores.
     *
     * Pour les options de la boîte de dialogue :
     * - "Oui" relance le jeu en appelant la méthode init().
     * - "Non" termine l'activité actuelle.
     * - "Tableau des scores" lance l'activité correspondant au tableau des scores en appelant
     *   la méthode goToScoreActivity().
     */
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

    /**
     * Gestionnaire d'événement pour le bouton de comparaison.
     *
     * Déclenché lorsque le bouton est cliqué.
     * Il effectue les actions suivantes :
     * - Récupère la valeur entrée par l'utilisateur depuis le champ de texte (txtNumber).
     * - Incrémente le score et met à jour l'affichage de l'historique.
     * - Compare la valeur entrée avec la valeur recherchée.
     * - Si la valeur entrée correspond à la valeur recherchée, gère le score, affiche les félicitations,
     *   et met à jour l'historique.
     * - Si la valeur entrée est inférieure à la valeur recherchée, affiche un message indiquant
     *   que la valeur recherchée est plus grande.
     * - Si la valeur entrée est supérieure à la valeur recherchée, affiche un message indiquant
     *   que la valeur recherchée est plus petite.
     * - Réinitialise le champ de texte et demande le focus pour la prochaine entrée.
     */
    private View.OnClickListener btnCompareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("DEBUG", "Button clicked");

            String strNumber = txtNumber.getText().toString();
            if (strNumber.equals("")) return;

            int enteredValue = Integer.parseInt(strNumber);
            lblHistory.append(strNumber + "\r\n");
            pgbScore.incrementProgressBy(1);
            score++;

            Date currentDate = new Date();
            if (enteredValue == searchedValue) {
                User user = databaseManager.getUserById(userId);
                int userScore = user.getScore();
                handleScore(currentDate, user, userScore);
                congratulations();

            } else if (enteredValue < searchedValue) {
                lblResult.setText(R.string.strGreater);
            } else {
                lblResult.setText(R.string.strLower);
            }

            txtNumber.setText("");
            txtNumber.requestFocus();
        };

    };

    /**
     * Gère la mise à jour du score de l'utilisateur dans la base de données.
     *
     * Compare le score actuel avec le score enregistré pour l'utilisateur et
     * effectue les actions suivantes :
     * - Si le score actuel est plus bas que le score enregistré ou si le score enregistré est 0,
     *   met à jour le score de l'utilisateur dans la base de données.
     * - Si un score existe pour l'utilisateur, met à jour ce score avec le nouveau score et
     *   la date actuelle. Sinon, crée un nouveau score pour l'utilisateur.
     *
     * @param currentDate La date actuelle.
     * @param user L'objet utilisateur dont le score doit être géré.
     * @param userScore Le score actuel de l'utilisateur.
     */
    private void handleScore(Date currentDate, User user, int userScore) {
        if (userScore == 0 || userScore > score) {
            databaseManager.updateUserScore(user, score);
            Score scoreToUpdate = databaseManager.getScoreByUserId(userId);
            if (scoreToUpdate == null) {
                databaseManager.insertScore(new Score(user, score, currentDate));
            } else {
                databaseManager.updateScore(user, scoreToUpdate, score, currentDate);
            }
            databaseManager.close();
        }
    }

    /**
     * Lance l'activité du tableau des scores.
     *
     * Cette méthode crée un nouvel intent pour démarrer l'activité ScoreActivity et
     * ajoute l'identifiant de l'utilisateur en tant qu'extra dans l'intent. Ensuite, elle
     * lance l'activité correspondante.
     */
    private void goToScoreActivity(){
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
}