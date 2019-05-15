package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class ViewFriendProfile extends Activity {

    private final static String DEBUG_TAG = "FriendProfile";
    private Button friendButton;
    private final static String friend_tag = "Friend";
    private final static String unfriend_tag = "Unfriend";
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private ArrayList<DisplayItem> mWishlist;
    private ClaimProductAdapter mProductAdapter;
    private String mWishlistKey;
    private String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_profile);
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userID");
        mUserID = userId;
        Log.d(DEBUG_TAG, "userID = " + userId);
        friendButton = findViewById(R.id.change_friend_status_button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getUserData(userId);
        setmRecyclerView();
        setFriendProfilePic(userId);
        setAsFriendButton();
    }

    //Need to know if this is a friend or not for button

    private void getUserData(String userID){
        mDatabase.child("/users").orderByChild("userId").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot userData = dataSnapshot.getChildren().iterator().next();
                    String username = userData.child("username").getValue().toString();
                    setFriendName(username);
                    String email = userData.child("email").getValue().toString();
                    findFriendWishlist(email);
                    String bio = userData.child("bio").getValue().toString();
                    setFriendBio(bio);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //left empty on purpose
            }
        });
    }

    private void setmRecyclerView(){
        mRecyclerView = findViewById(R.id.friend_profile_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void findFriendWishlist(String email){
        mDatabase.child("/userWishlist").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot user = dataSnapshot.getChildren().iterator().next();
                    mWishlistKey = user.getKey();
                    getFriendWishlist(user.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //empty on purpose
            }
        });
    }

    private void getFriendWishlist(String wishlistKey){
        mDatabase.child("userWishlist/" + wishlistKey + "/wishlist").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            Iterator<DataSnapshot> wishlistIterator = dataSnapshot.getChildren().iterator();

                            ArrayList<DisplayItem> wishListArrayList = new ArrayList<>();
                            while (wishlistIterator.hasNext()){

                                DataSnapshot currentItem = wishlistIterator.next();
                                String itemName = currentItem.child("item_name").getValue().toString();
                                String itemLink = currentItem.child("item_link").getValue().toString();
                                String item_image_url = currentItem.child("item_image_url").getValue().toString();
                                double itemPrice = Double.valueOf(currentItem.child("item_price").getValue().toString());

                                DisplayItem itemToDisplay = new DisplayItem(itemName, itemPrice, itemLink, item_image_url);
                                wishListArrayList.add(itemToDisplay);
                            }
                            displayItems(wishListArrayList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //empty on purpose
                    }
                }
        );
    }

    private void displayItems(ArrayList<DisplayItem> wishlist){
        mWishlist = wishlist;
        mProductAdapter = new ClaimProductAdapter(ViewFriendProfile.this, mWishlist);
        mRecyclerView.setAdapter(mProductAdapter);
        claimItemHandler();
    }

    public void onChangeFriendStatusButtonClick(View view){
        //Friend/unfriend

        if (isFriend()){
            addFriendToFirebase();
        } else {
            removeFriendFromFirebase();
        }
    }

    private boolean isFriend(){
        return !friendButton.getText().toString().equals(friend_tag);
        //if button reads friend, not friend
    }

    private void setFriendProfilePic(String userId){
        ImageView profilePicture = findViewById(R.id.friend_profilePic);
        StorageReference storageReference = mStorageRef.child("images/ProfilePics/" + userId + ".jpg");
        Glide.with(this )
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .signature(new StringSignature(storageReference.getMetadata().toString()))
                .into(profilePicture);
    }

    private void setFriendBio(String bio){
        TextView profileBio = findViewById(R.id.friend_bio);
        profileBio.setText(bio);
    }

    private void setFriendName(String name){
        TextView profileName = findViewById(R.id.friend_profileName);
        profileName.setText(name);
    }

    private void setAsFriendButton(){
        Button button = findViewById(R.id.change_friend_status_button);
        button.setText("friend");
    }

    private void setAsUnfriendButton(){
        Button button = findViewById(R.id.change_friend_status_button);
        button.setText("Unfriend");
    }

    private void removeFriendFromFirebase(){
        //TODO
    }

    private void addFriendToFirebase(){
        //TODO
    }

    private void claimItemHandler(){
        mProductAdapter.setOnRemoveListener(new DisplayProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final DisplayItem itemToRemove = mProductAdapter.getIndexedItem(position);
                new AlertDialog.Builder(ViewFriendProfile.this)
                        .setTitle("Claim item")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findItemAndRemove(itemToRemove);
                                removeItemFromRecycler(position);
                                claimMessage(itemToRemove);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    private void findItemAndRemove(final DisplayItem itemToSearch){
        mDatabase.child("userWishlist/" + mWishlistKey + "/wishlist")
                .orderByChild(DisplayItem.name_tag).equalTo(itemToSearch.getItemName()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            DataSnapshot itemFound = dataSnapshot.getChildren().iterator().next();
                            String itemName = itemFound.child(DisplayItem.name_tag).getValue().toString();
                            String itemUrl = itemFound.child(DisplayItem.url_tag).getValue().toString();
                            String image = itemFound.child(DisplayItem.image_tag).getValue().toString();
                            double price = Double.valueOf(itemFound.child(DisplayItem.price_tag).getValue().toString());
                            DisplayItem itemToRemove = new DisplayItem(itemName, price, itemUrl, image);
                            if (itemToSearch.equals(itemToRemove)){
                                removeItem(itemFound.getKey());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //empty on purpose
                    }
                }
        );
    }

    private void removeItemFromRecycler(int position){
        mWishlist.remove(position);
        mProductAdapter.notifyItemRemoved(position);
    }

    private void removeItem(String itemKey){
        mDatabase.child("userWishlist/" + mWishlistKey + "/wishlist/" + itemKey).removeValue();
        Toast.makeText(this, "Item claimed succesfully!", Toast.LENGTH_SHORT).show();
    }

    private void claimMessage(DisplayItem itemClaimed){
        DatabaseReference pushMessage = mDatabase.child("/claimMessages/" + mUserID);
        pushMessage.child("item_name").setValue(itemClaimed.getItemName());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        pushMessage.child("time_claimed").setValue(timestamp);
    }

    private boolean isFriendsWithUser(String friendID){

        return false;
    }

}