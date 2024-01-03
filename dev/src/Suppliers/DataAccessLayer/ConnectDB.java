package Suppliers.DataAccessLayer;

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
        System.out.println("Creating tables...");
        try(Statement statement = createStatement()){
            String query = "CREATE TABLE IF NOT EXISTS 'Supplier' (" +
                    "'supplier_id' INTEGER NOT NULL," +
                    "'supplier_name' TEXT NOT NULL," +
                    "'bank_account'	INTEGER NOT NULL," +
                    "'payment'	TEXT NOT NULL," +
                    "PRIMARY KEY('supplier_id'));";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'FixedSupplierDays' (" +
                    "'supplier_id'	INT NOT NULL," +
                    "'day'	INTEGER NOT NULL," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')," +
                    "PRIMARY KEY('supplier_id','day')" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'NoTransportSupplierInfo' (" +
            "'supplier_id'	INT NOT NULL," +
                    "'address'	TEXT NOT NULL," +
                    "'nextDeliveryDate'	DATE," +
                    "PRIMARY KEY('supplier_id')," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'OnOrderSupplierNumOfDays' ("+
            "'supplier_id'	INT NOT NULL," +
                    "'numberOfDaysToNextOrder'	INTEGER," +
                    "PRIMARY KEY('supplier_id')," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id'));";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'SupplyAgreement' ("+
            "'supplier_id'	INT NOT NULL," +
                    "'makat'	INT NOT NULL," +
                    "'list_price'	FLOAT NOT NULL," +
                    "'max_amount'	INTEGER NOT NULL," +
                    "'catalog_code'	INTEGER NOT NULL," +
                    "FOREIGN KEY('makat') REFERENCES 'Product'('makat')," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')," +
                    "PRIMARY KEY('supplier_id','makat'));";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'DiscountByProduct' (" +
            "'supplier_id'	INT NOT NULL," +
                    "'makat'	INT NOT NULL," +
                    "'numOfProducts'	INTEGER NOT NULL," +
                    "'val'	FLOAT NOT NULL," +
                    "'ByPrice'	BOOLEAN NOT NULL," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')," +
                    "FOREIGN KEY('makat') REFERENCES 'Product'('makat'), " +
                    "PRIMARY KEY('supplier_id','makat','numOfProducts','val','ByPrice')" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'DiscountByOrder' (" +
            "'supplier_id'	INT NOT NULL," +
                    "'minimalPrice'	FLOAT NOT NULL," +
                    "'val'	FLOAT NOT NULL," +
                    "'ByPrice'	BOOLEAN NOT NULL," +
                    "PRIMARY KEY('supplier_id','minimalPrice','val','ByPrice')," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'Contact' (" +
            "'supplier_id'	INT NOT NULL," +
                    "'contact_name'	TEXT NOT NULL," +
                    "'cellphone'	TEXT NOT NULL," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')," +
                    "PRIMARY KEY('supplier_id', 'cellphone')" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'Orders' ("+
            "'supplier_id'	INT NOT NULL," +
                    "'order_number'	INTEGER NOT NULL," +
                    "'branch_code'	TEXT NOT NULL," +
                    "'order_date'	DATE NOT NULL," +
                    "'order_status'	TEXT NOT NULL,"+
                    "'total_price'	FLOAT NOT NULL," +
                    "'orderDiscount'	FLOAT NOT NULL," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')," +
                    "PRIMARY KEY('order_number' AUTOINCREMENT)" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'OrderDetailsByProduct' ("+
            "'order_number'	INTEGER NOT NULL," +
                    "'makat'	INT NOT NULL," +
                    "'product_name'	TEXT NOT NULL," +
                    "'amount'	INTEGER NOT NULL," +
                    "'list_price'	FLOAT NOT NULL," +
                    "'discount'	FLOAT NOT NULL," +
                    "'final_price'	FLOAT NOT NULL," +
                    "FOREIGN KEY('makat') REFERENCES 'Product'('makat')," +
                    "FOREIGN KEY('order_number') REFERENCES 'Orders'('order_number')," +
                    "PRIMARY KEY('order_number')" + ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS 'FixedPeriodOrder' (" +
            "'branch'	TEXT NOT NULL," +
                    "'supplier_id'	INTEGER NOT NULL," +
                    "'makat'	INTEGER NOT NULL," +
                    "'amount'	INTEGER NOT NULL," +
                    "'ship_day'	INTEGER NOT NULL," +
                    "FOREIGN KEY('supplier_id') REFERENCES 'Supplier'('supplier_id')," +
                    "FOREIGN KEY('makat') REFERENCES 'Product'('makat')," +
                    "PRIMARY KEY('supplier_id','makat','branch'));";
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
        }
        finally {
            close_connect();
        }
    }

    public void resetTables() throws SQLException {
        try(Statement statement = createStatement()){
            String query = "DROP TABLE IF EXISTS Supplier;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS FixedSupplierDays;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS NoTransportSupplierInfo;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS OnOrderSupplierNumOfDays;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS SupplyAgreement;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS DiscountByProduct;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS DiscountByOrder;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS Contact;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS Orders;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS OrderDetailsByProduct;";
            statement.execute(query);
            query = "DROP TABLE IF EXISTS FixedPeriodOrder;";
            statement.execute(query);
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

