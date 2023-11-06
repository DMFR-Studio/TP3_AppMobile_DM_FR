package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.adapter.ScoreAdapter;
import com.example.tp3_dm_fr.database.Score;
import com.example.tp3_dm_fr.database.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private ListView scoreListView;
    private TextView scoreTableauTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreTableauTextView = findViewById(R.id.scoreTableauTextView);

        SpannableStringBuilder texte = new SpannableStringBuilder(scoreTableauTextView.getText().toString());
        texte.setSpan(new RelativeSizeSpan(2.0f), 0, texte.length(), 0);
        scoreTableauTextView.setText(texte);

        //TODO remplacer par data de la bd et trier en ordre croissant selon le score
        List<Score> scoreItems = new ArrayList<>();
        scoreItems.add(new Score(new User("John", "Doe", "exemple@gmail.com", "12345", "spring"), 100, new Date(2023-01-15)));
        scoreItems.add(new Score(new User("Alice", "Doe", "exemple2@gmail.com", "12345", "spring"), 50, new Date(2023-01-16)));

        scoreListView = (ListView) findViewById(R.id.scoreListView);
        ScoreAdapter adapter = new ScoreAdapter(this, R.layout.score_listview_items, scoreItems);
        scoreListView.setAdapter(adapter);
    }

    public void returnToGameActivity(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void returnToAuthUserActivity(View view){
        Intent intent = new Intent(this, AuthUserActivity.class);
        startActivity(intent);
    }
}