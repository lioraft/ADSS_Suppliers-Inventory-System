package Inventory.test;
import Inventory.BusinessLayer.Item;
import Inventory.BusinessLayer.ItemController;
import Inventory.BusinessLayer.CategoryController;
import Inventory.BusinessLayer.ProductController;
import Inventory.DataAccessLayer.Mapper.ProductDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ControllersTests {
    private static ItemController itemController;
    private static ProductController productController;
    private static CategoryController categoryController;
    private static Suppliers.ServiceLayer.ServiceController serviceSuppliers;


    @BeforeAll
    static void setUp() {
        itemController = ItemController.getInstance();
        productController = ProductController.getInstance();
        categoryController = CategoryController.getInstance();
        itemController.startConnection();
        productController.startConnection();
        categoryController.startConnection();
        serviceSuppliers = Suppliers.ServiceLayer.ServiceController.getInstance();
        serviceSuppliers.starConnection();

    }

    @org.junit.jupiter.api.Test
    void addItem() { //test that integrate with the suppliers
        assertTrue(addItemTest(1234, 60, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
        assertFalse(addItemTest(1234, 60, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
        assertTrue(addItemTest(1234, 62, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
        assertFalse(addItemTest(1234, 62, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
    }

    boolean addItemTest(Integer manufacturer, Integer barcode, String name, String expirationDate, double costPrice, int category, int productID, String size, String branch){
        try {
            itemController.addItem(manufacturer, barcode, name, expirationDate, costPrice, category, productID, size, branch);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @org.junit.jupiter.api.Test
    void removeItem() { //test that integrate with the suppliers
        assertTrue(removeItemTest(1234, 60, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
        assertFalse(removeItemTest(1234, 60, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
        assertTrue(removeItemTest(1234, 62, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
        assertFalse(removeItemTest(1234, 62, "Milk", LocalDate.now().toString(), 10, 1, 1, "1L", "A"));
    }

    boolean removeItemTest(Integer manufacturer, Integer barcode, String name, String expirationDate, double costPrice, int category, int productID, String size, String branch){
        try {
            itemController.removeItem(manufacturer, barcode, name, expirationDate, costPrice, category, productID, size, branch);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
