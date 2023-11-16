package com.example.tp3_dm_fr.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tp3_dm_fr.R;
import com.example.tp3_dm_fr.adapter.SpinnerAdapter;
import com.example.tp3_dm_fr.database.DatabaseManager;
import com.example.tp3_dm_fr.database.User;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NewAccountActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 123;
    private Button creerCompteButton;
    private EditText prenomEditText;
    private EditText nomEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner paysSpinner;
    private String paysChoisie;
    private TextView inscriptionTextView;
    private String test = "";
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        inscriptionTextView = findViewById(R.id.inscriptionTextView);

        //Mets le texte du "InscriptionTextView" en italique
        SpannableStringBuilder texte = new SpannableStringBuilder(inscriptionTextView.getText().toString());
        texte.setSpan(new StyleSpan(Typeface.ITALIC), 0, texte.length(), 0);
        inscriptionTextView.setText(texte);

        initField();
        createPaysSpinner();

        databaseManager = new DatabaseManager(this);
    }

    /**
     * Initialise les champs de saisie et les éléments graphiques liés à la création de compte.
     *
     * Les écouteurs de texte (TextWatchers) pour les champs de saisie effectuent les actions suivantes :
     * - Appellent les méthodes de validation appropriées (isPrenomValid, isNomValid, isEmailValid,
     *   isPasswordValid) en fonction des modifications de texte.
     */
    private void initField() {
        paysSpinner = findViewById(R.id.paysSpinner);
        creerCompteButton = findViewById(R.id.creerCompteButton);
        emailEditText = findViewById(R.id.courrielEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        nomEditText = findViewById(R.id.nomEditText);
        passwordEditText = findViewById(R.id.motDePasseEditTExt);
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

    }

    private void addUserAPI(String firstName, String lastName, String email, String password, String country) {
        String url = "https://tp-3-app-mobile-rest.vercel.app:8081/addUser";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("first_name", firstName);
            postData.put("last_name", lastName);
            postData.put("email", email);
            postData.put("password", password);
            postData.put("country", country);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    // Handle successful response (status 200)
                    Log.i("api", "user added successfully");
                },
                error -> {
                    // Handle error
                    Log.e("api", "user credentials invalid");
                });

        queue.add(jsonObjectRequest);
    }

    /**
     * Crée et configure le sélecteur de pays (Spinner).
     *
     * Les actions effectuées par cette méthode :
     * - Crée une liste de pays à partir des ressources.
     * - Initialise un adaptateur personnalisé (SpinnerAdapter) avec la liste de pays.
     * - Configure l'adaptateur pour utiliser un affichage de liste déroulante simple.
     * - Associe l'adaptateur au sélecteur de pays (paysSpinner).
     * - Définit un gestionnaire d'événements pour capturer la sélection d'un pays.
     * - Met à jour la variable paysChoisie avec le pays sélectionné.
     */
    private void createPaysSpinner() {
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

    /**
     * Vérifie la validité du prénom saisi.
     *
     * Évalue la longueur du prénom saisi. Si la longueur est inférieure à 3 caractères,
     * la méthode affiche un message d'erreur associé au champ de saisie du prénom.
     * Sinon, la méthode considère le prénom comme valide.
     *
     * @return true si le prénom est valide, sinon false.
     */
    private boolean isPrenomValid() {
        boolean prenomValid = false;
        if (prenomEditText.getText().toString().length() < 3) {
            prenomEditText.setError(getText(R.string.strPrenom));
        } else {
            prenomValid = true;
        }

        return prenomValid;
    }

    /**
     * Vérifie la validité du nom saisi.
     *
     * Évalue la longueur du nom saisi. Si la longueur est inférieure à 3 caractères,
     * la méthode affiche un message d'erreur associé au champ de saisie du nom.
     * Sinon, la méthode considère le nom comme valide.
     *
     * @return true si le nom est valide, sinon false.
     */
    private boolean isNomValid() {
        boolean nomValid = false;
        if (nomEditText.getText().toString().length() < 3) {
            nomEditText.setError(getText(R.string.strNom));
        } else {
            nomValid = true;
        }

        return nomValid;
    }

    /**
     * Vérifie si l'adresse e-mail est valide.
     *
     * Utilise une regex pour évaluer la validité de l'adresse e-mail saisie.
     *
     * @return true si l'adresse e-mail est valide, sinon false.
     */
    private boolean isEmailValid() {
        boolean emailValid = false;
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailEditText.getText().toString());

        if (!matcher.find()) {
            emailEditText.setError(getText(R.string.strCourrielInvalide));
        } else {
            emailValid = true;
        }

        return emailValid;
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
        boolean passwordValid = false;
        if (passwordEditText.getText().toString().length() < 6) {
            passwordEditText.setError(getText(R.string.strMotDePasse));
        } else {
            passwordValid = true;
        }

        return passwordValid;
    }

    /**
     * Ajoute un nouveau compte utilisateur à la base de données.
     *
     * Cérifie la validité des champs de saisie du prénom, du nom, de l'e-mail et du mot de passe.
     * Si tous les champs sont valides, elle insère un nouvel utilisateur dans la base de
     * données avec les informations saisies. Elle ferme ensuite la connexion à la base de données et affiche
     * une alerte de confirmation de création de compte.
     *
     * @param view La vue associée à l'événement de clic
     */
    public void addNewAccountToBD(View view) {
        boolean prenomValid = isPrenomValid();
        boolean nomValid = isNomValid();
        boolean emailValid = isEmailValid();
        boolean passwordValid = isPasswordValid();

        if (prenomValid && nomValid && emailValid && passwordValid) {
            databaseManager.insertUser(new User(
                    prenomEditText.getText().toString(),
                    nomEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    paysChoisie.toLowerCase()));
            databaseManager.close();
            addUserAPI(prenomEditText.getText().toString(),
                    nomEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    paysChoisie.toLowerCase());
            createAlertNewAccountConfirmation();
        }

    }

    /**
     * Crée et affiche une alerte de confirmation de création de compte avec un texte personnalisé.
     *
     * Génère un texte d'alerte avec un fond de couleur verte pour indiquer
     * la réussite de la création du compte. Elle utilise un constructeur d'alerte
     * avec un texte spécifique. Puis, elle affiche l'alerte générée.
     */
    private void createAlertNewAccountConfirmation() {
        String excellentStr = getString(R.string.strExcellent);
        SpannableStringBuilder texte = new SpannableStringBuilder(excellentStr);
        BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.GREEN);

        // Application de la couleur verte au texte
        texte.setSpan(colorSpan, 0, texte.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder alert = createAlertWindow(texte, getString(R.string.strCompteCree));
        alert.create().show();
    }

    /**
     * Génère un constructeur d'alerte avec un titre spécifique sous forme de texte enrichi
     * et un message donné. Elle utilise également une mise en page personnalisée pour personnaliser l'apparence de l'alerte.
     * Un bouton "OK" est ajouté à l'alerte avec une action associée pour retourner à l'activité d'authentification.
     *
     * @param title Le titre de l'alerte sous forme de texte enrichi.
     * @param message Le message de l'alerte.
     * @return Le constructeur d'alerte configuré.
     */
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

    /**
     * Retourne à l'activité d'authentification utilisateur.
     *
     */
    private void returnToAuthActivity() {
        Intent intent = new Intent(this, AuthUserActivity.class);
        startActivity(intent);
    }
}