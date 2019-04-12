package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Feed extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //initializes bottom navigation menu and its fragments/frames
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationMenu);

        final HomeFragment homeFragment = new HomeFragment();
        final ItemSearchFragment itemSearchFragment = new ItemSearchFragment();
        final NotificationsFragment notificationsFragment = new NotificationsFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.home) {
                    setFragment(homeFragment);
                    return true;
                } else if(id == R.id.search) {
                    setFragment(itemSearchFragment);
                    return true;
                } else if(id == R.id.notifications) {
                    setFragment(notificationsFragment);
                    return true;
                } else if(id == R.id.profile) {
                    setFragment(profileFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}

        //final ListView listView = (ListView) findViewById(R.id._dynamicfeedlist);

//        String demo[] = {"Riad has added \"Super Smash Bros. Ultimate\"",
//                "Cody has claimed \"Spider-Man: Into the Spider-Verse DVD\"",
//                "Connor has added \"King of The Hill DVD\"",
//                "Daniel has claimed surfboard",
//                "Jason has claimed \"Airdrop\" by Friendos"
//        };

//        ArrayList<String> demoList = new ArrayList<>(Arrays.asList(demo));


//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demoList) {
//            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
//                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
//                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
//                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
//                return view;
//            }
//        };

        //listView.setAdapter(arrayAdapter);


//    }

//    public void settingButton(View view) {
//        Intent settingsIntent = new Intent(this, Settings.class);
//        startActivity(settingsIntent);
//    }
//
//    public void searchButton(View view) {
//        //TODO connect me
////        Toast.makeText(this, "Link me to Search View", Toast.LENGTH_SHORT).show();
//        Intent searchIntent = new Intent(this, ProfileSearch.class);
//        startActivity(searchIntent);
//    }
//
//    public void myProfileButton(View view) {
//        //TODO connect me
//        //Toast.makeText(this, "Link me to myProfile View", Toast.LENGTH_SHORT).show();
//        Intent profileIntent = new Intent(this, MyProfile.class);
//        startActivity(profileIntent);
//    }
//}
