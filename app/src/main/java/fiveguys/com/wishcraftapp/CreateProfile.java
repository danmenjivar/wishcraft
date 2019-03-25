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

    private FirebaseAuth mAuth;

    private final String DEBUG_TAG = "ACCOUNT CREATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        FirebaseApp.initializeApp(this);//on create method, connect to Firebase
    }

    public void finalizeAccountCreateButton(View view) {//when user clicks to create account
        mAuth = FirebaseAuth.getInstance();

        EditText usernameEdit = (EditText) findViewById(R.id.editText_username);
        EditText passwordEdit = (EditText) findViewById(R.id.editText_password);
        EditText confirmPasswordEdit = (EditText) findViewById(R.id.editText_password_confirm);
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();


        if (!password.equals(confirmPasswordEdit.getText().toString())){//check if passwords match
            Toast.makeText(this, "Passwords do not match",Toast.LENGTH_SHORT);

    } else {

            mAuth.createUserWithEmailAndPassword(username, password)
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


        Intent createAccount = new Intent(this, Feed.class);
        startActivity(createAccount);

    }

    public void cancelButton(View view){
        Intent cancel = new Intent(this, Login.class);
        startActivity(cancel);
    }




}
