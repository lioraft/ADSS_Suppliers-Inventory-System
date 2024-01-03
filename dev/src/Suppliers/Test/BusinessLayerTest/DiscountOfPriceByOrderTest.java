package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.DiscountOfPriceByOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountOfPriceByOrderTest { // test class discount in price on order. testing only one method because all other method were inherited and
    // tested in other classes

    // discount of 50, if price is more than 700
    DiscountOfPriceByOrder discount = new DiscountOfPriceByOrder(50, 700);
    @Test
    void calculateDiscount() {
        assertEquals(50, discount.calculateDiscount(900));
    }
}