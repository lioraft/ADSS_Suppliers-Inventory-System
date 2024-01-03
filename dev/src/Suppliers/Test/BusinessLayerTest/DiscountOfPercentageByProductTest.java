package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.DiscountOfPercentageByProduct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountOfPercentageByProductTest { // a class that tests the discounts when ordering units from specific product by percentage

    // a new discount that will be tested - 20% discount, given when ordering more than 50 units of the product
    DiscountOfPercentageByProduct discount = new DiscountOfPercentageByProduct(20, 50);

    @Test
    void isEligibleForDiscount() {
        // discount should be eligible
        assertEquals(true, discount.isEligibleForDiscount(70, 490));
        // discount should not be eligible
        assertEquals(false, discount.isEligibleForDiscount(10, 70));
    }

    @Test
    void getNumOfProducts() {
        // should return 50
        assertEquals(50, discount.getNumOfProducts());
    }

    @Test
    void getValue() {
        // should return 20
        assertEquals(20, discount.getValue());
    }

    @Test
    void setValue() {
        // set value as 15 and then check it change
        discount.setValue(15);
        assertEquals(15, discount.getValue());
    }

    @Test
    void applyDiscount() {
        // applying 20% discount to value 1000, should return 800
        double price = discount.applyDiscount(1000);
        assertEquals(800, price);
    }

    @Test
    void calculateDiscount() {
        // applying 20% discount to value 1000, should return 200
        double discount_value = discount.calculateDiscount(1000);
        assertEquals(200, discount_value);
    }
}