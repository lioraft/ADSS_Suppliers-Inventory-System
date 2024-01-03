package Suppliers.BusinessLayer;


import java.time.LocalDate;
import java.util.ArrayList;

public class Order {

    // static variable that counts number of orders and assign serial number
    static int numberOfOrders = 0;

    // description of class: this class represents an order made. it includes the id of the supplier, the branch the order needs to be sent to, the date in which the order
    // was made, the supplier contact and the status of the order.

    private int order_number;
    private ArrayList<OrderDetailsByProduct> ordered_products; // list of all the information of ordered products
    private String branch_code; // the destination branch of the order
    private LocalDate date; // the estimated date of delivery
    private Status order_status; // the status of the order
    private double total_price; // the total cost of the order
    private double orderDiscount; // discount given on total order

    public Order(LocalDate date, String destination) { // constructor
        numberOfOrders++;
        order_number = numberOfOrders; // assign order number
        branch_code = destination;
        this.date = date;
        order_status = Status.InProcess;
        ordered_products = new ArrayList<OrderDetailsByProduct>(); // setting empty list
        orderDiscount = 0; // discount 0 until finishing order and calculating final price
    }

    // function that returns the number of the order
    public int getOrderNumber() {return order_number;}

    // if order was already made, we alter the order number to the one in the database
    public void setOrderNumberForOrderFromDB(int order_number) {this.order_number = order_number;}


    // function that takes in product code, and returns the details of that product in the order
    public OrderDetailsByProduct getOrderDetailsOfProduct(int product_code) {
        for (int i = 0; i < ordered_products.size(); i++) {
            // if finds product, return details
            if (ordered_products.get(i).getProductCode() == product_code) {
                return ordered_products.get(i);
            }
        }
        // if cant find, return null
        return null;
    }

    // function that takes in the details of new ordered product, creates listing, adds it to list of ordered products and returns the details
    public OrderDetailsByProduct addProducts(int code, String name, int amount, double price, double discount, double finalprice) {
        OrderDetailsByProduct new_product = new OrderDetailsByProduct(code, name, amount, price, discount, finalprice);
        ordered_products.add(new_product);
        total_price = total_price + finalprice;
        return new_product;
    }

    // a function that takes in the code of product and the amount to cancel, and cancels it. if can cancel returns true. else,
    // returns false
    public boolean cancelProduct(int code) {
        for (int i = 0; i < ordered_products.size(); i++) {
            // if finds product, delete it
            if (ordered_products.get(i).getProductCode() == code) {
                    ordered_products.remove(i);
                    return true;
            }
        }
        return false;
    }

    // a function that cancels order
    public void cancelOrder() {
        order_status = Status.Canceled;
    }

    // a function that marks the ordered as completed
    public void confirmDelivery() {
        order_status = Status.Completed;
    }

    // getters for attributes
    public String getBranch() {return branch_code;}
    public LocalDate getDateOfOrder() {return date;}
    public Status getOrderStatus() {return order_status;}

    public void setOrderStatus(String status) {
        if (status.equals("InProcess")) {
            order_status = Status.InProcess;
        }
        else if (status.equals("Canceled")) {
            order_status = Status.Canceled;
        }
        else if (status.equals("Completed")) {
            order_status = Status.Completed;
        }
        else {
            throw new IllegalArgumentException("Invalid status");
        }
    }

    public void setOrderDiscount(double discount) {
        orderDiscount = discount;
    }

    public double getDiscount() {
        return orderDiscount;
    }

    public double getTotalPrice() {return total_price;}

    // function that takes in the order discounts of supplier's and applies best discount to order
    public void applyOrderDiscount(ArrayList<DiscountByOrder> discountsByOrder) {
        double price_before_discount = total_price;
        for (int i = 0; i < discountsByOrder.size(); i++) {
            double newPrice = discountsByOrder.get(i).applyDiscount(price_before_discount);
            if (total_price > newPrice && newPrice > 0) {
                total_price = newPrice;
                orderDiscount = price_before_discount - newPrice;
            }
        }
    }

    // toString method
    public String toString() {
        String toReturn = "Order number: " + order_number + "\nDate: " + date + "\nOrder status: " + order_status + "\nTotal price: " + total_price;
        toReturn = toReturn + "\nOrdered products: ";
        for (int i = 0; i < ordered_products.size(); i++) {
            OrderDetailsByProduct orderDetails = ordered_products.get(i);
            toReturn = toReturn + orderDetails.toString();
        }
        return toReturn;
    }

    // function that returns a list of the ordered products
    public ArrayList<OrderDetailsByProduct> getOrderedProducts() {
        return ordered_products;
    }
}
