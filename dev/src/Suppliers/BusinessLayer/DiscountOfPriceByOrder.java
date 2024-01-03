package Suppliers.BusinessLayer;

public class DiscountOfPriceByOrder extends DiscountByOrder {
    // description of class: this class represents a discount that should be applied to order by total price,
    // meaning its cost should be at least the minimal price. a reduction will be made by fixed price value

    public DiscountOfPriceByOrder(double val, double minPrice) {
        super(val, minPrice);
    }

    // function that calculates the amount of money reduced in discount
    public double calculateDiscount(double price) {return value;}
}
