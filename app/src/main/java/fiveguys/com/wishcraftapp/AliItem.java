package fiveguys.com.wishcraftapp;

public class AliItem {

    private String itemName;
    private double itemPrice;
    private String itemLink;

    public AliItem(String itemName, double itemPrice, String itemLink) throws Exception {
        if (itemName.isEmpty() || itemPrice <= 0.0)
            throw new Exception("Empty fields");
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemLink = itemLink;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
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
}