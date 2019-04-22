package fiveguys.com.wishcraftapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.util.ArrayList;


public class ItemSearchFragment extends Fragment {
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

    public ItemSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchQuery = getView().findViewById(R.id.search_query_edittext);
        searchButton = getView().findViewById(R.id.search_button);
        searchQuery.addTextChangedListener(onSearchQueryEntered);
        mRecyclerView = getView().findViewById(R.id.recyler_aliSearch_results);
        mRecyclerView.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(new ProductAdapter(this.getActivity(), new ArrayList<AliItem>()));
    }

    public void onAddItemManuallyClick(View view) {
        showAddItemDialog();
    }

    //@Override
    public void applyItemData(String itemName, Double itemPrice, String itemLink) {
        AliItem itemToAdd = null;

        try {
            itemToAdd = new AliItem(itemName, itemPrice, itemLink, null);
            addItemToDatabase(itemToAdd);
        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "Can't add item with missing attributes!", Toast.LENGTH_SHORT).show();
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
        mProductAdapter = new ProductAdapter(ItemSearchFragment.this.getActivity(), results);
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
                    if (imageUrl != null && !imageUrl.isEmpty()){
                        newItem.child(IMAGE_URL).setValue(imageUrl);
                    }

                    Toast.makeText(ItemSearchFragment.this.getActivity(), item.getItemName() + " has been added to your list", Toast.LENGTH_SHORT).show();
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