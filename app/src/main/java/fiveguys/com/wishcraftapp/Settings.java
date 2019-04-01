package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }


    /**
     * When user clicks to save changes to their bio or username
     * @param view
     */
    public void saveChangesButton(View view) {

        Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();
    }

    /**
     * When user clicks log-out
     * @param view
     */
    public void backToLogin(View view) {
        Intent createAccountIntent = new Intent(this, Login.class);
        startActivity(createAccountIntent);
    }


}
