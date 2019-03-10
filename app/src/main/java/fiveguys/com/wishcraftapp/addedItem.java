package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import android.os.Bundle;

public class addedItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditem);
    }

    public void okButton(View view){
        Intent ok = new Intent(this,MyProfile.class);
        startActivity(ok);
    }
}
