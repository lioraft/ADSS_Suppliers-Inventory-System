package Inventory.BusinessLayer;

import Inventory.DataAccessLayer.Mapper.ProductDAO;
import Inventory_Suppliers.InventoryIntegrator;
import Suppliers.BusinessLayer.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//product controller as a singleton
public class ProductController {

    private final CategoryController categoryController;
    //    private static HashMap<Integer,List<Category>> categoryByProduct = new HashMap<Integer,List<Category>>();
//  private Dictionary<Integer, List<Item>> itemByProduct;
//    private static ArrayList<Category> categories = new ArrayList<Category>();
// private static ArrayList<Product> products = new ArrayList<Product>();
    private static HashMap<Integer, Product> ProductById = new HashMap<Integer, Product>();
    private static ProductController instance = null;
    private final ProductDAO productDAO;
    private boolean opened_connection = false;
    private InventoryIntegrator inventoryIntegrator;
    private HashMap<Integer, Order> orders = new HashMap<>();
    private HashMap <Integer, HashMap<Branch, Boolean>> ordersStatus = new HashMap<>(); //does this product in this branch are ordered?

    private ProductController() {
        categoryController = CategoryController.getInstance();
        productDAO = new ProductDAO();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //check for expired items
        //scheduler.scheduleAtFixedRate(this::checkForOrders, 0, 1, TimeUnit.DAYS);
        this.inventoryIntegrator = OrderController.getInstance();
    }

    public HashMap<Integer,Order> checkForOrders() {
        HashMap<Integer,Order> ordersNow = new HashMap<>();
        for (Integer makat : orders.keySet()) {
            if (orders.get(makat).getOrderStatus()!= Status.Completed) {
                Order order = orders.get(makat);
                if(Objects.equals(order.getDateOfOrder(), LocalDate.now())){
                    ordersNow.put(makat,order);
                }
            }else{
                orders.remove(makat);
            }
        }
        return ordersNow;
    }

    public  HashMap <Integer, HashMap<Branch, Boolean>> getOrdersStatus() {
        return ordersStatus;
    }
    public boolean isProductOrdered(int makat, String branch){
        if(ordersStatus.containsKey(makat)){
            if(ordersStatus.get(makat).containsKey(Branch.valueOf(branch))){
                return ordersStatus.get(makat).get(Branch.valueOf(branch));
            }
        }
        return false;
    }
    public String getProductNameByMakat(int makat){
        return ProductById.get(makat).getName();
    }
    public Integer getProductCategoryByMakat(int makat){
        return ProductById.get(makat).getCategory();
    }
    public static ProductController getInstance() {
        if (instance == null) {
            instance = new ProductController();
        }
        return instance;
    }

    public Product getProductById(Integer productId) {
        return ProductById.getOrDefault(productId, null);
    }

    public void reduceAmountOfProductByID(int productID, int amount , Item.Location locale,String branch) {
        //ProductById.get(productID).setCurrentAmount( ProductById.get(productID).getCurrentAmount()-amount);
        Product product =ProductById.get(productID);

        switch (locale){
            case STORE : {
                if(branch!=null){
                    if(product.isAmountBiggerThenMinAmount(branch,amount)) {
                        //in the future we will send a request to deliveries to get from the inventory
                        orderTheProductBelowTheMinAmount(productID, amount, branch, product);
                    }
                    else{
                        product.reduceItemsFromStore(1, Branch.valueOf(branch));
                    }
                }
                break;
            }
            case INVENTORY : {
                product.reduceItemsFromInventory(1);
                break;
            }
        }
        // reduce the amount of the product in the productDAO
        productDAO.reduceAmountOfProductByID(productID,amount);
    }
    public String reduceAmountOfProductByIDP(int productID, int amount , Item.Location locale,String branch) {
        //ProductById.get(productID).setCurrentAmount( ProductById.get(productID).getCurrentAmount()-amount);
        String ret = "";
        Product product =ProductById.get(productID);
        switch (locale){
            case STORE : {
                if(branch!=null){
                    if(product.isAmountBiggerThenMinAmount(branch,amount)) {
                        //in the future we will send a request to deliveries to get from the inventory
                        ret = orderTheProductBelowTheMinAmountP(productID, amount, branch, product);
                    }
                    else{
                        product.reduceItemsFromStore(1, Branch.valueOf(branch));
                    }
                }
                break;
            }
            case INVENTORY : {
                product.reduceItemsFromInventory(1);
                break;
            }
        }
        // reduce the amount of the product in the productDAO
        productDAO.reduceAmountOfProductByID(productID,amount);
        return ret;
    }

    public boolean checkIfAmountInInventoryByMakat(int makat, int amount){
        return productDAO.getAmountInInventoryByMakat(makat).size() == amount;
    }
    private void orderTheProductBelowTheMinAmount(int productID, int amount, String branch, Product product) {
        if (checkIfAmountInInventoryByMakat(productID, product.getMinAmount() * 3)) {
            System.out.println("Warning: NEED TO GET FROM INVENTORY");
            //product.reduceItemsFromInventory(amount);
        }
        product.reduceItemsFromInventory(amount);
        System.out.println("Warning: The amount of " + product.getName() + " in Site " + branch + " is below the minimum amount.");
        //TODO: send request to urgent supply for this product at this branch
        ArrayList<Integer> products = new ArrayList<>();
        products.add(product.getMakat());
        HashMap<Integer, Integer> Amount = new HashMap<>();
        Amount.put(product.getMakat(), product.getMinAmount() * 3);
        try{
            ordersStatus.computeIfAbsent(productID, k -> new HashMap<>());
            ordersStatus.get(productID).computeIfAbsent(Branch.valueOf(branch), k -> false);
            if(ordersStatus.get(productID).get(Branch.valueOf(branch))){
                return;
            }
            ArrayList<Order> ordersB = inventoryIntegrator.makeOrder(branch, products, Amount);
            for(Order order : ordersB){
                orders.put(productID, order);
            }
            HashMap<Branch, Boolean> branchBooleanHashMap = new HashMap<>();
            branchBooleanHashMap.put(Branch.valueOf(branch), true);
            ordersStatus.put(productID, branchBooleanHashMap);
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    private String orderTheProductBelowTheMinAmountP(int productID, int amount, String branch, Product product) {
        if (checkIfAmountInInventoryByMakat(productID, product.getMinAmount() * 3)) {
            System.out.println("Warning: NEED TO GET FROM INVENTORY");
            //product.reduceItemsFromInventory(amount);
        }
        product.reduceItemsFromInventory(amount);
        System.out.println("Warning: The amount of " + product.getName() + " in Site " + branch + " is below the minimum amount.");
        //TODO: send request to urgent supply for this product at this branch
        ArrayList<Integer> products = new ArrayList<>();
        products.add(product.getMakat());
        HashMap<Integer, Integer> Amount = new HashMap<>();
        Amount.put(product.getMakat(), product.getMinAmount() * 3);
        try{
            ordersStatus.computeIfAbsent(productID, k -> new HashMap<>());
            ordersStatus.get(productID).computeIfAbsent(Branch.valueOf(branch), k -> false);
            if(ordersStatus.get(productID).get(Branch.valueOf(branch))){
                return "The amount of " + product.getName() + " in Site " + branch + " is below the minimum amount.\nThe order is already in process";
            }
            ArrayList<Order> ordersB = inventoryIntegrator.makeOrder(branch, products, Amount);
            for(Order order : ordersB){
                orders.put(productID, order);
            }
            HashMap<Branch, Boolean> branchBooleanHashMap = new HashMap<>();
            branchBooleanHashMap.put(Branch.valueOf(branch), true);
            ordersStatus.put(productID, branchBooleanHashMap);
        }
        catch (Exception e){
            return "Error: " + e.getMessage() +"\nplease try manual order";
        }
        return "The amount of " + product.getName() + " in Site " + branch + " is below the minimum amount.\nThe order is in process";
    }


    //add product with category
    public void addProduct(String name, int minAmount, int categoryID,String subCategory, int makat , int supplierID) {
        if(ProductById.containsKey(makat)){
            System.out.println("product ID already exist");
            throw new IllegalArgumentException("product ID already exist");
        }
        if(categoryController.getCategoryById(categoryID)==null){
            System.out.println("category ID not exist");
            throw new IllegalArgumentException("category ID not exist");
        }
        if(SupplierController.getInstance().getSupplierByID(supplierID)==null){
            throw new IllegalArgumentException("Supplier does not exist");
        }
        Product product = new Product(name, minAmount, categoryID,subCategory, makat , supplierID);
        ArrayList <Product> products = new ArrayList<Product>();
        products.add(product);
        //products.add(product);
        //Category category = new Category(name,categoryID);
        //add to product by id dictionary
        ProductById.put(makat,product);
//        //add to category by product dictionary
//        categoryByProduct.put(makat,categories);
        //add to productDAO
        productDAO.addProduct(name,minAmount,categoryID,subCategory,makat,supplierID);
        //add to product by category dictionary
        categoryController.addProductByCategory(products,categoryID);

    }
    //get product
    public Product getProduct(int productID){
        return ProductById.get(productID);
    }
    //get product by category
    public ArrayList<Product> getProductsByCategory(int categoryID){
        return categoryController.getProductsByCategory(categoryID);
    }

    //set minimum amount
    public void setMinimum(int deliveryTime, int demand ,int productID){
        //set minimum amount
        if(ProductById.containsKey(productID)){
            ProductById.get(productID).setMinAmount(deliveryTime,demand);
            int minAmount = ProductById.get(productID).getMinAmount();
            //update the product in the productDAO
            productDAO.setMinimum(productID,minAmount);
        }
        else{
            throw new IllegalArgumentException("product not exist");
        }
    }



    public void setDiscountByProduct(int productID, float discount , String start, String end){
        if(getProductById(productID)!=null){
            ProductById.get(productID).setDiscount(start,end,discount);
            ProductById.get(productID).setDiscounts(discount,start,end);
            //update the product in the productDAO
            productDAO.setDiscountByProduct(productID,discount,start,end);
        }
        else{
            throw new IllegalArgumentException("product not exist");
        }
    }
    //get product discount
    public double getProductDiscount(int productID){
        return ProductById.get(productID).getDiscount();
    }
    //get the hash map of product by ID
    public HashMap<Integer, Product> getProductById() {
        return ProductById;
    }

    public void addItem(int productID){
        ProductById.get(productID).addItems(1);
        //update the product in the productDAO
        productDAO.addItem(productID);
    }
    public void updateAmount(int makat){
        getProductById(makat).addItems(1);
    }

    public int getAmountOfProduct(int productID,String branch) {

        if(ProductById.containsKey(productID)){
            return ProductById.get(productID).getCurrentAmount(branch);
        }
        else{
            throw new IllegalArgumentException("product not exist");
        }
    }


    public ArrayList<String> getDiscountsByProductId(int productID) {
        if(ProductById.get(productID)==null){
            throw new IllegalArgumentException("product not exist");
        }
        return ProductById.get(productID).getDiscounts();
    }

    public void startConnection() {
        try {
            if(!opened_connection){
                productDAO.startConnection();
                opened_connection = true;
                ProductById= productDAO.getProducts();
                HashMap<Integer,HashMap<Branch,Integer>> amountInBranch = productDAO.getAmountInBranch();
                for (Integer makat : amountInBranch.keySet()) {
                    ProductById.get(makat).setCurrentAmount(amountInBranch.get(makat));
                }
                HashMap<Integer,ArrayList<String>> Discounts = productDAO.getProductDiscoubt();
                for (Integer makat : Discounts.keySet()) {
                    ProductById.get(makat).setDiscountsList(Discounts.get(makat));
                }
                orders = productDAO.getOrders();
                for(Order order : orders.values()){
                    for(int makat : ProductById.keySet()){
                        OrderDetailsByProduct orderDetailsByProduct = order.getOrderDetailsOfProduct(makat);
                        if(orderDetailsByProduct!=null){
                            Branch branch = Branch.valueOf(order.getBranch());
                            HashMap<Branch, Boolean> orderStatus = new HashMap<>();
                            orderStatus.put(branch, true);
                            ordersStatus.put(makat,orderStatus);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void removeSampleData() {
        try {
            productDAO.removeSampleData();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isProductExist(int i) {
        //return ProductById.containsKey(i);
        return getProductById(i)!=null;
    }
}