package fiveguys.com.wishcraftapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DisplayProfileSearchAdapter extends RecyclerView.Adapter<DisplayProfileSearchAdapter.MyViewHolder>
{
    public static final int GET_FROM_GALLERY = 20;
    Context mContext;
    ArrayList<Friend> profiles;
    private OnFriendClickListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorageRef;
    //Keep Debugging
    //https://www.youtube.com/watch?v=vpObpZ5MYSE&t=717s

    protected void onCreate(Bundle saveInstanceState)
    {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public interface OnFriendClickListener {
        void onItemClick(int position);
    }

    public DisplayProfileSearchAdapter(Context c, ArrayList<Friend> p)
    {
        mContext = c;
        profiles = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_profile_search_auxiliary,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String userID = mAuth.getUid();
        StorageReference storageReference = mStorageRef.child("images/ProfilePics/" + userID + ".jpg");
        myViewHolder.name.setText(profiles.get(i).getName());
//        myViewHolder.bio.setText(profiles.get(i).getBioEditText());
        Glide.with(mContext).load(storageReference).fitCenter().into(myViewHolder.profilePicture);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;
        public TextView email;
        public TextView bio;
        public ImageView profilePicture;
        public Button addremoveButton;

        public MyViewHolder(View view)
        {
            super(view);
            this.name = itemView.findViewById(R.id.searchedName);
            this.bio = itemView.findViewById(R.id.searchedBio);
            this.profilePicture = itemView.findViewById(R.id.profilePicture);
            this.addremoveButton = itemView.findViewById(R.id.lookAddButton);
        }
    }
}






























//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//
//public class DisplayProfileSearchAdapter extends RecyclerView.Adapter<DisplayProfileSearchAdapter.ProfileViewHolder>{
//    private Context mContext;
//    private ArrayList<DisplayItem> mExampleList;
//    private DisplayProductAdapter.OnItemClickListener mListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnRemoveListener(DisplayProductAdapter.OnItemClickListener listener){
//        mListener = listener;
//    }
//
//    public DisplayProfileSearchAdapter(Context context, ArrayList<> list) {
//        mContext = context;
//        mExampleList = list;
//    }
//
//    public DisplayItem getIndexedItem(int index){
//        return mExampleList.get(index);
//    }
//
//
//    @NonNull
//    @Override
//    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout., viewGroup, false);
//        return new ProfileViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DisplayProductAdapter.ProductViewHolder productViewHolder, int i) {
//        DisplayItem item = mExampleList.get(i);
//
//        String productName = item.getItemName();
//        double productPrice = item.getItemPrice();
//        String imageUrl = item.getImageUrl();
//        productViewHolder.itemName.setText(productName);
//        productViewHolder.itemPrice.setText(String.format("$%.2f", productPrice));
//        Glide.with(mContext).load(imageUrl).asBitmap().fitCenter().into(productViewHolder.itemImage);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mExampleList.size();
//    }
//
//    public class ProfileViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView profileImage;
//        public TextView profileName;
//
//        public ProfileViewHolder(View itemView) {
//            super(itemView);
//            this.profileImage = itemView.findViewById(R.id.list_itemPicture);
//            this.profileName = itemView.findViewById(R.id.item_title_list);
//        }
//    }
//}
