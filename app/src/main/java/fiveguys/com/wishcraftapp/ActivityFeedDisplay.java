package fiveguys.com.wishcraftapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ActivityFeedDisplay{

    private String item_name;
    private double item_price;
    private String item_link;
    private String item_image_url;

    public ActivityFeedDisplay() {
        //empty on purpose
    }

    public ActivityFeedDisplay(String itemName, double itemPrice, String itemLink, String imageUrl) {
        this.item_name = itemName;
        this.item_price = itemPrice;
        this.item_link = itemLink;
        this.item_image_url = imageUrl;
    }

    public String getItemName() {
        return item_name;
    }

    public double getItemPrice() {
        return item_price;
    }

    public String getImageUrl() {
        return item_image_url;
    }

    public String getItemLink() {
        return item_link;
    }

    public void setItemName(String itemName) {
        this.item_name = itemName;
    }

    public void setItemPrice(double itemPrice) {
        this.item_price = itemPrice;
    }

    public void setItemLink(String itemLink) {
        this.item_link = itemLink;
    }

    public void setImageUrl(String imageUrl) {
        this.item_image_url = imageUrl;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("item_link", this.item_link);
        result.put("item_name", this.item_name);
        result.put("item_price", this.item_price);
        result.put("item_image_url", this.item_image_url);

        return result;
    }

    @Override
    public String toString() {
        return "AliItem{" +
                "itemName='" + item_name + '\'' +
                ", itemPrice=" + item_price +
                ", itemLink='" + item_link + '\'' +
                ", imageUrl='" + item_image_url + '\'' +
                '}';
    }
}