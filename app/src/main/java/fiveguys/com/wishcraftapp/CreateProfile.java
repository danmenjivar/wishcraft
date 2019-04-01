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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateProfile extends AppCompatActivity {

    private final String DEBUG_TAG = "EmailPassword";

    private FirebaseAuth mAuth; //initializing user authentication using firebase
    //Views
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;

    private Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);

        //Initializing Views
        this.userNameEditText = findViewById(R.id.editText_username);
        userNameEditText.addTextChangedListener(confirmButtonEnable);
        this.passwordEditText = findViewById(R.id.editText_password);
        passwordEditText.addTextChangedListener(confirmButtonEnable);
        this.confirmPasswordEditText = findViewById(R.id.editText_password_confirm);
        confirmPasswordEditText.addTextChangedListener(confirmButtonEnable);
        this.emailEditText = findViewById(R.id.editText_email);
        emailEditText.addTextChangedListener(confirmButtonEnable);
        confirmButton = findViewById(R.id.createAccount_button);


        FirebaseApp.initializeApp(this);
        this.mAuth = FirebaseAuth.getInstance();//on create method, connect to Firebase
    }

    private TextWatcher confirmButtonEnable = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Enable button when text is appropriate
            String usernameInput = userNameEditText.getText().toString();
            String emailInput = emailEditText.getText().toString();
            String passwordInput = passwordEditText.getText().toString();
            String passwordConfirmInput = confirmPasswordEditText.getText().toString();

            if (isValidEmail(emailInput) && isValidPassword(passwordInput) &&
                    isValidPassword(passwordConfirmInput) && !usernameInput.isEmpty()){
                confirmButton.setEnabled(true);
                confirmButton.setBackgroundColor(getResources().getColor(R.color.wc_logo_pink));
            } else {
                confirmButton.setEnabled(false);
                confirmButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void OnCreateAccountButtonClick(View view) {//when user clicks to create account

        final String email = emailEditText.getText().toString();
        final String username = userNameEditText.getText().toString();
        Log.d(DEBUG_TAG, "the username is " + username);
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        if (!password.equals(confirmPassword)) {//check if passwords match
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            Log.d("Danny", "passwords do not match");

        } else {
            createUserInFirebase(email, password, username);
        }


    }

    private void createUserInFirebase(final String email, String password, final String username) {
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(DEBUG_TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();//create user in auth
                            addNewUserToFirebaseDatabase(username, email);
                            //TODO updateUI
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(DEBUG_TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateProfile.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addNewUserToFirebaseDatabase(String username, String email) {
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference table = fb.child("users");
        DatabaseReference newUser = table.push();
        newUser.child("username").setValue(username);
        newUser.child("email").setValue(email);
    }

    public void cancelButtonClick(View view) {
        super.finish();//go back, pop this activity from the stack
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 6;
    }

    public void updateUI(FirebaseUser user) {
        //TODO

        Intent loginIntent = new Intent(this, Settings.class);
        loginIntent.putExtra("firebaseUser", user);
        startActivity(loginIntent);
        finish(); //prevents user from hitting back and logging out
    }

}
