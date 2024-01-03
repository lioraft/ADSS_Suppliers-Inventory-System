package Suppliers.BusinessLayer;

public abstract class DiscountByOrder extends Discount {

    protected double minimalPrice;

    // description of class: this discount represents discount that needs to be applied to total order, and not just by one type
    // of product
    public DiscountByOrder(double val, double price) { // constructor
        super(val);
        minimalPrice = price;
    }

    // function that checks if an order is eligible for discount
    public boolean isEligibleForDiscount(int products, double totalPrice) {
        return (totalPrice >= minimalPrice);
    }

    // function that returns the minimal price required for this discount
    public double getMinimalPrice() {return minimalPrice;}
}
