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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tp3_dm_fr.R;

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

    /**
     * Utilisée pour personnaliser l'apparence des éléments dans la liste déroulante
     * du sélecteur de pays. Elle fait la mise en page spécifiée pour chaque élément de la
     * liste déroulante et configure le texte et l'image en fonction des données du pays à la position spécifiée.
     *
     * @param position La position de l'élément dans la liste déroulante.
     * @param convertView La vue à réutiliser
     * @param parent Le groupe parent dans lequel la vue sera attachée.
     * @return La vue personnalisée pour l'élément à la position spécifiée dans la liste déroulante.
     */
    public View getCustomView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.countries_dropdown_items, parent, false);

        TextView country = row.findViewById(R.id.countryTextView);
        ImageView flag = row.findViewById(R.id.countryImageView);        ;

        Resources res = context.getResources();
        String drawableName = countriesList.get(position).toLowerCase();
        int resId = res.getIdentifier(drawableName,"drawable", context.getPackageName());

        flag.setImageResource(resId);
        country.setText(countriesList.get(position));

        return row;
    }

}
