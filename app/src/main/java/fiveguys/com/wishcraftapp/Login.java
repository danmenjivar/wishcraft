package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth; //firebase user login authorization

    private EditText loginEmailEditText;
    private EditText passwordEditText;
    private final String TAG = "LoginScreen";

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); //must be called for other fb methods to work
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance(); //grab the shared instance
        this.loginEmailEditText = (EditText) findViewById(R.id.login_email);
        this.passwordEditText = (EditText) findViewById(R.id.login_password);
        this.loginButton = findViewById(R.id.login_button);
        loginEmailEditText.addTextChangedListener(loginTextWatcher);
        passwordEditText.addTextChangedListener(loginTextWatcher);
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = loginEmailEditText.getText().toString();
            String passwordInput = passwordEditText.getText().toString();

            if (isValidEmail(emailInput) && isValidPassword(passwordInput)) {
                loginButton.setEnabled(true);
                loginButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO: go to User's profile
//        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user) {
        //TODO
        Intent loginIntent = new Intent(this, Feed.class);
        startActivity(loginIntent);
    }

    public void createAnAccountButton(View view) {
        Intent createAccountIntent = new Intent(this, CreateProfile.class);
        startActivity(createAccountIntent);
    }

    public void loginButtonClick(View view) {
        String email = loginEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        logUserIn(email, password);
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 6;
    }

    private void logUserIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);// send the user to their feed
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
