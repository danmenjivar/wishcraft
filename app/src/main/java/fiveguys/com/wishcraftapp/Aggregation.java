
package fiveguys.com.wishcraftapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aggregation {

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("skip")
    @Expose
    private Integer skip;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("scrollIdentifier")
    @Expose
    private String scrollIdentifier;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getScrollIdentifier() {
        return scrollIdentifier;
    }

    public void setScrollIdentifier(String scrollIdentifier) {
        this.scrollIdentifier = scrollIdentifier;
    }

}
