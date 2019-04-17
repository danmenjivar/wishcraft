package fiveguys.com.wishcraftapp;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class AliTest extends AppCompatActivity {

    private static final String DEBUG_TAG = "Ali";
    private final String apiAccessKey = "YNZBUIVFBSOPLNSO";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_test);
    }

    private void searchAliExpress(String itemSearchQuery) {
        String url = "https://api.aliseeks.com/v1/search";


        JsonObject request = new JsonObject();
        request.addProperty("text", itemSearchQuery);
        request.addProperty("sort", "BEST_MATCH");
        request.addProperty("currency", "USD");


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

    public void onSearchButtonClick(View view) {
        EditText userInput = findViewById(R.id.search_query_edittext);
        String userQuery = userInput.getText().toString();
        searchAliExpress(userQuery);
        //searchAliBestSelling();
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

    private void processJson(JsonObject jsonObject) {
        Log.d(DEBUG_TAG, "The JSON result is " + String.format("%s", jsonObject.toString()));
    }


}
