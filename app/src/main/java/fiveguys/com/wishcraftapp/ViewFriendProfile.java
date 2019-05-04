package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewFriendProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_profile);
    }

    public void user1click(View view) {
        // Intent intent = new Intent(this, ItemSearch.class);
        Toast.makeText(this, "Item has been claimed :D", Toast.LENGTH_SHORT).show();
        // startActivity(intent);
    }

}