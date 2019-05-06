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
    private String currentUser;
    private String friendKey;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search = findViewById(R.id.editText8);
        setContentView(R.layout.activity_profile_search);
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
        usersdb = FirebaseDatabase.getInstance().getReference("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public void gotoFriendProfile(View view){
        String search = this.search.getText().toString();
        Intent intent = new Intent(getApplicationContext(), ViewFriendProfile.class);
    }

    //TODO private void and hardcode a new friend


    //Click function
    public void onAddFriendButtonClick(View view){
        addFriendInDatabase("dDyYzZBz9uOP8vFHvq0H4kzHQaH3");
    }

    private void fetchUserFriendList(){
        friendsListdb.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void addFriendInDatabase(final String key){
        friendsListdb.orderByChild("useruid").equalTo(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    dataSnapshot.getChildren().iterator().next();
                    Log.d("CODY: ", dataSnapshot.getKey().toString());
                    DatabaseReference newFriend = friendsListdb.child(dataSnapshot.getKey() + "/friendslist").push();
                    newFriend.child("frienduid").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onViewFriendButtonClick(View view){
        displayFriendList();
    }
    private void displayFriendList(){
        friendsListdb.orderByChild("useruid").equalTo(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Log.d("CODY: ", friend.getName() + " and " + friend.getEmail() + " and " + friend.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void friendExists(String search){
        //todo: finish finding the friend if exists from search bar
        DatabaseReference userlistdb = FirebaseDatabase.getInstance().getReference("users");
        userlistdb.orderByChild("username").equalTo(search).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    DataSnapshot friendData = dataSnapshot.getChildren().iterator().next();
                    friendKey = friendData.getKey();
                    addFriendInDatabase(friendKey);
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
}
