package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private final String TAG = "LoginScreen"; //for debugging
    private FirebaseAuth mAuth; //firebase user login authorization
    private EditText loginEmailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); //must be called for other fb methods to work
        setContentView(R.layout.activity_login);
        this.mAuth = FirebaseAuth.getInstance(); //grab the shared auth instance
        this.loginEmailEditText = findViewById(R.id.login_email);
        this.passwordEditText = findViewById(R.id.login_password);
        this.passwordEditText.setOnEditorActionListener(logUserInListener);
        this.loginButton = findViewById(R.id.login_button);
        this.loginEmailEditText.addTextChangedListener(loginTextWatcher);//to enable button
        this.passwordEditText.addTextChangedListener(loginTextWatcher);//to enable button
    }

    //Method used to check if user provides appropriate data and enable button
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
//                loginButton.setTextColor(getResources().getColor(R.color.white));
            } else {
                loginButton.setEnabled(false);
                loginButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
//                loginButton.setTextColor(getResources().getColor(R.color.black));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //this must be here to compile
        }
    };

    //Listener logs the user in when they hit the "ACTION DONE" key on the virtual keyboard
    private TextView.OnEditorActionListener logUserInListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE){
                loginButtonClick(findViewById(R.id.login_button));
                return true;
            }

            return false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO: go to User's profile
        //updateUI(currentUser);
    }

    //Method to move user to appropriate spot in app
    public void updateUI(FirebaseUser user) {
        Intent loginIntent = new Intent(this, Feed.class);
        startActivity(loginIntent);
        finish(); //prevents user from hitting back and logging out
    }

    //Method takes user to CreateAccount Activity
    public void createAnAccountButton(View view) {
        Intent createAccountIntent = new Intent(this, CreateProfile.class);
        startActivity(createAccountIntent);
    }

    //Method used when user clicks "log me in" button
    public void loginButtonClick(View view) {
        final String email = loginEmailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        Thread t = new Thread() {
            public void run() {
                logUserIn(email, password); //use firebase to login user
            }
        };
        t.start();
    }

    //check if user entered a valid looking email address
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //check to see if user entered a password that is not empty and at least 6 characters in length
    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 6;
    }

    //Method logs user in using firebase authentication
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
                            failAnimation();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void failAnimation(){
        YoYo.with(Techniques.Shake)
                .delay(250)
                .duration(2000)
                .playOn(loginEmailEditText);
        YoYo.with(Techniques.Shake)
                .delay(250)
                .duration(2000)
                .playOn(passwordEditText);
    }
}
