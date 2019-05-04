package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class FeedButtons extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_buttons);




//        String demo[] = {"Riad has added \"Super Smash Bros. Ultimate\"",
//                "Cody has claimed \"Spider-Man: Into the Spider-Verse DVD\"",
//                "Connor has added \"King of The Hill DVD\"",
//                "Daniel has claimed surfboard",
//                "Jason has claimed \"Airdrop\" by Friendos"
        };

        //ArrayList<String> demoList = new ArrayList<>(Arrays.asList(demo));


//        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demoList) {
//            public View getView(int position, View convertView, ViewGroup parent) {
//                // Get the Item from ListView
//                View view = super.getView(position, convertView, parent);
//
//                // Initialize a TextView for ListView each Item
//                TextView tv = (TextView) view.findViewById(android.R.id.text1);
//
//                // Set the text color of TextView (ListView Item)
//
//                tv.setTextColor(Color.rgb(200,20,100));
//                // Generate ListView Item using TextView
//                return view;
//            }
//        };
//
//        listView.setAdapter(arrayAdapter);


   // }

    public void settingButton(View view) {
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }

    public void searchButton(View view) {
//        Toast.makeText(this, "Link me to Search View", Toast.LENGTH_SHORT).show();
        Intent searchIntent = new Intent(this, ProfileSearch.class);
        startActivity(searchIntent);
    }

    public void myProfileButton(View view) {

        //Toast.makeText(this, "Link me to myProfile View", Toast.LENGTH_SHORT).show();
        Intent profileIntent = new Intent(this, MyProfile.class);
        startActivity(profileIntent);
    }
    public void itemSearchButton(View view) {
        //Toast.makeText(this, "Link me to myProfile View", Toast.LENGTH_SHORT).show();
        Intent feedIntent = new Intent(this, AliTest.class);
        startActivity(feedIntent);
    }
}