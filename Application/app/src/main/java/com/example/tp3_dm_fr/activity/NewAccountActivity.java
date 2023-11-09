package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.adapter.SpinnerAdapter;
import com.example.tp3_dm_fr.database.DatabaseManager;
import com.example.tp3_dm_fr.database.User;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewAccountActivity extends AppCompatActivity {

    private Button creerCompteButton;
    private EditText prenomEditText;
    private EditText nomEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner paysSpinner;
    private String paysChoisie;
    private TextView inscriptionTextView;

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        inscriptionTextView = findViewById(R.id.inscriptionTextView);

        SpannableStringBuilder texte = new SpannableStringBuilder(inscriptionTextView.getText().toString());
        texte.setSpan(new StyleSpan(Typeface.ITALIC), 0, texte.length(), 0);
        inscriptionTextView.setText(texte);

        creerCompteButton = findViewById(R.id.creerCompteButton);
        prenomEditText = findViewById(R.id.prenomEditText);
        prenomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPrenomValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nomEditText = findViewById(R.id.nomEditText);
        nomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isNomValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEditText = findViewById(R.id.courrielEditText);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmailValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText = findViewById(R.id.motDePasseEditTExt);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPasswordValid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        paysSpinner = findViewById(R.id.paysSpinner);
        createPaysSpinner();

        databaseManager = new DatabaseManager( this );
    }

    private void createPaysSpinner(){

        List<String> countyList = Arrays.asList(getResources().getStringArray(R.array.countries_array));
        SpinnerAdapter paysSpinnerAdapter = new SpinnerAdapter(
                this,
                countyList
        );
        paysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paysSpinner.setAdapter(paysSpinnerAdapter);
        paysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                paysChoisie = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Gestion de l'événement lorsque rien n'est sélectionné
            }
        });
    }

    private boolean isPrenomValid(){
        boolean prenomValid = false;
        if(prenomEditText.getText().toString().length() < 3){
            prenomEditText.setError(getText(R.string.strPrenom));
        } else {
            prenomValid = true;
        }

        return prenomValid;
    }

    private boolean isNomValid(){
        boolean nomValid = false;
        if(nomEditText.getText().toString().length() < 3){
            nomEditText.setError(getText(R.string.strNom));
        } else {
            nomValid = true;
        }

        return nomValid;
    }

    private boolean isEmailValid(){
        boolean emailValid = false;
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailEditText.getText().toString());

        if(!matcher.find()){
            emailEditText.setError(getText(R.string.strCourrielInvalide));
        } else {
            emailValid = true;
        }

        return emailValid;
    }

    private boolean isPasswordValid(){
        boolean passwordValid = false;
        if(passwordEditText.getText().toString().length() < 3){
            passwordEditText.setError(getText(R.string.strMotDePasse));
        } else {
            passwordValid = true;
        }

        return passwordValid;
    }

    public void addNewAccountToBD(View view){
        boolean prenomValid = isPrenomValid();
        boolean nomValid = isNomValid();
        boolean emailValid = isEmailValid();
        boolean passwordValid = isPasswordValid();

        if(prenomValid && nomValid && emailValid && passwordValid){
            databaseManager.insertUser(new User(
                    prenomEditText.getText().toString(),
                    nomEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    paysChoisie.toLowerCase()));
            databaseManager.close();
            createAlertNewAccountConfirmation();
        }
    }

    private void createAlertNewAccountConfirmation() {
        String excellentStr = getString(R.string.strExcellent);
        SpannableStringBuilder texte = new SpannableStringBuilder(excellentStr);
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.GREEN);

        // Apply the colorSpan to the entire text
        texte.setSpan(colorSpan, 0, texte.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder alert = createAlertWindow(texte, getString(R.string.strCompteCree));
        alert.create().show();
    }



    private AlertDialog.Builder createAlertWindow(SpannableStringBuilder title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);
        TextView alertMessageTextView = dialogView.findViewById(R.id.alertMessageTextView);

        alertMessageTextView.setText(message);

        builder.setView(dialogView);
        builder.setTitle(title);
        builder.setPositiveButton(getText(R.string.strOk), (dialog, which) -> returnToAuthActivity());

        return builder;
    }


    private void returnToAuthActivity(){
        Intent intent = new Intent(this, AuthUserActivity.class);
        startActivity(intent);
    }
}