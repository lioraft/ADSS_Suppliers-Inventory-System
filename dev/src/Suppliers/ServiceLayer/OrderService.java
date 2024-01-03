package Suppliers.ServiceLayer;

import Suppliers.BusinessLayer.OrderController;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderService {

    //connect to OrderController
    private final OrderController orderController;

    //constructor
    public OrderService() {
        orderController = OrderController.getInstance();
    }

    public void addPeriodicOrder(String branchID, int supplierID, int productCode, int amount, int day) {
        orderController.addPeriodicOrder(branchID, supplierID, productCode, amount, day);
    }

    public void printOrders() {
        orderController.printAllOrders();
    }

    public void makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        orderController.makeOrder(branch, productsToOrder, productsAndAmounts);
    }

    public void cancelOrder(int orderID) {
        orderController.cancelOrder(orderID);
    }

    public void confirmOrder(int orderID) {
        orderController.confirmOrder(orderID);
    }

    // function that runs fixed orders of tomorrow
    public void makeFixedOrders() {
        orderController.scheduleFixedOrder();
    }

    // function that removes a periodic order of a supplier
    public void removePeriodicOrder(int day, int supplierID, int productCode, String branchID) {
        orderController.deletePeriodicOrder(day, supplierID, productCode, branchID);
    }

    // function that prints the orders of a supplier
    public void printOrdersOfSupplier(int id) {
        orderController.printOrdersOfSupplier(id);
    }

    public String getAllOrders() {
        return orderController.getAllOrders();
    }

    public String getOrdersOfSupplier(int supplierID) {
        return orderController.getOrdersOfSupplier(supplierID);
    }

    public void startConnection() {
        orderController.startConnection();
    }
}
