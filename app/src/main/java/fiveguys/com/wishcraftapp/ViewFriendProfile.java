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

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_profile);

        final ListView listView = (ListView) findViewById(R.id.friendlist);

        String demo[] = {"Bug-a-Splat",
                "Midterms graded",
                "Gurilla",
                "Gurilla glue",
                "S3 Riverdale",
                ":)"
        };

        ArrayList<String> demoList = new ArrayList<>(Arrays.asList(demo));


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demoList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };

        listView.setAdapter(arrayAdapter);

        this.mp = MediaPlayer.create(this, R.raw.my_jam);
    }

    public void user1click(View view) {
        // Intent intent = new Intent(this, ItemSearch.class);
        Toast.makeText(this, "Item has been claimed :D", Toast.LENGTH_SHORT).show();
        // startActivity(intent);
    }


    public void playSong(View view){
        if(!this.mp.isPlaying()){
            this.mp.start();
        } else {
            this.mp.pause();
        }
    }
}
