package Suppliers.BusinessLayer;

public abstract class Discount {
    // description of class: this class is the parent class of discount classes. it's abstract and therefore a discount's type must be
    // one of the inheritors
    protected double value; // the value of the discount

    public Discount(double val) {value = val;} // constructor

    // getter for class attribute
    public double getValue() {return value;}

    // setter for class attribute
    public void setValue(double newval) {value=newval;}

    // a method that takes in a price, and applies the discount to the given price
    public double applyDiscount(double price) {return (price - calculateDiscount(price));}

    // an abstract method that should be implemented in inheritors. it takes in a number of products and price of order, and checks if order is eligible for discount
    // based on the condition for discount
    public abstract boolean isEligibleForDiscount(int numOfProducts, double totalPrice);

    // function that calculates the amount of money reduced in discount
    public abstract double calculateDiscount(double price);


}
