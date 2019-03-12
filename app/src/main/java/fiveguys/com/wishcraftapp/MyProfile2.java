package fiveguys.com.wishcraftapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MyProfile2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile2);

        final ListView listView = (ListView) findViewById(R.id._dynamic_myWishlist);

        String demo[] = {"Champagne Bottles Qt: 7", "Diamonds Qt: 7", "ATM machine Qt: 1",
                "Gold watch Qt: 1", "Gold chain Qt: 1", "Rings Qt: 7"};

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

    }

    public void playSong(View view){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.rings);
        mp.start();
    }


    public void addItemDialogue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Item");
        builder.setMessage("Item to add: ");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //code to run when ok is pressed
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
