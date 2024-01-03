package Suppliers.BusinessLayer;

import Inventory.BusinessLayer.ProductController;
import Suppliers.DataAccessLayer.SupplyAgreementDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SupplyAgreementController { //Controller for supply agreement as singleton

    HashMap<Integer, ArrayList<Supplier>> suppliersByProduct; // <product_code, list<Supplier>>: the index is product
    // codes, and the values are list of suppliers that supply that product
    private static SupplyAgreementController instance = null; // the instance of the Supply Agreement Controller

    private boolean opened_connection = false; // boolean that indicates if connection to DB is open

    SupplyAgreementDAO saDAO; // the supply agreement DAO

    // controller constructor
    private SupplyAgreementController() {
        saDAO = new SupplyAgreementDAO();
        suppliersByProduct = new HashMap<Integer, ArrayList<Supplier>>();
    }

    // get instance of supply agreement controller. if it doesn't exist, create it
    public static SupplyAgreementController getInstance() {
        if (instance == null)
            instance = new SupplyAgreementController();
        return instance;
    }

    // function that adds supply agreement to supplier's list of agreements, and returns the agreement made
    public SupplyAgreement addSupplyAgreement(int supplierID, int productCode, double price, int catalog, int amount) {
        // get instance of supplier
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        // if doesn't exist, throw exception
        if (supplier == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // if product doesn't exist, throw exception
        if (ProductController.getInstance().getProductById(productCode) == null)
            throw new IllegalArgumentException("Product code doesn't exist");
        // initialize new supply agreement
        SupplyAgreement agreement = supplier.addAgreement(productCode, price, catalog, amount);
        // add to list of suppliers that supply this product
        if (!suppliersByProduct.containsKey(productCode))
            suppliersByProduct.put(productCode, new ArrayList<Supplier>());
        suppliersByProduct.get(productCode).add(supplier);
        // update db
        saDAO.addSupplyAgreement(supplierID, productCode, price, catalog, amount);
        // return the agreement initialized
        return agreement;
    }

    // function that removes supply agreement from supplier's list of agreements
    public void removeSupplyAgreement(int supplierID, int productCode) {
        // get instance of supplier
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        // if doesn't exist, throw exception
        if (supplier == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // if product doesn't exist, throw exception
        if (ProductController.getInstance().getProductById(productCode) == null)
            throw new IllegalArgumentException("Product code doesn't exist");
        // remove supply agreement from supplier's list of agreements
        if (!supplier.removeAgreement(productCode))
            throw new IllegalArgumentException("Supply agreement doesn't exist");
        // remove from list of suppliers that supply this product
        if (suppliersByProduct.containsKey(productCode))
            suppliersByProduct.get(productCode).remove(supplier);
        // update db
        saDAO.removeSupplyAgreement(supplierID, productCode);
    }

    // function that adds discount by order to supplier's list of discounts, based on discount option given
    public void addDiscountByOrder(int supplierID, double discountValue, double minPrice, int option) {
        // get instance of supplier
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        // if doesn't exist, throw exception
        if (supplier == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // create instance of discount by option given and add to list of discounts
        DiscountByOrder newDiscount;
        // discount by percentage
        if (option == 1) {
            newDiscount = new DiscountOfPercentageByOrder(discountValue, minPrice);
            supplier.addOrderDiscount(newDiscount);
        }
        // discount by price
        else if (option == 2) {
            newDiscount = new DiscountOfPriceByOrder(discountValue, minPrice);
            supplier.addOrderDiscount(newDiscount);
        }
        else throw new IllegalArgumentException("Invalid option");
        // update db
        boolean byprice;
        if (option == 1)
            byprice = false;
        else
            byprice = true;
        saDAO.addDiscountByOrder(supplierID, discountValue, minPrice, byprice);
    }

    // function that takes in agreement and discount information of that agreement, initialize the discount and adds to agreement
    public void addDiscountByProductToAgreement(int supplier_id, SupplyAgreement agreement, double discountValue, int numOfProducts, int option) {
        DiscountByProduct newDiscount;
        if (option == 1) {
            newDiscount = new DiscountOfPercentageByProduct(discountValue, numOfProducts);
            agreement.addDiscount(newDiscount);
        }
        else if (option == 2) {
            newDiscount = new DiscountOfPriceByProduct(discountValue, numOfProducts);
            agreement.addDiscount(newDiscount);
        }
        else {
            throw new IllegalArgumentException("Invalid option");
        }
        // update db
        boolean byprice;
        if (option == 1)
            byprice = false;
        else
            byprice = true;
        saDAO.addDiscountByProduct(supplier_id, agreement.getProductCode(), discountValue, numOfProducts, byprice);
    }

    // function that prints all the supply agreements of a supplier
    public void printAllSupplyAgreementsOfSupplier(int supplierID) {
        // get instance of supplier
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        // if doesn't exist, throw exception
        if (supplier == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // print all supply agreements of supplier
        supplier.printSupplyAgreements();
    }

    // get the suppliers who deliver a specific product
    public ArrayList<Supplier> getSuppliersByProduct(int productCode) {
        return suppliersByProduct.get(productCode);
    }

    public SupplyAgreement getSupplyAgreement(int id, int product_code) {
        Supplier supplier = SupplierController.getInstance().getSupplierByID(id);
        return supplier.getAgreement(product_code);
    }

    public void startConnection() {
        try {
            if (!opened_connection) {
                // get all supply agreements by supplier
                HashMap<Integer, ArrayList<SupplyAgreement>> agreementsBySupplier = this.saDAO.startConnection();
                // get list of suppliers
                Collection<Supplier> suppliers = SupplierController.getInstance().getSuppliers();
                // iterate all suppliers and add them to map based on the supply agreements they have
                for (Supplier supplier : suppliers) {
                    ArrayList<SupplyAgreement> agreements = agreementsBySupplier.get(supplier.getPrivateCompanyNumber());
                    if (agreements != null) {
                        for (SupplyAgreement agreement : agreements) {
                            suppliersByProduct.computeIfAbsent(agreement.getProductCode(), k -> new ArrayList<Supplier>());
                            suppliersByProduct.get(agreement.getProductCode()).add(supplier);
                        }
                    }
                }
                opened_connection = true;
            }
        } catch (Exception e) {
            System.out.println("Error connecting to supply agreements database");
        }
    }

    public String getSupplyAgreementsOfSupplier(int supplierID) throws SQLException {
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        if (supplier == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        return saDAO.getSupplyAgreementsOfSupplier(supplierID);
    }

    public String[] getSupplierList() {
        try {
            return saDAO.getSupplierList();
        } catch (SQLException e) {
            System.out.println("Error getting supplier list");
        }
        return null;
    }
}
