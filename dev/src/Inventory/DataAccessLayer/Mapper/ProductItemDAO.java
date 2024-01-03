package Inventory.DataAccessLayer.Mapper;

public class ProductItemDAO {
    //Todo: check if needed. if not, delete.if yes, need to make ProductItem table.
    private final ConnectDB connectDB = ConnectDB.getInstance();
    public ProductItemDAO() {
    }
    public void insert(int productID, int ItemId) {
        try {
            connectDB.createTables();
            String query = "INSERT INTO ProductItem (productID,ItemId) VALUES (" +productID+ ", '" + ItemId + "')";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void delete(int productID, int ItemId){
        try {
            connectDB.createTables();
            String query = "DELETE FROM ProductItem WHERE productID = " + productID + " AND ItemId = " + ItemId;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

}
