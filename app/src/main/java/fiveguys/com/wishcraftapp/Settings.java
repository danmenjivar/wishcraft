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
import android.widget.Button;
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

    private final String TAG = "Settings";
    private EditText usernameText;
    private FirebaseUser mUser;
    private DatabaseReference mUsersDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    public static final int GET_FROM_GALLERY = 20;
    private ImageView profilePicture;
    private EditText emailText;
    private User loggedInUser;
    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profilePicture = (ImageView) findViewById(R.id.profilePic);
        mAuth = FirebaseAuth.getInstance();//grab authentication
        mUser = mAuth.getCurrentUser(); //grab current logged in user
        emailText = findViewById(R.id.email_edit_Entry);
        usernameText = findViewById(R.id.username_change);
        loggedInUser = new User("", "");
        String email = mUser.getEmail(); //grab email to locate in database

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");///connect to fb database
        fetchUser(email);
    }

    //Fetches user data to display in settings
    private void fetchUser(String email){
        mUsersDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.hasChildren()){
                   User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                   //Log.d(TAG, user.username + " " + user.email);
                   emailText.setText(user.email);
                   loggedInUser.setEmail(user.email);
                   usernameText.setText(user.username);
                   loggedInUser.setUsername(user.username);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //couldn't read anything
            }
        });
    }

    //Method will straight up just delete that user from being authenticated
    public void onDeleteButtonClick(View view){
        //Todo: add a dialogue box to confirm

        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "delete succesful");
                } else {
                    Log.d(TAG, "user could not be deleted");
                }
            }
        });

    }

    //Method executes when user types new Password
    public void changePasswordOnClick(View view){

        EditText newPassword = findViewById(R.id.PasswordEntry);
        EditText newPasswordConfirm = findViewById(R.id.PasswordEntryConfirm);

        if (arePasswordsEqualAndValid(newPassword, newPasswordConfirm)){
            changePassword(newPassword.getText().toString());
        }
    }

    //Method updates firebase auth with new password for currently signed in user
    private void changePassword(String newPassword){
        mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "password updated");
                    Toast.makeText(Settings.this, "Changes saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "error, password not updated", task.getException());
                }
            }
        });
    }


    private void changeUsername(String email, String newUsername){
        mUsersDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        String keyId = ds.getKey();
                    }

                    User user = dataSnapshot.getChildren().iterator().next().getValue(User.class);
                    //Log.d(TAG, user.username + " " + user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //couldn't read anything
            }
        });

    }

    /**
     * When user clicks to save changes to their bio or username
     * @param view
     */
    public void saveChangesButton(View view) {

        boolean hasUserChangedUsernameField = !usernameText.getText().toString().equals(loggedInUser.username);
        boolean hasUserChangedEmailField = !emailText.getText().toString().equals(loggedInUser.email);

        if (hasUserChangedUsernameField){

        }



        if (hasUserChangedEmailField || hasUserChangedUsernameField){//user has changed either password or email field

        }




    }

   //Signs out user when they click the log out button system wide
    public void backToLogin(View view) {
        FirebaseAuth.getInstance().signOut(); //sign out user system-wide
        Intent createAccountIntent = new Intent(this, Login.class);
        startActivity(createAccountIntent); //return user to login screen
        finish();
    }


    ////Methods for changing profile pic
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

    //Checks if user enters password that is valid, i.e. not empty and at least 6 characters long
    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 6;
    }

    //Checks if both passwords match and the user entered are valid
    private boolean arePasswordsEqualAndValid(EditText password1, EditText password2){
        String newPassword = password1.getText().toString();
        String newPasswordConfirm = password2.getText().toString();

        boolean arePasswordsValid = isValidPassword(newPassword) && isValidPassword(newPasswordConfirm);

        if (!arePasswordsValid){
            Toast.makeText(this, "Too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(newPasswordConfirm)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
