package com.example.tp3_dm_fr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        creerCompteButton = findViewById(R.id.creerCompteButton);
        prenomEditText = findViewById(R.id.prenomEditText);
        nomEditText = findViewById(R.id.nomEditText);
        emailEditText = findViewById(R.id.courrielEditText);
        passwordEditText = findViewById(R.id.motDePasseEditTExt);
        paysSpinner = findViewById(R.id.paysSpinner);
        createPaysSpinner();
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
//                paysChoisie = (String) paysSpinnerAdapter.getItem(position);
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
            prenomEditText.setError("Votre prénom doit contenir au moins 3 caractères");
        } else {
            prenomValid = true;
        }

        return prenomValid;
    }

    private boolean isNomValid(){
        boolean nomValid = false;
        if(nomEditText.getText().toString().length() < 3){
            nomEditText.setError("Votre nom doit contenir au moins 3 caractères");
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
            emailEditText.setError("Courriel invalide");
        } else {
            emailValid = true;
        }

        return emailValid;
    }

    private boolean isPasswordValid(){
        boolean passwordValid = false;
        if(passwordEditText.getText().toString().length() < 3){
            passwordEditText.setError("Votre mot de passe doit contenir plus de 5 caractères");
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
            //TODO ajouter en BD
            createAlertNewAccountConfirmation();
        }
    }

    private void createAlertNewAccountConfirmation(){
        AlertDialog.Builder alert = createAlertWindow("Excellent", "Le compte a bien été créé !");
        alert.create().show();
    }

    private AlertDialog.Builder createAlertWindow(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", (dialog, which) -> returnToAuthActivity());

        return alert;
    }

    private void returnToAuthActivity(){
        Intent intent = new Intent(this, AuthUserActivity.class);
        startActivity(intent);
    }
}