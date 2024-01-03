package Inventory.DataAccessLayer.Mapper;

import Inventory.BusinessLayer.Branch;
import Inventory.BusinessLayer.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryProductDAO {
    private final ConnectDB connectDB = ConnectDB.getInstance();

    public CategoryProductDAO() {

    }

    public void insert(int categoryID, int productID) {
        try {
            connectDB.createTables();
            String query = "INSERT INTO Category_Product (categoryID, productID) VALUES (" + categoryID + ", " + productID + ")";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }
    public void addProductByCategory(ArrayList<Product> products, Integer categoryId){
        try {
            connectDB.createTables();
            for (Product product : products) {
                String query = "INSERT INTO Category_Product (category_id, makat) VALUES (" + categoryId + ", " + product.getMakat() + ")";
                connectDB.executeUpdate(query);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }
    public void delDete(int categoryID, int productID) {
        try {
            connectDB.createTables();
            String query = "DELETE FROM Category_Product WHERE categoryID = " + categoryID + " AND productID = " + productID;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public HashMap<Integer, ArrayList<Product>> startConnection() {
        HashMap<Integer, ArrayList<Product>> categoryProducts = new HashMap<>();
        HashMap<Integer, Product> products = new HashMap<>();
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Category_Product";
            ArrayList<HashMap<String,Object>> resultSet = connectDB.executeQuery(query);
//            products = ProductDAO.getInstance().loadData();
            ProductDAO productDAO=ProductDAO.getInstance();
            products= productDAO.getProducts();
            HashMap<Integer,HashMap<Branch,Integer>> amountInBranch = productDAO.getAmountInBranch();
            for (Integer makat : amountInBranch.keySet()) {
                products.get(makat).setCurrentAmount(amountInBranch.get(makat));
            }
            ArrayList<Product> productsList = new ArrayList<>(products.values());
            for (HashMap<String, Object> row : resultSet) {
                int categoryID = (int) row.get("category_id");
                int productID = (int) row.get("makat");
                Product product = productsList.stream().filter(p -> p.getMakat() == productID).findFirst().orElse(null);
                if (categoryProducts.containsKey(categoryID)) {
                    categoryProducts.get(categoryID).add(product);
                } else {
                    ArrayList<Product> productsArray = new ArrayList<>();
                    productsArray.add(product);
                    categoryProducts.put(categoryID, productsArray);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return categoryProducts;
    }
}