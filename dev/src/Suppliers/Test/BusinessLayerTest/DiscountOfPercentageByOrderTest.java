package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.DiscountOfPercentageByOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountOfPercentageByOrderTest { // a class that tests the discounts on order by percentage

    // a new discount that will be tested - 10% discount, given if price is above 500
    DiscountOfPercentageByOrder discount = new DiscountOfPercentageByOrder(10, 500);

    @Test
    void isEligibleForDiscount() {
        // discount should be eligible
        assertEquals(true, discount.isEligibleForDiscount(10, 700));
        // discount should not be eligible
        assertEquals(false, discount.isEligibleForDiscount(3, 120));
    }

    @Test
    void getMinimalPrice() {
        // should return 500
        assertEquals(500, discount.getMinimalPrice());
    }

    @Test
    void getValue() {
        // should return 10
        assertEquals(10, discount.getValue());
    }

    @Test
    void setValue() {
        // set value as 20 and then check it change
        discount.setValue(20);
        assertEquals(20, discount.getValue());
    }

    @Test
    void applyDiscount() {
        // applying 10% discount to value 1000, should return 900
        double price = discount.applyDiscount(1000);
        assertEquals(900, price);
    }

    @Test
    void calculateDiscount() {
        // applying 10% discount to value 1000, should return 100
        double discount_value = discount.calculateDiscount(1000);
        assertEquals(100, discount_value);
    }
}