package Inventory_Suppliers.PresentationLayer.Controller;
import Inventory_Suppliers.PresentationLayer.Model.Order;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderControllerNVC {
    // Handle actions related to order processing
    private Order order;
    private static OrderControllerNVC instance;
    private OrderControllerNVC() {order = new Order ();}
    public static OrderControllerNVC getInstance() {
        if(instance == null) {
            instance = new OrderControllerNVC();
        }
        return instance;
    }
    public String addPeriodicOrder(String branchID, int supplierID, int productCode, int amount, int day) {
        return order.addPeriodicOrder(branchID, supplierID, productCode, amount, day);
    }

    public String makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        return order.makeOrder(branch, productsToOrder, productsAndAmounts);
    }
    public String cancelOrder(int order_number) {
        return order.cancelOrder(order_number);
    }
    public String confirmOrder(int order_number) {
        return order.confirmOrder(order_number);
    }
    public String removePeriodicOrder(String branchID, int supplierID, int productCode, int day) {
        return order.removePeriodicOrder(branchID, supplierID, productCode, day);
    }

    public String getAllOrders() {
        return order.getAllOrders();
    }

    public String getOrdersOfSupplier(int supplierID) {
        return order.getOrdersOfSupplier(supplierID);
    }
}
