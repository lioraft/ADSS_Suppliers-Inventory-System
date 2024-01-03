package Inventory.DataAccessLayer.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectDB {
    public Connection conn;
    public final String url = "jdbc:sqlite:Inventory_Suppliers_Database.db";
    private static ConnectDB instance = null;

    private ConnectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        finally {
            close_connect();
        }
    }

    public static ConnectDB getInstance() {
        if (instance == null) {
            instance = new ConnectDB();
        }
        return instance;
    }

    public void createTables() throws SQLException {
        try(Statement statement = createStatement()){
            String query = "CREATE TABLE IF NOT EXISTS Category ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "Start_Discount Date,"
                    + "End_Discount Date,"
                    + "Discount FLOAT"
                    + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Product ("
                    + "makat INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "minAmount INTEGER NOT NULL,"
                    + "currentAmount INTEGER NOT NULL,"
                    + "amountInStore INTEGER,"
                    + "amountInInventory INTEGER,"
                    + "category_id INTEGER NOT NULL,"
                    + "sub_category TEXT,"
                    + "supplier_id INTEGER NOT NULL,"
                    + "Discount FLOAT,"
                    + "Start_Discount Date,"
                    + "End_Discount Date,"
                    + "FOREIGN KEY (category_id) REFERENCES Category(id)"
                    + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Items (" +
                    "producer INTEGER NOT NULL," +
                    "barcode INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "makat INTEGER NOT NULL," +
                    "name TEXT NOT NULL," +
                    "location TEXT NOT NULL," +
                    "is_defective INTEGER NOT NULL," +
                    "is_expired INTEGER NOT NULL," +
                    "expiration_date Date," +
                    "cost_price FLOAT NOT NULL," +
                    "selling_price FLOAT NOT NULL," +
                    "the_price_been_sold_at FLOAT," +
                    "defective_description TEXT," +
                    "size TEXT," +
                    "category INTEGER NOT NULL," +
                    "branch TEXT NOT NULL," +
                    "dateOfArrival TEXT NOT NULL," +
                    "FOREIGN KEY (makat) REFERENCES Product(makat),"+
                    "FOREIGN KEY (category) REFERENCES Category(id)"+");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Category_Product (" +
                    "category_id INTEGER NOT NULL," +
                    "makat INTEGER NOT NULL," +
                    "FOREIGN KEY (makat) REFERENCES Product(makat)" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Product_Discounts ("
                    + "makat INTEGER PRIMARY KEY,"
                    + "Discount FLOAT,"
                    + "Start_Discount Date,"
                    + "End_Discount Date,"
                    + "FOREIGN KEY (makat) REFERENCES Product(makat)"
                    + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS Product_Item (" +
                    "makat INTEGER NOT NULL," +
                    "barcode INTEGER NOT NULL PRIMARY KEY," +
                    "FOREIGN KEY (makat) REFERENCES Product(makat)," +
                    "FOREIGN KEY (barcode) REFERENCES Items(barcode)" +
                    ");";
            statement.execute(query);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            close_connect();
        }
    }

    public Statement createStatement() throws SQLException {
        conn = DriverManager.getConnection(url);
        return conn.createStatement();
    }

    public ArrayList<HashMap<String,Object>> convertToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        while (rs.next()){
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i){
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
    public ArrayList<HashMap<String, Object>> executeQuery(String query, Object... params) throws SQLException {
        try{
            createStatement();
            PreparedStatement ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            return convertToArrayList(rs);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            close_connect();
        }
        return null;
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
        try{
            createStatement();
            PreparedStatement ps = conn.prepareStatement("pragma foreign_keys=ON;");
            ps.executeUpdate();
            ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        finally {
            close_connect();
        }
    }

    public void resetTables() throws SQLException {
        try(Statement statement = createStatement()){
            String query = "DROP TABLE IF EXISTS Category;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS Product;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS Items;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS CATEGORY_PRODUCT;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS Product_Item;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS Product_Discounts;";
            statement.execute(query);
            createTables();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            close_connect();
        }
    }

    public void close_connect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}