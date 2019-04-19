
package fiveguys.com.wishcraftapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price_ {

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("value")
    @Expose
    private String value;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
