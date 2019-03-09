package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class feed extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    public void settingButton(View view) {
        Toast.makeText(this, "Link me to Settings View", Toast.LENGTH_SHORT).show();
//        Intent loginIntent = new Intent(this, feed.class);
//        startActivity(loginIntent);
    }

    public void searchButton(View view) {
        Toast.makeText(this, "Link me to Search View", Toast.LENGTH_SHORT).show();
//        Intent loginIntent = new Intent(this, feed.class);
//        startActivity(loginIntent);
    }

    public void myProfileButton(View view) {
        Toast.makeText(this, "Link me to myProfile View", Toast.LENGTH_SHORT).show();
        //        Intent loginIntent = new Intent(this, feed.class);
//        startActivity(loginIntent);
    }
}
