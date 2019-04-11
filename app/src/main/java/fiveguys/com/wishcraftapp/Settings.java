package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Settings extends Activity {

    private final String TAG = "Settings";
    private EditText usernameText;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    public static final int GET_FROM_GALLERY = 20;
    private ImageView profilePicture;
    private EditText emailText;
    private User loggedInUser;
    private String userKey;
    private EditText confirmPassword;

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
        final String email = mUser.getEmail(); //grab email to locate in database
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();///connect to fb database
        Thread thread = new Thread() {//speed up the fetching
            public void run() {
                fetchUser(email);
            }
        };
        thread.start();
        confirmPassword = findViewById(R.id.PasswordEntryConfirm);
        confirmPassword.setOnEditorActionListener(passwordKeyboardPress);
        String userID = mAuth.getUid();
        StorageReference storageReference = mStorageRef.child("images/ProfilePics/" + userID + ".jpg");

        // Load profilePic from firebase on start of activity
        ImageView iv = profilePicture;
        Glide.with(this )
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .signature(new StringSignature(storageReference.getMetadata().toString()))
                .into(iv);
    }

    //Fetches user data to display in settings
    private void fetchUser(String email) {
        mDatabase.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot userData = dataSnapshot.getChildren().iterator().next();
                    userKey = userData.getKey();//used when changing username and email
                    User user = userData.getValue(User.class);
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

    //Listener logs the user in when they hit the "ACTION DONE" key on the virtual keyboard
    private TextView.OnEditorActionListener passwordKeyboardPress = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE){
                changePasswordOnClick(findViewById(R.id.changePasswordButton));
                return true;
            }

            return false;
        }
    };

    //Method deletes that user from being authenticated
    public void onDeleteButtonClick(View view) {
        //Todo: add a dialogue box to confirm S2

        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "delete successful");
                } else {
                    Log.d(TAG, "user could not be deleted");
                }
            }
        });

    }

    //Method executes when user types new Password
    public void changePasswordOnClick(View view) {

        hideKeyboard();

        EditText newPassword = findViewById(R.id.PasswordEntry);
        EditText newPasswordConfirm = findViewById(R.id.PasswordEntryConfirm);

        if (arePasswordsEqualAndValid(newPassword, newPasswordConfirm)) {
            changePassword(newPassword.getText().toString());
        }
    }

    //Method updates firebase auth with new password for currently signed in user
    private void changePassword(String newPassword) {
        mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "password updated");
                    Toast.makeText(Settings.this, "Password updated", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "error, password not updated", task.getException());
                }
            }
        });
    }


    //Method updates username in firebase realtime database
    private void updateDatabaseUsername(final String newUsername) {
        loggedInUser.setUsername(newUsername);
        Map<String, Object> userValues = loggedInUser.toMap();
        Log.d(TAG, String.format("User key entered was %s", userKey));
        mDatabase.child("users/" + userKey).updateChildren(userValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "username change was successful");
                    Toast.makeText(Settings.this, String.format("Username changed to %s", newUsername), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "username change failed", task.getException());
                }

            }
        });
    }

    /**
     * When user clicks to save changes to their bio or username
     *
     * @param view
     */
    public void saveChangesButton(View view) {

        hideKeyboard();

        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();

        boolean hasUserChangedUsernameField = !username.equals(loggedInUser.username);
        boolean hasUserChangedEmailField = !email.equals(loggedInUser.email);

        if (hasUserChangedUsernameField) {
            updateDatabaseUsername(username);
        }

        if (hasUserChangedEmailField) {
            updateEmailAuth(email);
        }

    }

    //Method updates username in firebase realtime database
    private void updateDatabaseEmail(final String newEmail) {
        //First update the wishlist table
        mDatabase.child("userWishlist").orderByChild("email").equalTo(loggedInUser.email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot ds = dataSnapshot.getChildren().iterator().next();
                    String key = ds.getKey();
                    mDatabase.child("userWishlist").child(key).child("email").setValue(newEmail);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //exists for compilation, does nothing
            }
        });

        //then update, the users table
        loggedInUser.setEmail(newEmail);
        Map<String, Object> userValues = loggedInUser.toMap();
        Log.d(TAG, String.format("User key entered was %s", userKey));
        mDatabase.child("users/" + userKey).updateChildren(userValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "email change was successful");
                } else {
                    Log.d(TAG, "email change failed", task.getException());
                }

            }
        });
        Toast.makeText(this, String.format("%s saved as email", newEmail)
                , Toast.LENGTH_SHORT).show();
    }

    //Updates email in the user login authentication and then updates in the r.t.d.b.
    private void updateEmailAuth(final String email) {

        mUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, String.format("Email updated successfully to %s in FBAuth", email));
                    updateDatabaseEmail(email);
                } else {
                    Toast.makeText(Settings.this, email + " is already used", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG,"failed to update email in FBAuth", task.getException());
                }
            }
        });

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
        String picDirPath = picDirectory.getPath();
        Uri picData = Uri.parse(picDirPath);
        galleryImageGrab.setDataAndType(picData, "image/*");
        startActivityForResult(galleryImageGrab, GET_FROM_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GET_FROM_GALLERY) {
                //successfully heard from image gallery if here
                String userID = mUser.getUid();
                Uri imageUri = data.getData();
                InputStream imgStream;
                StorageReference storageReference = mStorageRef.child("images/ProfilePics/" + userID + ".jpg");
                try {
                    imgStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(imgStream);
                    if (bitmap != null) {
                        profilePicture.setImageBitmap(bitmap);
                    }
                    storageReference.putFile(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to Open Image", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    //Checks if user enters password that is valid, i.e. not empty and at least 6 characters long
    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 6;
    }

    //Checks if both passwords match and the user entered are valid
    private boolean arePasswordsEqualAndValid(EditText password1, EditText password2) {
        String newPassword = password1.getText().toString();
        String newPasswordConfirm = password2.getText().toString();

        boolean arePasswordsValid = isValidPassword(newPassword) && isValidPassword(newPasswordConfirm);

        if (!arePasswordsValid) {
            Toast.makeText(this, "Too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
