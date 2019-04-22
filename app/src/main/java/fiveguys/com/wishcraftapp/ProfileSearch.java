package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    //TODO pricate void and hardcode a new friend

    private void addFriendToFriendList(String nameOfFriend){
        friendsListdb.orderByChild("email").equalTo(currentUserEmail);
    }




}
