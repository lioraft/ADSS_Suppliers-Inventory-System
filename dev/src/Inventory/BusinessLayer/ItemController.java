package Inventory.BusinessLayer;

import Inventory.DataAccessLayer.Mapper.ItemDAO;
import Suppliers.BusinessLayer.Order;
import Suppliers.BusinessLayer.OrderController;
import Suppliers.BusinessLayer.OrderDetailsByProduct;
import Suppliers.BusinessLayer.SupplierController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//controller for items as singleton
public class ItemController {
    private static HashMap<Integer, ArrayList<Item>> soldItems = new HashMap<>(); //sold items by category ID

    private static HashMap<Integer, ArrayList<Item>> storageItems = new HashMap<>(); //storage items by category ID
    private static HashMap<Branch, HashMap<Integer, ArrayList<Item>>> inStoreItems = new HashMap<>(); //in store items by category ID
    //private static HashMap<String,HashMap<Integer, ArrayList<Item>>> inStoreItemsByBranch = new HashMap<>(); //in store items by category ID by branch.
    private static HashMap<Integer, ArrayList<Item>> defectiveItems = new HashMap<>(); //defective items by category ID
    private static final ArrayList<Item> items = new ArrayList<>(); //all items
    private static final HashMap<Integer, Item> expiredItems = new HashMap<>();
    private final ProductController productController;
    private final CategoryController categoryController;
    private static HashMap<Integer, Item> itemById = new HashMap<>();
    private static ItemController instance = null;
    private LocalDate lastReportDate;
    private static int DAYS_TO_Report = 7;
    private static final HashMap<Integer, ArrayList<Item>> storageItemsByProductID = new HashMap<>(); //storage items by Product ID
    private static final HashMap<Integer, ArrayList<Item>> inStoreItemsByProductId = new HashMap<>(); //in store items by Product ID
    private final ItemDAO itemDAO;
    private boolean connection_opened = false;
    private final SupplierController supplierController = SupplierController.getInstance();

    private ItemController() {
        productController = ProductController.getInstance();
        categoryController = CategoryController.getInstance();
        this.lastReportDate = LocalDate.now();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //check for expired items
        scheduler.scheduleAtFixedRate(this::checkForExpiredItems, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::checkForOrder, 0, 1, TimeUnit.DAYS);
        itemDAO = new ItemDAO();
    }

    public void checkForOrder() {
        HashMap<Integer, Order> orders = productController.checkForOrders();
        if (!orders.isEmpty()) {
            return;
        }
        for (Integer makat : orders.keySet()) {
            Order order = orders.get(makat);
            orders.get(makat).confirmDelivery();
            HashMap<Branch, Boolean> branchBooleanHashMap = new HashMap<>();
            Branch branch = Branch.valueOf(orders.get(makat).getBranch());
            branchBooleanHashMap.put(branch, false);
            productController.getOrdersStatus().computeIfAbsent(makat, k -> branchBooleanHashMap);
            OrderDetailsByProduct orderDetailsByProduct = order.getOrderDetailsOfProduct(makat);
            int amount = orderDetailsByProduct.getAmount();
            String branchName = order.getBranch();
            OrderController orderController = OrderController.getInstance();
            Integer supplierID = orderController.getSupplierIDByOrderNumber(order.getOrderNumber());
            int barcode = itemById.size() * 10;
            String name = productController.getProductNameByMakat(makat);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the expiration date of the product");
            String expirationDate = scanner.nextLine();
            double price = orderDetailsByProduct.getFinalPrice();
            System.out.println("Please enter the size of the product");
            String size = scanner.nextLine();
            Item.Location locate;
            Integer categoryId = productController.getProductCategoryByMakat(makat);
            for (int i = 0; i < amount; i++) {
                addItem(supplierID, barcode, name, expirationDate, price, categoryId, makat, size, branchName);

            }
        }
    }


    private void checkForExpiredItems() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (today == DayOfWeek.SUNDAY) { // or any other day of the week
            // Calculate the number of days between the last report date and today
            long daysSinceLastReport = ChronoUnit.DAYS.between(lastReportDate, LocalDate.now());
            if (daysSinceLastReport >= DAYS_TO_Report) {
                // Publish the report
                System.out.println("Publishing report...");
                publishExpiredReport();
                // Update the last report date to today
                lastReportDate = LocalDate.now();
            }
        }
    }

    private void publishExpiredReport() {
        for (HashMap<Integer, ArrayList<Item>> categoryItems : inStoreItems.values()) {
            removeExpired(categoryItems);
        }
        removeExpired(storageItems);
        if (expiredItems.isEmpty()) {
            System.out.println("No expired items");
            return;
        }
        ArrayList<Item> expiredItemsList = new ArrayList<>(expiredItems.values());
        for (Item item : expiredItemsList) {
            System.out.println(item.toString());
        }
    }

    private void removeExpired(HashMap<Integer, ArrayList<Item>> inStoreItems) {
        if (inStoreItems.isEmpty()) {
            return;
        }

        for (int i = 0; i < inStoreItems.size(); i++) {
            if (inStoreItems.get(i) == null) {
                continue;
            }
            for (int j = 0; j < inStoreItems.get(i).size(); j++) {
                if (inStoreItems.get(i).get(j).checkDate()) {
                    expiredItems.put(i, inStoreItems.get(i).get(j));
                    // update the item is expired in the DB
                    itemDAO.setExpired(inStoreItems.get(i).get(j).getBarcode());
                    inStoreItems.get(i).remove(inStoreItems.get(i).get(j));
                }
            }
        }
    }

    public void setDaysToReport(int days) {
        DAYS_TO_Report = days;
    }

    public static ItemController getInstance() {
        if (instance == null) {
            instance = new ItemController();
        }
        return instance;
    }

    public boolean addItem(Integer manufacturer, Integer barcode, String name, String expirationDate, double costPrice, int category, int productID, String size, String branch) {
        if (itemById.containsKey(barcode)) {
            throw new IllegalArgumentException("Item already exists");
        }
        if (!productController.getProductById().containsKey(productID)) {
            throw new IllegalArgumentException("Product does not exist");
        }
        if (!categoryController.getCategoryById().containsKey(category)) {
            throw new IllegalArgumentException("Category does not exist");
        }
        if (supplierController.getSupplierByID(manufacturer) == null) {
            throw new IllegalArgumentException("Supplier does not exist");
        }
        Branch branch1 = Branch.INVENTORY;
        Item.Location locate = null;
        Branch branchB = Branch.valueOf(branch);
        if (branchB == branch1) {
            locate = Item.Location.INVENTORY;
        } else {
            locate = Item.Location.STORE;
        }

        Item item = null;
        if (size == null || size.equals("") || size.equals("null")) {
            item = new Item(manufacturer, barcode, name, locate, expirationDate, costPrice, productID, branch);
        } else {
            item = new Item(manufacturer, barcode, name, locate, expirationDate, costPrice, productID, size, branch);
        }
        if (locate == Item.Location.INVENTORY) {
            storageItems.computeIfAbsent(category, k -> new ArrayList<>());
            storageItems.get(category).add(item);
        } else {
            HashMap<Integer, ArrayList<Item>> itemsByCategory = new HashMap<>();
            ArrayList<Item> items = new ArrayList<>();
            items.add(item);
            itemsByCategory.put(category, items);
            inStoreItems.computeIfAbsent(branchB, k -> new HashMap<>());
            inStoreItems.get(branchB).put(category, items);
        }
        items.add(item);
        //add to item by id dictionary
        itemById.put(barcode, item);
        //add the amount of the product
        productController.addItem(productID);
        //add the item to the database
        itemDAO.addItem(manufacturer, barcode, name, locate, expirationDate, costPrice, category, productID, size, item.getSellingPrice(), branch, item.getDateOfArrival().toString());
        return true;
    }

    //sold item
    public void itemSold(int CategoryID, int ItemID) {
        //check if there are sold item from the same category
        soldItems.computeIfAbsent(CategoryID, k -> new ArrayList<>());
        //get item from storage
        //add to sold items
        //remove from storage
        Item item = null;
        Branch branch = null;
        if (itemById.get(ItemID) == null) {
            throw new IllegalArgumentException("Item does not exist");
        } else {
            item = itemById.get(ItemID);
            branch = item.getBranch();
        }
        if (productController.getProductById().get(item.getMakat()).getCategoryID() != CategoryID) {
            throw new IllegalArgumentException("The category ID is not as the item real category ID");
        }
        if (inStoreItems.get(branch) == null) {
            throw new IllegalArgumentException("Item does not exist in store");
        } else {
            if (inStoreItems.get(branch).get(CategoryID) != null) {
                if (inStoreItems.get(branch).get(CategoryID).contains(item)) {
                    //add the item to sold items
                    soldItems.computeIfAbsent(CategoryID, k -> new ArrayList<>());
                    soldItems.get(CategoryID).add(item);
                    //remove from in store items
                    inStoreItems.get(branch).get(CategoryID).remove(item);
                    //remove from product amount
                    productController.reduceAmountOfProductByID(item.getMakat(), 1, Item.Location.STORE, item.getBranch().toString());
                    item.setCurrentLocation("SOLD");
                    // update the item is sold in the DB
                    itemDAO.setSold(item.getBarcode());
                }
            } else {
//                soldItems.get(CategoryID).add(item);
//                //remove from storage items
//                storageItems.get(CategoryID).remove(item);
//                //remove from product amount
//                productController.reduceAmountOfProductByID(item.getMakat(), 1, Item.Location.INVENTORY,null);
//                item.setCurrentLocation("SOLD");
//                // update the item is sold in the DB
//                itemDAO.setSold(item.getBarcode());
                throw new IllegalArgumentException("Item does not exist in store");
                //order item
            }
            double thePriceBeenSoldAt = getDiscount(ItemID);
            //update the price been sold
            item.setThePriceBeenSoldAt(thePriceBeenSoldAt);
            //update the price the item been sold at in the DB
            itemDAO.setThePriceBeenSoldAt(item.getBarcode(), thePriceBeenSoldAt);
        }
    }

    //get item
    public Item getItem(int CategoryID, int ItemID) {
        Item item = itemById.get(ItemID);
        Branch branch = item.getBranch();
        //get item from storage
        if (inStoreItems.get(branch).get(CategoryID).contains(item)) {
            return inStoreItems.get(branch).get(CategoryID).get(ItemID);
        }
        throw new IllegalArgumentException("Item not found");
    }

    //+ItemsInStock()
    public ArrayList<Item> itemsInStock(int CategoryID) {
        //get all items from storage
        ArrayList<Item> items = new ArrayList<>();
        if (storageItems.get(CategoryID) != null) {
            items.addAll(storageItems.get(CategoryID));
        }
        for (Branch branch : inStoreItems.keySet()) {
            if (inStoreItems.get(branch).get(CategoryID) != null) {
                items.addAll(inStoreItems.get(branch).get(CategoryID));
            }
        }
        return items;
    }


    public void moveItemToStore(int categoryID, int itemID, String branch) {
        Item item = null;
        Branch branch1 = Branch.valueOf(branch);
        if (itemById.get(itemID) == null) {
            throw new IllegalArgumentException("No such item ");
        } else {
            item = itemById.get(itemID);
            item.setBranch(branch1);
        }
        //check if there are items in this category in store
        if (inStoreItems.get(branch1) == null) {
            throw new IllegalArgumentException("No such branch ");
        } else {
            if (inStoreItems.get(branch1).get(categoryID) == null) {
                throw new IllegalArgumentException("No such category ");
            } else {
                inStoreItems.computeIfAbsent(branch1, k -> new HashMap<>());
                inStoreItems.get(branch1).computeIfAbsent(categoryID, k -> new ArrayList<>());
                if (inStoreItems.get(branch1).get(categoryID).contains(item)) {
                    throw new IllegalArgumentException("Item already in store");
                } else if (storageItems.get(categoryID).contains(item)) {
                    //add the item to in store items
                    inStoreItems.get(item.getBranch()).get(categoryID).add(item);
                    //remove from storage items
                    storageItems.get(categoryID).remove(item);
                    //update the location in the DB
                    itemDAO.moveItemToStore(itemID);
                } else {
                    throw new IllegalArgumentException("Item not found");
                }
            }
        }
    }

    //getDiscount
    public double getDiscount(int ItemID) {
        Item item = itemById.get(ItemID);
        double discountProduct = productController.getProductDiscount(item.getMakat());
        double pricePerDiscountProduct = item.getSellingPrice() * (100 - discountProduct) * (0.01);
        int productID = itemById.get(ItemID).getMakat();
        int categoryID = productController.getProductById(productID).getCategoryID();
        double categoryDiscount = categoryController.getCategoryDiscount(categoryID);
        double pricePerDiscountCategory = item.getSellingPrice() * (100 - categoryDiscount) * (0.01);
        return Math.min(pricePerDiscountCategory, pricePerDiscountProduct);
    }

    //change to one item at a time
    public boolean defective(Integer DefItem, int CategoryId, String reason) {
        if (itemById.get(DefItem) == null) {
            return false;
        } else {
            Item item = itemById.get(DefItem);
            if (item.getLocation() == Item.Location.SOLD) {
                System.out.println("Item is sold");
                return false;
            }
            Branch branch = item.getBranch();
            if (inStoreItems.get(branch) == null) {
                return false;
            } else {
                if (inStoreItems.get(branch).get(CategoryId).contains(item)) {
                    //compute if absent
                    defectiveItems.computeIfAbsent(CategoryId, k -> new ArrayList<>());
                    //add the item to sold items
                    defectiveItems.get(CategoryId).add(item);
                    //remove from in store items
                    inStoreItems.get(branch).get(CategoryId).remove(item);
                    //remove from product amount
                    productController.reduceAmountOfProductByID(item.getMakat(), 1, Item.Location.STORE, item.getBranch().toString());
                    //update the defctive description
                    item.setDefectiveDescription(reason);
                    //update is_defective in the DB
                    itemDAO.defective(item.getBarcode(), reason);
                } else {
                    if (storageItems.get(CategoryId) == null) {
                        return false;
                    }
                    if (storageItems.get(CategoryId).contains(item)) {
                        //add the item to sold items
                        defectiveItems.computeIfAbsent(CategoryId, k -> new ArrayList<>());
                        defectiveItems.get(CategoryId).add(item);
                        //remove from in store items
                        storageItems.computeIfAbsent(CategoryId, k -> new ArrayList<>());
                        storageItems.get(CategoryId).remove(item);
                        //remove from product amount
                        productController.reduceAmountOfProductByID(item.getMakat(), 1, Item.Location.INVENTORY, null);
                        //update is_defective in the DB
                        itemDAO.defective(item.getBarcode(), reason);

                    }
                }
                return true;
            }
        }
    }

    //get all defective items
    public ArrayList<Item> getDefectiveItems(int CategoryID) {
        return defectiveItems.get(CategoryID);
    }

    public void getToBeExpiredReport(int days) {
        ArrayList<Item> allItems = new ArrayList<>();
        if (inStoreItems.isEmpty() && storageItems.isEmpty()) {
            System.out.println("No items in Inventory");
            return;
        } else if (inStoreItems.isEmpty()) {
            for (int i = 0; i < storageItems.size(); i++) {
                allItems.addAll(storageItems.get(i));
            }
        } else if (storageItems.isEmpty()) {
            for (int i = 0; i < inStoreItems.size(); i++) {
                for (Branch branch : inStoreItems.keySet()) {
                    if (inStoreItems.get(branch).get(i) != null) {
                        allItems.addAll(inStoreItems.get(branch).get(i));
                    }
                }
            }
        }
        for (Item allItem : allItems) {
            if (allItem.getExpirationDate() == null) {
                continue;
            }
            if (allItem.getExpirationDate().isBefore(LocalDate.now().plusDays(days))) {
                System.out.println(allItem);
            }
        }

    }

    public void getExpiredReport() {
        // Publish the report
        System.out.println("Publishing report...");
        publishExpiredReport();
    }

    public void getDefectiveReport() {
        // Publish the report
        System.out.println("Publishing report...");
        publishDefectiveReport();
    }

    private void publishDefectiveReport() {
        if (defectiveItems.isEmpty()) {
            System.out.println("No defective items");
        } else {
            Collection<ArrayList<Item>> defectiveItemsList = defectiveItems.values();
            for (ArrayList<Item> item : defectiveItemsList) {
                for (Item value : item) {
                    System.out.println(value.toString());
                }
            }
        }
    }

    public void getInventoryReport() {
        // Publish the report
        System.out.println("Publishing report...");
        publishInventoryReport();
    }

    private void publishInventoryReport() {
        for (Item item : itemById.values()) {
            System.out.println(item.toString());
        }
    }

    public String publishInventoryReportS() {
        StringBuilder reportBuilder = new StringBuilder();

        for (Item item : itemById.values()) {
            reportBuilder.append(item.toString()).append("\n");
        }

        return reportBuilder.toString();
    }

    public ArrayList<Item> getItemsInStore() {
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < inStoreItems.size(); i++) {
            for (Branch branch : inStoreItems.keySet()) {
                if (inStoreItems.get(branch).get(i) != null) {
                    items.addAll(inStoreItems.get(branch).get(i));
                }
            }
        }
        return items;
    }

    public double getPrice(int ItemID) {
        Item item = itemById.get(ItemID);
        //check if product has discount
        double discountProduct = productController.getProductDiscount(item.getMakat());
        double pricePerDiscountProduct = 0;
        if (discountProduct != 0) {
            pricePerDiscountProduct = item.getSellingPrice() * ((100 - discountProduct) / 100);
        }

        //check if category has discount
        int productID = item.getMakat();
        int categoryID = productController.getProductById(productID).getCategoryID();
        double categoryDiscount = categoryController.getCategoryDiscount(categoryID);
        double pricePerDiscountCategory = 0;
        if (categoryDiscount != 0) {
            pricePerDiscountCategory = item.getSellingPrice() * ((100 - categoryDiscount) / 100);
        }
        if (pricePerDiscountCategory == 0 && pricePerDiscountProduct == 0) {
            if (item.isDefective())
                return item.getSellingPrice() * 0.5;
            return item.getSellingPrice();
        } else {
            if (pricePerDiscountCategory == 0) {
                if (item.isDefective())
                    return Math.min(pricePerDiscountProduct, item.getSellingPrice() * 0.5);
                return pricePerDiscountProduct;
            } else if (pricePerDiscountProduct == 0) {
                if (item.isDefective())
                    return Math.min(pricePerDiscountCategory, item.getSellingPrice() * 0.5);
                return pricePerDiscountCategory;
            } else {
                if (item.isDefective())
                    return Math.min(Math.min(pricePerDiscountCategory, pricePerDiscountProduct), item.getSellingPrice() * 0.5);
                return Math.min(pricePerDiscountCategory, pricePerDiscountProduct);
            }
        }
    }

    public void setLocation(int barcode, String currentLocation) {
        if (itemById.containsKey(barcode)) {
            itemById.get(barcode).setCurrentLocation(currentLocation);
        } else {
            throw new IllegalArgumentException("Item does not exist");
        }
    }

    public void startConnection() {
        try {
            if (!connection_opened) {
                itemDAO.startConnection();
                connection_opened = true;
                soldItems = itemDAO.getSoldItems();
                itemById = itemDAO.getItemById();
                defectiveItems = itemDAO.getDefectiveItems();
                int categoryId;
                Branch branch;
                for (Item item : itemById.values()) {
                    if (item.getLocation() != Item.Location.SOLD) {
//                    productController.updateAmount(item.getMakat());
                        categoryId = productController.getProductById(item.getMakat()).getCategoryID();
                        branch = item.getBranch();
                        if (item.getLocation() == Item.Location.INVENTORY) {
                            if (!storageItems.containsKey(categoryId)) {
                                storageItems.put(categoryId, new ArrayList<>());
                            }
                            storageItems.get(categoryId).add(item);
                        } else {
                            if (!inStoreItems.containsKey(branch)) {
                                inStoreItems.put(branch, new HashMap<>());
                                if (!inStoreItems.get(branch).containsKey(categoryId)) {
                                    inStoreItems.get(branch).put(categoryId, new ArrayList<>());
                                }
                            } else if (!inStoreItems.get(branch).containsKey(categoryId)) {
                                inStoreItems.get(branch).put(categoryId, new ArrayList<>());
                            }
                            inStoreItems.get(branch).get(categoryId).add(item);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error connecting to DB");
        }
    }

    public void removeItem(Integer manufacturer, Integer barcode, String name, String expirationDate, double costPrice, int category, int productID, String size, String branch) {
        Item item = itemById.get(barcode);
        if (item == null) {
            throw new IllegalArgumentException("Item does not exist");
        }
        if (item.getLocation() == Item.Location.SOLD) {
            throw new IllegalArgumentException("Item is sold");
        }
        if (item.getLocation() == Item.Location.INVENTORY) {
            storageItems.get(category).remove(item);
        } else {
            inStoreItems.get(Branch.valueOf(branch)).get(category).remove(item);
        }
        itemById.remove(barcode);
        itemDAO.removeItem(barcode);
    }

    public String publishDefectiveReportS() {
        StringBuilder reportBuilder = new StringBuilder();
        if (defectiveItems.isEmpty()) {
            reportBuilder.append("No defective items");
        } else {
            Collection<ArrayList<Item>> defectiveItemsList = defectiveItems.values();
            for (ArrayList<Item> item : defectiveItemsList) {
                for (Item value : item) {
                    reportBuilder.append(value.toString()).append("\n");
                }
            }
        }
        return reportBuilder.toString();
    }

    public String publishExpiredReportS() {
        StringBuilder reportBuilder = new StringBuilder();
        if (expiredItems.isEmpty()) {
            reportBuilder.append("No expired items");
        } else {
            ArrayList<Item> expiredItemsList = (ArrayList<Item>) expiredItems.values();
            for (Item item : expiredItemsList) {
                reportBuilder.append(item.toString()).append("\n");
            }
        }
        return reportBuilder.toString();
    }

    public String getToBeExpiredReportS(int days) {
        ArrayList<Item> allItems = new ArrayList<>();
        if (inStoreItems.isEmpty() && storageItems.isEmpty()) {
            return "No items in Inventory";
        } else if (inStoreItems.isEmpty()) {
            for (int i = 0; i < storageItems.size(); i++) {
                allItems.addAll(storageItems.get(i));
            }
        } else if (storageItems.isEmpty()) {
            for (int i = 0; i < inStoreItems.size(); i++) {
                for (Branch branch : inStoreItems.keySet()) {
                    if (inStoreItems.get(branch).get(i) != null) {
                        allItems.addAll(inStoreItems.get(branch).get(i));
                    }
                }
            }
        }
        for (Item allItem : allItems) {
            if (allItem.getExpirationDate() == null) {
                continue;
            }
            if (allItem.getExpirationDate().isBefore(LocalDate.now().plusDays(days))) {
                return allItem.toString();
            }
        }
        return "No items in Inventory";
    }

    public String ItemSoldP(int categoryID, int itemID) {
        String ret = "";
        //check if there are sold item from the same category
        soldItems.computeIfAbsent(categoryID, k -> new ArrayList<>());
        //get item from storage
        //add to sold items
        //remove from storage
        Item item = null;
        Branch branch = null;
        if (itemById.get(itemID) == null) {
            throw new IllegalArgumentException("Item does not exist");
        } else {
            item = itemById.get(itemID);
            branch = item.getBranch();
        }
        if (productController.getProductById().get(item.getMakat()).getCategoryID() != categoryID) {
            throw new IllegalArgumentException("The category ID is not as the item real category ID");
        }
        if (inStoreItems.get(branch) == null) {
            throw new IllegalArgumentException("Item does not exist in store");
        } else {
            if (inStoreItems.get(branch).get(categoryID) != null) {
                if (inStoreItems.get(branch).get(categoryID).contains(item)) {
                    //add the item to sold items
                    soldItems.computeIfAbsent(categoryID, k -> new ArrayList<>());
                    soldItems.get(categoryID).add(item);
                    //remove from in store items
                    inStoreItems.get(branch).get(categoryID).remove(item);
                    //remove from product amount
                    ret = productController.reduceAmountOfProductByIDP(item.getMakat(), 1, Item.Location.STORE, item.getBranch().toString());
                    item.setCurrentLocation("SOLD");
                    // update the item is sold in the DB
                    itemDAO.setSold(item.getBarcode());
                }else{
                    throw new IllegalArgumentException("Item does not exist in store");
                }
            } else {
//                soldItems.get(CategoryID).add(item);
//                //remove from storage items
//                storageItems.get(CategoryID).remove(item);
//                //remove from product amount
//                productController.reduceAmountOfProductByID(item.getMakat(), 1, Item.Location.INVENTORY,null);
//                item.setCurrentLocation("SOLD");
//                // update the item is sold in the DB
//                itemDAO.setSold(item.getBarcode());
                throw new IllegalArgumentException("Category does not exist in store");
                //order item
            }
            double thePriceBeenSoldAt = getDiscount(itemID);
            //update the price been sold
            item.setThePriceBeenSoldAt(thePriceBeenSoldAt);
            //update the price the item been sold at in the DB
            itemDAO.setThePriceBeenSoldAt(item.getBarcode(), thePriceBeenSoldAt);
        }
        return ret;
    }

    //like func Defective just return informative string
    public String setDefectiveItemP(int barcode, int categoryID, String reason) {
        String ret = "";
        Item item = null;
        Branch branch = null;
        if (itemById.get(barcode) == null) {
            throw new IllegalArgumentException("Item does not exist");
        } else {
            item = itemById.get(barcode);
            branch = item.getBranch();
        }
        if (productController.getProductById().get(item.getMakat()).getCategoryID() != categoryID) {
            throw new IllegalArgumentException("The category ID is not as the item real category ID");
        }
        if (inStoreItems.get(branch) == null) {
            throw new IllegalArgumentException("Item does not exist in store");
        } else {
            if (inStoreItems.get(branch).get(categoryID) != null) {
                if (inStoreItems.get(branch).get(categoryID).contains(item)) {
                    //add the item to defective items
                    defectiveItems.computeIfAbsent(categoryID, k -> new ArrayList<>());
                    defectiveItems.get(categoryID).add(item);
                    //remove from in store items
                    inStoreItems.get(branch).get(categoryID).remove(item);
                    //remove from product amount
                    ret = productController.reduceAmountOfProductByIDP(item.getMakat(), 1, Item.Location.STORE, item.getBranch().toString());
                    //update the defctive description
                    item.setDefectiveDescription(reason);
                    //update is_defective in the DB
                    itemDAO.defective(item.getBarcode(), reason);
                } else {
                    throw new IllegalArgumentException("Item does not exist in store");
                }
            } else {
                if (storageItems.get(categoryID).contains(item)) {
                    //add the item to sold items
                    defectiveItems.computeIfAbsent(categoryID, k -> new ArrayList<>());
                    defectiveItems.get(categoryID).add(item);
                    //remove from in store items
                    storageItems.computeIfAbsent(categoryID, k -> new ArrayList<>());
                    storageItems.get(categoryID).remove(item);
                    //remove from product amount
                    ret = productController.reduceAmountOfProductByIDP(item.getMakat(), 1, Item.Location.INVENTORY, null);
                    //update the defctive description
                    item.setDefectiveDescription(reason);
                    //update is_defective in the DB
                    itemDAO.defective(item.getBarcode(), reason);

                }
            }
        }
        return ret;

    }

}
