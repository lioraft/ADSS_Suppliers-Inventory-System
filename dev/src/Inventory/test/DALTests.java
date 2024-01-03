package Inventory.test;
import Inventory.BusinessLayer.Item;
import Inventory.DataAccessLayer.Mapper.ConnectDB;
import Inventory.DataAccessLayer.Mapper.CategoryDAO;
import Inventory.DataAccessLayer.Mapper.ProductDAO;
import Inventory.DataAccessLayer.Mapper.ItemDAO;
import Inventory.ServiceLayer.ServiceController;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
public class DALTests {
    //Writing tests for the DAL
    private static ConnectDB connectDB;
    private static CategoryDAO categoryDAO;
    private static ProductDAO productDAO;
    private static ItemDAO itemDAO;
    private static ServiceController service;
    private static Suppliers.ServiceLayer.ServiceController serviceSuppliers;

    @BeforeAll
    static void setUp() {
        connectDB = ConnectDB.getInstance();
        categoryDAO = new CategoryDAO();
        productDAO = ProductDAO.getInstance();
        itemDAO = new ItemDAO();
        service = ServiceController.getInstance();
        serviceSuppliers = Suppliers.ServiceLayer.ServiceController.getInstance();
        serviceSuppliers.starConnection();
        service.addData();
        service.starConnection();
    }

    @AfterAll
    static void closeConnection() {
        connectDB.close_connect();
    }

    @org.junit.jupiter.api.Test
    void addCategory() {
        assertFalse(categoryDAO.addCategory("Dairy products", 0));
        assertTrue(categoryDAO.addCategory("Dairy products", 4));
        assertFalse(categoryDAO.addCategory("HelloWorld", 0));
    }
    @org.junit.jupiter.api.Test
    void removeCategory() {
        assertTrue(categoryDAO.removeCategory(4));
    }
    @org.junit.jupiter.api.Test
    void addProduct() {
        assertFalse(productDAO.addProduct("Milk", 5, 0,"milk", 0, 0));
        assertTrue(productDAO.addProduct("Milky", 5, 0,"milk", 6, 0));
        assertFalse(productDAO.addProduct("Milky", 5, 6, "milk",20, 0));
    }

    @org.junit.jupiter.api.Test
    void removeProduct() {
        assertTrue(productDAO.removeProduct(6));
    }

    @org.junit.jupiter.api.Test
    void addItem() {
        Item.Location location = Item.Location.STORE;
        String name = "Itay";
        LocalDate dateOfArrival = LocalDate.now();
        assertFalse(itemDAO.addItem(1234, 55, name, location, null, 20, 10, 0, "Big", 50, "A",dateOfArrival.toString()));
        assertTrue(itemDAO.addItem(1234, 55, name, location, null, 20, 0, 0, "Big", 50, "A",dateOfArrival.toString()));
        assertFalse(itemDAO.addItem(1234, 55, name, location, null, 20, 0, 20, "Big", 50, "A",dateOfArrival.toString()));
    }

    @org.junit.jupiter.api.Test
    void removeItem() {
        assertTrue(itemDAO.removeItem(55));
    }

    @org.junit.jupiter.api.Test
    void getItemById(){
        HashMap<Integer, Item> item = itemDAO.getItemById();
        assertEquals(item.get(0).getBarcode(), 0);
        assertNotEquals(item.get(0).getBarcode(), 1);
    }
}
