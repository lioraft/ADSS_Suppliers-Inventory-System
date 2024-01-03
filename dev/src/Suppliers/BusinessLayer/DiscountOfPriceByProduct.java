package Suppliers.BusinessLayer;

public class DiscountOfPriceByProduct extends DiscountByProduct {

    // description of class: an order will be eligible for discount when ordering more units of same product than minimal amount of units.
    // class is represented in percentage, meaning its value is the amount of money reduced from original price
    public DiscountOfPriceByProduct(double val, int products) {
        super(val, products);
    }

    // function that calculates the amount of money reduced in discount
    public double calculateDiscount(double price) {return value;}
}
