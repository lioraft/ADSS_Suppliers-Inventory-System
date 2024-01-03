package Inventory.BusinessLayer;

import java.time.LocalDate;

public class Item {
    String size=null;
    private int producerID;
    private String name;

    private Branch branch;

    public void setDefectiveDescription(String reason) {
        this.defDescription = reason;
    }

    public Location getLocation() {
        return currentLocation;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch1) {
        this.branch = branch1;
    }


    public enum Location {STORE, INVENTORY ,SOLD};
    private Location currentLocation;
    private boolean isExpired = false;
    private LocalDate expirationDate;
    private  LocalDate dateOfArrival;
    //המחיר ששילמנו על המוצר מהספק
    private double costPrice;
    //המחיר שעלה המוצר
    private double sellingPrice;
    //המחיר שהמוצר נמכר בו!
    private double thePriceBeenSoldAt=-1 ;
    private boolean isDefective = false;
    private final int makat;
    private final int barcode;
    private String defDescription;



    public Item(int producer, int barcode, String name, Location currentLocation, String expirationDate, double costPrice, int makat , String branch) {
        this.producerID = producer;
        this.name = name;
        this.currentLocation = currentLocation;
        try {
            if(expirationDate == null || expirationDate.equals("null"))
                this.expirationDate = null;
            else
                this.expirationDate = LocalDate.parse(expirationDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }
        this.costPrice = costPrice;
        this.makat = makat;
        this.barcode = barcode;

        this.branch=Branch.valueOf(branch);
        this.sellingPrice = (double) Math.round(costPrice*(1.17+0.2)* 100)/ 100;        this.dateOfArrival=LocalDate.now();
    }

    public Item(int producer, int barcode, String name, Location currentLocation, String expirationDate, double costPrice, int makat , String size, String branch) {
        this.producerID = producer;
        this.name = name;
        this.size=size;
        this.currentLocation = currentLocation;
        try {
            if(expirationDate == null || expirationDate.equals("null"))
                this.expirationDate = null;
            else
                this.expirationDate = LocalDate.parse(expirationDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }
        this.costPrice = costPrice;
        this.makat = makat;
        this.barcode = barcode;
        this.branch=Branch.valueOf(branch);
        this.sellingPrice = (double) Math.round(costPrice*(1.17+0.2)* 100)/ 100;
        this.dateOfArrival=LocalDate.now();
    }
    @Override
    public String toString(){
        String categoryString = "";
        String productString = "";
        try {
            Category category = CategoryController.getInstance().getCategoryByProductID(makat);
            categoryString = category.toString();
            Product product = ProductController.getInstance().getProductById(makat);
            productString = product.toString();
        } catch (IllegalArgumentException e) {
            // handle the exception or log an error message
        }
        if(isDefective)
            return "producer: " + producerID + " name: " + name + " currentLocation: " + currentLocation + " isExpired: " + isExpired + " expirationDate: " + expirationDate + " costPrice: " + costPrice + " sellingPrice: " + sellingPrice + " isDefective: " + isDefective + " defDescription: " + defDescription + " productID: " + makat + " category: " + categoryString + " product: " + productString;
        return "producer: " + producerID + " name: " + name + " currentLocation: " + currentLocation + " isExpired: " + isExpired + " expirationDate: " + expirationDate + " costPrice: " + costPrice + " sellingPrice: " + sellingPrice + " isDefective: " + isDefective + " productID: " + makat + " category: " + categoryString + " product: " + productString + " size: " + size + " branch: " + branch+" dateOfArrival: "+dateOfArrival;
    }


    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public boolean isDefective() {
        return isDefective;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentLocation(String currentLocation) {
        switch (currentLocation) {
            case "STORE":
                this.currentLocation = Location.STORE;
                break;
            case "INVENTORY":
                this.currentLocation = Location.INVENTORY;
                break;
            case "SOLD":
                this.currentLocation = Location.SOLD;
                break;
            default:
                throw new IllegalArgumentException("Invalid location");
        }
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCostPrice(float costPrice) {
        this.costPrice = costPrice;
    }

//    public void setSellingPrice(double sellingPrice) {
//        this.sellingPrice = sellingPrice;
//    }

    public void setDefective(String defDescription) {
        this.defDescription = defDescription;
        isDefective = true;
    }
    /**
     * check if the item is expired
     * @return true if the item is expired
     */
    public boolean checkDate(){
        if(expirationDate == null)
            return false;
        LocalDate today = LocalDate.now();
        if (today.isAfter(expirationDate)){
            isExpired = true;
        }
        return isExpired;
    }

    public int getMakat(){
        return this.makat;
    }
    public double getThePriceBeenSoldAt() {
        return thePriceBeenSoldAt;
    }

    public LocalDate getDateOfArrival() {
        return dateOfArrival;
    }
    public void setDateOfArrival(LocalDate dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }
    public void setThePriceBeenSoldAt(double thePriceBeenSoldAt) {
        this.thePriceBeenSoldAt = thePriceBeenSoldAt;
    }

    public int getBarcode(){
        return this.barcode;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getPrice(){
        return this.sellingPrice;
    }
}