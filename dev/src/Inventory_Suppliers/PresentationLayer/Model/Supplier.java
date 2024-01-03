package Inventory_Suppliers.PresentationLayer.Model;

import Suppliers.BusinessLayer.Payment;
import Suppliers.BusinessLayer.SupplyAgreement;
import Suppliers.ServiceLayer.ServiceController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Supplier {
    // Supplier properties, getters, setters, business logic, etc.
    private ServiceController serviceController = ServiceController.getInstance();
    private final Inventory.ServiceLayer.ServiceController inventoryServiceController = Inventory.ServiceLayer.ServiceController.getInstance();

    // Constructor
    public Supplier() {
        // Connect to the service layer
        serviceController.starConnection();
        inventoryServiceController.starConnection();
    }

    public String removeSupplier(int id) {
            return serviceController.removeSupplier(id);
    }
    public String addFixedDaysSupplier(String name, int id, int bank, Payment pay, ArrayList<Integer> days) {
        return serviceController.addFixedDaysSupplier(name, id, bank, pay, days);
    }

    public String addShipDayToFixedSupplier(int id, int dayNumber) {
        return serviceController.addShipDayToFixedSupplier(id, dayNumber);
    }

    public String addOnOrderSupplier(String name, int id, int bank, Payment pay) {
        return serviceController.addOnOrderSupplier(name, id, bank, pay);
    }

    public String addNoTransportSupplier(String name, int id, int bank, Payment pay, String address) {
        return serviceController.addNoTransportSupplier(name, id, bank, pay, address);
    }

    public String addContactToSupplier(int supplier_id, String contact_name, String phone) {
            return serviceController.addContactToSupplier(supplier_id, contact_name, phone);
    }

    public String removeContactOfSupplier(int supplier_id, String contact_name, String cellphone) {
        return serviceController.removeContactOfSupplier(supplier_id, contact_name, cellphone);
    }

    public String changeSupplierName(int id, String name) {
            return serviceController.changeSupplierName(id, name);
    }

    public String changeBankAccount(int id, int bankAccount) {
        return serviceController.changeBankAccount(id, bankAccount);
    }

    public String changePayment(int id, Payment payment) {
        return serviceController.changePayment(id, payment);
    }

    public String setNextDeliveryDateOfOnOrderSupplier(int id, int days) {
        return serviceController.setNextDeliveryDateOfOnOrderSupplier(id, days);
    }

    public String setNextDeliveryDateOfNoTransportSupplier(int id, int day, int month, int year) {
        return serviceController.setNextDeliveryDateOfNoTransportSupplier(id, day, month, year);
    }

    public String removeShipDayFromFixedSupplier(int id, int dayNumber) {
        return serviceController.removeShipDayFromFixedSupplier(id, dayNumber);
    }

    ///////////////////////////OrderService/////////////////////////////

    // function that adds fixed time period order
    public String addPeriodicOrder(String branchID, int supplierID, int productCode, int amount, int day) {
            return serviceController.addPeriodicOrder(branchID, supplierID, productCode, amount, day);
    }

    // function that prints the order of a supplier
    public boolean printOrdersOfSupplier(int id) {
            return serviceController.printOrdersOfSupplier(id);
    }

    public boolean printAllOrders() {
            return serviceController.printAllOrders();
    }

    public String makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
            return serviceController.makeOrder(branch, productsToOrder, productsAndAmounts);
    }

    public String cancelOrder(int order_number) {
            return serviceController.cancelOrder(order_number);
    }

    public String confirmOrder(int order_number) {
        return serviceController.confirmOrder(order_number);
    }

    public String removePeriodicOrder(String branchID, int supplierID, int productCode, int day) {
            return serviceController.removePeriodicOrder(branchID, supplierID, productCode, day);
    }


    ///////////////////////////SupplyAgreementService/////////////////////////////

    public SupplyAgreement getSupplyAgreement(int supplier_id, int makat) {
        return serviceController.getSupplyAgreement(supplier_id, makat);
    }

    public String addSupplyAgreement(int supplierID, int productCode, double price, int catalog, int amount) {
            return serviceController.addSupplyAgreement(supplierID, productCode, price, catalog, amount);
    }


    public String addDiscountByOrder(int supplierID, double discountValue, double minPrice, int option) {
        return serviceController.addDiscountByOrder(supplierID, discountValue, minPrice, option);
    }

    public boolean addDiscountByProductToAgreement(int id, SupplyAgreement agreement, double discountValue, int numOfProducts, int option) {
        return serviceController.addDiscountByProductToAgreement(id, agreement, discountValue, numOfProducts, option);
    }

    public String removeSupplyAgreement(int supplierID, int productCode) {
        return serviceController.removeSupplyAgreement(supplierID, productCode);
    }

    public boolean printAllSupplyAgreementsOfSupplier(int supplierID) {
        return serviceController.printAllSupplyAgreementsOfSupplier(supplierID);
    }

    // function that activates the scheduling of tomorrow's fixed orders
    public void makeFixedOrders() {
       serviceController.makeFixedOrders();
    }


    public String getSupplyAgreementsOfSupplier(int supplierID) throws SQLException {
        return serviceController.getSupplyAgreementsOfSupplier(supplierID);
    }

    public String[] getSupplierList() {
        return serviceController.getSupplierList();
    }
}
