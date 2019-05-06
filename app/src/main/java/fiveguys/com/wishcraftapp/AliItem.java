package fiveguys.com.wishcraftapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class AliItem {

    private String itemName;
    private double itemPrice;
    private String itemLink;
    private String imageUrl;

    public AliItem() {
        //empty on purpose
    }

    public AliItem(String itemName, double itemPrice, String itemLink, String imageUrl) throws Exception {
        if (itemName.isEmpty() || itemPrice <= 0.0)
            throw new Exception("Empty fields");
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemLink = itemLink;
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("item_link", this.itemLink);
        result.put("item_name", this.itemName);
        result.put("item_price", this.itemPrice);
        result.put("item_image_url", this.imageUrl);

        return result;
    }

    @Override
    public String toString() {
        return "AliItem{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemLink='" + itemLink + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}