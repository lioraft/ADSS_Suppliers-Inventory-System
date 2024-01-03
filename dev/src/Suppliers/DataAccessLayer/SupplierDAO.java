package Suppliers.DataAccessLayer;

import Suppliers.BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class SupplierDAO {
    private final Suppliers.DataAccessLayer.ConnectDB connectDB = ConnectDB.getInstance();

    public SupplierDAO() {
    }

    public HashMap<Integer, Supplier> loadData() {
        HashMap<Integer, Supplier> suppliers = new HashMap<>();
        try {
            // create query for extracting data of fixed days suppliers
            String query = "SELECT * FROM Supplier WHERE supplier_id IN (SELECT supplier_id FROM FixedSupplierDays)";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                int supplier_id = (int) row.get("supplier_id");
                Supplier fixed_supplier = new FixedDaysSupplier((String) row.get("supplier_name"), supplier_id, (int) row.get("bank_account"), Payment.valueOf((String) row.get("payment")));
                // get days of fixed days supplier from DB fixeddays table
                query = "SELECT * FROM FixedSupplierDays WHERE supplier_id = " + (int) row.get("supplier_id");
                ArrayList<HashMap<String, Object>> days = connectDB.executeQuery(query);
                for (HashMap<String, Object> day : days) {
                    ((FixedDaysSupplier) fixed_supplier).addShipDay((int) day.get("day"));
                }
                suppliers.put(supplier_id, fixed_supplier);
            }

            // create query for extracting data of on order suppliers
            query = "SELECT * FROM Supplier WHERE supplier_id IN (SELECT supplier_id FROM OnOrderSupplierNumOfDays)";
            resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                int supplier_id = (int) row.get("supplier_id");
                Supplier on_order_supplier = new OnOrderSupplier((String) row.get("supplier_name"), supplier_id, (int) row.get("bank_account"), Payment.valueOf((String) row.get("payment")));
                query = "SELECT numberOfDaysToNextOrder FROM OnOrderSupplierNumOfDays WHERE numberOfDaysToNextOrder IS NOT NULL AND supplier_id = " + (int) row.get("supplier_id");
                if (connectDB.executeQuery(query).size() != 0) {
                    int days = (int) connectDB.executeQuery(query).get(0).get("numberOfDaysToNextOrder");
                    ((OnOrderSupplier) on_order_supplier).setNumberOfDaysToNextOrder(days);
                }
                suppliers.put(supplier_id, on_order_supplier);
            }
            // create query for extracting data of suppliers of no transport suppliers
            query = "SELECT * FROM Supplier WHERE supplier_id IN (SELECT supplier_id FROM NoTransportSupplierInfo)";
            resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                String query2 = "SELECT * FROM NoTransportSupplierInfo WHERE supplier_id = " + (int) row.get("supplier_id");
                String address = (String) connectDB.executeQuery(query2).get(0).get("address");
                Supplier no_transport_supplier = new NoTransportSupplier((String) row.get("supplier_name"), (int) row.get("supplier_id"), (int) row.get("bank_account"), Payment.valueOf((String) row.get("payment")), address);
                int supplier_id = (int) row.get("supplier_id");
                if (connectDB.executeQuery(query2).get(0).get("nextDeliveryDate") != null) {
                    String[] date = connectDB.executeQuery(query2).get(0).get("nextDeliveryDate").toString().split("-");
                    ((NoTransportSupplier) no_transport_supplier).setNextDeliveryDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                }
                suppliers.put(supplier_id, no_transport_supplier);
            }

            // get contacts of each supplier
            query = "SELECT * FROM Contact";
            resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                Supplier supplier = suppliers.get((int) row.get("supplier_id"));
                supplier.addContact((String) row.get("contact_name"), (String) row.get("cellphone"));
            }

            // get discounts of each supplier
            query = "SELECT * FROM DiscountByOrder";
            resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                Supplier supplier = suppliers.get((int) row.get("supplier_id"));
                if ((int)row.get("ByPrice") == 1) {
                    supplier.addOrderDiscount(new DiscountOfPriceByOrder((double)row.get("val"), (double)row.get("minimalPrice")));
                }
                else {
                    supplier.addOrderDiscount(new DiscountOfPercentageByOrder((double)row.get("val"), (double)row.get("minimalPrice")));
                }
            }

            // extract information of orders
            query = "SELECT * FROM Orders";
            resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                String[] dateString = row.get("order_date").toString().split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[2]));
                Order order = suppliers.get((int) row.get("supplier_id")).addExistingOrder(date, (String) row.get("branch_code"));
                order.setOrderDiscount((double) row.get("orderDiscount"));
                order.setOrderStatus((String) row.get("order_status"));
                // add order details for each order
                query = "SELECT * FROM OrderDetailsByProduct WHERE order_number = " + (int) row.get("order_number");
                ArrayList<HashMap<String, Object>> details = connectDB.executeQuery(query);
                for (HashMap<String, Object> row2 : details) {
                    order.addProducts((int) row2.get("makat"), (String) row2.get("product_name"), (int) row2.get("amount"), (double)row2.get("list_price"), (double)row2.get("discount"), (double)row2.get("final_price"));
                }
            }

            // extract information of supply agreements
            query = "SELECT * FROM SupplyAgreement";
            resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                Supplier current_supplier = suppliers.get((int) row.get("supplier_id"));
                SupplyAgreement supplyagree = current_supplier.addAgreement((int) row.get("makat"), (double) row.get("list_price"), (int) row.get("catalog_code"), (int) row.get("max_amount"));
                query = "SELECT * FROM DiscountByProduct WHERE supplier_id = " + row.get("supplier_id") + " AND makat = " + row.get("makat");
                ArrayList<HashMap<String, Object>> resultSet2 = connectDB.executeQuery(query);
                for (HashMap<String, Object> row2 : resultSet2) {
                    DiscountByProduct discount;
                    if ((int)row2.get("ByPrice") == 1) {
                        discount = new DiscountOfPriceByProduct((double) row2.get("val"), (int) row2.get("numOfProducts"));
                    }
                    else {
                        discount = new DiscountOfPercentageByProduct((double) row2.get("val"), (int) row2.get("numOfProducts"));
                    }
                    supplyagree.addDiscount(discount);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return suppliers;
    }


    // add supplier to the database
    public void addSupplier(String supplier_name, int supplier_id, int bank_account, Payment payment_method) {
        try {
            String query = "INSERT INTO Supplier (supplier_id, supplier_name, bank_account, payment) VALUES (" + supplier_id + ",'" + supplier_name + "'," + bank_account + ",'" + payment_method.toString() + "')";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds shipping days for fixed days supplier
    public void addDaysForFixedSupplier(int id, int day) {
        try {
            String query = "INSERT INTO FixedSupplierDays (supplier_id, day) VALUES (" + id + "," + day + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that removes shipping days for fixed days supplier
    public void removeDaysForFixedSupplier(int id, int day) {
        try {
            String query = "DELETE FROM FixedSupplierDays WHERE supplier_id = " + id + " AND day = " + day;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds address of no transport supplier
    public void addAddressForNoTransport(int id, String address) {
        try {
            String query = "INSERT INTO NoTransportSupplierInfo (supplier_id, address, nextDeliveryDate) VALUES (" + id + ",'" + address + "'," + null + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that sets next delivery date for no transport supplier
    public void setNextDeliveryDateForNoTransport(int id, int day, int month, int year) {
        try {
            String query = "UPDATE NoTransportSupplierInfo SET nextDeliveryDate = '" + year + "-" + month + "-" + day + "' WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds next delivery date for no transport supplier
    public void addNextDeliveryDateForNoTransport(int id, int day, int month, int year) {
        try {
            String query = "UPDATE NoTransportSupplierInfo SET nextDeliveryDate = '" + year + "-" + month + "-" + day + "' WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that inserts new on order supplier record to table of on order suppliers
    public void addOnOrderSupplier(int supplier_id)
    {
        try {
            String query = "INSERT INTO OnOrderSupplierNumOfDays (supplier_id, numberOfDaysToNextOrder) VALUES (" + supplier_id + ", NULL)";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that takes in id of supplier, name of contact and cellphone, and adds the contact to supplier's list of contacts
    public void addContact(int id, String name, String cellphone) {
        try {
            String query = "INSERT INTO Contact (supplier_id, contact_name, cellphone) VALUES (" + id + ",'" + name + "','" + cellphone + "')";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that takes in id of supplier, name of contact and cellphone, and removes the contact from supplier's list of contacts
    public void removeContact(int id, String name, String cellphone) {
        try {
            String query = "DELETE FROM Contact WHERE supplier_id = " + id + " AND contact_name = '" + name + "' AND cellphone = '" + cellphone + "'";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // remove supplier from suppliers tables
    public void removeSupplier(int id) {
        try {
            String query = "DELETE FROM FixedSupplierDays WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM NoTransportSupplierInfo WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM OnOrderSupplierNumOfDays WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM SupplyAgreement WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM DiscountByProduct WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM DiscountByOrder WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM Contact WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM FixedPeriodOrder WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            query = "DELETE FROM Supplier WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
            removeOrdersOfSupplier(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that selects all the orders of a specified supplier and deletes them from OrderDetailsByProduct and from Orders
    private void removeOrdersOfSupplier(int id) {
        try {
            String query = "SELECT * FROM Orders WHERE supplier_id = " + id;
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                query = "DELETE FROM OrderDetailsByProduct WHERE order_id = " + row.get("order_id");
                connectDB.executeUpdate(query);
            }
            query = "DELETE FROM Orders WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that takes in number of days and updates the number of days in db
    public void setNumOfDaysForOnOrder(int id, int numOfDays) {
        try {
            String query = "UPDATE 'OnOrderSupplierNumOfDays' SET numberOfDaysToNextOrder = " + numOfDays + " WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds number of days for on order supplier
    public void addNumOfDaysForOnOrder(int id, int numOfDays) {
        try {
            String query = "INSERT INTO OnOrderSupplierNumOfDays (supplier_id, numberOfDaysToNextOrder) VALUES (" + id + "," + numOfDays + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void setName(int id, String name) {
        try {
            String query = "UPDATE Supplier SET supplier_name = '" + name + "' WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void setBankAccount(int id, int bank) {
        try {
            String query = "UPDATE Supplier SET bank_account = " + bank + " WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void setPayment(int id, String payment) {
        try {
            String query = "UPDATE Supplier SET payment = '" + payment + "' WHERE supplier_id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public HashMap<Integer, Supplier> startConnection() throws SQLException {
        return loadData();
    }
}

