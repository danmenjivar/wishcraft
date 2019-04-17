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
    private static String requestSearchURL = "https://api.aliseeks.com/v1/search/realtime";
    private final String apiAccessKey = "YNZBUIVFBSOPLNSO";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_test);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url = "https://api.aliseeks.com/v1/search/realtime";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            protected Map<String, String> getParams() {
//                Map<String, String> myData = new HashMap<>();
//                myData.put("Field", "Value");
//                return myData;
//            }
//        };
    }


    //{
    //  "text": "battery backup",
    //  "category": "200003482",
    //  "priceRange": {
    //    "from": 12.50,
    //    "to": 30.00
    //  },
    //  "attributes": [
    //    {
    //      "id": "16",
    //      "value": "256"
    //    }
    //  ],
    //  "shipToCountry": "US",
    //  "shipFromCountry": "CN",
    //  "sort": "BEST_MATCH",
    //  "skip": 20
    //}
    private void searchAliExpress(String itemSearchQuery){
        JsonObject json = new JsonObject();
        json.addProperty("X-Api-Client-Id", apiAccessKey); //append a header

        JsonObject request = new JsonObject();
        request.addProperty("text", "battery backup");
        request.addProperty("category", "200003482");

        JsonObject priceRange = new JsonObject();
        priceRange.addProperty("from", 12.50);
        priceRange.addProperty("to", 30.00);

        request.add("priceRange", priceRange);

        JsonArray attributes = new JsonArray();
        JsonObject attributesJson = new JsonObject();
        attributesJson.addProperty("id", "16");
        attributesJson.addProperty("value", "256");

        attributes.add(attributesJson);
        request.add("attributes", attributes);
        request.addProperty("shipToCountry", "US");
        request.addProperty("shipFromCountry", "CN");
        request.addProperty("sort", "BEST_MATCH");
        request.addProperty("skip", 20);

        json.add("searchRequest", json);

        Log.d(DEBUG_TAG, "The POST request is: " + json.toString());

        Ion.with(this)
                .load(requestSearchURL)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override

                    public void onCompleted(Exception e, JsonObject result) {
                        //do stuff with the result or error
                        Log.d(DEBUG_TAG,"The JSON result is: " + result.toString());
                    }
                });

    }

    public void onSearchButtonClick(View view){
        EditText userInput = findViewById(R.id.search_query_edittext);
        String userQuery = userInput.getText().toString();
//        searchAliExpress(userQuery);
        searchAliBestSelling();
    }


    private void searchAliBestSelling(){


        String url = "https://api.aliseeks.com/v1/search/bestSelling";


        JsonObject request = new JsonObject();
        request.addProperty("range", "top");
        request.addProperty("category", "all");
        request.addProperty("skip", "20");
        request.addProperty("locale", "en_US");
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
                        //do stuff with the result or error
                        //Log.d(DEBUG_TAG,"The JSON result is: " + result.toString());
                    }
                });
    }




//https://www.kompulsa.com/how-to-send-a-post-request-in-android/ tutorial


//        POST /v1/search/realtime HTTP/1.1
//        {
//            "text": "battery backup",
//                "category": "200003482",
//                "priceRange": {
//            "from": 12.50,
//                    "to": 30.00
//        },
//            "attributes": [
//            {
//                "id": "16",
//                    "value": "256"
//            }
//  ],
//            "shipToCountry": "US",
//                "shipFromCountry": "CN",
//                "sort": "BEST_MATCH",
//                "skip": 20
//        }
//




}
