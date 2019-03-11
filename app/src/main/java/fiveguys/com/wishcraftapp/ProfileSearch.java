package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ProfileSearch extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishcraftmyprofile);
    }

    public void addfriendButton(View view){

        Intent addItem = new Intent(this,view_friend_profile.class);
        startActivity(addItem);
    }


}
