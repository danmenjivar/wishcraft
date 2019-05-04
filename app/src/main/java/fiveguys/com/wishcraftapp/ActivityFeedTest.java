package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class ActivityFeedTest extends Activity  {

    private static final String TAG = "Activity Feed";

    private DatabaseReference friendsListdb;
    private FirebaseAuth fbauth;
    private long numOfFriends;
    private String globalFriendName[];
    private String userEmail;
    private RecyclerView mRecyclerView;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private int gfnCount = 0;
    //private
    private ArrayList<ActivityFeedDisplay> wishListArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_buttons);
        globalFriendName = new String[50];
        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();
        mUser = fbauth.getCurrentUser();
        mRecyclerView = findViewById(R.id.activity_feed);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mAdapter = new ActivityFeedAdapter(this,new ArrayList<ActivityFeedDisplay>());
        mRecyclerView.setAdapter(new ActivityFeedAdapter(this,new ArrayList<ActivityFeedDisplay>()));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        wishListArray = new ArrayList<>();
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
        getUserInfo();

    }
    //change to getEmailFromUid
    private void getEmailFromUsername(String name){

        globalFriendName[gfnCount]=name;
        gfnCount++;
        mDatabase.child("users").orderByChild("username").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();
                    User user = currentUser.getValue(User.class);
                    String email = user.getEmail();
                    findNameInWishlists(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //stays empty
            }
        });
    }
    private void getUserInfo(){
        friendsListdb.orderByChild("email").equalTo(mUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();
                    findUserWishlist(currentUser.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void findNameInWishlists(String email){
        mDatabase.child("userWishlist").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            DataSnapshot user = dataSnapshot.getChildren().iterator().next();
                            Log.d(TAG, "key: " + user.getKey());
                            getUserWishlist(user.getKey());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }
    private void findUserWishlist(String key){
        //Query emailQuery = mDatabase.child("userWishlist").orderByChild("email").equalTo(mUser.getEmail());
        //Query emailQuery = mDatabase.child("userFriendslist").orderByChild("email").equalTo(mUser.getEmail());

        final DatabaseReference placeToGrabFriends = friendsListdb.child(key + "/friendslist");
        placeToGrabFriends.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        numOfFriends =snapshot.getChildrenCount();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Friend friend = postSnapshot.getValue(Friend.class);
                            //change this to getUid and adjust friend.class accordingly
                            getEmailFromUsername((friend.getName()));
                        }
                    }

                        @Override
                        public void onCancelled (@NonNull DatabaseError databaseError){
                    }
        });
    }

    public void getUserWishlist( String friend ) {


        //Query fListQuery = mDatabase.child("userFriendslist").orderByChild("email").equalTo(userEmail);
        Query listQuery = mDatabase.child("userWishlist/" + friend + "/wishlist");
        gfnCount = 0;
            listQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        Iterator<DataSnapshot> wishListIter = dataSnapshot.getChildren().iterator();
                        while (wishListIter.hasNext()) {
                            DataSnapshot currentItem = wishListIter.next();
                            double itemPrice = Double.valueOf(currentItem.child("item_price").getValue().toString());
                            String itemName = globalFriendName[gfnCount] + " added: " + currentItem.child("item_name").getValue().toString();
                            String itemLink = currentItem.child("item_link").getValue().toString();
                            String itemImageUrl = currentItem.child("item_image_url").getValue().toString();
                            //to sort by time
                            String entryKey = currentItem.getKey();
                            ActivityFeedDisplay itemToDisplay = new ActivityFeedDisplay(itemName, itemPrice, itemLink, itemImageUrl,entryKey);
                            wishListArray.add(itemToDisplay);

                        }
                        if(gfnCount == numOfFriends-1){
                            displayActivityFeed();
                        }
                        gfnCount++;
                    }
                }

                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }


    public void displayActivityFeed(){
        Collections.sort(wishListArray, new Comparator<ActivityFeedDisplay>() {
            public int compare(ActivityFeedDisplay v1, ActivityFeedDisplay v2) {
                return v1.getEntryKey().compareTo(v2.getEntryKey());
            }
        });
        Collections.reverse(wishListArray);
        mRecyclerView.setAdapter(new ActivityFeedAdapter(this, wishListArray));
    }
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



