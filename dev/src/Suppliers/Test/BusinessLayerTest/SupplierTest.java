package Suppliers.Test.BusinessLayerTest;

import Suppliers.BusinessLayer.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    // create a new fixed days supplier that will be tested on methods implemented in supplier class
    FixedDaysSupplier fd_supplier = new FixedDaysSupplier("lior", 209259993, 1234567, Payment.Credit);
    // new discount that will be used in tests of discount related methods
    DiscountOfPriceByOrder discount = new DiscountOfPriceByOrder(100, 100);

    // create new contact that will be used in functions testing
    SupplierContact contact = new SupplierContact("ana", "0525381648");

    @Test // getter of id
    void getPrivateCompanyNumber() {
        assertEquals(209259993, fd_supplier.getPrivateCompanyNumber());
    }

    @Test // getter of name
    void getName() {
        assertEquals("lior", fd_supplier.getName());
    }

    @Test // getter of bank account
    void getBankAccount() {
        assertEquals(1234567, fd_supplier.getBankAccount());
    }

    @Test // adding an agreement to supplier's list of agreements, and testing it was added properly
    void addAgreement() {
        SupplyAgreement sa = fd_supplier.addAgreement(13, 100, 43, 5); // add method
        assertNotNull(sa);
        assertEquals(13, sa.getProductCode());
        assertEquals(100, sa.getListPrice());
        assertEquals(43, sa.getCatalogCode());
        assertEquals(5, sa.getMaxAmount());
    }

    @Test
    void getAgreement() {
        SupplyAgreement sa = fd_supplier.addAgreement(13, 100, 43, 5); // add method
        assertEquals(sa, fd_supplier.getAgreement(sa.getProductCode()));
    }

    @Test
    void removeAgreement() {
        fd_supplier.addAgreement(13, 100, 43, 5);
        assertEquals(false, fd_supplier.removeAgreement(10)); // should return false because there's no such agreement
        assertEquals(true, fd_supplier.removeAgreement(13)); // should return true
        assertEquals(false, fd_supplier.removeAgreement(13)); // should return false because there's no such agreement anymore
    }

    @Test
    void addOrderDiscount() { // add discount to list and test it was added
        fd_supplier.addOrderDiscount(discount);
        assertEquals(discount, fd_supplier.getDiscounts().get(0));
    }

    @Test
    void addNewOrder() {
        // add order
        Order order = fd_supplier.addNewOrder("1");
        // test order was created successfully
        assertNotNull(order);
        // get list of orders
        ArrayList<Order> orders = fd_supplier.getOrdersHistory();
        // test order was added properly
        assertEquals(orders.get(0), order);

    }

    @Test
    void applyOrderDiscountsOnOrder() {
        // adding one more discount
        DiscountOfPercentageByOrder discount2 = new DiscountOfPercentageByOrder(20, 100);
        fd_supplier.addOrderDiscount(discount);
        fd_supplier.addOrderDiscount(discount2);
        // create new order
        Order order = fd_supplier.addNewOrder("2");
        // adding product to order
        order.addProducts(1, "milk", 100, 10, 0, 1000);
        // apply order discounts
        fd_supplier.applyOrderDiscountsOnOrder(order);
        // testing discount was applied - 20% off
        assertEquals(800, order.getTotalPrice());

    }

    @Test
    void addContact() {
        // create new contact
        SupplierContact contact2 = fd_supplier.addContact("noa", "1800400400");
        // test contact was added properly
        assertEquals(contact2, fd_supplier.getContact("noa", "1800400400"));
    }

    @Test
    void getListPriceOfProducts() {
        // add agreement
        fd_supplier.addAgreement(2, 25, 43, 5);
        // test it calculates price correctly
        assertEquals(75, fd_supplier.getListPriceOfProducts(2, 3));
        assertEquals(50, fd_supplier.getListPriceOfProducts(2, 2));
    }

    @Test
    void getCatalogNumber() {
        // add agreements
        fd_supplier.addAgreement(2, 25, 43, 5);
        fd_supplier.addAgreement(4, 25, 41, 5);
        // test it gets the catalog number correctly
        assertEquals(41, fd_supplier.getCatalogNumber(4));
        assertEquals(43, fd_supplier.getCatalogNumber(2));
    }

    @Test
    void getMaxAmountOfUnits() {
        // add agreement
        fd_supplier.addAgreement(4, 25, 41, 5);
        // test it gets number of units correctly
        assertEquals(5, fd_supplier.getMaxAmountOfUnits(4));
    }

    @Test
    void getBestPossiblePrice() {
        // add agreement
        SupplyAgreement sa = fd_supplier.addAgreement(4, 25, 41, 15);
        // add discounts for product in agreement
        DiscountOfPriceByProduct discount2 = new DiscountOfPriceByProduct(100, 10);
        sa.addDiscount(discount2);
        DiscountOfPercentageByProduct discount3 = new DiscountOfPercentageByProduct(5, 5);
        sa.addDiscount(discount3);
        // should pick discount 2, and reduce 100 from price
        assertEquals(150, fd_supplier.getBestPossiblePrice(4, 10));
    }

    @Test
    void getContact() {
        // add contact
        SupplierContact contact2 = fd_supplier.addContact("shakira", "1700707060");
        // should return same contact
        assertEquals(contact2, fd_supplier.getContact("shakira", "1700707060"));

    }

    @Test
    void getOrdersHistory() {
        // add orders
        Order order = fd_supplier.addNewOrder("1");
        Order order2 = fd_supplier.addNewOrder("2");
        Order order3 = fd_supplier.addNewOrder("3");
        // test orders were created successfully
        assertNotNull(order);
        assertNotNull(order2);
        assertNotNull(order3);
        // get list of orders
        ArrayList<Order> orders = fd_supplier.getOrdersHistory();
        // test orders were added properly
        assertEquals(orders.get(0), order);
        assertEquals(orders.get(1), order2);
        assertEquals(orders.get(2), order3);
    }

    @Test
    void getDiscounts() {
        // adding one discounts
        DiscountOfPercentageByOrder discount2 = new DiscountOfPercentageByOrder(20, 100);
        DiscountOfPriceByOrder discount3 = new DiscountOfPriceByOrder(300, 200);
        fd_supplier.addOrderDiscount(discount);
        fd_supplier.addOrderDiscount(discount2);
        fd_supplier.addOrderDiscount(discount3);
        // test discounts were added properly
        assertEquals(discount, fd_supplier.getDiscounts().get(0));
        assertEquals(discount2, fd_supplier.getDiscounts().get(1));
        assertEquals(discount3, fd_supplier.getDiscounts().get(2));
    }
}