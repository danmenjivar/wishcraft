package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FirebaseApp.initializeApp(this); //onCreate method used to connect to Firebase
//        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
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
        Intent loginIntent = new Intent(this, Feed.class);
        startActivity(loginIntent);
    }
}
