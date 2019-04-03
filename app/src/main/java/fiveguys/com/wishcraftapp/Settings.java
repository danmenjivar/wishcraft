package fiveguys.com.wishcraftapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class Settings extends Activity {

    private EditText usernameChange;

    private FirebaseUser mUser;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();//grab authentication
        mUser = mAuth.getCurrentUser(); //grab current logged in user

        String email = mUser.getEmail(); //grab email to locate in database

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


}
