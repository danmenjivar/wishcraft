package fiveguys.com.wishcraftapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MyProfile extends AppCompatActivity  {

    private static final String TAG = "MyProfile";
    public static final int GET_FROM_GALLERY = 20;
    private ImageView profilePicture;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userKey;
    private TextView usernameText;
    private EditText bioEditText;
    private User loggedInUser;
    private Button changeBioButton;
    private ArrayList<DisplayItem> wishListArrayList;
    private RecyclerView mRecyclerView;
    private DataSnapshot userWishlist;
    private DisplayProductAdapter mProductAdapter;
    private String mUserWishListKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        profilePicture = (ImageView) findViewById(R.id.profilePic);
        usernameText = findViewById(R.id.profileName);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getUsername();
        // Load profilePic from firebase on start of activity
        setUserProfilePic();
        bioEditText = findViewById(R.id.bio_editText);
        bioEditText.addTextChangedListener(isBioChanged);
        changeBioButton = findViewById(R.id.change_bio_button);
        setUserBio();
        findUserWishlist();
        wishListArrayList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.user_profile_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setAdapter(new DisplayProductAdapter(this, wishListArrayList));
    }

    private void setUserProfilePic(){
        String userID = mAuth.getUid();
        StorageReference storageReference = mStorageRef.child("images/ProfilePics/" + userID + ".jpg");
        Glide.with(this )
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .signature(new StringSignature(storageReference.getMetadata().toString()))
                .into(profilePicture);
    }

    private void setUserBio(){
        mDatabase.child("users").orderByChild("email").equalTo(mUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot userData = dataSnapshot.getChildren().iterator().next();
                    userKey = userData.getKey();
                    User user = userData.getValue(User.class);
                    loggedInUser = user;
                    bioEditText.setText(user.bio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Must be left empty to compile
            }
        });
    }

    public void onChangeBioButtonClick(View view){
        hideKeyboard();
        String newBio = bioEditText.getText().toString();
        updateBioInDatabase(newBio);

    }

    private TextWatcher isBioChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            String bioInput = bioEditText.getText().toString();

            if (!bioInput.isEmpty() && !bioInput.equals(loggedInUser.bio)){
                changeBioButton.setEnabled(true);
                changeBioButton.setBackgroundColor(getResources().getColor(R.color.wc_logo_pink));
            } else {
                changeBioButton.setEnabled(false);
                changeBioButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateBioInDatabase(final String newBio){
        loggedInUser.setBio(newBio);
        Map<String, Object> userValues = loggedInUser.toMap();
        Log.d(TAG, String.format("User key entered was %s", userKey));
        mDatabase.child("users/" + userKey).updateChildren(userValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "username change was successful");
                    Toast.makeText(MyProfile.this, String.format("Bio updated"), Toast.LENGTH_SHORT).show();
                    changeBioButton.setEnabled(false);
                    changeBioButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
                } else {
                    Log.d(TAG, "username change failed", task.getException());
                }

            }
        });
    }

    private void findUserWishlist(){
        mDatabase.child("userWishlist").orderByChild("email").equalTo(mUser.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    DataSnapshot user = dataSnapshot.getChildren().iterator().next();
                    mUserWishListKey = user.getKey();
                    getUserWishlist(user.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void removeItemHandler(){
        mProductAdapter.setOnRemoveListener(new DisplayProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                final DisplayItem itemToRemove = mProductAdapter.getIndexedItem(position);
                new AlertDialog.Builder(MyProfile.this)
                        .setTitle("Remove item")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findItem(itemToRemove);
                                removeItemFromRecycler(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    private void removeItemFromRecycler(int position){
        wishListArrayList.remove(position);
        mProductAdapter.notifyItemRemoved(position);
    }

    private void getUserWishlist(String userKey){

        mDatabase.child("userWishlist/" + userKey + "/wishlist").addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    userWishlist = dataSnapshot;
                    Iterator<DataSnapshot> wishListIterator = dataSnapshot.getChildren().iterator();
//                    Log.d(TAG, wishListIterator.next().getKey());

                    while (wishListIterator.hasNext()){
                        DataSnapshot currentItem = wishListIterator.next();

                        String itemName = currentItem.child("item_name").getValue().toString();
                        String itemLink = currentItem.child("item_link").getValue().toString();
                        String item_image_url = currentItem.child("item_image_url").getValue().toString();
                        double itemPrice = Double.valueOf(currentItem.child("item_price").getValue().toString());

                        DisplayItem itemToDisplay = new DisplayItem(itemName, itemPrice, itemLink, item_image_url);
                        wishListArrayList.add(itemToDisplay);
                    }

                    displayItems();
//                    for (DisplayItem item1 : wishListArrayList){
//                        Log.d(TAG, item1.toString());
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayItems(){
        mProductAdapter = new DisplayProductAdapter(MyProfile.this, wishListArrayList);
        mRecyclerView.setAdapter(mProductAdapter);
        removeItemHandler();
    }


    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void findItem(final DisplayItem itemToSearch){
        mDatabase.child("userWishlist/" + mUserWishListKey + "/wishlist")
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
//                                Log.d(TAG, "Item to remove: " + itemName + " with key: " + itemFound.getKey());
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


    private void removeItem(String itemKey){
        mDatabase.child("userWishlist/" + mUserWishListKey + "/wishlist/" + itemKey).removeValue();
        Toast.makeText(this, "Removed item succesfully!", Toast.LENGTH_SHORT).show();
    }

    /*
    public void onClickEditPic(View v) {
        Intent galleryImageGrab = new Intent(Intent.ACTION_PICK);
        File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirPath =  picDirectory.getPath();
        Uri picData = Uri.parse(picDirPath);
        galleryImageGrab.setDataAndType(picData,"image/*");
        startActivityForResult(galleryImageGrab, GET_FROM_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == GET_FROM_GALLERY){
                //successfully heard from image gallery if here
                Uri imageUri = data.getData();
                InputStream imgStream;
                try {
                    imgStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imgStream);
                    profilePicture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to Open Image",Toast.LENGTH_LONG).show();
                }
            }

        }

    }
    */



    public void getUsername(){
        String email = mUser.getEmail();
        Query usernameQuery = mDatabase.child("users").orderByChild("email").equalTo(email);

        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot userData = dataSnapshot.getChildren().iterator().next();
                    userKey = userData.getKey();//used when changing username and email
                    User user = userData.getValue(User.class);
                    usernameText.setText(user.username + "\'s wishlist");
                }
            }

            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

}
