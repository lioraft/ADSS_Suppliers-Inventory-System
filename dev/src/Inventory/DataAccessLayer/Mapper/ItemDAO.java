package Inventory.DataAccessLayer.Mapper;

import Inventory.BusinessLayer.Branch;
import Inventory.BusinessLayer.Item;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemDAO {
    private final ConnectDB connectDB = ConnectDB.getInstance();

    public ItemDAO() {

    }

    public void loadData() {
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            for (HashMap<String, Object> row : resultSet) {
                Item item;
                String location1 = (String) row.get("location");
                Item.Location location = Item.Location.valueOf(location1);
                if (row.get("size") != null) {
                    item = new Item((Integer) row.get("producer"), (int) row.get("barcode"), (String) row.get("name"), location, (String) row.get("expiration_date"), (double) row.get("cost_price"), (int) row.get("makat"), (String) row.get("branch"));
                } else {
                    item = new Item((Integer) row.get("producer"), (int) row.get("barcode"), (String) row.get("name"), location, (String) row.get("expiration_date"), (double) row.get("cost_price"), (int) row.get("makat"), (String) row.get("size"), (String) row.get("branch"));
                }
                item.setDateOfArrival(LocalDate.parse((String) row.get("dateOfArrival")));
                if ((int) row.get("is_defective") == 1)
                    item.setDefective(row.get("defective_description").toString());
                if ((int) row.get("is_expired") == 1)
                    item.setExpired(true);
                if (row.get("selling_price") != null)
                    item.setSellingPrice((double) row.get("selling_price"));
                if (row.get("the_price_been_sold_at") != null)
                    item.setThePriceBeenSoldAt((double) row.get("the_price_been_sold_at"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public boolean addItem(Integer manufacturer, Integer barcode, String name, Item.Location locate, String expirationDate, double costPrice, int category, int productID, String size, double sellingPrice, String branch, String dateOfArrival) {
        try {
            connectDB.createTables();
            String query = "INSERT INTO Items (producer, barcode, name, location, expiration_date, cost_price, category, makat, size, is_defective, is_expired, selling_price,branch,dateOfArrival) VALUES ('" + manufacturer + "', " + barcode + ", '" + name + "', '" + locate + "', '" + expirationDate + "', " + costPrice + ", " + category + ", '" + productID + "', '" + size + "', 0, 0, " + sellingPrice + ", '" + branch + "', '" + dateOfArrival + "')";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            connectDB.close_connect();
        }
        return true;
    }

    public void setSold(int barcode) {
        try {
            connectDB.createTables();
            String query = "UPDATE Items SET location = 'SOLD' WHERE barcode = " + barcode;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public String getItem(Integer CategoryID, Integer ItemID) {
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items WHERE makat = " + CategoryID + " AND barcode = " + ItemID;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return "Item sold successfully";
    }

    public String itemsInStock(Integer CategoryID) {
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items WHERE makat = " + CategoryID + " AND (location = 'INVENTORY' OR location = 'STORE')";
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
        return "Item sold successfully";
    }

    public void moveItemToStore(Integer ItemID) {
        try {
            connectDB.createTables();
            String query = "UPDATE Items SET location = 'STORE' WHERE barcode = " + ItemID;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void defective(Integer DefItem, String reason) {
        try {
            connectDB.createTables();
            String query = "UPDATE Items SET is_defective = 1 WHERE barcode = " + DefItem;
            connectDB.executeUpdate(query);
            query = "UPDATE Items SET defective_description = '" + reason + "' WHERE barcode = " + DefItem;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void setExpired(int barcode) {
        try {
            connectDB.createTables();
            String query = "UPDATE Items SET is_expired = 1 WHERE barcode = " + barcode;
            connectDB.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void setThePriceBeenSoldAt(int barcode, double thePriceBeenSoldAt) {
        try {
            connectDB.createTables();
            String query = "UPDATE Items SET the_price_been_sold_at = " + thePriceBeenSoldAt + " WHERE barcode = " + barcode;
            connectDB.executeUpdate(query);
            //update the location to be sold
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectDB.close_connect();
        }
    }

    public void startConnection() throws SQLException {
        connectDB.createTables();
        loadData();
    }

    public HashMap<Integer, ArrayList<Item>> getSoldItems() {
        try {
            connectDB.createTables();
            String query = "SELECT id FROM Category ";
            ArrayList<HashMap<String, Object>> CategorySet = connectDB.executeQuery(query);
            HashMap<Integer, ArrayList<Item>> soldItems = new HashMap<>();
            for (HashMap<String, Object> row : CategorySet) {
                int categoryID = (int) row.get("id");
                query = "SELECT * FROM Items WHERE category = " + categoryID + " AND location = 'SOLD'";
                ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
                ArrayList<Item> items = new ArrayList<>();
                for (HashMap<String, Object> itemRow : resultSet) {
                    Item item;
                    Item.Location location = Item.Location.valueOf((String) itemRow.get("location"));
                    if (itemRow.get("size") != null) {
                        item = new Item((Integer) itemRow.get("producer"), (int) itemRow.get("barcode"), (String) itemRow.get("name"), location, (String) itemRow.get("expiration_date"), (double) itemRow.get("cost_price"), (int) itemRow.get("makat"), (String) itemRow.get("branch"));
                    } else {
                        item = new Item((Integer) itemRow.get("producer"), (int) itemRow.get("barcode"), (String) itemRow.get("name"), location, (String) itemRow.get("expiration_date"), (double) itemRow.get("cost_price"), (int) itemRow.get("makat"), (String) itemRow.get("size"), (String) itemRow.get("branch"));
                    }
                    if ((int) itemRow.get("is_defective") == 1)
                        item.setDefective(itemRow.get("defective_description").toString());
                    if ((int) itemRow.get("is_expired") == 1)
                        item.setExpired(true);
                    if (itemRow.get("selling_price") != null)
                        item.setSellingPrice((double) itemRow.get("selling_price"));
                    if (itemRow.get("the_price_been_sold_at") != null)
                        item.setThePriceBeenSoldAt((double) itemRow.get("the_price_been_sold_at"));
                    items.add(item);
                }
                soldItems.put(categoryID, items);
            }
            return soldItems;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<Integer, Item> getItemById() {
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            HashMap<Integer, Item> items = new HashMap<>();
            for (HashMap<String, Object> itemRow : resultSet) {
                Item item;
                Item.Location location = Item.Location.valueOf((String) itemRow.get("location"));
                if (itemRow.get("size") != null) {
                    item = new Item((Integer) itemRow.get("producer"), (int) itemRow.get("barcode"), (String) itemRow.get("name"), location, (String) itemRow.get("expiration_date"), (double) itemRow.get("cost_price"), (int) itemRow.get("makat"), (String) itemRow.get("branch"));
                } else {
                    item = new Item((Integer) itemRow.get("producer"), (int) itemRow.get("barcode"), (String) itemRow.get("name"), location, (String) itemRow.get("expiration_date"), (double) itemRow.get("cost_price"), (int) itemRow.get("makat"), (String) itemRow.get("size"), (String) itemRow.get("branch"));
                }
                if ((int) itemRow.get("is_defective") == 1)
                    item.setDefective(itemRow.get("defective_description").toString());
                if ((int) itemRow.get("is_expired") == 1)
                    item.setExpired(true);
                if (itemRow.get("selling_price") != null)
                    item.setSellingPrice((double) itemRow.get("selling_price"));
                if (itemRow.get("the_price_been_sold_at") != null)
                    item.setThePriceBeenSoldAt((double) itemRow.get("the_price_been_sold_at"));
                items.put((int) itemRow.get("barcode"), item);
            }
            return items;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<Branch, Integer> getBranches(int makat) {
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items WHERE makat = " + makat + " AND location = 'STORE' G;";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            HashMap<Branch, Integer> branches = new HashMap<>();
            for (HashMap<String, Object> branchRow : resultSet) {
                Branch branch = Branch.valueOf((String) branchRow.get("Branch"));
                branches.put(branch, (int) branchRow.get("barcode"));
            }
            for (Branch branch : Branch.values()) {
                if (!branches.containsKey(branch))
                    branches.put(branch, 0);
            }
            return branches;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<Integer, ArrayList<Item>> getDefectiveItems() {
        try {
            connectDB.createTables();
            String query = "SELECT * FROM Items WHERE is_defective = 1";
            ArrayList<HashMap<String, Object>> resultSet = connectDB.executeQuery(query);
            HashMap<Integer, ArrayList<Item>> items = new HashMap<>();
            for (HashMap<String, Object> itemRow : resultSet) {
                Item item;
                Item.Location location = Item.Location.valueOf((String) itemRow.get("location"));
                if (itemRow.get("size") != null) {
                    item = new Item((Integer) itemRow.get("producer"), (int) itemRow.get("barcode"), (String) itemRow.get("name"), location, (String) itemRow.get("expiration_date"), (double) itemRow.get("cost_price"), (int) itemRow.get("makat"), (String) itemRow.get("branch"));
                } else {
                    item = new Item((Integer) itemRow.get("producer"), (int) itemRow.get("barcode"), (String) itemRow.get("name"), location, (String) itemRow.get("expiration_date"), (double) itemRow.get("cost_price"), (int) itemRow.get("makat"), (String) itemRow.get("size"), (String) itemRow.get("branch"));
                }
                item.setDefective(itemRow.get("defective_description").toString());
                if ((int) itemRow.get("is_expired") == 1)
                    item.setExpired(true);
                if (itemRow.get("selling_price") != null)
                    item.setSellingPrice((double) itemRow.get("selling_price"));
                if (itemRow.get("the_price_been_sold_at") != null)
                    item.setThePriceBeenSoldAt((double) itemRow.get("the_price_been_sold_at"));
                if (items.containsKey((int) itemRow.get("makat"))) {
                    items.get((int) itemRow.get("makat")).add(item);
                } else {
                    ArrayList<Item> itemsList = new ArrayList<>();
                    itemsList.add(item);
                    items.put((int) itemRow.get("makat"), itemsList);
                }
            }
            return items;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeItem(int id) {
        try {
            connectDB.createTables();
            String query = "DELETE FROM Items WHERE barcode = " + id;
            connectDB.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            connectDB.close_connect();
        }
    }
}
