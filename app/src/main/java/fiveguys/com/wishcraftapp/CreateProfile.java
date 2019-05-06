package fiveguys.com.wishcraftapp;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private Boolean hasClicked;

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
        confirmPasswordEditText.setOnEditorActionListener(createAccountListener);
        confirmButton = findViewById(R.id.createAccount_button);
        hasClicked = false;

        FirebaseApp.initializeApp(this);
        this.mAuth = FirebaseAuth.getInstance();//on create method, connect to Firebase
    }

    //Method ensures button is enabled when good input is provided
    private TextWatcher confirmButtonEnable = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //empty, must exist for compilation purposes
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Enable button when text is appropriate
            String usernameInput = userNameEditText.getText().toString();
            String emailInput = emailEditText.getText().toString();
            String passwordInput = passwordEditText.getText().toString();
            String passwordConfirmInput = confirmPasswordEditText.getText().toString();

            if (isValidEmail(emailInput) && isValidPassword(passwordInput) &&
                    isValidPassword(passwordConfirmInput) && !usernameInput.isEmpty()
            && !hasClicked){
                confirmButton.setEnabled(true);
                confirmButton.setBackgroundColor(getResources().getColor(R.color.wc_logo_pink));
            } else {
                confirmButton.setEnabled(false);
                confirmButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //empty, must exist for compilation purposes
        }
    };

    //Listener logs the user in when they hit the "ACTION DONE" key on the virtual keyboard
    private TextView.OnEditorActionListener createAccountListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_GO){
                onCreateAccountButtonClick(confirmButton);
                return true;
            }

            return false;
        }
    };

    public void onCreateAccountButtonClick(View view) {//when user clicks to create account
        hasClicked = true;
        confirmButton.setEnabled(false);
        hideKeyboard();
        final String email = emailEditText.getText().toString();
        final String username = userNameEditText.getText().toString();
        //Log.d(DEBUG_TAG, "the username is " + username);
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {//check if passwords match
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            //Log.d("Danny", "passwords do not match");
        } else {
            createUserInFirebase(email, password, username);
        }
        hasClicked = false;
        confirmButton.setEnabled(true);
    }

    //Method makes new firebase auth instance
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
                            instantiateEmptyWishlist(email);
                            instantiateFriendslist(email);
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

    //Makes a new entry in the wishlists table in the real time firebase database
    private void instantiateEmptyWishlist(String email) {
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference table = fb.child("userWishlist");
        DatabaseReference newList = table.push();
        newList.child("email").setValue(email);
        newList.child("wishlist").setValue(email.hashCode());
    }

    //Makes a new entry in the friendslist table in the real time firebase database
    private void instantiateFriendslist(String email) {
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference table = fb.child("userFriendslist");
        DatabaseReference newList = table.push();
        newList.child("email").setValue(email);
        newList.child("friendslist").setValue(email.hashCode());
    }

    //Makes new entry in the users table in the real time firebase database
    private void addNewUserToFirebaseDatabase(String username, String email) {
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference table = fb.child("users");
        DatabaseReference newUser = table.push();
        newUser.child("username").setValue(username);
        newUser.child("email").setValue(email);
        newUser.child("bio").setValue("bio");
        newUser.child("userId").setValue(mAuth.getCurrentUser().getUid());
    }

    //Takes the user back to the login screen
    public void cancelButtonClick(View view) {
        super.finish();//go back, pop this activity from the stack
    }

    //Returns true if user types an email looking string
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //Returns true if user types a non empty password that is at least 6 characters long
    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 6;
    }

    //Method used to move user to next screen after successful profile creation
    public void updateUI(FirebaseUser user) {
        Intent loginIntent = new Intent(this, FeedButtons.class);
        startActivity(loginIntent);
        finish(); //prevents user from hitting back and logging out
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
