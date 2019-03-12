package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class MyProfile extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishcraftmyprofile);


        final ListView listView = (ListView) findViewById(R.id._dynamicfeedlist);

        String fakeList[] = {"...",
                "...",
                "...",
                "...",
                "..."
        };

        ArrayList<String> demoList = new ArrayList<>(Arrays.asList(fakeList));


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

    public void addItemButton(View view){
        Toast.makeText(this, "Link me to Item Search", Toast.LENGTH_SHORT).show();
        //Intent addItem = new Intent(this,ItemSearch.class);
        //startActivity(addItem);
    }
}