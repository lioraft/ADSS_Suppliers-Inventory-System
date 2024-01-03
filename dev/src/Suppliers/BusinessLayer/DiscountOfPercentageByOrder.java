package Suppliers.BusinessLayer;

public class DiscountOfPercentageByOrder extends DiscountByOrder {

    // description of class: this class represents a discount that should be applied to order by total price of order,
    // meaning its price should be at least minimal price. a reduction will be made in the total price by percentage

    public DiscountOfPercentageByOrder(double val, double minPrice) {
        super(val, minPrice);
    }

    // function that calculates the amount of money reduced in discount
    public double calculateDiscount(double price) {return (value*price/100);}
}
