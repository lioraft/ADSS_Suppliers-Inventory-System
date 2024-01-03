package Suppliers.DataAccessLayer;

import Suppliers.BusinessLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderDAO {
    private final Suppliers.DataAccessLayer.ConnectDB connectDB = ConnectDB.getInstance();

    public OrderDAO() {
    }

    public HashMap<Integer, Order> loadData() {
        HashMap<Integer, Order> orders = new HashMap<>();
        try {
            // create query for extracting data of orders
            String query = "SELECT * FROM Orders";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                String dateString = row.get("order_date").toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateString, formatter);
                Order order = new Order(date, (String) row.get("branch_code"));
                order.setOrderNumberForOrderFromDB((int) row.get("order_number"));
                order.setOrderDiscount((double) row.get("orderDiscount"));
                order.setOrderStatus((String) row.get("order_status"));
                // add order details for each order
                query = "SELECT * FROM OrderDetailsByProduct WHERE order_number = " + (int) row.get("order_number");
                ArrayList<HashMap<String, Object>> details = connectDB.executeQuery(query);
                for (HashMap<String, Object> row2 : details) {
                    order.addProducts((int) row2.get("makat"), (String) row2.get("product_name"), (int) row2.get("amount"), (double)row2.get("list_price"), (double)row2.get("discount"), (double)row2.get("final_price"));
                }
                orders.put(order.getOrderNumber(), order);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return orders;
    }

    // function that adds an order to database
    public void addOrder(int supplier_id, int order_number, String branch, LocalDate date, String status, double totalprice, double discount) {
        try {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // date in "yyyy-MM-dd" format
            String query = "INSERT INTO 'Orders' (supplier_id, order_number, branch_code, order_date, order_status, total_price, orderDiscount) VALUES (" + supplier_id + "," + order_number + ", '" + branch + "', '" + formattedDate + "', '" + status + "', " + totalprice + ", " + discount + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds record of order details to database
    public void addOrderDetailsByProduct(int order_number, int makat, String product_name, int amount, double list_price, double discount, double final_price) {
        try {
            String query = "INSERT INTO 'OrderDetailsByProduct' (order_number, makat, product_name, amount, list_price, discount, final_price) VALUES (" + order_number + ", " + makat + ", '" + product_name + "', " + amount + ", " + list_price + ", " + discount + ", " + final_price + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that takes in product code and returns the name of the product
    public String getProductName(int productCode) {
        String productName = "";
        try {
            String query = "SELECT name FROM Product WHERE makat = " + productCode;
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            productName = (String) resultSet.get(0).get("name");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return productName;
    }

    // function that changes order status to cancel
    public void cancelOrder(int order_number) {
        try {
            String query = "UPDATE 'Orders' SET order_status = 'Canceled' WHERE order_number = " + order_number;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that changes order status to confirmed
    public void confirmOrder(int order_number) {
        try {
            String query = "UPDATE 'Orders' SET order_status = 'Completed' WHERE order_number = " + order_number;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that adds FixedPeriodOrder to database
    public void addFixedPeriodOrder(int supplier_id, String branch_code, int makat, int amount, int ship_day) {
        try {
            String query = "INSERT INTO 'FixedPeriodOrder' (supplier_id, branch, makat, amount, ship_day) VALUES (" + supplier_id + ", '" + branch_code + "', " + makat + ", " + amount + ", " + ship_day + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    // function that takes in supplier id, product code and branch, and removes the record from db
    public void removeFixedPeriodOrder(int supplierID, int productCode, String branch) {
        try {
            String query = "DELETE FROM FixedPeriodOrder WHERE supplier_id = " + supplierID + " AND makat = " + productCode + " AND branch = '" + branch + "'";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public ArrayList<ArrayList<FixedPeriodOrder>> getFixedPeriodOrders() {
        ArrayList<ArrayList<FixedPeriodOrder>> fixed_orders = new ArrayList<ArrayList<FixedPeriodOrder>>();
        // create lists of 7 days
        for (int i = 0; i < 7; i++) {
            fixed_orders.add(new ArrayList<FixedPeriodOrder>());
        }
        try {
            // create query for extracting data
            String query = "SELECT * FROM FixedPeriodOrder";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                int day = (int) row.get("ship_day");
                FixedPeriodOrder order = new FixedPeriodOrder((int) row.get("supplier_id"), (String) row.get("branch"), (int) row.get("makat"), (int) row.get("amount"));
                fixed_orders.get(day).add(order);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return fixed_orders;
    }

    public String setOrderStatus(int order_number, String status) {
        try {
            String query = "UPDATE Orders SET order_status = " + status + " WHERE order_number = " + order_number;
            connectDB.executeUpdate(query);
            return "Status updated successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Order not found";
        } finally {
            connectDB.close_connect();
        }
    }

    public String setOrderDiscount(int order_number, double discount) {
        try {
            String query = "UPDATE Supplier SET orderDiscount = " + discount + " WHERE order_number = " + order_number;
            connectDB.executeUpdate(query);
            return "Discount updated successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Order not found";
        } finally {
            connectDB.close_connect();
        }
    }

    public HashMap<Integer, Order>  startConnection() throws SQLException {
        return loadData();
    }
}
