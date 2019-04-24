package fiveguys.com.wishcraftapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    private DatabaseReference friendsListdb;
    private String currentUserEmail;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_search);
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_notifications, container, false);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile_search, container, false);
    }

    //TODO pricate void and hardcode a new friend

    private void addFriendToFriendList(String nameOfFriend){
        friendsListdb.orderByChild("email").equalTo(currentUserEmail);
    }
}
