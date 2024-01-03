package Inventory_Suppliers.PresentationLayer.Controller;

import Inventory.BusinessLayer.Product;
import Inventory_Suppliers.PresentationLayer.Model.Item;

import java.util.ArrayList;

public class InventoryController {
    private static InventoryController instance;
    private Item item;

    private InventoryController() {
        item = new Item();
    }
    public static InventoryController getInstance() {
        if(instance == null) {
            instance = new InventoryController();
        }
        return instance;
    }

    public void getInventoryReport() {
        // Get the inventory report from the model
        item.getInventoryReport();
    }

    public String publishInventoryReport() {
        // Publish the inventory report to the view
        return item.publishInventoryReport();
    }

    public String publishDefectiveReport() {
        // Publish the inventory report to the view
        return item.publishDefectiveReport();
    }

    public String publishExpiredReport() {
        // Publish the inventory report to the view
        return item.publishExpiredReport();
    }

    public String getDiscountByProductId(String productId) {
        return item.getDiscountByProductId(productId);
    }

    public ArrayList<StringBuilder> publishInventoryReportByCategory(ArrayList<Integer> categories) {
        return item.publishInventoryReportByCategory(categories);
    }

    public ArrayList<Product> publishProductByCategory(int i) {
        return item.publishProductByCategory(i);
    }

    public String publishToBeExpiredReport(int i) {
        return item.publishToBeExpiredReport(i);
    }

    public int publishAmountOfProduct(Integer productId, String productBranch) {
        return item.publishAmountOfProduct(productId, productBranch);
    }

    public String addCategory(String categoryName, int categoryId) {
        if(item.addCategory(categoryName, categoryId)) {
            return "Category added successfully";
        } else {
            return "Category already exists";
        }
    }

    public boolean addProduct(String name, int minAmount, int categoryID,String sub_category, int makat , int supplierID){
        return item.addProduct(name, minAmount, categoryID, sub_category, makat, supplierID);
    }

    public String addItems(int manufacturer, int barcode, String name, String expirationDate, int costPrice, int categoryID2, int productID, String size, String branch, int amount) {
        try{
            for(int i = 0; i < amount; i++) {
               Boolean added=item.addItem(manufacturer, barcode, name, expirationDate, costPrice, categoryID2, productID, size, branch);
                if(!added){
                   return "Error adding items";
               }
            }
            return "Items added successfully";
        }
        catch (Exception e){
            return "Error adding items";
        }

    }

    public void setMinimumAmount(int deliveryTime, int demand, int productID) {
        item.setMinimumAmount(deliveryTime, demand, productID);
    }

    public void setDiscountByProduct(int productID, float discount, String start, String end) {
        item.setDiscountByProduct(productID, discount, start, end);
    }

    public void setDiscountByCategory(int categoryID, float discount, String start, String end) {
        item.setDiscountByCategory(categoryID, discount, start, end);
    }

    public void setHowOftenToGetDefectiveReport(int days) {
        item.setHowOftenToGetDefectiveReport(days);
    }

    public String sellItem(int categoryID, int itemID) {
        return item.sellItem(categoryID, itemID);
    }

    public String setDefectiveItem(int barcode, int categoryID, String reason) {
         return item.setDefectiveItem(barcode, categoryID, reason);
    }

    public boolean isProductExist(int i) {
        return item.isProductExist(i);
    }
    // Handle actions related to inventory management

}
