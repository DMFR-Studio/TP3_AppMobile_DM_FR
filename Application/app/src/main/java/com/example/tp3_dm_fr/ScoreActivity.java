package com.example.tp3_dm_fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private ListView scoreListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Sample data for your ListView
        List<Score> scoreItems = new ArrayList<>();

        scoreItems.add(new Score(new User("John", "Doe", "exemple@gmail.com"), 100, new Date(2023-01-15)));
        scoreItems.add(new Score(new User("Alice", "Doe", "exemple2@gmail.com"), 50, new Date(2023-01-16)));

        // Initialize the ListView and set the Adapter
        scoreListView = (ListView) findViewById(R.id.scoreListView);
        ScoreAdapter adapter = new ScoreAdapter(this, R.layout.score_listview_items, scoreItems); // Use the correct layout resource
        scoreListView.setAdapter(adapter);

    }

    public void returnToPreviousView(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}