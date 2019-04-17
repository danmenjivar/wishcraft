package fiveguys.com.wishcraftapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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

