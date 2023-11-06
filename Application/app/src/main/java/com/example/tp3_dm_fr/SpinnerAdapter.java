package com.example.tp3_dm_fr;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    List<String> countriesList;

    public SpinnerAdapter(Context context, List<String> countriesList){
        super(context, R.layout.activity_new_account, countriesList);
        this.context = context;
        this.countriesList = countriesList;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.countries_dropdown_items, parent, false);

        TextView country = row.findViewById(R.id.countryTextView);
        ImageView flag = row.findViewById(R.id.countryImageView);        ;

        Resources res = context.getResources();
        String drawableName = countriesList.get(position).toLowerCase();
        int resId = res.getIdentifier(drawableName,"drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resId);

        country.setText(countriesList.get(position));
        flag.setImageDrawable(drawable);

        return row;
    }

}
