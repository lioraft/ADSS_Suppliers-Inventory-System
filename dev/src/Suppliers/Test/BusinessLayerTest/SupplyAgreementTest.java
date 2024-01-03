package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.DiscountOfPriceByProduct;
import Suppliers.BusinessLayer.SupplyAgreement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplyAgreementTest {

    // object that will be tested
    SupplyAgreement sa = new SupplyAgreement(1, 100, 40, 5);

    @Test
    void addDiscount() { // also tests getDiscounts
        // initialize new discount and add it
        DiscountOfPriceByProduct discount = new DiscountOfPriceByProduct(10, 3);
        sa.addDiscount(discount);
        // test discount was added
        assertEquals(discount, sa.getDiscounts().get(0));
    }

    @Test
    void removeDiscount() {
        // initialize new discount and add it
        DiscountOfPriceByProduct discount = new DiscountOfPriceByProduct(10, 3);
        sa.addDiscount(discount);
        // test discount was added
        assertEquals(discount, sa.getDiscounts().get(0));
        // remove discount
        sa.removeDiscount(3, 10);
        // test discount was removed
        assertEquals(true, sa.getDiscounts().isEmpty());
    }

    @Test
    void getProductCode() {
        assertEquals(1, sa.getProductCode());
    }

    @Test
    void getListPrice() {
        assertEquals(100, sa.getListPrice());
    }

    @Test
    void getCatalogCode() {
        assertEquals(40, sa.getCatalogCode());
    }

    @Test
    void getMaxAmount() {
        assertEquals(5, sa.getMaxAmount());
    }

    @Test
    void getMinimalPrice() {
        assertEquals(300, sa.getMinimalPrice(3));
        // add discount
        DiscountOfPriceByProduct discount = new DiscountOfPriceByProduct(10, 3);
        // test price after discount
        sa.addDiscount(discount);
        assertEquals(290, sa.getMinimalPrice(3));
    }

    @Test
    void getListPriceBeforeDiscounts() {
        assertEquals(300, sa.getListPriceBeforeDiscounts(3));
    }
}