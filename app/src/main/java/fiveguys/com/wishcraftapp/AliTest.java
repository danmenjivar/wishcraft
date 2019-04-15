package fiveguys.com.wishcraftapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class AliTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ali_test);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.aliseeks.com/v1/search/realtime";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        });

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
}
