package Inventory.DataAccessLayer.Mapper;

import Inventory.BusinessLayer.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoryDAO {
    private final ConnectDB connectDB = ConnectDB.getInstance();
    public CategoryDAO() {
    }
    public HashMap<Integer, Category> loadData() {
        HashMap<Integer, Category> categories = new HashMap<>();
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Category";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                String name = (String) row.get("name");
                int id = (int) row.get("id");
                Category category = new Category(name, id);
                String startDiscount = (String) row.get("Start_Discount");
                String endDiscount = (String) row.get("End_Discount");
                Double discount = (Double) row.get("Discount");
                if (startDiscount != null && endDiscount != null && discount != 0.0) {
                    category.setDiscount(startDiscount, endDiscount, discount.floatValue());
                }
                categories.put(id, category);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return categories;
    }
    public boolean addCategory(String name, int id){
        try {
            connectDB.createTables();
            String query = "INSERT INTO Category (name, id) VALUES ('" + name + "', " + id + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            connectDB.close_connect();
        }
        return true;
    }

    public String setDiscountByCategory(Integer categoryId, String startDiscount, String endDiscount, float discount){
        try {
            connectDB.createTables();
            String query = "UPDATE Category SET Start_Discount = '" + startDiscount + "', End_Discount = '" + endDiscount + "', Discount = " + discount + " WHERE id = " + categoryId;
            connectDB.executeUpdate(query);
            return "Discount added successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Discount already exists";
        } finally {
            connectDB.close_connect();
        }
    }


    public HashMap<Integer, Category> startConnection() throws SQLException {
        connectDB.createTables();
        return loadData();
    }

    public boolean removeCategory(int id) {
        try {
            connectDB.createTables();
            String query = "DELETE FROM Category WHERE id = " + id;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            connectDB.close_connect();
        }
        return true;
    }

    }

