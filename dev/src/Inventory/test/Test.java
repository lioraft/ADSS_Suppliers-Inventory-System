package Inventory.test;

import Inventory.ServiceLayer.ServiceController;
import Inventory.BusinessLayer.Item;
import Inventory.BusinessLayer.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
//sometimes the test fails because of the connection to the DB
class TestServiceController {
    private static ServiceController service;
    private static Suppliers.ServiceLayer.ServiceController serviceSuppliers;


    @BeforeAll
    static void setUp() {
        service = ServiceController.getInstance();
        service.removeSampleData();
        serviceSuppliers = Suppliers.ServiceLayer.ServiceController.getInstance();
        serviceSuppliers.starConnection();
        service.addData();
        service.starConnection();
    }
    @AfterAll
    static void closeConnection() {
        ServiceController service = ServiceController.getInstance();
        service.removeSampleData();
    }

    @org.junit.jupiter.api.Test
    void addCategory() {
        assertFalse(service.addCategory("Dairy products", 0));
        assertTrue(service.addCategory("Dairy products", 4));
        assertFalse(service.addCategory("HelloWorld", 0));
    }

    @org.junit.jupiter.api.Test
    void addProduct() {
        assertFalse(service.addProduct("Milk", 5, 0,"milk", 0, 0));
        assertTrue(service.addProduct("Milk", 5, 0,"milk", 6, 0));
        assertFalse(service.addProduct("Milky", 5, 0, "milk",0, 0));
    }

    @org.junit.jupiter.api.Test //test that integrate with the suppliers
    void ValidAddItem() {
        assertTrue(service.addItem(1234, 45, "Pot 1L", null, 55, 2, 4, "", "A"));
    }

    @org.junit.jupiter.api.Test
    void UnValidAddItem() { //test that integrate with the suppliers
        assertFalse(service.addItem(2580, 27, "Pot 1L", null, 55, 2, 4, "","A"));
        assertFalse(service.addItem(2580, 44, "Pot 1L", null, 55, 5, 4, "", "A"));
        assertFalse(service.addItem(2580, 43, "Pot 1L", null, 55, 5, 8, "", "A"));
    }
    @org.junit.jupiter.api.Test
    void itemSold() { //test that integrate with the suppliers
        assertTrue(service.itemSold(0,0));
        assertFalse(service.itemSold(5,0));
        assertFalse(service.itemSold(2,0));
        assertTrue(service.itemSold(1,14));
    }

    @org.junit.jupiter.api.Test
    void setMinimum() {
        ArrayList<Product> products = service.getProductsByCategory(0);
        Product p = products.get(0);
        assertEquals(5, products.get(0).getMinAmount());
        service.setMinimum(3, 10,products.get(0).getMakat());
        assertEquals(5, products.get(0).getMinAmount());
        assertNotEquals(30, products.get(0).getMinAmount());
        service.setMinimum(1, 3,products.get(0).getMakat());
        assertEquals(5, products.get(0).getMinAmount());
    }


    @org.junit.jupiter.api.Test
    void setDiscountByProduct() {
        Item i = service.getItemsInStock(0).get(0);
        assertEquals(i.getPrice(), service.getPrice(i.getBarcode()));
        service.setDiscountByProduct(0, 10, "2023-01-01", "2023-08-01");
        assertEquals(13.7, service.getPrice(i.getBarcode()));
    }

    @org.junit.jupiter.api.Test
    void setDiscountByCategory() {
        Item i = service.getItemsInStock(2).get(0);
        assertEquals(i.getPrice(), service.getPrice(i.getBarcode()));
        service.setDiscountByCategory(2, 10, "2023-01-01", "2023-08-01");
        assertEquals(0.9*(i.getPrice()), service.getPrice(i.getBarcode()));
    }

    @org.junit.jupiter.api.Test
    void getMinDiscount() {
        Item i = service.getItemsInStock(1).get(0);
        assertEquals(i.getPrice(), service.getPrice(i.getBarcode()));
        service.setDiscountByCategory(1, 15, "2023-01-01", "2023-08-01");
        service.setDiscountByProduct(2, 10, "2023-01-01", "2023-08-01");
        assertEquals(0.85*(i.getPrice()), service.getPrice(i.getBarcode()));
    }

    @org.junit.jupiter.api.Test
    void getAmountOfProduct1() {
        assertEquals(4, service.getAmountOfProduct(0, "A"));
    }

    @org.junit.jupiter.api.Test
    void getAmountOfProduct2() {
        assertEquals(2, service.getAmountOfProduct(1, "A"));
    }

    @org.junit.jupiter.api.Test
    void getAmountOfProduct3() {
        assertEquals(2, service.getAmountOfProduct(2, "A"));
    }

    @org.junit.jupiter.api.Test
    void ValidDecreaseItemFollowedMakeOrder(){ //test that integrate with the suppliers
        service.itemSold(0, 4);
        assertTrue(service.getProductOrderUrgentStatus(0, "B"));
    }

    @org.junit.jupiter.api.Test
    void unValidDecreaseItemFollowedMakeOrder(){
        assertFalse(service.getProductOrderUrgentStatus(0, "A"));
    } //test that integrate with the suppliers

}

