package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.util.LogTime;
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

import java.util.ArrayList;
import java.util.Iterator;

public class FriendsFragment extends Fragment implements View.OnClickListener{
    private DatabaseReference friendsListdb;
    private DatabaseReference usersdb;
    private String currentUser;
    private TextInputLayout friendsearchlayout;
    private EditText friendname;

    //Phresh new code
    private ArrayList<String> listOfFriendStrings;
    RecyclerView recyclerView;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    public FriendsFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CODY: ", "WHAT?");
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
        usersdb = FirebaseDatabase.getInstance().getReference("users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Phresh new code
        listOfFriendStrings = new ArrayList<>(10);

        searchFriendFromDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View RootView = inflater.inflate(R.layout.activity_profile_search, container, false);
        friendsearchlayout = (TextInputLayout) RootView.findViewById(R.id.textInputLayout);
        friendname = friendsearchlayout.getEditText();

        recyclerView = RootView.findViewById(R.id.friendlist_recycler_view);
        return RootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.stop_with_bad_names:
                onAddFriendButtonClick(view);
                break;
            case R.id.go_to_profile:
                onClickGoToFriendProfile(view);
                break;
            case R.id.button7:
                onRemoveFriendButtonClick(view);
                break;
        }
    }

    //passes name from search bar to a usersearch to add their uid
    public void onAddFriendButtonClick(View view){
        if(friendname != null) {
            Log.d("CODY: ", friendsearchlayout.getEditText().getText().toString());
            friendExists(friendname.getText().toString(),1);
        }
        else
            Toast.makeText(FriendsFragment.this.getContext(), "Stop Passing Null You Git.", Toast.LENGTH_SHORT).show();
    }

    public void onClickGoToFriendProfile(View view){
        friendExists(friendname.getText().toString(),2);
    }

    public void goToFriendProfile(String userid) {
        Intent friendprofile = new Intent(getActivity().getApplicationContext(), ViewFriendProfile.class);
        friendprofile.putExtra("userID", userid);
        startActivity(friendprofile);
    }

    //Phresh new code
    //Phresh new code
    //Phresh new code
    private void searchFriendFromDatabase()
    {
        friendsListdb.child("/"+currentUser+"/").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChildren())
                {
                    DataSnapshot userFriendList;
                    userFriendList = dataSnapshot;
                    Iterator<DataSnapshot> friendListIterator = dataSnapshot.getChildren().iterator();


                    while(friendListIterator.hasNext())
                    {
                        DataSnapshot currentFriend = friendListIterator.next();

                        String friendName = currentFriend.child("frienduid").getValue().toString(); //should be esbKC4lYsfPeOcWHPruLirQyrWs2
                        Log.d("Friend UID is: ", friendName);


                        //Good here so far. call new function to go to users and pull the username from there
                        comparingUIDtoUsername(friendName);
                    }
                    Log.d("onDataChange: ", "Do I get here?????");

                    System.out.println("size is :" +listOfFriendStrings.size());

                    //LAST STEP
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        }); //should be Q4493iMsoRUt2lVaFZCAdHxW5Os2

    }


    //new function to be called by searchFriendFromDatabase
    public void comparingUIDtoUsername(String uid)
    {
        usersdb.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    dataSnapshot = dataSnapshot.getChildren().iterator().next();
                    Log.d("onDataChange: Some key", dataSnapshot.getValue().toString());
                    String stringysName = dataSnapshot.child("username").getValue().toString();


                    listOfFriendStrings.add(String.valueOf(stringysName));
                    System.out.println("size is asd:" +listOfFriendStrings.size());

                }
                System.out.println("FINAL SIZE IS:" +listOfFriendStrings.size());
                initRecyclerView();
                System.out.println("FINAL SIZE IS:" +listOfFriendStrings.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        displayFriendsList();
    }


    private void initRecyclerView()
    {
        Log.d("Initrecycler", "reached it" );

//        RecyclerView recyclerView = findViewById(R.id.friendlist_recycler_view);
        ProfileSearchAdapter adapter = new ProfileSearchAdapter(FriendsFragment.this.getContext(), listOfFriendStrings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }

//    public void displayFriendsList()
//    {
//        Log.d("displayFriendsList:","por que");
//        for (int i = 0; i<listOfFriendStrings.size(); i++)
//        {
//            Log.d("Ugly ho #" + i + ": ", listOfFriendStrings.get(i));
//            System.out.println("Ugly ho #" + i + ": " + listOfFriendStrings.get(i));
//        }
//    }



    //Phresh new code
    //Phresh new code
    //Phresh new code



    //stores frienduid into friendlist
    private void addFriendInDatabase(String key){
        DatabaseReference newFriend = friendsListdb.child("/"+currentUser+"/").push();
        newFriend.child("frienduid").setValue(key);
    }
    //passes uid of friend to remove to friend search
    public void onRemoveFriendButtonClick(View view){
        friendExists(friendname.getText().toString(), 3);
    }
    //searches database to remove specific friend
    private void removeFriend(String key){
        friendsListdb.child("/"+currentUser+"/").orderByChild("frienduid").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    dataSnapshot = dataSnapshot.getChildren().iterator().next();
                    Log.d("CODY: ", dataSnapshot.getKey());
                    friendsListdb.child("/"+currentUser+"/"+dataSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //throws the result of name search to the actual add function
    private void friendExists(String name, final int num){
        usersdb.orderByChild("username").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    dataSnapshot = dataSnapshot.getChildren().iterator().next();
                    String friendKey = dataSnapshot.getKey();
                    usersdb.child(friendKey).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(num == 1)
                                addFriendInDatabase(dataSnapshot.getValue().toString());
                            else if(num == 2){
                                Log.d("CODY: gotoid: ",dataSnapshot.getValue().toString());
                                goToFriendProfile(dataSnapshot.getValue().toString());
                            }
                            else if(num == 3){
                                removeFriend(dataSnapshot.getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
