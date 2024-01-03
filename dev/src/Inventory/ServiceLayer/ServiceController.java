package Inventory.ServiceLayer;

import Inventory.BusinessLayer.Item;
import Inventory.BusinessLayer.Product;

import java.util.ArrayList;

public class ServiceController {
    private final Inventory.ServiceLayer.CategoryService categoryService;
    private final ProductService productService;
    private final ItemService itemService;
    private static ServiceController serviceController = null;

    private ServiceController() {
        categoryService = new CategoryService();
        productService = new ProductService();
        itemService = new ItemService();
    }

    public static ServiceController getInstance() {
        if(serviceController == null) {
            serviceController = new ServiceController();
        }
        return serviceController;
    }

    ///////////////////////////ItemService/////////////////////////////
    public Boolean addItem(Integer manufacturer, Integer barcode, String name, String expirationDate, double costPrice,int category, int productID ,String size,String branch){
        try{
            itemService.addItem(manufacturer, barcode, name, expirationDate, costPrice, category, productID ,size,branch);
        } catch (Exception e) {
            return false;
        }
        return true;

    }
    public void getItem(int CategoryID, int ItemID) {
        itemService.getItem(CategoryID, ItemID);
    }
    public void moveItemToStore(int CategoryID, int ItemID, String branch) {
        itemService.moveItemToStore(CategoryID, ItemID, branch);
    }
    public boolean getProductOrderUrgentStatus(int makat, String branch) {
        return productService.getProductOrderUrgentStatus(makat, branch);
    }
    public Boolean itemSold(int CategoryID, int ItemID) {
        try{
            itemService.ItemSold(CategoryID, ItemID);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public double getPrice(int barcode) {
        return itemService.getPrice(barcode);
    }
    public ArrayList<Item> getItemsInStock(int categoryID) {
        return itemService.getItemsInStock(categoryID);
    }

    ///////////////////////////ProductService/////////////////////////////
    public boolean addProduct(String name, int minAmount, int categoryID,String sub_category, int makat , int supplierID) {
        try{
            productService.addProduct(name, minAmount, categoryID, sub_category,makat , supplierID);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public void setMinimum(int deliveryTime, int demand ,int productID) {
        productService.setMinimum(deliveryTime, demand, productID);
    }
    public void setDiscountByProduct(int productID, float discount , String start, String end) {
        productService.setDiscountByProduct(productID, discount, start, end);
    }
    public ArrayList<Product> getProductsByCategory(int categoryID) {

        return productService.getProductsByCategory(categoryID);
    }

    ///////////////////////////CategoryService/////////////////////////////
    public boolean addCategory(String name, int id) {
        try{
            categoryService.addCategory(name, id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    //    public List<Product> getProductsInCategory(int id) {
//        categoryService.getProductsInCategory(id);
//    }
    public void setDiscountByCategory(int categoryID, float discount , String start, String end) {
        categoryService.setDiscountByCategory(categoryID, discount, start, end);
    }


    public void getExpiredReport() {
        itemService.getExpiredReport();
    }

    public void getToBeExpiredReport(int days) {
        itemService.getToBeExpiredReport(days);
    }

    public void getDefectiveReport() {
        itemService.getDefectiveReport();
    }

    public void setDefectiveReport(int days) {
        itemService.setDaysToReport(days);
    }

    public void getInventoryReport() {
        itemService.getInventoryReport();
    }

    public ArrayList<Item> getItemsInStore() {
        return itemService.getItemsInStore();
    }

    public int getAmountOfProduct(int productID,String branch) {
        return productService.getAmountOfProduct(productID,branch);
    }

//    public void setDiscountBySupplier(int supplierID, int productID, Double discount) {
//        productService.setDiscountBySupplier(supplierID, productID, discount);
//    }

    public ArrayList<String> getDiscountsByProductId(int productID) {
        return productService.getDiscountsByProductId(productID);
    }
    /*
        this function is for testing purposes only
     */
    public void addData() {
        String expirationDateStr = "2023-12-31";
        this.addCategory("Dairy products", 0);
        this.addCategory("Meat products", 1);
        this.addCategory("Housewares", 2);
        this.addProduct("Milk", 5, 0, "milk",0, 1234);
        this.addProduct("Cheese", 2, 0, "cheese",1, 1234);
        this.addProduct("Salami", 2, 1, "salami",2, 1234);
        this.addProduct("Beef Fillet ", 2, 1, "beef fillet",3, 1234);
        this.addProduct("Broom", 2, 2, "broom",4, 1234);
        this.addProduct("Pot", 2, 2,"pot" ,5, 1234);
        this.addItem(1234, 0, "Milk 3%", expirationDateStr, 6.9, 0, 0 ,"1L","A");
        this.addItem(1234, 1, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","A");
        this.addItem(1234, 2, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","A");
        this.addItem(1234, 3, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","A");
        this.addItem(1234, 4, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","B");
        this.addItem(1234, 5, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","B");
        this.addItem(1234, 6, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","B");
        this.addItem(1234, 7, "Milk 3%", expirationDateStr, 6.9, 0, 0,"1L","C");
        this.addItem(3691, 8, "Cheese 3%", expirationDateStr, 10, 0, 1,"250g","A");
        this.addItem(3691, 9, "Cheese 3%", expirationDateStr, 10, 0, 1,"250g","A");
        this.addItem(3691, 10, "Cheese 3%", expirationDateStr, 10, 0, 1,"250g","B");
        this.addItem(3691, 11, "Cheese 3%", expirationDateStr, 10, 0, 1,"250g","C");
        this.addItem(3691, 12, "Cheese 3%", expirationDateStr, 10, 0, 1,"250g","C");
        this.addItem(3691, 13, "Cheese 3%", expirationDateStr, 10, 0, 1,"250g","C");
        this.addItem(2345, 14, "Salami 5%", expirationDateStr, 15, 1, 2,"500g","A");
        this.addItem(2345, 15, "Salami 5%", expirationDateStr, 15, 1, 2 ,"500g","A");
        this.addItem(2345, 16, "Salami 5%", expirationDateStr, 15, 1, 2,"500g","B");
        this.addItem(2345, 17, "Salami 5%", expirationDateStr, 15, 1, 2, "500g","C");
        this.addItem(2345, 18, "Salami 5%", expirationDateStr, 15, 1, 2, "500g","G");
        this.addItem(2345, 19, "Salami 5%", expirationDateStr, 15, 1, 2 ,"500g","F");
        this.addItem(3456, 20, "Beef Fillet", expirationDateStr, 100, 1, 3,"1k","D");
        this.addItem(3456, 21, "Beef Fillet", expirationDateStr, 100, 1, 3 ,"1k","H");
        this.addItem(3456, 22, "Beef Fillet", expirationDateStr, 100, 1, 3, "1k","I");
        this.addItem(3456, 23, "Beef Fillet", expirationDateStr, 100, 1, 3, "1k","G");
        this.addItem(3456, 24, "Beef Fillet", expirationDateStr, 100, 1, 3 ,"1k","F");
        this.addItem(3456, 25, "Beef Fillet", expirationDateStr, 100, 1, 3, "1k","F");
        this.addItem(3456, 26, "Beef Fillet", expirationDateStr, 100, 1, 3,"1k","F");
        this.addItem(2580, 27, "Pot", null, 55, 2, 5 ,"1L","F");
        this.addItem(2580, 28, "Pot", null, 55, 2, 5,"1L","F");
        this.addItem(2580, 29, "Pot", null, 55, 2, 5,"0.5L","F");
        this.addItem(2580, 30, "Pot", null, 55, 2, 5,"0.5L","F");
        this.addItem(2580, 31, "Pot", null, 55, 2, 5,"0.5L","D");
        this.addItem(1473, 32, "Broom", null, 15, 2, 4,"","D");
        this.addItem(1473, 33, "Broom", null, 15, 2, 4,"","D");
        this.addItem(1473, 34, "Broom", null, 15, 2, 4,"","E");
        this.addItem(1473, 35, "Broom", null, 15, 2, 4,"","E");
        this.addItem(1473, 36, "Broom", null, 15, 2, 4,"","E");
        this.addItem(1473, 37, "Broom", null, 15, 2, 4,"","E");
        this.addItem(1473, 38, "Broom", null, 15, 2, 4,"","I");
        this.addItem(1473, 39, "Broom", null, 15, 2, 4,"","H");
        this.addItem(1473, 40, "XP", "2023-05-09", 15, 2, 5,"","H");
    }

    public ArrayList<StringBuilder> getInventoryReportByCategory(ArrayList<Integer> categoryList) {
        return categoryService.getReportOfItemsInStockByCategory(categoryList);
    }

    public void starConnection() {
        categoryService.startConnection();
        productService.startConnection();
        itemService.startConnection();
    }

    public void removeSampleData() {
        productService.removeSampleData();
    }


    public boolean setDefectiveItem(int barcode, int categoryID, String reason) {
        return itemService.setDefectiveItem(barcode, categoryID, reason);
    }

    public String publishInventoryReport() {
        return itemService.publishInventoryReportS();
    }

    public String publishDefectiveReport() {
        return itemService.publishDefectiveReportS();
    }

    public String publishExpiredReport() {
        return itemService.publishExpiredReportS();
    }

    public String getToBeExpiredReportS(int i) {
        return itemService.getToBeExpiredReportS(i);
    }


    public boolean isProductExist(int i) {
        return productService.isProductExist(i);
    }

    public String itemSoldP(int categoryID, int itemID) {
        String ret = "";
        try{
            ret = itemService.ItemSoldP(categoryID, itemID);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return ret;
    }

    public String setDefectiveItemP(int barcode, int categoryID, String reason) {
        String ret = "";
        try{
            ret = itemService.setDefectiveItemP(barcode, categoryID, reason);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return ret;
    }
}