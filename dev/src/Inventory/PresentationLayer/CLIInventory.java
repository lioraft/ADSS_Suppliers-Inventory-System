package Inventory.PresentationLayer;
import Inventory.ServiceLayer.ServiceController;

import java.util.ArrayList;
import java.util.Scanner;

public class CLIInventory {
    private Scanner scanner = new Scanner(System.in);
    private final ServiceController serviceController = ServiceController.getInstance();
    private final Suppliers.ServiceLayer.ServiceController supplierServiceController = Suppliers.ServiceLayer.ServiceController.getInstance();

    public void start() {
        int choice;
        do {
//            String localDir = System.getProperty("user.dir");
//            System.out.println(localDir);
            serviceController.starConnection();
            supplierServiceController.starConnection();
            try{
            printMenu();
            scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {
                    addStuff();
                    start();
                    break;
                }
                case 2: {
                    setStuff();
                    start();
                    break;
                }
                case 3: {
                    getStuff();
                    start();
                    break;
                }
                case 4: {
                    removeSampleData();
                    start();
                    break;
                }
                case 5: {
                    serviceController.addData();
                    start();
                    break;
                }
                case 6: {
                    System.out.println("Bye Bye");
                    System.exit(0);
                }
            }}catch (Exception e){
                System.out.println("Invalid input, please try again");
                start();
            }
        } while (true);
    }


    private void removeSampleData() {
        serviceController.removeSampleData();
        System.out.println("Sample data removed, please start the program again");
    }

    public void printMenu() {
        System.out.println("Hello, What would you like to do?");
        System.out.println("1. Add product, category or item");
        System.out.println("2. Set new configuration");
        System.out.println("3. Get reports and information about the inventory");
        System.out.println("4. Remove the sample data");
        System.out.println("5. Add sample data");
        System.out.println("6. Exit");
    }

    public void addStuff() {
        System.out.println("What would you like to add?");
        System.out.println("1. Add a product");
        System.out.println("2. Add a category");
        System.out.println("3. Add an item or items to a product");
        System.out.println("4. Go back to the main menu");
        scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1: {
                addProduct();
                System.out.println("Product added successfully");
                start();
            }
            case 2: {
                addCategory();
                System.out.println("Category added successfully");
                start();
            }
            case 3: {
                addItems();
                start();
            }
            case 4: {
                start();
            }
        }
    }

    private void setDefectiveItem() {
        System.out.println("Enter item barcode");
        int barcode = scanner.nextInt();
        System.out.println("Enter item category id");
        int categoryID = scanner.nextInt();
        System.out.println("Enter reason for defect");
        scanner.nextLine();
        String reason = scanner.nextLine();
        if(serviceController.setDefectiveItem(barcode, categoryID, reason)) {
            System.out.println("Item set as defective successfully");
        }
        else {
            System.out.println("Item not found");
        }
    }

    private void addProduct() {
        System.out.println("Enter product name");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.println("Enter product minimum amount");
        int minAmount = scanner.nextInt();
        System.out.println("Enter product makat");
        int makat = scanner.nextInt();
        System.out.println("Enter product category id");
        int categoryID = scanner.nextInt();
        System.out.println("Enter product supplier id");
        int supplierID = scanner.nextInt();
        System.out.println("enter product sub category name");
        scanner.nextLine();
        String subCategoryName = scanner.nextLine();
        if(serviceController.addProduct(name, minAmount, categoryID, subCategoryName, makat, supplierID)){
            System.out.println("Product added successfully");
        }else{
            System.out.println("Product already exist");
            addStuff();
        }
    }

    private void addCategory() {
        System.out.println("Enter category name");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.println("Enter category id");
        int id = scanner.nextInt();
        if(!serviceController.addCategory(name, id)){
            addStuff();
        }
    }

    private void addItems() {
        System.out.println("Enter how many items you would like to add");
        int amount = scanner.nextInt();
        System.out.println("Enter item manufacturer");
        Integer manufacturer = scanner.nextInt();
        System.out.println("Enter item barcode");
        int barcode = scanner.nextInt();
        System.out.println("Enter item name");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.println("Enter item expiration date in the form of yyyy-mm-dd");
        String expirationDate = scanner.next();
        System.out.println("Enter item cost price");
        float costPrice = scanner.nextFloat();
        System.out.println("Enter item category id");
        int categoryID2 = scanner.nextInt();
        System.out.println("Enter item product id");
        int productID = scanner.nextInt();
        System.out.println("Enter item size if exist otherwise press enter or write null");
        scanner.nextLine();
        String size = scanner.nextLine();
        System.out.println("Enter item branch");
        String branch = scanner.next();
        try {
            for(int i = 0; i < amount; i++){
              if(!serviceController.addItem(manufacturer, barcode++, name, expirationDate, costPrice, categoryID2, productID, size, branch.toUpperCase())){
                  throw new Exception();
              }
            }
        }catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            start();
        }
        System.out.println("Items added successfully");
    }

    private void setStuff() {
        System.out.println("What would you like to set?");
        System.out.println("1. Set minimum amount of a product");
        System.out.println("2. Set discount by product");
        System.out.println("3. Set discount by category");
        System.out.println("4. Set how often to get defective report");
        System.out.println("5. Sell item");
        System.out.println("6. set item as defective");
        System.out.println("7. Go back to the main menu");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1: {
                setMinimumAmount();
                break;
            }
            case 2: {
                setDiscountByProduct();
                break;
            }
            case 3: {
                setDiscountByCategory();
                break;
            }
            case 4: {
                setHowOftenToGetDefectiveReport();
                break;
            }
            case 5: {
                sellItem();
                break;
            }
            case 6: {
                setDefectiveItem();
                break;
            }
            case 7: {
                start();
            }
        }
    }

    private void sellItem() {
        System.out.println("Enter item barcode");
        int barcode = scanner.nextInt();
        System.out.println("Enter item category id");
        int categoryID = scanner.nextInt();
        try {
            serviceController.itemSold(categoryID, barcode);
        } catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            setStuff();
        }
    }

    private void moveItemToStore() {
        System.out.println("Enter item barcode");
        int barcode = scanner.nextInt();
        System.out.println("Enter item category id");
        int categoryID = scanner.nextInt();
        System.out.println("Enter item branch");
        String branch = scanner.next();
        try {
            serviceController.moveItemToStore(barcode, categoryID, branch);
        } catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            setStuff();
        }
    }

    private void setHowOftenToGetDefectiveReport() {
        System.out.println("Enter how often to get defective report");
        int howOften = scanner.nextInt();
        try {
            serviceController.setDefectiveReport(howOften);
        } catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            setStuff();
        }
    }

    private void setMinimumAmount() {
        System.out.println("Enter product id");
        int productID = scanner.nextInt();
        System.out.println("Enter the amount of days to the next delivery");
        int deliveryTime = scanner.nextInt();
        System.out.println("Enter demand");
        int demand = scanner.nextInt();
        try {
            serviceController.setMinimum(deliveryTime, demand, productID);
        } catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            setStuff();
        }
    }

    private void setDiscountByProduct() {
        System.out.println("Enter product id");
        int productID = scanner.nextInt();
        System.out.println("Enter discount");
        float discount = scanner.nextFloat();
        System.out.println("Enter start date in the form of yyyy-mm-dd");
        String startDate = scanner.next();
        System.out.println("Enter end date in the form of yyyy-mm-dd");
        String endDate = scanner.next();
        try {
            serviceController.setDiscountByProduct(productID, discount, startDate, endDate);
        } catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            setStuff();
        }
    }

    private void setDiscountByCategory() {
        System.out.println("Enter category id");
        int categoryID = scanner.nextInt();
        System.out.println("Enter discount");
        float discount = scanner.nextFloat();
        System.out.println("Enter start date in the form of yyyy-mm-dd");
        String startDate = scanner.next();
        System.out.println("Enter end date in the form of yyyy-mm-dd");
        String endDate = scanner.next();
        try {
            serviceController.setDiscountByCategory(categoryID, discount, startDate, endDate);
        } catch (Exception e) {
            System.out.println("Invalid input try again from the beginning");
            setStuff();
        }
    }

    private void getStuff() {
        System.out.println("What would you like to get?");
        System.out.println("1. Get Inventory Report");
        System.out.println("2. Get Defective Report");
        System.out.println("3. Get Expired Report");
        System.out.println("4. Get discounts by product id");
        System.out.println("5. Get inventory report by category/ies");
        System.out.println("6. Get products by category");
        System.out.println("7. Get to be expired report");
        System.out.println("8. Get amount of a product");
        System.out.println("9. Go back to the main menu");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1: {
                serviceController.getInventoryReport();
                break;
            }
            case 2: {
                serviceController.getDefectiveReport();
                break;
            }
            case 3: {
                serviceController.getExpiredReport();
                break;
            }
            case 4: {
                System.out.println("Enter product id");
                int productID = scanner.nextInt();
                ArrayList<String> discounts = serviceController.getDiscountsByProductId(productID);
                if(discounts.isEmpty()){
                    System.out.println("There are no discounts for this product");
                }
                else{
                    System.out.println(discounts);
                }
                break;
            }
            case 5: {
                System.out.println("Enter how many categories you would like to be in the report");
                int amountCategory = scanner.nextInt();
                ArrayList<Integer> categoryList = new ArrayList<>();
                for (int i = 0; i < amountCategory; i++) {
                    System.out.println("Enter the category you would like to be in the report");
                    categoryList.add(scanner.nextInt());
                }
                try {
                    System.out.println(serviceController.getInventoryReportByCategory(categoryList));
                    start();
                } catch (Exception e) {
                    System.out.println("Category doesn't exist");
                    start();
                }
                break;
            }
            case 6: {
                System.out.println("Enter category id");
                int categoryID3 = scanner.nextInt();
                try {
                    System.out.println(serviceController.getProductsByCategory(categoryID3));
                    start();
                } catch (Exception e) {
                    System.out.println("Category doesn't exist, please try again");
                    getStuff();
                }
                break;
            }
            case 7: {
                System.out.println("Enter how many days till expiration");
                int days = scanner.nextInt();
                try {
                    serviceController.getToBeExpiredReport(days);
                    start();
                } catch (Exception e) {
                    System.out.println("Invalid input,please try again from the beginning");
                    getStuff();
                }
                break;
            }
            case 8: {
                System.out.println("Enter product id");
                int productID = scanner.nextInt();
                System.out.println("Enter product branch");
                String branch = scanner.next();
                try {
                    System.out.println("The current amount of makat: "+productID+" in "+branch+" is "+serviceController.getAmountOfProduct(productID, branch));
                    start();
                } catch (Exception e) {
                    System.out.println("Product doesn't exist");
                    getStuff();
                }
                break;
            }
            case 9: {
                start();
            }

        }
    }
}