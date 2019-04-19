package fiveguys.com.wishcraftapp;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class AliTest extends AppCompatActivity implements AddItemDialog.AddItemDialogListener {

    private final static String NAME = "item_name";
    private final static String PRICE = "item_price";
    private final static String LINK = "item_link";
    private static final String DEBUG_TAG = "Ali";
    private final String apiAccessKey = "YNZBUIVFBSOPLNSO";
    private FirebaseAuth fbauth;
    private String userEmail;
    private String fbUserKey;
    private EditText searchQuery;
    private Button searchButton;
    private RecyclerView searchResultList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_test);
        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();
        searchQuery = findViewById(R.id.search_query_edittext);
        searchButton = findViewById(R.id.search_button);
        searchQuery.addTextChangedListener(onSearchQueryEntered);
        searchResultList = findViewById(R.id.recyler_aliSearch_results);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));


    }

    private void searchAliExpress(String itemSearchQuery) {
        String url = "https://api.aliseeks.com/v1/search";

        JsonObject request = new JsonObject();
        request.addProperty("text", itemSearchQuery);
        request.addProperty("sort", "BEST_MATCH");
        request.addProperty("currency", "USD");
        request.addProperty("limit", 5);

        Log.d(DEBUG_TAG, "The POST request is: " + request.toString());

        Ion.with(this)
                .load(url)
                .addHeader("X-Api-Client-Id", apiAccessKey)
                .setJsonObjectBody(request)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(DEBUG_TAG, String.format(result.toString()));
                        processJsonResponse(result);
                    }
                });

    }

    public void onSearchButtonClick(View view) {
        String userQuery = searchQuery.getText().toString();
        searchAliExpress(userQuery);
    }

    public void onAddItemManuallyClick(View view) {
        showAddItemDialog();
    }

    private void showAddItemDialog() {
        AddItemDialog addItemDialog = new AddItemDialog();
        addItemDialog.show(getSupportFragmentManager(), "addItemDialog");
    }

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

    private void processJsonResponse(JsonObject jsonObject) {
        //TODO
        JsonArray itemsResults = jsonObject.getAsJsonArray("items");
        final ArrayList<?> jsonArray = new Gson().fromJson(itemsResults, ArrayList.class);
        Log.d(DEBUG_TAG, "Arraylist: " + jsonArray);


    }

    @Override
    public void applyItemData(String itemName, Double itemPrice, String itemLink) {
        AliItem itemToAdd = null;

        try {
            itemToAdd = new AliItem(itemName, itemPrice, itemLink);
            addItemToDatabase(itemToAdd);
        } catch (Exception e) {
            Toast.makeText(this, "Can't add item with missing attributes!", Toast.LENGTH_SHORT).show();
        }
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //needs to be empty to compile
            }
        });

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View listedItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            listedItem = itemView;
        }

        public void setDetails(Context context, String itemName, double itemPrice, String itemImage){
            ImageView item_image = listedItem.findViewById(R.id.list_itemPicture);
            TextView item_name = listedItem.findViewById(R.id.item_title_list);
            TextView item_price = listedItem.findViewById(R.id.item_price_list);
            Button item_claim_button = listedItem.findViewById(R.id.item_add_button_list);

            item_name.setText(itemName);
            String prettyPrice = String.format("$%.2f", item_price);
            item_price.setText(prettyPrice);
            Glide.with(context).load(itemImage).into(item_image);


        }
    }

    private TextWatcher onSearchQueryEntered = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userInput = searchQuery.getText().toString();
            if (userInput.isEmpty() || userInput.trim().length() == 0){//empty or whitespace
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
}


