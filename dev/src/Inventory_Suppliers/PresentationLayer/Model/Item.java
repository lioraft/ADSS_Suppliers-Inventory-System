package Inventory_Suppliers.PresentationLayer.Model;

import Inventory.BusinessLayer.Product;
import Inventory.ServiceLayer.ServiceController;

import java.util.ArrayList;

public class Item {
    private final ServiceController serviceController = ServiceController.getInstance();
    public Item() {
        serviceController.starConnection();
        Suppliers.ServiceLayer.ServiceController.getInstance().starConnection();
    }
    public void getInventoryReport() {
        // Get the inventory report from the model
        serviceController.getInventoryReport();
    }

    public String publishInventoryReport() {
        // Publish the inventory report to the view
        return serviceController.publishInventoryReport();
    }

    public String publishDefectiveReport() {
        // Publish the inventory report to the view
        return serviceController.publishDefectiveReport();
    }

    public String publishExpiredReport() {
        // Publish the inventory report to the view
        return serviceController.publishExpiredReport();
    }

    public String getDiscountByProductId(String productId) {
        StringBuilder discount = new StringBuilder();
        discount.append("Discount: ");
        try {
            discount.append(serviceController.getDiscountsByProductId(Integer.parseInt(productId)));
        } catch (Exception e) {
            discount.append("Invalid product ID");
        }

        return discount.toString();
    }

    public ArrayList<StringBuilder> publishInventoryReportByCategory(ArrayList<Integer> categories) {
        return serviceController.getInventoryReportByCategory(categories);
    }

    public ArrayList<Product> publishProductByCategory(int i) {
        return serviceController.getProductsByCategory(i);
    }

    public String publishToBeExpiredReport(int i) {
        return serviceController.getToBeExpiredReportS(i);
    }

    public int publishAmountOfProduct(Integer productId, String productBranch) {
        return serviceController.getAmountOfProduct(productId, productBranch);
    }

    public boolean addCategory(String categoryName, int categoryId) {
        return serviceController.addCategory(categoryName, categoryId);
    }

    public boolean addProduct(String name, int minAmount, int categoryID,String sub_category, int makat , int supplierID) {
        return serviceController.addProduct(name, minAmount, categoryID, sub_category, makat, supplierID);
    }

    public Boolean addItem(int manufacturer, int barcode, String name, String expirationDate, int costPrice, int categoryID2, int productID, String size, String branch) {
        return serviceController.addItem(manufacturer, barcode, name, expirationDate, costPrice, categoryID2, productID, size, branch);
    }

    public void setMinimumAmount(int deliveryTime, int demand, int productID) {
        serviceController.setMinimum(deliveryTime, demand, productID);
    }

    public void setDiscountByProduct(int productID, float discount, String start, String end) {
        serviceController.setDiscountByProduct(productID, discount, start, end);
    }

    public void setDiscountByCategory(int categoryID, float discount, String start, String end) {
        serviceController.setDiscountByCategory(categoryID, discount, start, end);
    }

    public void setHowOftenToGetDefectiveReport(int days) {
        serviceController.setDefectiveReport(days);
    }

    public String sellItem(int categoryID, int itemID) {
        return serviceController.itemSoldP(categoryID, itemID);
    }

    public String setDefectiveItem(int barcode, int categoryID, String reason) {
        return serviceController.setDefectiveItemP(barcode, categoryID, reason);
    }

    public boolean isProductExist(int i) {
        return serviceController.isProductExist(i);
    }
    // Item properties, getters, setters, business logic, etc.
}
