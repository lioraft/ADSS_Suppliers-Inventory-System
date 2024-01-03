package Inventory.BusinessLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Product {
    private String name;
    private int minAmount;
    private HashMap<Branch, Integer> currentAmount;
    private int amountInStore;
    private int amountInInventory;
    private int categoryID;
    private int makat;
    private  int supplierID = -1;
    private final ArrayList<String> discounts;
    private final String sub_category;


    private double discount;
    private LocalDate startDiscount;
    private LocalDate endDiscount;





    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public Product(String name, int minAmount, int categoryID,String sub_category, int makat , int supplierID) {
        this.name = name;
        this.minAmount = minAmount;
        this.currentAmount = new HashMap<>();
        this.amountInStore = 0;
        this.amountInInventory = 0;
        this.categoryID = categoryID;
        this.sub_category = sub_category;
        this.makat = makat;
        this.supplierID = supplierID;
        this.discounts = new ArrayList<>();

    }


    public int getMakat() {
        return makat;
    }

    public void setMakat(int makat) {
        this.makat = makat;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int deliveryTime, int demand ) {
        int storeState=demand*deliveryTime;
        if(minAmount<=storeState)
            this.minAmount = storeState;
    }

    public HashMap<Branch, Integer> getCurrentAmount() {
        return currentAmount;
    }
    public  Integer getCurrentAmount(String branch) {
        if(currentAmount.get(Branch.valueOf(branch))!=null){
         return currentAmount.get(Branch.valueOf(branch));
        }
        return 0;
    }
//
//    public void setCurrentAmount(Branch branch, int amount) {
//        this.currentAmount.put(branch, amount);
//    }
    public void setCurrentAmount(HashMap<Branch, Integer> amount) {
        for(Branch branch : Branch.values())
            this.currentAmount.put(branch, amount.get(branch));
    }

    public int getAmountInStore() {
        return amountInStore;
    }

    public void setAmountInStore(int amountInStore) {
        this.amountInStore =- amountInStore;
    }

    public int getAmountInInventory() {
        return amountInInventory;
    }

    public void setAmountInInventory(int amountInInventory) {
        this.amountInInventory =- amountInInventory;
    }

    public int getCategory() {
        return categoryID;
    }

    public void setCategory(int category) {
        this.categoryID = category;
    }

    //this method is used to reduce items from inventory and
    // raise a warning if the amount is below the minimum amount
    public void reduceItems(int amount, Branch branch){
        if(amount <= this.currentAmount.get(branch))
            this.currentAmount.put(branch, this.currentAmount.get(branch) - amount);
//        if(minAmount >= this.currentAmount.get(branch)) {
//            //in the future we will send a request to deliveries to get from the inventory
//
//            System.out.println("Warning: The amount of " + name + "in Site " + branch.toString() + " is below the minimum amount");
//            //TODO: send request to urgent supply for this product at this branch
//            ArrayList<Integer> products = new ArrayList<>();
//            products.add(makat);
//            HashMap<Integer, Integer> Amount = new HashMap<>();
//            Amount.put(makat, getMinAmount() * 3);
//            inventoryIntegrator.makeOrder(branch.toString(), products, Amount);
//        }
    }

    //function that return if the amount is bigger then the minimum amount
    public boolean isAmountBiggerThenMinAmount(String branch, int amount){
        return minAmount >= this.currentAmount.get(Branch.valueOf(branch))-amount;
    }
    //when we add items its only into the inventory
    public void addItems(int amount){
        this.amountInInventory += amount;
    }
    public void addItemsToStore(int amount){
        this.amountInStore += amount;
    }
    public void addItemsToInventory(int amount){
        this.amountInInventory += amount;
    }
    public void reduceItemsFromStore(int amount, Branch branch){
        if(amount <= this.amountInStore) {
            this.amountInStore -= amount;
            reduceItems(amount, branch);
        }
    }
    public void reduceItemsFromInventory(int amount){
        if(amount <= this.amountInInventory) {
            this.amountInInventory -= amount;
            //reduceItems(amount);
        }
    }
    //    public void setSupplierID(int supplierID) {
//        this.supplierID = supplierID;
//    }
    public int getSupplierID() {
        return supplierID;
    }


    public void setDiscount(String start, String end, double discount) {
        try{
            LocalDate startDiscount = LocalDate.parse(start);
            LocalDate endDiscount = LocalDate.parse(end);
            this.discount=discount;
            this.startDiscount=startDiscount;
            this.endDiscount=endDiscount;
        }
        catch (Exception e){
            System.out.println("Error in date format");
        }
    }


    public double getDiscount() {
        if(startDiscount == null || endDiscount == null)
            return 0;
        LocalDate now = LocalDate.now();
        if(now.isBefore(endDiscount) && now.isAfter(startDiscount))
            return discount;
        else
            return 0;

    }

    public String toString(){
        return "Product name: "+name+" Product id: "+makat+" Product sub category: "+sub_category;
    }

//    public void setDiscountByProductID(int supplierID, Double discount) {
//        discounts.add(discount);
//    }

    public ArrayList<String> getDiscounts() {
        return discounts;
    }
    public void setDiscounts(double discount, String start, String end) {
        discounts.add("Discount: "+discount+" Start date: "+start+" End date: "+end);

    }
    public void setDiscountsList(ArrayList<String> discounts) {
        this.discounts.addAll(discounts);
    }
}