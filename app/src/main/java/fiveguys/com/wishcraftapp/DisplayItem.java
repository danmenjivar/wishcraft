package fiveguys.com.wishcraftapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DisplayItem {
    public static String name_tag = "item_name";
    public static String url_tag = "item_link";
    public static String price_tag = "item_price";
    public static String image_tag = "item_image_url";

    private String item_name;
    private double item_price;
    private String item_link;
    private String item_image_url;

    public DisplayItem() {
        //empty on purpose
    }

    public DisplayItem(String itemName, double itemPrice, String itemLink, String imageUrl) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayItem that = (DisplayItem) o;
        return Double.compare(that.item_price, item_price) == 0 &&
                Objects.equals(item_name, that.item_name) &&
                Objects.equals(item_link, that.item_link) &&
                Objects.equals(item_image_url, that.item_image_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_name, item_price, item_link, item_image_url);
    }
}