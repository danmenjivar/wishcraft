package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Settings extends Activity {

    private EditText usernameChange;

    private FirebaseUser mUser;

    private DatabaseReference mDatabase;

    private StorageReference mStorageRef;

    private FirebaseAuth mAuth;



    public static final int GET_FROM_GALLERY =20;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profilePicture = (ImageView) findViewById(R.id.profilePic);

        mAuth = FirebaseAuth.getInstance();//grab authentication
        mUser = mAuth.getCurrentUser(); //grab current logged in user

        String email = mUser.getEmail(); //grab email to locate in database

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        Query currentUsernameData = mDatabase.orderByChild("email").equalTo(email);
        currentUsernameData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot user : dataSnapshot){
//
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Log.d("danny", currentUsernameData.toString());

        usernameChange = findViewById(R.id.username_change);
        //DatabaseReference currentUser = mDatabase.child("email").equals(username);
        usernameChange.addTextChangedListener(changeAttributes);



    }

    private TextWatcher changeAttributes = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            usernameChange.setText(mUser.getEmail());

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * When user clicks to save changes to their bio or username
     * @param view
     */
    public void saveChangesButton(View view) {

        Toast.makeText(this, "Changes saved.", Toast.LENGTH_SHORT).show();
    }

    /**
     * When user clicks log-out
     * @param view
     */
    public void backToLogin(View view) {
        mAuth.getInstance().signOut();



//                .signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // ...
//
//                    }
//                });
        Intent createAccountIntent = new Intent(this, Login.class);
        startActivity(createAccountIntent);
        finish();
    }

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
                String userID = mUser.getUid();
                Uri imageUri = data.getData();
                InputStream imgStream;
                StorageReference storageReference = mStorageRef.child("images/ProfilePics/"+ userID+".jpg");
                //Log.d("danny", "username");

                try {
                    imgStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imgStream);
                    if(bitmap != null) {
                        profilePicture.setImageBitmap(bitmap);
                    }
                    storageReference.putFile(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to Open Image",Toast.LENGTH_LONG).show();
                }
            }

        }

    }


}
