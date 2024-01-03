package Inventory_Suppliers;

import Suppliers.BusinessLayer.Order;

import java.util.ArrayList;
import java.util.HashMap;

public interface InventoryIntegrator {
    public Order getPeriodicOrder(int order_number);

    // branch - the branch that ordered the products
    // productsToOrder - arraylist of product codes
    // productsAndAmounts - hashmap of product codes and amounts to order of each product
    public ArrayList<Order> makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts);
    // send the arguments to function OrderController.getInstance().makeOrder(branch, productsToOrder, productsAndAmounts); and it will make the order

}
