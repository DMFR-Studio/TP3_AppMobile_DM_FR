package com.example.tp3_dm_fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUserActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button connexionButton;
    private Button newAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_user);
        initEditField();
    }

    private void initEditField() {
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        connexionButton = findViewById(R.id.connexionButton);
        connexionButton.setEnabled(false);
        newAccountButton = findViewById(R.id.newAccountButton);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidate(emailInput.getText().toString())){
                    emailInput.setError("Courriel invalide");
                } else {
                    connexionButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordInput.getText().toString().length() < 5){
                    passwordInput.setError("Le mot de passe doit contenir plus de 5 caractères");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private boolean isValidate(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
    public void connectionOnClick(View view) {
        //TODO vérifier que compte est en BD
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void newAccountOnClick(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }
}