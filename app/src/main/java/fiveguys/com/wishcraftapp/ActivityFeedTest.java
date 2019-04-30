package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.Iterator;


public class ActivityFeedTest extends Activity  {

    private FirebaseAuth fbauth;
    private String userEmail;
    private RecyclerView mRecyclerView;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    //private
    private ArrayList<ActivityFeedDisplay> wishListArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_feed_test);
        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();
        mUser = fbauth.getCurrentUser();
        mRecyclerView = findViewById(R.id.recyler_activity_feed);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mAdapter = new ActivityFeedAdapter(this,new ArrayList<ActivityFeedDisplay>());
        mRecyclerView.setAdapter(new ActivityFeedAdapter(this,new ArrayList<ActivityFeedDisplay>()));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        wishListArray = new ArrayList<>();
        findWishlist();

    }
    private void findWishlist(){
        mDatabase.child("userWishlist").orderByChild("email").equalTo(mUser.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            DataSnapshot user = dataSnapshot.getChildren().iterator().next();
                            getWishlist(user.getKey());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void getWishlist(String userKey){


        Query fListQuery = mDatabase.child("userFriendslist").orderByChild("email").equalTo(userEmail);
        Query listQuery = mDatabase.child("userFriendslist/" + userKey + "/wishlist");

        listQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> wishListIter = dataSnapshot.getChildren().iterator();
                    while (wishListIter.hasNext()){
                        DataSnapshot currentItem = wishListIter.next();
                        double itemPrice = Double.valueOf(currentItem.child("item_price").getValue().toString());
                        String itemName = currentItem.child("item_name").getValue().toString();
                        String itemLink = currentItem.child("item_link").getValue().toString();
                        String itemImageUrl = currentItem.child("item_image_url").getValue().toString();
                        ActivityFeedDisplay itemToDisplay = new ActivityFeedDisplay(itemName, itemPrice, itemLink, itemImageUrl);
                        wishListArray.add(itemToDisplay);
                    }
                    displayActivityFeed();
                }
            }

            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public void displayActivityFeed(){
        mRecyclerView.setAdapter(new ActivityFeedAdapter(this, wishListArray));
    }

}

