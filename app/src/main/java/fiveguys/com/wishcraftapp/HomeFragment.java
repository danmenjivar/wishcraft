package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

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
    private int gfnIndex = 0;
    private ArrayList<ActivityFeedDisplay> wishListArray;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();
        mUser = fbauth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View RootView = inflater.inflate(R.layout.activity_home_fragment, container, false);
        globalFriendName = new String[50];
        mRecyclerView = RootView.findViewById(R.id.activity_feed);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        wishListArray = new ArrayList<>();
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");

        return RootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(new ActivityFeedAdapter(this.getContext(),new ArrayList<ActivityFeedDisplay>()));
        Thread haha = new Thread(){
            public void run(){
                findUserWishlist();
            }
        };
        haha.start();
    }

    //change to getEmailFromUid
   /* private void getEmailFromUid(String uid){
        mDatabase.child("users").orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
    }*/

    private void findWishlistKey(String uid) {
        mDatabase.child("userWishlist").orderByChild("uniqueId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();
                    getUserWishlist(currentUser.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

   /* private void findNameInWishlists(final String uid){
        mDatabase.child("userWishlist").orderByChild("uniqueId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            DataSnapshot user = dataSnapshot.getChildren().iterator().next();
                            Log.d(TAG, "key: " + user.getKey());
                            findName(uid);
                            getUserWishlist(user.getKey());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }*/

    private void findUserWishlist(){

        Query placeToGrabFriends = mDatabase.child("userFriendslist/" + mUser.getUid()).orderByChild("userid");
        placeToGrabFriends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                numOfFriends =snapshot.getChildrenCount();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DataSnapshot friendUid = postSnapshot.child("frienduid");

                    String uid = friendUid.getValue().toString();
                    findName(uid);
                    findWishlistKey(uid);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });
    }

    private void findName(String uniqueId){
        mDatabase.child("users").orderByChild("userId").equalTo(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();
                    String username = currentUser.child("username").getValue().toString();
                    globalFriendName[gfnCount]=username;
                    gfnCount++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getClaimMessages(){
        final DatabaseReference claimMsgReference = FirebaseDatabase.getInstance().getReference("claimMessages");
        DatabaseReference claimMsg = claimMsgReference.child(mUser.getUid());
        claimMsg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> wishListIter = dataSnapshot.getChildren().iterator();
                    while (wishListIter.hasNext()) {
                        DataSnapshot currentItem = wishListIter.next();
                        double itemPrice ='\0';
                        String itemLink = "";
                        String itemImageUrl = "";
                        String itemName = "Your \"" + currentItem.child("item_name").getValue().toString() +"\" has been claimed";

                        String entryKey = currentItem.getKey();
                        ActivityFeedDisplay itemToDisplay = new ActivityFeedDisplay(itemName, itemPrice, itemLink, itemImageUrl, entryKey);
                        wishListArray.add(itemToDisplay);
                    }
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });
    }

    public void getUserWishlist( String friend ) {

        Query listQuery = mDatabase.child("userWishlist/" + friend + "/wishlist");
        listQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Iterator<DataSnapshot> wishListIter = dataSnapshot.getChildren().iterator();
                    while (wishListIter.hasNext()) {
                        DataSnapshot currentItem = wishListIter.next();
                        double itemPrice = Double.valueOf(currentItem.child("item_price").getValue().toString());
                        String itemName = globalFriendName[gfnIndex] + " added: " + currentItem.child("item_name").getValue().toString();
                        String itemLink = currentItem.child("item_link").getValue().toString();
                        String itemImageUrl = currentItem.child("item_image_url").getValue().toString();
                        //to sort by time
                        String entryKey = currentItem.getKey();
                        ActivityFeedDisplay itemToDisplay = new ActivityFeedDisplay(itemName, itemPrice, itemLink, itemImageUrl,entryKey);
                        wishListArray.add(itemToDisplay);

                    }

                    if(gfnIndex == numOfFriends-1){
                        displayActivityFeed();
                    }

                    gfnIndex++;
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
        mRecyclerView.setAdapter(new ActivityFeedAdapter(this.getContext(), wishListArray));
    }
}