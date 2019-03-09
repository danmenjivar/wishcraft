package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void createAnAccountButton(View view) {
        Toast.makeText(this, "Link me to Create Account", Toast.LENGTH_SHORT).show();
//        Intent createAccountIntent = new Intent(this, );
//        startActivity();
    }

    public void loginButton(View view) {
        Intent loginIntent = new Intent(this, Feed.class);
        startActivity(loginIntent);
    }
}
