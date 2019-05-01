package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.media.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileSearch extends AppCompatActivity {

    private DatabaseReference friendsListdb;
    private DatabaseReference usersdb;
    private String currentUserEmail;
    private String friendKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
        usersdb = FirebaseDatabase.getInstance().getReference("users");
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }


    public void addfriendButton(View view){
        Intent addItem = new Intent(this,ViewFriendProfile.class);
        startActivity(addItem);
    }

    //TODO private void and hardcode a new friend


    //Click function
    public void onAddFriendButtonClick(View view){

            fetchUserFriendList();

//        String userKey = friendsListdb.orderByChild("email").equalTo(currentUserEmail)
//        DatabaseReference table = friendsListdb.child(userKey).child("friendslist");
//        DatabaseReference newFriend = table.push();
//        newFriend.child("friend").setValue("Tom from Myspace");
//
//        Toast.makeText(this, "Added Friend to list", Toast.LENGTH_SHORT).show();


    }

    private void fetchUserFriendList(){
        friendsListdb.orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();
                    Log.d("Jason", currentUser.getKey());
                    addFriendInDatabase(currentUser.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //stays empty
            }
        });
    }

    private void addFriendInDatabase(String key){
        DatabaseReference placeToPush = friendsListdb.child(key + "/friendslist");
        DatabaseReference newFriend = placeToPush.push();
        newFriend.child("name").setValue("ironman");
    }

    public void onViewFriendButtonClick(View view){
        displayFriendList();
    }
    private void displayFriendList(){
        friendsListdb.orderByChild("email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();
                    Log.d("CODY", currentUser.getKey());
                    findFriendsInDatabase(currentUser.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //stays empty
            }
        });
    }

    private void findFriendsInDatabase(String key){
        final DatabaseReference placeToGrabFriends = friendsListdb.child(key + "/friendslist");
        placeToGrabFriends.orderByChild(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot friendData : dataSnapshot.getChildren()) {
                    if (friendData.hasChildren()) {
                        Log.d("CODY: ", friendData.getKey());
                       // friendData = friendData.getChildren().iterator().next();
                        Friend friend = friendData.getValue(Friend.class);
                        Log.d("CODY: ", friend.getName());
                        setFriendData(friend.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setFriendData(final String username){
        DatabaseReference userlistdb = FirebaseDatabase.getInstance().getReference("users");
        userlistdb.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.hasChildren()) {
                    Log.d("CODY: ", datasnapshot.getKey());
                    DataSnapshot friendData = datasnapshot.getChildren().iterator().next();
                    friendKey = friendData.getKey();
                    Log.d("CODY: ", friendKey);
                    Friend friend = friendData.getValue(Friend.class);
                    friend.setName(username);
                    Log.d("CODY: ", friend.getName() + " and " + friend.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //code friend search first
    //picture, name, button(add friend)
        //check if already freind

        //create base freind object
        //name, picture, wishlist, email, etc
        //edit this base object
    //Figure out most efficient way to view friend list

    //Search click function
    public void onSearchFriendButtonClick(View view){
        EditText searchName = findViewById(R.id.searchFriendName);

        lookupFriendsListdb(searchName.getText().toString());
        //return username
        //return email
        //return
    }

    //Pulls names similar to search
    private void lookupFriendsListdb(String name){

        usersdb.orderByChild("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                //https://www.youtube.com/watch?v=-fUXqc-h94o
                }
                else
                {
                    Toast.makeText(this, "Could not find friend :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                //Daniel-san says this stays empty

            }
        });
    }

/*
p v viewFirndlist(DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();)
{
}
 */

}
