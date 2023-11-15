package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.adapter.ScoreAdapter;
import com.example.tp3_dm_fr.database.DatabaseManager;
import com.example.tp3_dm_fr.database.Score;
import com.example.tp3_dm_fr.database.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private ListView scoreListView;
    private TextView scoreTableauTextView;
    private DatabaseManager databaseManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getInt("userId");

        scoreTableauTextView = findViewById(R.id.scoreTableauTextView);

        //Mets le texte du "scoreTableauTextView" deux fois plus gros
        SpannableStringBuilder texte = new SpannableStringBuilder(scoreTableauTextView.getText().toString());
        texte.setSpan(new RelativeSizeSpan(2.0f), 0, texte.length(), 0);
        scoreTableauTextView.setText(texte);

        databaseManager = new DatabaseManager(this);

        List<Score> scores = databaseManager.readScores();
        //Tri les scores en ordre croissant des scores
        Collections.sort(scores, Comparator.comparingInt(Score::getScore));

        databaseManager.close();

        scoreListView = findViewById(R.id.scoreListView);
        ScoreAdapter adapter = new ScoreAdapter(this, R.layout.score_listview_items, scores);
        scoreListView.setAdapter(adapter);
    }

    /**
     * Retourne au jeu
     *
     * @param view La vue associée à l'événement de clic
     */
    public void returnToGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    /**
     * Retourne à la page de connexion
     *
     * @param view La vue associée à l'événement de clic
     */
    public void returnToAuthUserActivity(View view) {
        Intent intent = new Intent(this, AuthUserActivity.class);
        startActivity(intent);
    }
}
