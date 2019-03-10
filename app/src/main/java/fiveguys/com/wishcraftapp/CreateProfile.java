package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.WishCraftCreateProfile);
    }

    public void finalizeAccountCreateButton(View view){
        Toast.makeText(this,"Link this to feed page",Toast.LENGTH_SHORT).show();
        //Intent createAccount = new Intent(this,Feed.class);
        //startActivity(createAccount);
    }
    public void cancelButton(View view){
        Toast.makeText(this,"Link this to login page",Toast.LENGTH_SHORT).show();
        //Intent cancel = new Intent(this,);
        //startActivity();
    }
}
