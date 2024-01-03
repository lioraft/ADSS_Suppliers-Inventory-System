package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.OrderDetailsByProduct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDetailsByProductTest {

    // initializing order details for test
    OrderDetailsByProduct orderdet = new OrderDetailsByProduct(1, "popcorn", 100, 5, 20, 480);
    @Test
    void getProductCode() {
        assertEquals(1, orderdet.getProductCode());
    }

    @Test
    void getProductName() {
        assertEquals("popcorn", orderdet.getProductName());
    }

    @Test
    void getAmount() {
        assertEquals(100, orderdet.getAmount());
    }

    @Test
    void getListPrice() {
        assertEquals(5, orderdet.getListPrice());
    }

    @Test
    void getDiscountGiven() {
        assertEquals(20, orderdet.getDiscountGiven());
    }

    @Test
    void getFinalPrice() {
        assertEquals(480, orderdet.getFinalPrice());
    }
}