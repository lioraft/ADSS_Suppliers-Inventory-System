package Inventory.ServiceLayer;

import Inventory.BusinessLayer.Item;
import Inventory.BusinessLayer.ItemController;

import java.util.ArrayList;

public class ItemService {
    //connect to item controller
    private final ItemController itemController;

    public ItemService() {
        this.itemController = ItemController.getInstance();
    }
    // AddItem(Place:string,Manufacturer:string ,Integer c )
    public void addItem(Integer manufacturer , Integer barcode, String name, String expirationDate, double costPrice , int category, int productID,String size,String branch){
        itemController.addItem( manufacturer, barcode, name, expirationDate, costPrice ,category,productID,size,branch);
    }

    // getItem(ID: int ,barcode: int)
    public void getItem(int CategoryID,int ItemID) {
        itemController.getItem(CategoryID,ItemID);
    }

    //move item to store
    public void moveItemToStore(int CategoryID,int ItemID,String branch){
        itemController.moveItemToStore(CategoryID,ItemID,branch);
    }

    //update Item has been sold
    public void ItemSold(int CategoryID,int ItemID){
        itemController.itemSold(CategoryID,ItemID);
        //need to update product amount
    }
    //return a list of items in stock (both at store and storage)
    //I think that this function not needed any more. what do you say?/
    public ArrayList<Item> getItemsInStock(int categoryID) {
        return itemController.itemsInStock(categoryID);
    }
    /*Make item a Defective */
//    public void setDefectiveItems(Integer items,int categoryId, String reason){
//        itemController.defective(items,categoryId, reason);
//    }

    public void setDaysToReport(int days){
        itemController.setDaysToReport(days);
    }


    public void getExpiredReport() {
        itemController.getExpiredReport();
    }

    public void getToBeExpiredReport(int days) {
        itemController.getToBeExpiredReport(days);
    }

    public void getDefectiveReport() {
        itemController.getDefectiveReport();
    }

    public void getInventoryReport() {
        itemController.getInventoryReport();
    }

    public ArrayList<Item> getItemsInStore() {
        return itemController.getItemsInStore();
    }

    public double getPrice(int barcode) {
        return itemController.getPrice(barcode);
    }

    public void startConnection() {
        itemController.startConnection();
    }

    public boolean setDefectiveItem(int barcode, int categoryID, String reason) {
        return itemController.defective(barcode, categoryID, reason);
    }


    public String publishInventoryReportS() {
        return itemController.publishInventoryReportS();
    }

    public String publishDefectiveReportS() {
        return itemController.publishDefectiveReportS();
    }

    public String publishExpiredReportS() {
        return itemController.publishExpiredReportS();
    }

    public String getToBeExpiredReportS(int i) {
        return itemController.getToBeExpiredReportS(i);
    }

    public String ItemSoldP(int categoryID, int itemID) {
        return itemController.ItemSoldP(categoryID, itemID);
    }

    public String setDefectiveItemP(int barcode, int categoryID, String reason) {
        return itemController.setDefectiveItemP(barcode, categoryID, reason);
    }
}