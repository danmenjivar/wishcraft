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

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth; //firebase user login authorization

    private EditText loginEmailEditText;
    private EditText passwordEditText;
    private final String TAG = "LoginScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); //must be called for other fb methods to work
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance(); //grab the shared instance
        this.loginEmailEditText = (EditText) findViewById(R.id.login_email);
        this.passwordEditText = (EditText) findViewById(R.id.login_password);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //TODO: go to User's profile
//        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){
        //TODO
    }

    public void createAnAccountButton(View view) {
        //TODO connect me
        //Toast.makeText(this, "Link me to Create Account", Toast.LENGTH_SHORT).show();
        Intent createAccountIntent = new Intent(this, CreateProfile.class);
        startActivity(createAccountIntent);
    }

    public void loginButton(View view) {
        String email = loginEmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO send the user to their feed
                            //updateUI(user);
                            Intent loginIntent = new Intent(Login.this, Feed.class);
                            startActivity(loginIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });



    }
}
