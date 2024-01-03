package Inventory_Suppliers.PresentationLayer.Controller;
import Inventory_Suppliers.PresentationLayer.Model.Supplier;
import Suppliers.BusinessLayer.Payment;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierControllerMVC {
    // Handle actions related to managing suppliers
    private Supplier supplier;
    private static SupplierControllerMVC instance;

    private SupplierControllerMVC() {
        supplier = new Supplier();
    }

    public static SupplierControllerMVC getInstance() {
        if(instance == null) {
            instance = new SupplierControllerMVC();
        }
        return instance;
    }

    public String removeSupplier(int id) {
        return supplier.removeSupplier(id);
    }
    public String addFixedDaysSupplier(String name, int id, int bank, Payment pay, ArrayList<Integer> days) {
        return supplier.addFixedDaysSupplier(name, id, bank, pay, days);
    }

    public String addShipDayToFixedSupplier(int id, int dayNumber) {
        return supplier.addShipDayToFixedSupplier(id, dayNumber);
    }

    public String addOnOrderSupplier(String name, int id, int bank, Payment pay) {
        return supplier.addOnOrderSupplier(name, id, bank, pay);
    }

    public String addNoTransportSupplier(String name, int id, int bank, Payment pay, String address) {
        return supplier.addNoTransportSupplier(name, id, bank, pay, address);
    }

    public String addContactToSupplier(int supplier_id, String contact_name, String phone) {
        return supplier.addContactToSupplier(supplier_id, contact_name, phone);
    }

    public String removeContactOfSupplier(int supplier_id, String contact_name, String cellphone) {
        return supplier.removeContactOfSupplier(supplier_id, contact_name, cellphone);
    }

    public String changeSupplierName(int id, String name) {
        return supplier.changeSupplierName(id, name);
    }

    public String changeBankAccount(int id, int bankAccount) {
        return supplier.changeBankAccount(id, bankAccount);
    }

    public String changePayment(int id, Payment payment) {
        return supplier.changePayment(id, payment);
    }

    public String setNextDeliveryDateOfOnOrderSupplier(int id, int days) {
        return supplier.setNextDeliveryDateOfOnOrderSupplier(id, days);
    }

    public String setNextDeliveryDateOfNoTransportSupplier(int id, int day, int month, int year) {
        return supplier.setNextDeliveryDateOfNoTransportSupplier(id, day, month, year);
    }

    public String removeShipDayFromFixedSupplier(int id, int dayNumber) {
        return supplier.removeShipDayFromFixedSupplier(id, dayNumber);
    }


    ///////////////////////////SupplyAgreementService/////////////////////////////


    public String addSupplyAgreement(int supplierID, int productCode, double price, int catalog, int amount) {
        return supplier.addSupplyAgreement(supplierID, productCode, price, catalog, amount);
    }


    public String addDiscountByOrder(int supplierID, double discountValue, double minPrice, int option) {
        return supplier.addDiscountByOrder(supplierID, discountValue, minPrice, option);
    }

    public String removeSupplyAgreement(int supplierID, int productCode) {
        return supplier.removeSupplyAgreement(supplierID, productCode);
    }

    public String printAllSupplyAgreements(int supplierID) throws SQLException {
        return supplier.getSupplyAgreementsOfSupplier( supplierID);
    }


    public String[] getSupplierList() {
        return supplier.getSupplierList();
    }
}
