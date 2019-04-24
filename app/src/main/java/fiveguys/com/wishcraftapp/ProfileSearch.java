package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    public void addFriendToFriendListButton(View view){
        friendsListdb.orderByChild("email").equalTo(currentUserEmail);
//        friendsListdb.child("friendslist").setValue("Tom from Myspace");
//        friendsListdb.push();

        DatabaseReference table = friendsListdb.child("friendlist");
        DatabaseReference newFriend = table.push();
        newFriend.child("friends").setValue("Tom from Myspace");

        Toast.makeText(this, "Added Friend to list", Toast.LENGTH_SHORT).show();


    }




}
