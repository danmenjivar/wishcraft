package fiveguys.com.wishcraftapp;

import android.content.Context;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class AliSearch extends AppCompatActivity implements AddItemDialog.AddItemDialogListener {

    private final static String NAME = "item_name";
    private final static String PRICE = "item_price";
    private final static String LINK = "item_link";
    private final static String IMAGE_URL = "item_image_url";
    private static final String DEBUG_TAG = "Ali";
    private final String apiAccessKey = "YNZBUIVFBSOPLNSO";
    private FirebaseAuth fbauth;
    private String userEmail;
    private String fbUserKey;
    private EditText searchQuery;
    private Button searchButton;
    private RecyclerView mRecyclerView;
    private ProductAdapter mProductAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_test);
        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();
        searchQuery = findViewById(R.id.search_query_edittext);
        searchButton = findViewById(R.id.search_button);
        searchQuery.addTextChangedListener(onSearchQueryEntered);
        mRecyclerView = findViewById(R.id.recyler_aliSearch_results);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ProductAdapter(this, new ArrayList<AliItem>()));
    }

    public void onAddItemManuallyClick(View view) {
        showAddItemDialog();
    }

    @Override
    public void applyItemData(String itemName, Double itemPrice, String itemLink) {
        AliItem itemToAdd = null;

        try {
            itemToAdd = new AliItem(itemName, itemPrice, itemLink, "");
            addItemToDatabase(itemToAdd);
        } catch (Exception e) {
            Toast.makeText(this, "Can't add item with missing attributes!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSearchButtonClick(View view) {
        String userQuery = searchQuery.getText().toString();
        searchAliExpress(userQuery);
        hideKeyboard();
    }

    private void searchAliExpress(String itemSearchQuery) {
        String url = "https://api.aliseeks.com/v1/search";

        JsonObject request = new JsonObject();
        request.addProperty("text", itemSearchQuery);
        request.addProperty("sort", "BEST_MATCH");
        request.addProperty("currency", "USD");
        //request.addProperty("limit", 5);

        Log.d(DEBUG_TAG, "The POST request is: " + request.toString());

        Ion.with(this)
                .load(url)
                .addHeader("X-Api-Client-Id", apiAccessKey)
                .setJsonObjectBody(request)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(DEBUG_TAG,"Result : " + result.toString());
                        processJsonResponse(result);
                    }
                });

    }

    private void processJsonResponse(JsonObject jsonObject) {
        //TODO
        JsonArray itemsResults = jsonObject.getAsJsonArray("items");
        ArrayList<AliItem> productsList = new ArrayList<>();
        for (int i = 0; i < itemsResults.size(); i++) {
            JsonObject currentItem = itemsResults.get(i).getAsJsonObject();
            String itemName = currentItem.get("title").toString().replace("\"", "");
            String imageUrl = currentItem.get("imageUrl").toString().replace("\"","");
            double itemPrice = currentItem.get("price").getAsJsonObject().get("value").getAsDouble();
            String itemUrl = currentItem.get("detailUrl").getAsString();
            try {
                productsList.add(new AliItem(itemName, itemPrice, itemUrl, imageUrl));
            } catch (Exception e) {
                Log.d(DEBUG_TAG, "Couldn't add item to arraylist");
            }
        }
        displaySearchResults(productsList);


    }

    private void displaySearchResults(ArrayList<AliItem> results){
        mProductAdapter = new ProductAdapter(AliSearch.this, results);
        mRecyclerView.setAdapter(mProductAdapter);
        aliAddItemHandler();
    }

    private void aliAddItemHandler(){
        mProductAdapter.setOnAddListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AliItem newitem = mProductAdapter.getIndexedItem(position);
                addItemToDatabase(newitem);

            }
        });

    }


    private void showAddItemDialog() {
        AddItemDialog addItemDialog = new AddItemDialog();
        addItemDialog.show(getSupportFragmentManager(), "addItemDialog");
    }


    private void addItemToDatabase(final AliItem item) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("userWishlist");
        database.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    fbUserKey = dataSnapshot.getChildren().iterator().next().getKey();
                    Log.d(DEBUG_TAG + ": Firebase", "User key to write to fb is: " + fbUserKey);
                    DatabaseReference newItem = database.child(fbUserKey + "/wishlist").push();
                    newItem.child(NAME).setValue(item.getItemName());
                    newItem.child(PRICE).setValue(item.getItemPrice());
                    newItem.child(LINK).setValue(item.getItemLink());
                    String imageUrl = item.getImageUrl();
                    newItem.child(IMAGE_URL).setValue(imageUrl);


                    Toast.makeText(AliSearch.this, item.getItemName() + " has been added to your list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //needs to be empty to compile
            }
        });

    }

    private TextWatcher onSearchQueryEntered = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userInput = searchQuery.getText().toString();
            if (userInput.isEmpty() || userInput.trim().length() == 0) {//empty or whitespace
                searchButton.setEnabled(false);
                searchButton.setBackgroundColor(getResources().getColor(R.color.disable_grey));
            } else {
                searchButton.setEnabled(true);
                searchButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //empty but neccesary to compile
        }
    };
    private void searchAliBestSelling() {

        String url = "https://api.aliseeks.com/v1/search";

        JsonObject request = new JsonObject();
        request.addProperty("sort", "MONTHLY_TRANSACTION_RATE");
        request.addProperty("currency", "USD");
        request.addProperty("category", 100003070);

        Log.d(DEBUG_TAG, "The POST request is: " + request.toString());

        Ion.with(this)
                .load(url)
                .addHeader("X-Api-Client-Id", apiAccessKey)
                .setJsonObjectBody(request)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(DEBUG_TAG, "The JSON result is " + result.toString());
                    }
                });
    }
    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}


