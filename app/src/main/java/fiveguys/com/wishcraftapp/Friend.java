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

public class Friend extends AppCompatActivity{

    private String name;
    private String email;
    private String uid;
    private ImageView profilePicture;
    private EditText bioEditText;


    public Friend() {
    }

    public Friend(String name) {
        this.name = name;
    }

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Friend(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public Friend(String name, String email, String uid, ImageView profilePicture, EditText bioEditText) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.profilePicture = profilePicture;
        this.bioEditText = bioEditText;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ImageView getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBioEditText(EditText bioEditText) {
        this.bioEditText = bioEditText;
    }

    public EditText getBioEditText() {
        return bioEditText;
    }

}
