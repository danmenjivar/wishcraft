package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
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
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search);
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
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
/*

p v viewFirndlist(DataSnapshot currentUser = dataSnapshot.getChildren().iterator().next();)
{

}



 */

    private void addFriendInDatabase(String key){
        DatabaseReference placeToPush = friendsListdb.child(key + "/friendslist");
        DatabaseReference newFriend = placeToPush.push();
        newFriend.child("name").setValue("Jason");
    }

    //code friend search first
    //picture, name, button(add friend)
        //check if already freind

        //create base freind object
        //name, picture, wishlist, email, etc
        //edit this base object
    //Figure out most efficient way to view friend list


}
