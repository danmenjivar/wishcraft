package fiveguys.com.wishcraftapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfileSearchAdapter extends RecyclerView.Adapter<ProfileSearchAdapter.ViewHolder>{
    private static final String TAG = "ProfileSearchAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mImageButton;
    private ArrayList<String> mbio = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    private DatabaseReference friendsListdb;

    private Context mContext;
    ArrayList<Friend> profiles;
    ArrayList<Friend> list;
    ProfileSearchAdapter adapter;
    RecyclerView recyclerView;


    public ProfileSearchAdapter(Context context, ArrayList<String> mImageNames, ArrayList<String> mImages) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = context;
    }

    protected void onCreate(Bundle saveInstanceState)
    {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference("userFriendslist");
        friendsListdb = FirebaseDatabase.getInstance().getReference("userFriendslist");
        Log.d(TAG, "onCreate: troll" + friendsListdb.getKey());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Problems with context

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_profile_search_auxiliary,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called dis one.");
//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(viewHolder.image);
        String userID = "Q4493iMsoRUt2lVaFZCAdHxW5Os2";
//        friendsListdb.orderByChild(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.hasChildren())
//                {
//                    dataSnapshot.getChildren().iterator().next();
//                    Log.d( "Success ho ", dataSnapshot.getKey());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: FAILLLLLL");
//            }
//        });
//        String userID = mAuth.getUid();
        StorageReference storageReference = mStorageRef.child("images/ProfilePics/" + userID + ".jpg");
//        viewHolder.PSAfriendName.setText(profiles.get(i).getName());
////        myViewHolder.bio.setText(profiles.get(i).getBioEditText());
//        Glide.with(mContext).load(storageReference).fitCenter().into(viewHolder.PSAimage);


        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .signature(new StringSignature(storageReference.getMetadata().toString()))
                .into(viewHolder.PSAimage);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView PSAimage;
        TextView PSAfriendName;
        TextView PSAfriendBio;
        Button PSAfriendButton;
        RelativeLayout PSAfriendParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PSAimage = itemView.findViewById(R.id.auxProfilePicture);
            PSAfriendName = itemView.findViewById(R.id.searchedName);
            //PSAfriendBio = itemView.findViewById(R.id.searchedBio);
            //PSAfriendButton = itemView.findViewById(R.id.lookAddButton);
            PSAfriendParentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
