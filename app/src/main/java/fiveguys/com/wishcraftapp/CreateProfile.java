package fiveguys.com.wishcraftapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishcraftcreateprofile);
    }

    public void finalizeAccountCreateButton(View view){
        //Intent createAccount = new Intent(this, Feed.class);
        //startActivity(createAccount);
    }
    public void cancelButton(View view){
        //Intent cancel = new Intent(this, Login.class);
        //startActivity(cancel);
    }
}
