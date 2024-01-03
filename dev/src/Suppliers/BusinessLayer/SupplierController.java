package Suppliers.BusinessLayer;

import Suppliers.DataAccessLayer.SupplierDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SupplierController { //Controller for supplier as singleton
    HashMap<Integer, Supplier> suppliers; // <id, Supplier>: map that matches id to supplier
    private static SupplierController instance = null; // the instance of the supplier controller
    SupplierDAO supplierDAO; // the supplier DAO
    private boolean opened_connection = false; // boolean that indicates if connection to DB is open

    // constructor
    private SupplierController() {
        suppliers = new HashMap<>();
        supplierDAO = new SupplierDAO();
    }

    // get instance of supplier controller. if it doesn't exist, create it
    public static SupplierController getInstance() {
        if (instance == null)
            instance = new SupplierController();
        return instance;
    }

    // add new fixed days supplier
    public void addFixedDaysSupplier(String name, int id, int bank, Payment pay, ArrayList<Integer> days) {
        if (suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID already exists");
        FixedDaysSupplier supplier = new FixedDaysSupplier(name, id, bank, pay);
        for (Integer day : days)
            supplier.addShipDay(day);
        // add fixed days supplier
        suppliers.put(id, supplier);
        // add to db
        supplierDAO.addSupplier(name, id, bank, pay);
        for (int day : days) {
            supplierDAO.addDaysForFixedSupplier(id, day);
        }
    }

    // function that adds ship day to fixed days supplier
    public void addShipDayToFixedSupplier(int id, int dayNumber) {
        if (!suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        if (!(suppliers.get(id) instanceof FixedDaysSupplier))
            throw new IllegalArgumentException("Supplier is not a fixed days supplier");
        FixedDaysSupplier supplier = (FixedDaysSupplier) suppliers.get(id);
        // add to supplier
        supplier.addShipDay(dayNumber);
        // update in db
        supplierDAO.addDaysForFixedSupplier(id, dayNumber);
    }

    // function that removes ship day from fixed days supplier
    public void removeShipDayFromFixedSupplier(int id, int dayNumber) {
        if (!suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        if (!(suppliers.get(id) instanceof FixedDaysSupplier))
            throw new IllegalArgumentException("Supplier is not a fixed days supplier");
        FixedDaysSupplier supplier = (FixedDaysSupplier) suppliers.get(id);
        // remove from supplier
        supplier.removeShipDay(dayNumber);
        // update in db
        supplierDAO.removeDaysForFixedSupplier(id, dayNumber);
    }

    // add new on order supplier
    public void addOnOrderSupplier(String name, int id, int bank, Payment pay) {
        if (suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID already exists");
        Supplier supplier = new OnOrderSupplier(name, id, bank, pay);
        // add supplier
        suppliers.put(id, supplier);
        // add to db
        supplierDAO.addSupplier(name, id, bank, pay);
        supplierDAO.addOnOrderSupplier(id);
    }

    // add new no transport supplier
    public void addNoTransportSupplier(String name, int id, int bank, Payment pay, String address) {
        if (suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID already exists");
        Supplier supplier = new NoTransportSupplier(name, id, bank, pay, address);
        // add supplier
        suppliers.put(id, supplier);
        // add to db
        supplierDAO.addSupplier(name, id, bank, pay);
        supplierDAO.addAddressForNoTransport(id, address);
    }

    // function that takes in the ID of a supplier, and returns the instance of it
    public Supplier getSupplierByID(int id) {
        if (!suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        return suppliers.get(id);
    }

    // function that takes in the ID of a supplier, and removes it from map of suppliers
    public void removeSupplierByID(int id) {
        if (!suppliers.containsKey(id))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // remove supplier from all product lists
        ArrayList<SupplyAgreement> agreements = suppliers.get(id).getAllAgreements();
        int i = 0;
        while (!agreements.isEmpty()) {
            SupplyAgreement agreement = agreements.get(i);
            int product = agreement.getProductCode();
            SupplyAgreementController.getInstance().removeSupplyAgreement(id, product);
            i++;
        }
        // remove supplier from suppliers list
        suppliers.remove(id);
        // remove from db
        supplierDAO.removeSupplier(id);
    }

    // function that takes in the ID of a supplier, and details of new contact, and adds to it the supplier's contact list
    public void addContactToSupplier(int supplier_id, String contact_name, String phone) {
        if (!suppliers.containsKey(supplier_id))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        Supplier supplier = suppliers.get(supplier_id);
        if (supplier.getContact(contact_name, phone) != null)
            throw new IllegalArgumentException("Contact already exists");
        // add contact
        supplier.addContact(contact_name, phone);
        // add to db
        supplierDAO.addContact(supplier_id, contact_name, phone);
    }

    // function that takes in the ID of a supplier, and details of an existing contact, and removes it from supplier's contact list
    public void removeContactOfSupplier(int supplier_id, String contact_name, String phone) {
        if (!suppliers.containsKey(supplier_id))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // remove the contact
        suppliers.get(supplier_id).removeContact(contact_name, phone);
        // remove from db
        supplierDAO.removeContact(supplier_id, contact_name, phone);
    }

    // function that sets the next delivery date of on order supplier
    public void setNextDeliveryDateOfOnOrderSupplier(int supplierID, int days) {
        // if supplier doesn't exist
        if (!suppliers.containsKey(supplierID))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // get supplier and update next delivery date
        Supplier supplier = suppliers.get(supplierID);
        if (supplier.getNextShippingDate() == null)
            // add number of dao days to next order, if date not exist
            supplierDAO.addNumOfDaysForOnOrder(supplierID, days);
        else
            supplierDAO.setNumOfDaysForOnOrder(supplierID, days); // if date exists, update it
        // update supplier
        ((OnOrderSupplier) supplier).setNumberOfDaysToNextOrder(days);

    }

    // function that sets the next delivery date of no transport supplier
    public void setNextDeliveryDateOfNoTransportSupplier(int supplierID, int day, int month, int year) {
        // if supplier doesn't exist
        if (!suppliers.containsKey(supplierID))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // get supplier and update next delivery date
        Supplier supplier = suppliers.get(supplierID);
        if (supplier.getNextShippingDate() == null)
            supplierDAO.addNextDeliveryDateForNoTransport(supplierID, day, month, year); // if date not exist, add it
        else
            supplierDAO.setNextDeliveryDateForNoTransport(supplierID, day, month, year); // if date exists, update it
        // add date to supplier
        ((NoTransportSupplier) supplier).setNextDeliveryDate(day, month, year);
    }

    // function that changes a supplier's name
    public void changeSupplierName(int supplierID, String newName) {
        // if supplier doesn't exist
        if (!suppliers.containsKey(supplierID))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // get supplier and update name
        Supplier supplier = suppliers.get(supplierID);
        supplier.setName(newName);
        // update db
        supplierDAO.setName(supplierID, newName);
    }

    // function that changes a supplier's payment method
    public void changePayment(int supplierID, Payment newPayment) {
        // if supplier doesn't exist
        if (!suppliers.containsKey(supplierID))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // get supplier and update payment method
        Supplier supplier = suppliers.get(supplierID);
        supplier.setPaymentMethod(newPayment);
        // update db
        supplierDAO.setPayment(supplierID, newPayment.toString());
    }

    // function that changes a supplier's bank account
    public void changeBankAccount(int supplierID, int bankAccount) {
        // if supplier doesn't exist
        if (!suppliers.containsKey(supplierID))
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // get supplier and update bank account
        Supplier supplier = suppliers.get(supplierID);
        supplier.setBankAccount(bankAccount);
        // update db
        supplierDAO.setBankAccount(supplierID, bankAccount);
    }

    //getter of suppliers
    public ArrayList<Supplier> getSuppliers() {
        return new ArrayList<>(suppliers.values()); }

    public void startConnection() {
        try {
            if (!opened_connection) {
                suppliers = this.supplierDAO.startConnection();
                opened_connection = true;
            }
        } catch (Exception e) {
            System.out.println("Error connecting to supplier database");
        }
    }

}
