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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);

        //Initializing Views
        this.userNameEditText = findViewById(R.id.editText_username);
        this.passwordEditText = findViewById(R.id.editText_password);
        this.confirmPasswordEditText = findViewById(R.id.editText_password_confirm);
        this.emailEditText = findViewById(R.id.editText_email);

        FirebaseApp.initializeApp(this);
        this.mAuth = FirebaseAuth.getInstance();//on create method, connect to Firebase
    }


    public void finalizeAccountCreateButton(View view) {//when user clicks to create account

        final String email = emailEditText.getText().toString();
        final String username = userNameEditText.getText().toString();
        Log.d("danny", "the username is " + username);
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        if (!password.equals(confirmPassword)){//check if passwords match
            Toast.makeText(this, "Passwords do not match!",Toast.LENGTH_SHORT).show();
            Log.d("Danny", "passwords do not match");

    } else {

            this.mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(DEBUG_TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();//create user in auth
                                createNewUserDatabase(username, email);
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

    public void createNewUserDatabase(String username, String email){
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference table = fb.child("wishcraftapp-883ac/users");
        DatabaseReference newUser = table.push();
        newUser.child("username").setValue(username);
        newUser.child("email").setValue(email);


    }

    public void cancelButton(View view){
        Intent cancel = new Intent(this, Login.class);
        startActivity(cancel);
    }




}
