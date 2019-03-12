package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ItemSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void user1click(View view) {
       // Intent intent = new Intent(this, ItemSearch.class);
        Toast.makeText(this, "Added Item to list", Toast.LENGTH_SHORT).show();
       // startActivity(intent);
    }

    public void toMyProfile(View view) {
        Intent intent = new Intent(this, MyProfile.class);

        startActivity(intent);
    }


}
