package Suppliers.BusinessLayer;
import Inventory.BusinessLayer.ProductController;
import Suppliers.DataAccessLayer.OrderDAO;
import Inventory_Suppliers.InventoryIntegrator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Timer;
import java.util.TimerTask;


public class OrderController implements InventoryIntegrator { //Controller for order as singleton
    HashMap<Integer, Order> orders; // <id, Order>: map that matches id to order
    ArrayList<ArrayList<FixedPeriodOrder>> fixed_orders; // List<list<FixedPeriodOrder>>: array list of lists of period order, each list is the list of orders of that day (from 0 as
    // sunday to 6 as saturday)
    // that should be executed on those days
    private static OrderController instance = null; // the instance of the Order Controller
    private boolean opened_connection = false; // boolean that indicates if connection to DB is open
    OrderDAO orderDAO; // the order DAO

    // constructor
    private OrderController() {
        // initialize data structures
        orderDAO = new OrderDAO();
        orders = new HashMap<Integer, Order>();
        fixed_orders = new ArrayList<ArrayList<FixedPeriodOrder>>();
    }

    // get instance of order controller. if it doesn't exist, create it
    public static OrderController getInstance() {
        if (instance == null)
            instance = new OrderController();
        return instance;
    }

    // print all orders
    public void printAllOrders() {
        for (Order order : orders.values())
            System.out.println(order);
    }

    public String getAllOrders() {
        String allOrders = "";
        for (Order order : orders.values())
            allOrders += order.toString() + "\n";
        return allOrders;
    }

    // this function takes in a list of optional supppliers who deliver all the products in the productsToOrder list, and a map with product code as key and
    // the amount to order as value. it finds the cheapest supplier, makes an order and returns it
    private Order cheapestSupplier(String branch, ArrayList<Supplier> optionalSuppliers, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        Supplier minSupplier = null;
        double minTotalPrice = -1;
        // iterating all suppliers and finding cheapest supplier for product
        for (int i = 0; i < optionalSuppliers.size(); i++) {
            // variable for total price if order from current supplier
            double current_total_price = 0;
            // iterating all products to order
            for (int j = 0; j < productsToOrder.size(); j++){
                // get best possible price for supplier
                double current_price = optionalSuppliers.get(i).getBestPossiblePrice(productsToOrder.get(j), productsAndAmounts.get(productsToOrder.get(j)));
                // add price of current product to total price
                current_total_price = current_total_price + current_price;
            }
            // if first supplier to check, or better option, set as minimal supplier
            if (current_total_price < minTotalPrice || minSupplier == null) {
                minTotalPrice = current_total_price;
                minSupplier = optionalSuppliers.get(i);
            }
        }
        // make order from chosen supplier
        return makeOrderFromSupplier(branch, minSupplier, productsToOrder, productsAndAmounts);
    }

    // function that finds the fastest suppliers of a product
    private ArrayList<Supplier> fastestSuppliers(int product_code) {
        // get list of suppliers of product
        ArrayList<Supplier> suppliers = SupplyAgreementController.getInstance().getSuppliersByProduct(product_code);
        // if no suppliers, return null
        if ( suppliers == null ||suppliers.size() == 0 )
            return null;
        // if only one supplier, return it
        if (suppliers.size() == 1)
            return suppliers;
        else {
            // create today's date
            LocalDate today = LocalDate.now();
            // variable for fastest supplier
            Supplier fastestSupplier = null;
            // variable for time difference
            long closestDiff = -1;
            // iterating all suppliers
            for (int i = 0; i < suppliers.size(); i++) {
                // get delivery time of current supplier
                LocalDate currentDeliveryTime = suppliers.get(i).getNextShippingDate();
                // get time difference
                long curDiff = ChronoUnit.DAYS.between(today, currentDeliveryTime);
                // if first supplier to check, or faster delivery time, set as fastest supplier
                if (Math.abs(curDiff) < Math.abs(closestDiff) || fastestSupplier == null) {
                    closestDiff = curDiff;
                    fastestSupplier = suppliers.get(i);
                }
            }
            // create list of fastest suppliers
            ArrayList<Supplier> fastest_suppliers = new ArrayList<>();
            // add first fastest supplier
            fastest_suppliers.add(fastestSupplier);
            // iterating all suppliers again to find all suppliers with same delivery time
            for (int i = 0; i < suppliers.size(); i++) {
                // get delivery time of current supplier
                LocalDate currentDeliveryTime = suppliers.get(i).getNextShippingDate();
                // get time difference
                long curDiff = ChronoUnit.DAYS.between(today, currentDeliveryTime);
                // if equals to fastest supplier, add to list
                if (Math.abs(curDiff) == Math.abs(closestDiff) && suppliers.get(i) != fastestSupplier) {
                    fastest_suppliers.add(suppliers.get(i));
                }
            }
            // return fastest suppliers
            return fastest_suppliers;
        }
    }

    // function that takes in supplier, hashmap of product codes and total price for their portion, and hashmap of product codes and amounts. it creates an order and
    // adds it to list of supplier's orders
    private Order makeOrderFromSupplier(String branch, Supplier supplier, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        // starting the order
        Order newOrder = supplier.addNewOrder(branch);
        System.out.println("---INITIALIZING ORDER---");
        // iterating all products and adding to order
        for (int i = 0; i < productsToOrder.size(); i++) {
            int units = productsAndAmounts.get(productsToOrder.get(i));
            String product_name = orderDAO.getProductName(productsToOrder.get(i));
            double list_price = supplier.getListPriceOfProducts(productsToOrder.get(i), 1);
            double price_before_discount = supplier.getListPriceOfProducts(productsToOrder.get(i), units);
            double final_price = supplier.getBestPossiblePrice(productsToOrder.get(i), units);
            double discount_given = price_before_discount - final_price;
            newOrder.addProducts(productsToOrder.get(i), product_name, units, list_price, discount_given, final_price);
        }
        // when finished, print and return order
        System.out.println("---ORDER WAS PLACED!---");
        // apply order discounts on order made
        supplier.applyOrderDiscountsOnOrder(newOrder);
        // add order and order details to database
        orderDAO.addOrder(supplier.getPrivateCompanyNumber(), newOrder.getOrderNumber(), newOrder.getBranch(), newOrder.getDateOfOrder(), newOrder.getOrderStatus().toString(), newOrder.getTotalPrice(), newOrder.getDiscount());
        ArrayList<OrderDetailsByProduct> orderedProducts = newOrder.getOrderedProducts();
        for (int j = 0; j < orderedProducts.size(); j++) {
            orderDAO.addOrderDetailsByProduct(newOrder.getOrderNumber(), orderedProducts.get(j).getProductCode(), orderedProducts.get(j).getProductName(), orderedProducts.get(j).getAmount(), orderedProducts.get(j).getListPrice(), orderedProducts.get(j).getDiscountGiven(), orderedProducts.get(j).getFinalPrice());
        }
        return newOrder;
    }


    // function that makes shortage order from supplier
    public ArrayList<Order> makeOrder(String branch, ArrayList<Integer> productsToOrder, HashMap<Integer, Integer> productsAndAmounts) {
        // creating a list of suppliers that are capable of delivering all products
        ArrayList<Supplier> relevantSuppliers = SupplierController.getInstance().getSuppliers();
       //create a list of orders
        ArrayList<Order> order_list = new ArrayList<>();
        // add suppliers who deliver this product to list of capable suppliers
        for (int i = 0; i < productsToOrder.size(); i++) {
            if(relevantSuppliers == null ||relevantSuppliers.isEmpty() || fastestSuppliers(productsToOrder.get(i)) == null || fastestSuppliers(productsToOrder.get(i)).isEmpty())
                break;
            else
                relevantSuppliers.retainAll(fastestSuppliers(productsToOrder.get(i)));
        }
        // if list of suppliers who can deliver all products is not empty, find cheapest supplier among them
        if (!relevantSuppliers.isEmpty()) {
            Order newOrder = cheapestSupplier(branch, relevantSuppliers, productsToOrder, productsAndAmounts);
            orders.put(newOrder.getOrderNumber(), newOrder);
            //add new order to list of orders
            order_list.add(newOrder);
        }
        // if list is empty, we need to divide the order and therefore to find fastest & cheapest supplier per product
        else {
            // map of suppliers and the products they deliver
            HashMap<Supplier, ArrayList<Integer>> suppliersAndProducts = new HashMap<>(); // <supplier, list<product_code>>
            // iterate all products to order
            for (int i = 0; i < productsToOrder.size(); i++) {
                // min price for current product
                double minPrice = -1;
                Supplier minSupplier = null;
                // get list of fastest suppliers who ship products fastest
                ArrayList<Supplier> supplierForProduct = fastestSuppliers(productsToOrder.get(i));
                if(supplierForProduct == null || supplierForProduct.isEmpty())
                    throw new IllegalArgumentException("No supplier was found. can't make order!");
                for (int j = 0; j < supplierForProduct.size(); j++) {
                    // finding best price for product for current supplier
                    double currentPrice = supplierForProduct.get(j).getBestPossiblePrice(productsToOrder.get(i), productsAndAmounts.get(productsToOrder.get(i)));
                    // if can make order
                    if (productsAndAmounts.get(productsToOrder.get(i)) <= supplierForProduct.get(j).getMaxAmountOfUnits(productsToOrder.get(i))) {
                        if (currentPrice < minPrice || minSupplier == null) {
                            minSupplier = supplierForProduct.get(j);
                            minPrice = currentPrice;
                        }
                    }
                }
                // if no supplier found, meaning can't make order
                if (minSupplier == null || minPrice == -1) {
                    throw new IllegalArgumentException("No supplier was found. can't make order!");
                }
                // getting current list of products to order from supplier
                ArrayList<Integer> minSupplierCurrentProducts = suppliersAndProducts.get(minSupplier);
                if (minSupplierCurrentProducts == null) {
                    minSupplierCurrentProducts = new ArrayList<>();
                }
                // add product to list
                minSupplierCurrentProducts.add(productsToOrder.get(i));
                // add to map
                suppliersAndProducts.put(minSupplier, minSupplierCurrentProducts);
            }
            // iterate the dictionary and make orders for matching suppliers
            for (Supplier currentSup : suppliersAndProducts.keySet()) {
                Order newOrder = makeOrderFromSupplier(branch, currentSup, suppliersAndProducts.get(currentSup), productsAndAmounts);
                orders.put(newOrder.getOrderNumber(), newOrder);
                //add new order to list of orders
                order_list.add(newOrder);
            }

        }
        return order_list;
    }

    // function that cancels an order
    public void cancelOrder(int numOfOrder) {
        Order order = orders.get(numOfOrder);
        if (order == null)
            throw new IllegalArgumentException("Order number does not exist");
        else {
            // update in database
            orderDAO.cancelOrder(order.getOrderNumber());
            // cancel order
            order.cancelOrder();
        }
    }

    // function that confirms an order
    public void confirmOrder(int numOfOrder) {
        Order order = orders.get(numOfOrder);
        if (order == null)
            throw new IllegalArgumentException("Order number does not exist");
        // confirm order
        order.confirmDelivery();
        // update in database
        orderDAO.confirmOrder(order.getOrderNumber());
    }

    // function that takes in order number and returns the supplier id
    public int getSupplierIDByOrderNumber(int orderNumber){
        ArrayList<Supplier> suppliers = SupplierController.getInstance().getSuppliers();
        for (int i = 0; i < suppliers.size(); i++) {
            ArrayList<Order> orders = suppliers.get(i).getOrdersHistory();
            for (int j = 0; j < orders.size(); j++) {
                if(orders.get(j).getOrderNumber() == orderNumber)
                    return suppliers.get(i).getPrivateCompanyNumber();
            }
        }
        return -1;
    }

    // function that initializes timer, schedule a task of fixed orders, which the system will run daily at 8 am
    public void scheduleFixedOrder() {
        // create new timer
            Timer timer = new Timer();

            // set the time in which the system will run the task - we chose 8 am
            int hour = 8;
            int minute = 0;
            int second = 0;

        // create a new timer task
        TimerTask makeFixedOrdersTask = new TimerTask() {
            // override the run method to make fixed orders
            @Override
            public void run() {
                // make fixed orders of today
                makeTodaysFixedOrder();
            }
        };
        // Schedule the task to run every day at the specified time (8 am)
            timer.schedule(makeFixedOrdersTask, getTime(hour, minute, second), 24 * 60 * 60 * 1000L);
        }

        // function that checks tomorrow's day, and makes the fixed period order of the day that are in the map of fixed orders
    private void makeTodaysFixedOrder() {
        // create todays date
        LocalDate today = LocalDate.now();
        // get the day
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        // get list of fixed period orders of tomorrow
        ShipmentDays day = ShipmentDays.valueOf(dayOfWeek.toString());
        ArrayList<FixedPeriodOrder> ordersToMake = fixed_orders.get(day.ordinal() + 1);
        // iterate all orders in the list and make them
        for (FixedPeriodOrder order : ordersToMake) {
            // get supplier
            Supplier supplier = SupplierController.getInstance().getSupplierByID(order.getSupplierID());
            // get product to order
            ArrayList<Integer> productsToOrder = new ArrayList<>();
            productsToOrder.add(order.getProductCode());
            // get amount of product to order
            HashMap<Integer, Integer> productsAndAmounts = new HashMap<>();
            productsAndAmounts.put(order.getProductCode(), order.getAmount());
            // make order
            Order newOrder = makeOrderFromSupplier(order.getBranch(), supplier, productsToOrder, productsAndAmounts);
            // add order to list of orders
            orders.put(newOrder.getOrderNumber(), newOrder);
            getPeriodicOrder(newOrder.getOrderNumber());
        }
    }

    // function that returns the fixed order that was made. function sends the order to inventory module
    public Order getPeriodicOrder(int order_number) {
        return orders.get(order_number);
    }


    // function that takes in arguments of desired time, and returns it in long format
        public static long getTime(int hour, int minute, int second) {
            long currentTime = System.currentTimeMillis();
            long targetTime = currentTime - (currentTime % (24 * 60 * 60 * 1000L)) + (hour * 60 * 60 * 1000L) + (minute * 60 * 1000L) + (second * 1000L);
            if (targetTime < currentTime) {
                targetTime += 24 * 60 * 60 * 1000L;
            }
            return targetTime;
        }

        // function that takes in the information of a new fixed period order and adds it to list of orders by day
        public void addPeriodicOrder(String branchID, int supplierID, int productCode, int amount, int day) {
        //check if the day is valid
            if(day < 0 || day > 6){
                throw new IllegalArgumentException("Day is not valid");
            }
            //check if the supplier exist
            Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
            if(supplier == null){
                throw new IllegalArgumentException("Supplier does not exist");
            }
            if (ProductController.getInstance().getProductById(productCode) == null)
                throw new IllegalArgumentException("Product does not exist");
        FixedPeriodOrder newFixedOrder = new FixedPeriodOrder(supplierID, branchID, productCode, amount);
        fixed_orders.get(day).add(newFixedOrder);
        // add to database
            orderDAO.addFixedPeriodOrder(supplierID, branchID, productCode, amount, day);
        }

    public void startConnection() {
        try {
            if (!opened_connection) {
                orders = this.orderDAO.startConnection();
                fixed_orders = this.orderDAO.getFixedPeriodOrders();
                opened_connection = true;
            }
        } catch (Exception e) {
            System.out.println("Error connecting to order database");
        }
    }

    // a function that delete periodic order
    public void deletePeriodicOrder(int day, int supplierID, int productCode, String branch){
        //check if the day is valid
        if(day < 0 || day > 6){
            throw new IllegalArgumentException("Day is not valid");
        }
        //check if the supplier exist
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        if(supplier == null){
            throw new IllegalArgumentException("Supplier does not exist");
        }
        if (ProductController.getInstance().getProductById(productCode) == null)
            throw new IllegalArgumentException("Product does not exist");
        //check if the order exist by checking if in the specific day there is an order with the same supplier and product code and branch
        FixedPeriodOrder order = null;
        for(FixedPeriodOrder fixedPeriodOrder : fixed_orders.get(day)){
            if(fixedPeriodOrder.getSupplierID() == supplierID && fixedPeriodOrder.getProductCode() == productCode && fixedPeriodOrder.getBranch().equals(branch)){
                order = fixedPeriodOrder;
                break;
            }
        }
        // update database
        orderDAO.removeFixedPeriodOrder(supplierID,productCode, order.getBranch());
        //delete the order
        fixed_orders.get(day).remove(order);
    }

    // function that takes in supplier's id and prints his orders history
    public void printOrdersOfSupplier(int supplierID) {
        // if supplier doesn't exist
        if (SupplierController.getInstance().getSupplierByID(supplierID) == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        // get supplier and print orders
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        supplier.printOrderHistory();
    }

    // function that takes in supplier's id and returns his orders history as a string
    public String getOrdersOfSupplier(int supplierID) {
        Supplier supplier = SupplierController.getInstance().getSupplierByID(supplierID);
        if (SupplierController.getInstance().getSupplierByID(supplierID) == null)
            throw new IllegalArgumentException("Supplier ID doesn't exist");
        ArrayList<Order> orders = supplier.getOrdersHistory();
        String ordersString = "";
        for (Order order : orders) {
            ordersString += order.toString() + "\n";
        }
        return ordersString;
    }
}
