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

        //Mets le texte du "Title" en gras
        SpannableStringBuilder texte = new SpannableStringBuilder(title.getText().toString());
        texte.setSpan(new StyleSpan(Typeface.BOLD), 0, texte.length(), 0);
        title.setText(texte);

        initField();
        databaseManager = new DatabaseManager(this);
    }

    /**
     * Initialise les champs de saisie et les boutons pour l'adresse e-mail et le mot de passe,
     * ainsi que les écouteurs nécessaires. Cette méthode configure des écouteurs de texte
     * (TextWatchers) pour les champs de saisie de l'e-mail et du mot de passe afin de gérer la
     * validation de la saisie de l'utilisateur et d'activer/désactiver en conséquence le bouton de
     * connexion.
     *
     */
    private void initField() {
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

    /**
     * Vérifie si l'adresse e-mail et le mot de passe sont valides.
     *
     * Cette méthode renvoie true si l'e-mail et le mot de passe sont considérés comme valides
     *
     * @return true si l'e-mail et le mot de passe sont valides, sinon false.
     */
    private boolean isEmailAndPasswordValid() {
        return emailIsValid && passwordIsValid;
    }

    /**
     * Vérifie si le mot de passe est valide.
     *
     * Évalue la longueur du mot de passe saisi et renvoie true si sa longueur
     * est supérieure à 5 caractères
     *
     * @return true si le mot de passe est valide, sinon false.
     */
    private boolean isPasswordValid() {
        return passwordInput.getText().toString().length() > 5;
    }

    /**
     * Vérifie si l'adresse e-mail est valide.
     *
     * Utilise une regex pour évaluer la validité de l'adresse e-mail saisie.
     *
     * @return true si l'adresse e-mail est valide, sinon false.
     */
    private boolean isEmailValid() {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailInput.getText().toString());
        return matcher.find();
    }

    /**
     * Gère l'événement de clic sur le bouton de connexion.
     *
     * Récupère les données d'utilisateur correspondant à l'e-mail et au mot de passe saisis
     * à partir de la base de données. Si un utilisateur est trouvé, une nouvelle activité de jeu
     * est lancée avec l'identifiant de l'utilisateur en tant qu'extra dans l'intent.
     * Sinon, un message d'erreur est affiché indiquant une combinaison e-mail/mot de passe invalide.
     *
     * @param view La vue associée à l'événement de clic
     */
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

    /**
     * Gère l'événement de clic sur le bouton de création de nouveau compte.
     *
     * Crée un nouvel intent pour démarrer l'activité NewAccountActivity et lance cette activité.
     *
     * @param view La vue associée à l'événement de clic (le bouton de création de nouveau compte).
     */
    public void newAccountOnClick(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }
}