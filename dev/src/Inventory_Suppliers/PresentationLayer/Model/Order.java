package Inventory_Suppliers.PresentationLayer.Model;

import Suppliers.ServiceLayer.ServiceController;
import java.util.ArrayList;
import java.util.HashMap;


public class Order {
    // Order properties, getters, setters, business logic, etc.
    private final ServiceController serviceController = ServiceController.getInstance();
    private final Inventory.ServiceLayer.ServiceController inventoryServiceController = Inventory.ServiceLayer.ServiceController.getInstance();

    public Order() {
        serviceController.starConnection();
        inventoryServiceController.starConnection();
    }

    public String addPeriodicOrder(String branchID, int supplierID, int productCode, int amount, int day) {
        return serviceController.addPeriodicOrder(branchID, supplierID, productCode, amount, day);
    }

    public boolean printOrdersOfSupplier(int id) {
        return serviceController.printOrdersOfSupplier(id);
    }

    public boolean printAllOrders() {
        return serviceController.printAllOrders();
    }

    public String makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        return serviceController.makeOrder(branch, productsToOrder, productsAndAmounts);
    }

    public String cancelOrder(int order_number) {
        return serviceController.cancelOrder(order_number);
    }

    public String confirmOrder(int order_number) {
        return serviceController.confirmOrder(order_number);
    }

    public String removePeriodicOrder(String branchID, int supplierID, int productCode, int day) {
        return serviceController.removePeriodicOrder(branchID, supplierID, productCode, day);
    }

    public String getAllOrders() {
        return serviceController.getAllOrders();
    }

    public String getOrdersOfSupplier(int supplierID) {
        return serviceController.getOrdersOfSupplier(supplierID);
    }
}
