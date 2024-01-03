package Suppliers.ServiceLayer;
import Suppliers.BusinessLayer.Payment;
import Suppliers.BusinessLayer.SupplierController;
import Suppliers.BusinessLayer.SupplyAgreement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ServiceController { // service controller as singleton
    private final OrderService orderService;
    private final SupplierService supplierService;
    private final SupplyAgreementService saService;
    private static ServiceController serviceController = null;

    // constructor
    private ServiceController() {
        orderService = new OrderService();
        supplierService = new SupplierService();
        saService = new SupplyAgreementService();
    }

    public static ServiceController getInstance() {
        if(serviceController == null) {
            serviceController = new ServiceController();
        }
        return serviceController;
    }

    ///////////////////////////SupplierService/////////////////////////////

    public String removeSupplier(int id) {
        try{
            supplierService.removeSupplier(id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Supplier removed successfully";
    }
    public String addFixedDaysSupplier(String name, int id, int bank, Payment pay, ArrayList<Integer> days) {
        try{
            supplierService.addFixedDaysSupplier(name, id, bank, pay, days);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Supplier was added successfully";
    }

    public String addShipDayToFixedSupplier(int id, int dayNumber) {
        try{
            supplierService.addShipDayToFixedSupplier(id, dayNumber);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Ship day was added successfully";
    }

    public String addOnOrderSupplier(String name, int id, int bank, Payment pay) {
        try{
            supplierService.addOnOrderSupplier(name, id, bank, pay);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Supplier was added successfully";
    }

    public String addNoTransportSupplier(String name, int id, int bank, Payment pay, String address) {
        try{
            supplierService.addNoTransportSupplier(name, id, bank, pay, address);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Supplier was added successfully";
    }

    public String addContactToSupplier(int supplier_id, String contact_name, String phone) {
        try{
            supplierService.addContactToSupplier(supplier_id, contact_name, phone);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Contact was added successfully";
    }

    public String removeContactOfSupplier(int supplier_id, String contact_name, String cellphone) {
        try{
            supplierService.removeContactOfSupplier(supplier_id, contact_name, cellphone);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Contact was removed successfully";
    }

    public String changeSupplierName(int id, String name) {
        try{
            supplierService.changeSupplierName(id, name);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Name was changed successfully";
    }

    public String changeBankAccount(int id, int bankAccount) {
        try{
            supplierService.changeBankAccount(id, bankAccount);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Bank account was changed successfully";
    }

    public String changePayment(int id, Payment payment) {
        try{
            supplierService.changePayment(id, payment);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Payment method was changed successfully";
    }

    public String setNextDeliveryDateOfOnOrderSupplier(int id, int days) {
        try{
            supplierService.setNextDeliveryDateOfOnOrderSupplier(id, days);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Next delivery date was set successfully";
    }

    public String setNextDeliveryDateOfNoTransportSupplier(int id, int day, int month, int year) {
        try{
            supplierService.setNextDeliveryDateOfNoTransportSupplier(id, day, month, year);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Next delivery date was set successfully";
    }

    public String removeShipDayFromFixedSupplier(int id, int dayNumber) {
        try{
            supplierService.removeShipDayFromFixedSupplier(id, dayNumber);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Day was removed successfully";
    }

    ///////////////////////////OrderService/////////////////////////////

    // function that adds fixed time period order
    public String addPeriodicOrder(String branchID, int supplierID, int productCode, int amount, int day) {
        try{
        orderService.addPeriodicOrder(branchID, supplierID, productCode, amount, day);
    } catch (Exception e) {
        return e.getMessage();
    }
        return "Fixed periodic order was added successfully";
    }

    // function that returns all orders as a string
    public String getAllOrders() {
        try{
            return orderService.getAllOrders();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getOrdersOfSupplier(int supplierID) {
        try{
            return orderService.getOrdersOfSupplier(supplierID);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // function that prints the order of a supplier
    public boolean printOrdersOfSupplier(int id) {
        try{
            orderService.printOrdersOfSupplier(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean printAllOrders() {
        try{
            orderService.printOrders();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        try{
            orderService.makeOrder(branch, productsToOrder, productsAndAmounts);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Finished ordering products";
    }

    public String cancelOrder(int order_number) {
        try{
            orderService.cancelOrder(order_number);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Order was cancelled successfully";
    }

    public String confirmOrder(int order_number) {
        try{
            orderService.confirmOrder(order_number);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Delivery confirmed";
    }

    public String removePeriodicOrder(String branchID, int supplierID, int productCode, int day) {
        try{
            orderService.removePeriodicOrder(day, supplierID, productCode, branchID);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Periodic order was removed successfully";
    }


    ///////////////////////////SupplyAgreementService/////////////////////////////

    public SupplyAgreement getSupplyAgreement(int supplier_id, int makat) {
        try{
            saService.getSupplyAgreement(supplier_id, makat);
        } catch (Exception e) {
            return null;
        }
        return saService.getSupplyAgreement(supplier_id, makat);
    }

    public String addSupplyAgreement(int supplierID, int productCode, double price, int catalog, int amount) {
        try{
            saService.addSupplyAgreement(supplierID, productCode, price, catalog, amount);
        } catch (Exception e) {
           return e.getMessage();
        }
        return "Succeed";
    }


    public String addDiscountByOrder(int supplierID, double discountValue, double minPrice, int option) {
        try{
            saService.addDiscountByOrder(supplierID, discountValue, minPrice, option);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Discount was added successfully";
    }

    public boolean addDiscountByProductToAgreement(int id, SupplyAgreement agreement, double discountValue, int numOfProducts, int option) {
        try{
            saService.addDiscountByProductToAgreement(id, agreement, discountValue, numOfProducts, option);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String removeSupplyAgreement(int supplierID, int productCode) {
        try{
            saService.removeSupplyAgreement(supplierID, productCode);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Supply agreement was removed successfully";
    }

    public boolean printAllSupplyAgreementsOfSupplier(int supplierID) {
        try{
            saService.printAllSupplyAgreementsOfSupplier(supplierID);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // a function that returns supplier's supply agreements as a string
    public String getSupplyAgreementsOfSupplier(int supplierID) throws SQLException {
        try{
            saService.getSupplyAgreementsOfSupplier(supplierID);
        } catch (Exception e) {
            return e.getMessage();
        }
        return saService.getSupplyAgreementsOfSupplier(supplierID);
    }

    // function that activates the scheduling of tomorrow's fixed orders
    public void makeFixedOrders() {
        orderService.makeFixedOrders();
    }

    public void starConnection() {
        supplierService.startConnection();
        saService.startConnection();
        orderService.startConnection();
    }


      // these functions are for testing purposes only: we used them to load data
    public void addData() {
        // add suppliers
        // fixed days
        this.addFixedDaysSupplier("John Smith", 1234, 5678, Payment.Credit, new ArrayList<Integer>(Arrays.asList(1, 3, 5)));
        this.addFixedDaysSupplier("Bob Johnson", 2345, 6789, Payment.Credit, new ArrayList<Integer>(Arrays.asList(0, 1, 2)));
        this.addFixedDaysSupplier("Alice Smith", 3456, 7890, Payment.TransferToAccount, new ArrayList<Integer>(Arrays.asList(0)));
        // on order
        this.addOnOrderSupplier("Jane Doe", 5678, 9012, Payment.Cash);
        this.addOnOrderSupplier("Noa Yeshayahu", 7890, 2345, Payment.Checks);
        this.addOnOrderSupplier("Sarah Kim", 9876, 5432, Payment.Cash);
        // no transport
        this.addNoTransportSupplier("Jack Lee", 3691, 1357, Payment.TransferToAccount, "Mapu 2");
        this.addNoTransportSupplier("David Cohen", 2580, 2468, Payment.Credit, "Ben Yehuda 5");
        this.addNoTransportSupplier("Michael Levi", 1473, 8024, Payment.Checks, "Dizengoff 10");
        // set delivery dates
        SupplierController.getInstance().setNextDeliveryDateOfNoTransportSupplier(3691, 2, 11, 2023);
        SupplierController.getInstance().setNextDeliveryDateOfNoTransportSupplier(2580, 1, 11, 2023);
        SupplierController.getInstance().setNextDeliveryDateOfNoTransportSupplier(1473, 5, 11, 2023);
        SupplierController.getInstance().setNextDeliveryDateOfOnOrderSupplier(5678, 5);
        SupplierController.getInstance().setNextDeliveryDateOfOnOrderSupplier(7890, 2);
        SupplierController.getInstance().setNextDeliveryDateOfOnOrderSupplier(9876, 1);
        // add contacts
        this.addContactToSupplier(1234, "John Smith", "0541234567");
        this.addContactToSupplier(1234, "Lior Aftabi", "0504652585");
        this.addContactToSupplier(2345, "Alice Johnson", "0521112222");
        this.addContactToSupplier(3456, "Bob Lee", "0533334444");
        this.addContactToSupplier(7890, "Tom Cohen", "0505556666");
        this.addContactToSupplier(9876, "Joshua Brown", "0547778888");
        this.addContactToSupplier(9876, "Osher Cohen", "0584736252");
        this.addContactToSupplier(5678, "Ana Zak", "0525381648");
        this.addContactToSupplier(3691, "Noa Kirel", "0501134562");
        this.addContactToSupplier(2580, "Omer Adam", "0507463520");
        this.addContactToSupplier(1473, "Yael Shelbia", "0501124263");
        // add supply agreements
        this.addSupplyAgreement(1234, 0, 2.50, 101, 500);
        this.addSupplyAgreement(3691, 1, 3.50, 101, 1000);
        this.addSupplyAgreement(2345, 2, 1.80, 102, 300);
        this.addSupplyAgreement(3456, 3, 3.20, 103, 200);
        this.addSupplyAgreement(2580, 4, 2.00, 104, 1100);
        this.addSupplyAgreement(1473, 5, 1.50, 105, 500);
        this.addSupplyAgreement(1473, 0, 3.50, 105, 200);
        // add periodic orders
        this.addPeriodicOrder("A", 1234, 0, 50, 3);
        this.addPeriodicOrder("B", 3691, 1, 100, 5);
        this.addPeriodicOrder("C", 2345, 2, 30, 1);
        this.addPeriodicOrder("D", 3456, 3, 20, 0);
        this.addPeriodicOrder("E", 2580, 4, 25, 2);
        this.addPeriodicOrder("F", 1473, 5, 10, 4);
        // add discounts by order
        this.addDiscountByOrder(1234, 10.0, 500.00, 1);
        this.addDiscountByOrder(3691, 100.0, 1000.00, 2);
        this.addDiscountByOrder(2345, 50.00, 1000.00, 2);
        this.addDiscountByOrder(3456, 5.0, 2000.00, 1);
        this.addDiscountByOrder(2580, 10.0, 1000.00, 1);
        // add discounts by product to agreements
        this.addDiscountByProductToAgreement(1234, SupplierController.getInstance().getSupplierByID(1234).getAllAgreements().get(0), 10.0, 100, 1);
        this.addDiscountByProductToAgreement(1473, SupplierController.getInstance().getSupplierByID(1473).getAllAgreements().get(0), 15.0, 200, 2);
        // add orders
        ArrayList<Integer> productsToOrder = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
        HashMap<Integer, Integer> productsAndAmounts = new HashMap<Integer, Integer>();
        productsAndAmounts.put(0, 10);
        productsAndAmounts.put(1, 20);
        productsAndAmounts.put(2, 30);
        productsAndAmounts.put(3, 40);
        productsAndAmounts.put(4, 50);
        productsAndAmounts.put(5, 60);
        this.makeOrder("A", productsToOrder, productsAndAmounts);
        productsToOrder = new ArrayList<Integer>(Arrays.asList(2, 3, 5));
        HashMap<Integer, Integer> productsAndAmounts2 = new HashMap<Integer, Integer>();
        productsAndAmounts2.put(2, 13);
        productsAndAmounts2.put(3, 24);
        productsAndAmounts2.put(5, 35);
        this.makeOrder("B", productsToOrder, productsAndAmounts2);
    }

    public String[] getSupplierList() {
        return saService.getSupplierList();
    }
}

