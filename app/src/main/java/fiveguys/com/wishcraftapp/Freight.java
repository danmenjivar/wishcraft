
package fiveguys.com.wishcraftapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Freight {

    @SerializedName("price")
    @Expose
    private Price_ price;
    @SerializedName("type")
    @Expose
    private String type;

    public Price_ getPrice() {
        return price;
    }

    public void setPrice(Price_ price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
