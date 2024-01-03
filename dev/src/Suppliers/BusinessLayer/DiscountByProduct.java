package Suppliers.BusinessLayer;

public abstract class DiscountByProduct extends Discount {
    int numOfProducts;

    // description of class: this discount by specific product, meaning an order will be eligible for this discount when ordering
    // more than number of units of the same product
    public DiscountByProduct(double val, int products) { // constructor
        super(val);
        numOfProducts = products;
    }

    // function that checks if an order is eligible for discount
    public boolean isEligibleForDiscount(int products, double totalPrice) {
        return (products >= numOfProducts);
    }

    public int getNumOfProducts() { return numOfProducts;}
}
