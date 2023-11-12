package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.database.DatabaseManager;
import com.example.tp3_dm_fr.database.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUserActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button connexionButton;
    private Button newAccountButton;
    private TextView title;
    private TextView errorMessage;
    private DatabaseManager databaseManager;
    private boolean passwordIsValid;
    private boolean emailIsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_user);

        passwordIsValid = false;
        emailIsValid = false;

        title = findViewById(R.id.title);

        SpannableStringBuilder texte = new SpannableStringBuilder(title.getText().toString());
        texte.setSpan(new StyleSpan(Typeface.BOLD), 0, texte.length(), 0);
        title.setText(texte);

        initEditField();
        databaseManager = new DatabaseManager(this);
    }

    private void initEditField() {
        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        connexionButton = findViewById(R.id.connexionButton);
        connexionButton.setEnabled(false);
        newAccountButton = findViewById(R.id.newAccountButton);
        errorMessage = findViewById(R.id.errorMessageTextView);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                connexionButton.setEnabled(isEmailAndPasswordValid());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorMessage.setText("");
                if(!isEmailValid()){
                    emailInput.setError(getText(R.string.strCourrielInvalide));
                    emailIsValid = false;
                } else {
                    emailIsValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                connexionButton.setEnabled(isEmailAndPasswordValid());
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                connexionButton.setEnabled(isEmailAndPasswordValid());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                errorMessage.setText("");
                if(!isPasswordValid()){
                    passwordInput.setError(getText(R.string.strMotDePasse));
                    passwordIsValid = false;
                } else {
                    passwordIsValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                connexionButton.setEnabled(isEmailAndPasswordValid());
            }
        });


    }

    private boolean isEmailAndPasswordValid() {
        return emailIsValid && passwordIsValid;
    }

    private boolean isPasswordValid() {
        return passwordInput.getText().toString().length() > 5;
    }

    private boolean isEmailValid() {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailInput.getText().toString());
        return matcher.find();
    }
    public void connectionOnClick(View view) {
        User user = databaseManager.getUserByEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString());
        if(user != null){
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("userId",user.getIdUser());
            startActivity(intent);
        } else {
            errorMessage.setText(getText(R.string.emailMotDePasseInvalide));
        }
    }
    public void newAccountOnClick(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }
}