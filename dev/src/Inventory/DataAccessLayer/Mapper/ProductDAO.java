package Inventory.DataAccessLayer.Mapper;

import Inventory.BusinessLayer.Branch;
import Inventory.BusinessLayer.Category;
import Inventory.BusinessLayer.Product;
import Suppliers.BusinessLayer.Order;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDAO {
    private final ConnectDB connectDB = ConnectDB.getInstance();
    private static ProductDAO instance = null;
    public ProductDAO() {
    }

    public static ProductDAO getInstance() {
        if (instance == null)
            instance = new ProductDAO();
        return instance;
    }

    public HashMap<Integer, Product> loadData() {
        HashMap<Integer, Product> products = new HashMap<>();
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Product";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                String name = (String) row.get("name");
                int id = (int) row.get("makat");
                int minAmount = (int) row.get("minAmount");
                //int currentAmount = (int) row.get("currentAmount");
                int categoryID = (int) row.get("category_id");
                String sub_category = (String) row.get("sub_category");
                int supplierID = (int) row.get("supplier_id");
                Product product = new Product(name, minAmount, categoryID, sub_category, id, supplierID);
                //product.setCurrentAmount(currentAmount);
                String startDiscount = (String) row.get("Start_Discount");
                String endDiscount = (String) row.get("End_Discount");
                Double discount = (Double) row.get("Discount");
                if (startDiscount != null && endDiscount != null && discount != 0.0) {
                    product.setDiscount(startDiscount, endDiscount, discount.floatValue());
                }
                products.put(id, product);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return products;
    }

    public Boolean addProduct(String name, int minAmount, int categoryID, String sub_category, int makat, int supplierID){
        try {
            connectDB.createTables();
            String query = "INSERT INTO Product (name, currentAmount, minAmount, category_ID, sub_category, makat, supplier_id) VALUES ('" + name + "'," + 0 + ", " + minAmount + ", " + categoryID + ", '" + sub_category + "', " + makat + ", " + supplierID + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            connectDB.close_connect();
        }
        return true;
    }


    public String setMinimum(int productID, int minAmount){
        try {
            connectDB.createTables();
            String query = "UPDATE Product SET minAmount = " + minAmount + " WHERE makat = " + productID;
            connectDB.executeUpdate(query);
            return "Minimum amount updated successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Product not found";
        } finally {
            connectDB.close_connect();
        }
    }
    public String addItemToProduct(int productID, int amount){
        try {
            connectDB.createTables();
            String query = "UPDATE Product SET currentAmount = currentAmount + " + amount + " WHERE makat = " + productID;
            connectDB.executeUpdate(query);
            return "Item added successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Product not found";
        } finally {
            connectDB.close_connect();
        }
    }

    public void reduceAmountOfProductByID(int productID, int amount) {
        try {
            connectDB.createTables();
            String query = "UPDATE Product SET currentAmount = currentAmount - " + amount + " WHERE makat = " + productID;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void setDiscountByProduct(int productID, float discount, String start, String end) {
        try {
            connectDB.createTables();
            String query =null;
            query = "UPDATE Product SET Start_Discount = '" + start + "', End_Discount = '" + end + "', Discount = " + discount + " WHERE makat = " + productID;
            connectDB.executeUpdate(query);
            query = "INSERT INTO Product_Discounts (makat, Start_Discount, End_Discount, Discount) VALUES (" + productID + ", '" + start + "', '" + end + "', " + discount + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public HashMap<Integer,ArrayList<String>> getProductDiscoubt(){
        HashMap<Integer,ArrayList<String>> productDiscount = new HashMap<>();
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Product_Discounts GROUP BY makat";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            for(HashMap<String,Object> row : resultSet){
                int makat = (int) row.get("makat");
                String start = (String) row.get("Start_Discount");
                String end = (String) row.get("End_Discount");
                Double discount = (Double) row.get("Discount");
                ArrayList<String> discountDetails = new ArrayList<>();
                discountDetails.add("Discount: "+discount+" Start date: "+start+" End date: "+end);
                productDiscount.put(makat,discountDetails);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return productDiscount;

    }
    public void addItem(int productID) {
        try {
            connectDB.createTables();
            String query = "UPDATE Product SET currentAmount = currentAmount + 1 WHERE makat = " + productID;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }
    public ArrayList<Integer> getAmountInInventoryByMakat(int makat) {
        ArrayList<Integer> amountInInventory = new ArrayList<>();
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items WHERE makat = " + makat + " AND location = 'INVENTORY'";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                amountInInventory.add((int) row.get("barcode"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return amountInInventory;
    }

    public void startConnection() throws SQLException {
        connectDB.createTables();
        loadData();
    }

    public void removeSampleData() {
        try {
            connectDB.createTables();
            connectDB.resetTables();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }



    public HashMap<Integer, Product> getProducts() {
        try{
            connectDB.createTables();
            String query = "SELECT * FROM Product";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            HashMap<Integer, Product> products = new HashMap<>();
            for (HashMap<String, Object> row : resultSet) {
                String start = (String) row.get("Start_Discount");
                String end = (String) row.get("End_Discount");
                Double discount = (Double) row.get("Discount");
                Product product = new Product((String) row.get("name"), (int) row.get("minAmount"), (int) row.get("category_id"), (String) row.get("sub_category"), (int) row.get("makat"), (int) row.get("supplier_id"));
                if(start!=null && end!=null && discount!=null) {
                    product.setDiscount(start, end, discount);

                }
                products.put(product.getMakat(), product);
            }
            return products;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            connectDB.close_connect();
        }
    }

    public HashMap<Integer, Order> getOrders() {
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


    public HashMap<Integer, HashMap<Branch, Integer>> getAmountInBranch() {
        //SELECT makat,count(barcode), branch
        //FROM Items
        //GROUP BY makat ,branch;
        try {
            connectDB.createTables();
            String query = "SELECT makat, branch, count(barcode) FROM Items WHERE location != 'SOLD' GROUP BY makat, branch";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            HashMap<Integer, HashMap<Branch, Integer>> amountInBranch = new HashMap<>();
            for (HashMap<String, Object> row : resultSet) {
                int makat = (int) row.get("makat");
                Branch branch = Branch.valueOf((String) row.get("branch"));
                int amount = (int) row.get("count(barcode)");
                if (!amountInBranch.containsKey(makat)) {
                    amountInBranch.put(makat, new HashMap<>());
                }
                amountInBranch.get(makat).put(branch, amount);
            }
            return amountInBranch;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get amount in branch");
        } finally {
            connectDB.close_connect();
        }
    }

    public int getNumOfItems() {
        try {
            connectDB.createTables();
            String query = "SELECT COUNT(*) FROM Items";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            return (int) resultSet.get(0).get("COUNT(*)");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to get number of items");
        } finally {
            connectDB.close_connect();
        }
    }

    public boolean removeProduct(int id) {
        try {
            connectDB.createTables();
            String query = "DELETE FROM Product WHERE makat = " + id;
            connectDB.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            connectDB.close_connect();
        }
    }
}