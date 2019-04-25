package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.*;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MyProfile extends AppCompatActivity {

    private static final String TAG = "MyProfile";
    public static final int GET_FROM_GALLERY =20;
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



    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
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
                    usernameText.setText(user.username);
                }
            }

            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

}
