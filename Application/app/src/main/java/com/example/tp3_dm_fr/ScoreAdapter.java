package com.example.tp3_dm_fr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp3_dm_fr.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {

    private int ressource;

    public ScoreAdapter(Context context, int resource, List<Score> data) {
        super(context, resource, data);
        this.ressource = resource;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(ressource, null);
        }

        Score scoreItem = getItem(position);

        ImageView paysImageView = view.findViewById(R.id.paysImageView);
        TextView scorePrenomTextView = view.findViewById(R.id.scorePrenomTextView);
        TextView scoreNomTextView = view.findViewById(R.id.scoreNomTextView);
        TextView scoreTextView = view.findViewById(R.id.scoreTextView);
        TextView scoreDateTextView = view.findViewById(R.id.scoreDateTextView);

        if(view != null){
//            if( != null){
//                paysImageView.setImageResource(scoreItem.getImageResource());
//            }
            paysImageView.setImageResource(R.drawable.spring);
            if(scorePrenomTextView != null){
                scorePrenomTextView.setText(scoreItem.getUser().getFirstName());
            }
            if(scoreNomTextView != null){
                scoreNomTextView.setText(scoreItem.getUser().getLastName());
            }
            if(scoreTextView != null){
                scoreTextView.setText(String.valueOf(scoreItem.getScore()));
            }
            if(scoreDateTextView != null){
                scoreDateTextView.setText(scoreItem.getWhen().toString());
            }
        }

        return view;
    }
}
