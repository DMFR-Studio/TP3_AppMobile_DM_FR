package com.example.tp3_dm_fr.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.database.Score;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScoreAdapter extends ArrayAdapter<Score> {

    private int ressource;
    private Context context;

    public ScoreAdapter(Context context, int resource, List<Score> data) {
        super(context, resource, data);
        this.ressource = resource;
        this.context = context;
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
            if(paysImageView != null){
                Resources res = context.getResources();
                String drawableName = scoreItem.getUser().getCountry();
                int resId = res.getIdentifier(drawableName,"drawable", context.getPackageName());
                paysImageView.setImageResource(resId);
            }

            if(scorePrenomTextView != null){
                scorePrenomTextView.setText(scoreItem.getUser().getFirstName());
            }
            if(scoreNomTextView != null){
                scoreNomTextView.setText(scoreItem.getUser().getLastName());
            }
            if(scoreTextView != null){
                scoreTextView.setText(String.valueOf(scoreItem.getScore()));
            }
            if (scoreDateTextView != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = dateFormat.format(scoreItem.getWhen());
                scoreDateTextView.setText(formattedDate);
            }
        }

        return view;
    }
}
