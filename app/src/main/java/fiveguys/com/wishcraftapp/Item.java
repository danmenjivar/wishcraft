
package fiveguys.com.wishcraftapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("detailUrl")
    @Expose
    private String detailUrl;
    @SerializedName("lotSize")
    @Expose
    private Integer lotSize;
    @SerializedName("lotUnit")
    @Expose
    private String lotUnit;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("ratings")
    @Expose
    private Integer ratings;
    @SerializedName("orders")
    @Expose
    private Integer orders;
    @SerializedName("freight")
    @Expose
    private Freight freight;
    @SerializedName("seller")
    @Expose
    private Seller seller;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Integer getLotSize() {
        return lotSize;
    }

    public void setLotSize(Integer lotSize) {
        this.lotSize = lotSize;
    }

    public String getLotUnit() {
        return lotUnit;
    }

    public void setLotUnit(String lotUnit) {
        this.lotUnit = lotUnit;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Integer getRatings() {
        return ratings;
    }

    public void setRatings(Integer ratings) {
        this.ratings = ratings;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Freight getFreight() {
        return freight;
    }

    public void setFreight(Freight freight) {
        this.freight = freight;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

}
