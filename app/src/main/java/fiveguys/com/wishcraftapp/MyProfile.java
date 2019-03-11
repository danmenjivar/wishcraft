package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishcraftmyprofile);
    }

    public void addItemButton(View view){

        Intent addItem = new Intent(this,ItemSearch.class);
        startActivity(ItemSearch.class);
    }
}