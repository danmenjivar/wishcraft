package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class CreateProfile extends AppCompatActivity {

    private FirebaseAuth mAuth; //

    private final String DEBUG_TAG = "EmailPassword";

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText emailEditText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);

        //Views
        this.userNameEditText = findViewById(R.id.editText_username);
        this.passwordEditText = findViewById(R.id.editText_password);
        this.confirmPasswordEditText = findViewById(R.id.editText_password_confirm);
        this.emailEditText = findViewById(R.id.editText_email);

        FirebaseApp.initializeApp(this);
        this.mAuth = FirebaseAuth.getInstance();//on create method, connect to Firebase
    }

    public void finalizeAccountCreateButton(View view) {//when user clicks to create account

        String email = emailEditText.getText().toString();
        String username = userNameEditText.getText().toString();
        Log.d("danny", "the username is " + username);
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        if (!password.equals(confirmPassword)){//check if passwords match
            Toast.makeText(this, "Passwords do not match!",Toast.LENGTH_SHORT);
            Log.d("Danny", "passwords do not match");

    } else {

            this.mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(DEBUG_TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //TODO updateUI

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(DEBUG_TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(CreateProfile.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //TODO updateUI
                            }

                        }
                    });
        }



    }

    public void cancelButton(View view){
        Intent cancel = new Intent(this, Login.class);
        startActivity(cancel);
    }




}
