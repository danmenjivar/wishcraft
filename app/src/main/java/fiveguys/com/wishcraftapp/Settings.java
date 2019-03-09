package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void saveChangesButton(View view) {
        Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();
    }

    public void backToLogin(View view) {
        Intent createAccountIntent = new Intent(this, Login.class);
        startActivity(createAccountIntent);
    }


}
