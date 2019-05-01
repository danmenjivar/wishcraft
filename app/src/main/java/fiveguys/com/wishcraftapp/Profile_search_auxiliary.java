package fiveguys.com.wishcraftapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Profile_search_auxiliary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_search_auxiliary);
    }

    private String friendName;

    public Profile_search_auxiliary() {
        //empty on purpose
    }

    public Profile_search_auxiliary(String itemName, double itemPrice, String itemLink, String imageUrl) throws Exception {
        if (itemName.isEmpty() || itemPrice <= 0.0)
            throw new Exception("Empty fields");
        this.friendName = itemName;
    }

    public String getItemName() {
        return friendName;
    }

    public void setItemName(String itemName) {
        this.friendName = itemName;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("item_name", this.friendName);

        return result;
    }

    @Override
    public String toString() {
        return "AliItem{" +
                "itemName='" + friendName + '\'' +
                '}';
    }
}
