package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.DiscountOfPriceByProduct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountOfPriceByProductTest { // test class discount in price on order. testing only one method because all other method were inherited and
    // tested in other classes

    // discount of 200 in price,if more than 30 units of the same product were ordered
    DiscountOfPriceByProduct discount = new DiscountOfPriceByProduct(200, 30);

    @Test
    void calculateDiscount() { assertEquals(200, discount.calculateDiscount(800));}
}