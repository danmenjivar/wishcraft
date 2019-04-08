package fiveguys.com.wishcraftapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ItemSearch extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText searchText;
    private Button addItemButton;
    private String userKey;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemsearch);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance(); //grab authentication
        mUser = mAuth.getCurrentUser(); //grab current logged in userN
        this.searchText = findViewById(R.id.searchItem);
        searchText.addTextChangedListener(addItemEnable);
        loggedInUser = new User("", "");
        String email = mUser.getEmail();    //check later for NULLPOINTEREXCEPTIO
        addItemButton = findViewById(R.id.addItem1);
        fetchUser(email);
    }

    private void fetchUser(String email) {
        mDatabase.child("userWishlist").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot userData = dataSnapshot.getChildren().iterator().next();
                    userKey = userData.getKey();
                    User user = userData.getValue(User.class);
                    loggedInUser.setEmail(user.email);
                    loggedInUser.setUsername(user.username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //nothing for now
            }
        });
    }

    private TextWatcher addItemEnable = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //empty, must exist for compilation purposes
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String searchInput = searchText.getText().toString();

            if(isValidSearch(searchInput) && !searchInput.isEmpty()) {
                addItemButton.setEnabled(true);
                addItemButton.setBackgroundColor(getResources().getColor(R.color.wc_logo_pink));
            } else {
                addItemButton.setEnabled(false);
                addItemButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //empty, must exist for compilation purposes
        }
    };

    private boolean isValidSearch(String search) {
        return !search.isEmpty();
    }

    public void user1click(View view) {
        // Intent intent = new Intent(this, ItemSearch.class);
        String itemToBeAdded = searchText.getText().toString();
        addItemToFirebaseDatabase(itemToBeAdded);
        Toast.makeText(this, "Added Item to list", Toast.LENGTH_SHORT).show();
        // startActivity(intent);
    }

    private void addItemToFirebaseDatabase(String item) {
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference table = fb.child("userWishlist").child(userKey).child("wishlist");
        DatabaseReference newItem = table.push();
        newItem.child("item").setValue(item);
    }

    public void gotoProfile(View view) {
        Intent intent = new Intent(this, MyProfile.class);
        //Toast.makeText(this, "Added Item to list", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}