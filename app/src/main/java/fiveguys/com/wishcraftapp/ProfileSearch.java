package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ProfileSearch extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);
    }


    public void addfriendButton(View view){
        Intent addItem = new Intent(this,ViewFriendProfile.class);
        startActivity(addItem);
    }


}
