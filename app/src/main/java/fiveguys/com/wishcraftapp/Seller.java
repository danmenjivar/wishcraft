
package fiveguys.com.wishcraftapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seller {

    @SerializedName("storeName")
    @Expose
    private String storeName;
    @SerializedName("positiveFeedback")
    @Expose
    private Integer positiveFeedback;
    @SerializedName("totalFeedback")
    @Expose
    private Integer totalFeedback;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getPositiveFeedback() {
        return positiveFeedback;
    }

    public void setPositiveFeedback(Integer positiveFeedback) {
        this.positiveFeedback = positiveFeedback;
    }

    public Integer getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalFeedback(Integer totalFeedback) {
        this.totalFeedback = totalFeedback;
    }

}
