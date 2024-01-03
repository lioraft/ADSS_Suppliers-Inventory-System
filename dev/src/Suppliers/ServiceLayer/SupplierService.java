package Suppliers.ServiceLayer;

import Suppliers.BusinessLayer.Payment;
import Suppliers.BusinessLayer.SupplierController;
import java.util.ArrayList;

public class SupplierService {
        //connect to SupplierController controller
        private final SupplierController supplierController;
        //constructor
    public SupplierService() {
            supplierController = SupplierController.getInstance();
        }

        //addFixedDaysSupplier
    public void addFixedDaysSupplier(String name, int id, int bank, Payment pay, ArrayList<Integer> days) {
            supplierController.addFixedDaysSupplier(name, id, bank, pay, days);
        }

    public void addShipDayToFixedSupplier(int id, int dayNumber) {
        supplierController.addShipDayToFixedSupplier(id, dayNumber);
    }

    public void addOnOrderSupplier(String name, int id, int bank, Payment pay) {
        supplierController.addOnOrderSupplier(name, id, bank, pay);
        }

    public void addNoTransportSupplier(String name, int id, int bank, Payment pay, String address) {
        supplierController.addNoTransportSupplier(name, id, bank, pay, address);
    }

    public void addContactToSupplier(int supplier_id, String contact_name, String phone) {
        supplierController.addContactToSupplier(supplier_id, contact_name, phone);
    }

    public void changeSupplierName(int supplier_id, String name) {
        supplierController.changeSupplierName(supplier_id, name);
    }

    public void changeBankAccount(int supplier_id, int bankAccount) {
        supplierController.changeBankAccount(supplier_id, bankAccount);
    }

    public void changePayment(int supplier_id, Payment pay) {
        supplierController.changePayment(supplier_id, pay);
    }

    public void removeSupplier(int supplier_id) {
        supplierController.removeSupplierByID(supplier_id);
    }

    public void removeContactOfSupplier(int supplier_id, String contact_name, String cellphone) {
        supplierController.removeContactOfSupplier(supplier_id, contact_name, cellphone);
    }

    public void setNextDeliveryDateOfOnOrderSupplier(int supplier_id, int day) {
        supplierController.setNextDeliveryDateOfOnOrderSupplier(supplier_id, day);
    }

    public void setNextDeliveryDateOfNoTransportSupplier(int id, int day, int month, int year) {
        supplierController.setNextDeliveryDateOfNoTransportSupplier(id, day, month, year);
    }

    public void removeShipDayFromFixedSupplier(int id, int dayNumber) {
        supplierController.removeShipDayFromFixedSupplier(id, dayNumber);
    }

    public void startConnection() {
            supplierController.startConnection();
    }

//    public String[] getSupplierList(String categoryID, String makat) {
//        return supplierController.getSupplierList(categoryID, makat);
//    }

    //public void removeSampleData() {
//        supplierController.removeSampleData();
//    }

}
