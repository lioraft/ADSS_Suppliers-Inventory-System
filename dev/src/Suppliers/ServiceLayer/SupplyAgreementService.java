package Suppliers.ServiceLayer;

import Suppliers.BusinessLayer.SupplyAgreement;
import Suppliers.BusinessLayer.SupplyAgreementController;

import java.sql.SQLException;

public class SupplyAgreementService {

    private final SupplyAgreementController saController;

    public SupplyAgreementService() {
        saController = SupplyAgreementController.getInstance();
    }

    public void addSupplyAgreement(int supplierID, int productCode, double price, int catalog, int amount) {
        saController.addSupplyAgreement(supplierID, productCode, price, catalog, amount);
    }

    public void addDiscountByOrder(int supplierID, double discountValue, double minPrice, int option) {
        saController.addDiscountByOrder(supplierID, discountValue, minPrice, option);
    }

    public void addDiscountByProductToAgreement(int id, SupplyAgreement agreement, double discountValue, int numOfProducts, int option) {
        saController.addDiscountByProductToAgreement(id, agreement, discountValue, numOfProducts, option);
    }

    public void removeSupplyAgreement(int supplierID, int productCode) {
        saController.removeSupplyAgreement(supplierID, productCode);
    }

    public void printAllSupplyAgreementsOfSupplier(int supplierID) {
        saController.printAllSupplyAgreementsOfSupplier(supplierID);
    }

    public SupplyAgreement getSupplyAgreement(int supplierID, int productCode) {
        return saController.getSupplyAgreement(supplierID, productCode);
    }

    public void startConnection() {
        saController.startConnection();
    }

    public String getSupplyAgreementsOfSupplier(int supplierID) throws SQLException {
        return saController.getSupplyAgreementsOfSupplier(supplierID);
    }

    public String[] getSupplierList() {
        return saController.getSupplierList();
    }

//    public void removeSampleData() {
//        saController.removeSampleData();
//    }
}
