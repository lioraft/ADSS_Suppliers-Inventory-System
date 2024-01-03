package Suppliers.Test.CLI_Test;

import Suppliers.BusinessLayer.*;
import Suppliers.PresentationLayer.CLI;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class CLITest {
    // initialize the CLI for test
    CLI cli = new CLI();

    @Test
    void addSupplier() {
        // create input for FixedDaysSupplier
        String input = "123\nSupplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();

        // check if supplier was added to suppliers
        assertNotNull(SupplierController.getInstance().getSupplierByID(123));
        assertEquals(SupplierController.getInstance().getSupplierByID(123).getName(), "Supplier 1");

        // create input for OnOrderSupplier
        input = "234\nSupplier 2\n234567\n2\n2\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add OnOrderSupplier
        cli.addSupplier();

        // check if supplier was added to suppliers
        assertNotNull(SupplierController.getInstance().getSupplierByID(234));
        assertEquals(SupplierController.getInstance().getSupplierByID(234).getName(), "Supplier 2");

        // create input for NoTransportSupplier
        input = "345\nSupplier 3\n345678\n4\n3\n123 Main St.\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add NoTransportSupplier
        cli.addSupplier();

        // check if supplier was added to suppliers
        assertNotNull(SupplierController.getInstance().getSupplierByID(345));
        assertEquals(SupplierController.getInstance().getSupplierByID(345).getName(), "Supplier 3");
    }

    @Test
    void removeSupplier() {
        // create input for FixedDaysSupplier
        String input = "123\nSupplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();

        // create input for remove method
        input = "123\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // call the removeSupplier method
        cli.removeSupplier();

        // check that the supplier was removed from the list of suppliers
        assertThrowsExactly(IllegalArgumentException.class, () -> SupplierController.getInstance().getSupplierByID(123));
    }

    @Test
    void addContact() {
        // create input for FixedDaysSupplier
        String input = "123" + "\n" + "Supplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();

        // create input for function
        input = "lior\n0525381648\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add contact
        cli.addContact(123);

        // test
        assertEquals("lior", SupplierController.getInstance().getSupplierByID(123).getContact("lior", "0525381648").getContactName());
        assertEquals("0525381648", SupplierController.getInstance().getSupplierByID(123).getContact("lior", "0525381648").getPhone());
    }

    @Test
    void removeContact() {
        // create input for FixedDaysSupplier
        String input = "123" + "\n" + "Supplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();

        // create input for function
        input = "lior\n0525381648\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add contact
        cli.addContact(123);

        input = "lior\n0525381648\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // remove contact
        cli.removeContact(123);

        // test it was removed - it's supposed to return null object
        assertNull(SupplierController.getInstance().getSupplierByID(123).getContact("lior", "0525381648"));
    }

    @Test
    void addSupplyAgreement() {
        // create input for FixedDaysSupplier
        String input = "123" + "\n" + "Supplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();
        Supplier supplier = SupplierController.getInstance().getSupplierByID(123);
        // add product to list of products
        Inventory.ServiceLayer.ServiceController.getInstance().addProduct("Milk", 5, 0, "milk",0, 0);

        ByteArrayInputStream in2 = new ByteArrayInputStream(("123\n0\n10.0\n123\n100\n1\n10\n1\n20.0\n2\n10.0\n2\n").getBytes());
        System.setIn(in2);

        // run method
        cli.addSupplyAgreement();
        // Assert that the agreement was added to supliers list of agreements
        assertNotNull(supplier.getAgreement(0));
        assertEquals(10.0, supplier.getAgreement(0).getListPrice());
        assertEquals(123, supplier.getAgreement(0).getCatalogCode());
        assertEquals(100, supplier.getAgreement(0).getMaxAmount());
        // test supplier was added to map of products and suppliers that ship them
        assertTrue(SupplyAgreementController.getInstance().getSuppliersByProduct(0).contains(supplier));
    }

    @Test
    void removeSupplyAgreement() {
        // create input for FixedDaysSupplier
        String input = "123" + "\n" + "Supplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();

        // get supplier created
        Supplier supplier = SupplierController.getInstance().getSupplierByID(123);

        // add product to list of products
        Inventory.ServiceLayer.ServiceController.getInstance().addProduct("Milk", 5, 0, "milk",0, 123);

        ByteArrayInputStream in2 = new ByteArrayInputStream(("123\n0\n10.0\n123\n100\n1\n10\n1\n20.0\n2\n10.0\n2\n").getBytes());
        System.setIn(in2);
        // run method
        cli.addSupplyAgreement();

        //set input for removal
        in2 = new ByteArrayInputStream(("123\n0\n").getBytes());
        System.setIn(in2);

        //remove agreement and test it was removed
        cli.removeSupplyAgreement();
        assertFalse(SupplyAgreementController.getInstance().getSuppliersByProduct(0).contains(supplier));
        assertTrue(supplier.getAllAgreements().isEmpty());
    }

    @Test
    void addOrderDiscountForSupplier() {
        // create input for FixedDaysSupplier
        String input = "123" + "\n" + "Supplier 1\n123456\n1\n1\n1\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add FixedDaysSupplier
        cli.addSupplier();

        // get supplier created
        Supplier supplier = SupplierController.getInstance().getSupplierByID(123);

        // create input for function
        input = "123\n1\n20\n100\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // add the discount to supplier
        cli.addOrderDiscountForSupplier();

        // test discount was added
        assertEquals(20, supplier.getDiscounts().get(0).getValue());
        assertEquals(100, supplier.getDiscounts().get(0).getMinimalPrice());
    }

    @Test
    void makeOrder() {
        // test function is written on the initial data added with the function in service class
        OrderController orderController = OrderController.getInstance();

        /* first order */
        // make list of products
        ArrayList<Integer> products = new ArrayList<>();
        products.add(0);
        products.add(1);
        products.add(2);
        // make map of products and amounts
        HashMap<Integer, Integer> productsAndAmounts = new HashMap<>();
        productsAndAmounts.put(0, 10);
        productsAndAmounts.put(1, 20);
        productsAndAmounts.put(2, 30);
        // make order
        ArrayList<Order> orders = orderController.makeOrder("A", products, productsAndAmounts);
        // test orders were made properly
        assertEquals(3, orders.size());
        assertEquals("A", orders.get(0).getBranch());
        assertEquals(0, orders.get(2).getOrderedProducts().get(0).getProductCode());
        assertEquals(10, orders.get(2).getOrderedProducts().get(0).getAmount());
        assertEquals(1, orders.get(1).getOrderedProducts().get(0).getProductCode());
        assertEquals(20, orders.get(1).getOrderedProducts().get(0).getAmount());
        assertEquals(2, orders.get(0).getOrderedProducts().get(0).getProductCode());
        assertEquals(30, orders.get(0).getOrderedProducts().get(0).getAmount());
    }

}