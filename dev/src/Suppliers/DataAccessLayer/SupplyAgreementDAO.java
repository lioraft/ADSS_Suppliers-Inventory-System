package Suppliers.DataAccessLayer;

import Suppliers.BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class SupplyAgreementDAO {

    private final Suppliers.DataAccessLayer.ConnectDB connectDB = ConnectDB.getInstance();

    public SupplyAgreementDAO() {
    }

    public HashMap<Integer, ArrayList<SupplyAgreement>> loadData() {
        HashMap<Integer, ArrayList<SupplyAgreement>> agreements = new HashMap<>();
        try {
            // create query for extracting data of supply agreements
            String query = "SELECT * FROM SupplyAgreement";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                SupplyAgreement supplyagree = new SupplyAgreement((int) row.get("makat"), (double) row.get("list_price"), (int) row.get("catalog_code"), (int) row.get("max_amount"));
                query = "SELECT * FROM DiscountByProduct WHERE supplier_id = " + row.get("supplier_id") + " AND makat = " + row.get("makat");
                ArrayList<HashMap<String, Object>> resultSet2 = connectDB.executeQuery(query);
                if (resultSet2 != null) {
                    for (HashMap<String, Object> row2 : resultSet2) {
                        DiscountByProduct discount;
                        if ((int) row2.get("ByPrice") == 1) {
                            discount = new DiscountOfPriceByProduct((double) row2.get("val"), (int) row2.get("numOfProducts"));
                        } else {
                            discount = new DiscountOfPercentageByProduct((double) row2.get("val"), (int) row2.get("numOfProducts"));
                        }
                        supplyagree.addDiscount(discount);
                    }
                }
                ArrayList<SupplyAgreement> current_supplier = agreements.get((int) row.get("supplier_id"));
                if (current_supplier == null) {
                    current_supplier = new ArrayList<>();
                }
                // add agreement to supplier's list of agreements
                current_supplier.add(supplyagree);
                // add agreement to map of agreements
                agreements.put((int) row.get("supplier_id"), current_supplier);
            }

        }catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        // return map of agreements
        return agreements;
    }

    // function that adds supply agreement to db
    public void addSupplyAgreement(int supplierID, int productCode, double price, int catalog, int amount) {
        try {
        String query = "INSERT INTO SupplyAgreement (supplier_id, makat, list_price, catalog_code, max_amount) VALUES (" + supplierID + ", " + productCode + ", " + price + ", " + catalog + ", " + amount + ")";
        connectDB.executeUpdate(query);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    } finally {
        connectDB.close_connect();
    }
    }

    // function that removes supply agreement from db
    public void removeSupplyAgreement(int supplierID, int productCode) {
        try {
            String query = "DELETE FROM SupplyAgreement WHERE supplier_id = " + supplierID + " AND makat = " + productCode;
            connectDB.executeUpdate(query);
            query = "DELETE FROM DiscountByProduct WHERE supplier_id = " + supplierID + " AND makat = " + productCode;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds discount by order to db
    public void addDiscountByOrder(int supplierID, double discountValue, double minPrice, boolean option) {
        try {
            String query = "INSERT INTO DiscountByOrder (supplier_id, val, minimalPrice, ByPrice) VALUES (" + supplierID + ", " + discountValue + ", " + minPrice + ", " + option + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void addDiscountByProduct(int supplierID, int productCode, double discountValue, int numOfProducts, boolean option) {
        try {
            String query = "INSERT INTO DiscountByProduct (supplier_id, makat, val, numOfProducts, ByPrice) VALUES (" + supplierID + ", " + productCode + ", " + discountValue + ", " + numOfProducts + ", " + option + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public HashMap<Integer, ArrayList<SupplyAgreement>> startConnection() throws SQLException {
        return loadData();
    }

    public String getSupplyAgreementsOfSupplier(int supplierID) throws SQLException {
        String query = "SELECT * FROM SupplyAgreement WHERE supplier_id = " + supplierID;
        ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
        String result = "";
        for (HashMap<String, Object> row : resultSet) {
            result += "makat: " + row.get("makat") + ", list_price: " + row.get("list_price") + ", catalog_code: " + row.get("catalog_code") + ", max_amount: " + row.get("max_amount") + "\n";
        }
        return result;
    }

    public String[] getSupplierList() throws SQLException {
        String query = "SELECT * FROM Supplier";
        ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
        String[] result = new String[resultSet.size()];
        int i = 0;
        for (HashMap<String, Object> row : resultSet) {
            result[i] = String.valueOf(row.get("supplier_id"));
            i++;
        }
        return result;
    }
}
