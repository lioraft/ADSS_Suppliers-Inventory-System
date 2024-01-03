package Inventory_Suppliers.PresentationLayer.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class MainWindow extends JFrame {
    public MainWindow(String jobTitle) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle bounds = gc.getBounds();

        int screenWidth = bounds.width;
        int screenHeight = bounds.height;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, screenHeight-50));
        setResizable(false);
        // start connection with the server
        try {
            // Load the background image
            Image backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("background new.jpg")));

            // Create the main container panel
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Draw the background image
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            };
            mainPanel.setLayout(new BorderLayout());
            if(!jobTitle.equalsIgnoreCase("storemanager") && !jobTitle.equalsIgnoreCase("stockkeeper") && !jobTitle.equalsIgnoreCase("suppliermanager")){
                displayError();
                return;
            }
            switch (jobTitle.toLowerCase()) {
                case "storemanager":
                    storeManagerMain(mainPanel , " Store Manager");
                    break;
                case "stockkeeper":
                    stockKeeper(mainPanel," Stock Keeper");
                    break;
                case "suppliermanager":
                    supplierManager(mainPanel," Supplier Manager");
                    break;
            }

            setContentPane(mainPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pack();
        setLocationRelativeTo(null);
    }

    private void displayError() {
        JOptionPane.showMessageDialog(this, "Invalid Job Title", "Invalid Job Title", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private void storeManagerMain(JPanel mainPanel, String jobTitle) {
        if(!jobTitle.equals(" Store Manager")){
            //close the window
            this.dispose();
            //stop the program
            System.exit(0);
        }

        //creat choose panel for the store manager to choose which panel he wants to see
        int widthBottom = 265;
        int heightBottom = 50;
        InventoryTableView inventoryTableView = new InventoryTableView();
        // Create the main container panel
        mainPanel.setLayout(null);
        Color color = Color.decode("#ff6d83");
        Color opacityColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
        // Create the textPanel1 and textPanelD panels with null layout
        JPanel textPanel1 = new JPanel(new BorderLayout());
        JPanel textPanelD = new JPanel(new BorderLayout());
        // Create the titleLabel1
        JLabel titleLabel1 = new JLabel(checkTime() + " Store Manager and Welcome to the Inventory-Supplier Management System");
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel1.setForeground(Color.BLACK);
        titleLabel1.setPreferredSize(new Dimension(800, 80));
        titleLabel1.setBackground(opacityColor);
        titleLabel1.setOpaque(true);
        textPanel1.add(titleLabel1, BorderLayout.CENTER);
        textPanel1.setBounds(0, 0, 1200, 80);
        //craete the title label 2
        JLabel titleLabel2 = new JLabel("Choose Panel:");
        titleLabel2.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel2.setForeground(Color.BLACK);
        titleLabel2.setPreferredSize(new Dimension(265, 50));
        titleLabel2.setBackground(opacityColor);
        titleLabel2.setOpaque(true);
        textPanelD.add(titleLabel2, BorderLayout.CENTER);
        textPanelD.setBounds(0, 80, 265, 50);
        //add the Store Manager button
        JButton storeManagerButton = addjButton(widthBottom, heightBottom, color, "Store Manager actions");
        storeManagerButton.addActionListener(e->{
            //would like to move to the store manager panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();
            storeManager(mainPanel," Store Manager");
        });
        storeManagerButton.setBounds(0, 130, widthBottom, heightBottom);
        //add the stockkeeper button
        JButton stockKeeperButton = addjButton(widthBottom, heightBottom, color, "Stock Keeper actions");
        stockKeeperButton.addActionListener(e->{
            //would like to move to the stockkeeper panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();
            stockKeeper(mainPanel," Store Manager");
        });
        stockKeeperButton.setBounds(0, 180, widthBottom, heightBottom);
        //add the supplier manager button
        JButton supplierManagerButton = addjButton(widthBottom, heightBottom, color, "Supplier Manager actions");
        supplierManagerButton.addActionListener(e->{
            //would like to move to the supplier manager panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();
            supplierManager(mainPanel," Store Manager");
        });
        supplierManagerButton.setBounds(0, 230, widthBottom, heightBottom);
        JButton exitButton = addjButton(widthBottom, heightBottom, color, "Exit");
        exitButton.addActionListener(e->{
            //close the window
            this.dispose();
            //stop the program
            System.exit(0);
        });
        exitButton.setBounds(0, 280, widthBottom, heightBottom);


        //we would like to create a sidebar for the buttons
        JPanel sideBar = new JPanel();
        sideBar.setLayout(null);
        sideBar.setBounds(0, 80, widthBottom, 820);
        sideBar.setBackground(opacityColor);
        mainPanel.add(exitButton);
        mainPanel.add(textPanel1, BorderLayout.NORTH);
        mainPanel.add(textPanelD);
        mainPanel.add(storeManagerButton);
        mainPanel.add(stockKeeperButton);
        mainPanel.add(supplierManagerButton);
        mainPanel.add(sideBar);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void stockKeeper(JPanel mainPanel ,String jobTitle) {
        int widthBottom = 265;
        int heightBottom = 50;
        int yLocation =130;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, null);
        InventoryTableView inventoryTableView = new InventoryTableView();
        // Create the main container panel
        mainPanel.setLayout(null);
        Color color = Color.decode("#ff6d83");
        Color opacityColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
        // Create the textPanel1 and textPanelD panels with null layout
        JPanel textPanel1 = new JPanel(new BorderLayout());
        // Create the titleLabel1
        JLabel titleLabel1 = new JLabel(checkTime() + jobTitle +" and Welcome to the Inventory-Supplier Management System");
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel1.setForeground(Color.BLACK);
        titleLabel1.setPreferredSize(new Dimension(800, 80));
        titleLabel1.setBackground(opacityColor);
        titleLabel1.setOpaque(true);
        textPanel1.add(titleLabel1, BorderLayout.CENTER);
        textPanel1.setBounds(0, 0, 1200, 80);
        //we would like to create a sidebar for the buttons
        JPanel sideBar = new JPanel();
        sideBar.setLayout(null);
        sideBar.setBounds(0, 80, widthBottom, 820);
        sideBar.setBackground(opacityColor);
        //add the add product button
        JButton addProductButton = addjButton(widthBottom, heightBottom, color, "Add Product");
        addProductButton.addActionListener(e -> {
                    try {
                        inventoryTableView.handleAddProduct();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(addProductButton, exception);
                    }
        });
        addProductButton.setBounds(0, yLocation,widthBottom, heightBottom);


        /// add the add category button
        JButton addCategoryButton = addjButton(widthBottom, heightBottom, color, "Add Category");
        addCategoryButton.addActionListener(e -> {
                    try {
                        inventoryTableView.handleAddCategory();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(addCategoryButton, exception);
                    }
        });

        addCategoryButton.setBounds(0, yLocation+51, widthBottom, heightBottom);
        //add the add Items button
        JButton addItemsButton = addjButton(widthBottom, heightBottom, color, "Add Items");
        addItemsButton.addActionListener(e -> {
                    try {
                        inventoryTableView.handleAddItems();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(addItemsButton, exception);
                    }
        });
        addItemsButton.setBounds(0, yLocation+51*2, widthBottom, heightBottom);
        //add the set minimum amount button
        JButton setMinimumAmountButton = addjButton(widthBottom, heightBottom, color, "Set Minimum Amount");
        setMinimumAmountButton.addActionListener(e -> {
                    try{
                        inventoryTableView.handleSetMinimumAmount();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(setMinimumAmountButton, "Error: Please enter a valid information");
                    }
        });
        setMinimumAmountButton.setBounds(0, yLocation+51*3, widthBottom, heightBottom);
        //Set discount by product
        JButton setDiscountByProductButton = addjButton(widthBottom, heightBottom, color, "Set Discount By Product");
        setDiscountByProductButton.addActionListener(e -> {
                    try{
                        inventoryTableView.handleSetDiscountByProduct();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(setDiscountByProductButton, "Error: Please enter a valid information");
                    }
        });
        setDiscountByProductButton.setBounds(0, yLocation+51*4, widthBottom, heightBottom);


        //Set discount by category
        JButton setDiscountByCategoryButton = addjButton(widthBottom, heightBottom, color, "Set Discount By Category");
        setDiscountByCategoryButton.addActionListener(e -> {
                    try{
                        inventoryTableView.handleSetDiscountByCategory();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(setDiscountByCategoryButton, "Error: Please enter a valid information");
                    }
        });
        setDiscountByCategoryButton.setBounds(0, yLocation+51*5, widthBottom, heightBottom);

        //Set how often to get defective report
//        JButton setDefectiveReportButton = addjButton(widthBottom, heightBottom, color, "Set Defective Report");
//        setDefectiveReportButton.addActionListener(e -> {
//                    try{
//                        inventoryTableView.handleSetDefectiveReport();
//                    } catch (Exception exception) {
//                        JOptionPane.showMessageDialog(setDefectiveReportButton, "Error: Please enter a valid information");
//                    }
//        });
//        setDefectiveReportButton.setBounds(0, yLocation+51*6, widthBottom, heightBottom);

        //sellItem
        JButton sellItemButton = addjButton(widthBottom, heightBottom, color, "Sell Item");
        sellItemButton.addActionListener(e -> {
                    try{
                        inventoryTableView.handleSellItem();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(sellItemButton, "Error: Please enter a valid information");
                    }
        });
        sellItemButton.setBounds(0, yLocation+51*6, widthBottom, heightBottom);
        // set item as defective
        JButton setItemAsDefectiveButton = addjButton(widthBottom, heightBottom, color, "Set Item As Defective");
        setItemAsDefectiveButton.addActionListener(e -> {
                    try{
                        inventoryTableView.handleSetItemAsDefective();
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(setItemAsDefectiveButton, "Error: Please enter a valid information");
                    }
        });
        setItemAsDefectiveButton.setBounds(0, yLocation+51*7, widthBottom, heightBottom);

        //add back to storeManagerMain  button
        JButton backToMainMenuButton = addjButton(widthBottom, heightBottom, color,"Exit");
        backToMainMenuButton.addActionListener(e->{
            //would like to move to the store manager panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();
            storeManagerMain(mainPanel, jobTitle);
        });

        backToMainMenuButton.setBounds(0, yLocation+51*8, widthBottom, heightBottom);



        // Add the panels to the mainPanel
        mainPanel.add(backToMainMenuButton);
        mainPanel.add(setItemAsDefectiveButton);
        mainPanel.add(sellItemButton);
//        mainPanel.add(setDefectiveReportButton);
        mainPanel.add(setDiscountByCategoryButton);
        mainPanel.add(setDiscountByProductButton);
        mainPanel.add(setMinimumAmountButton);
        mainPanel.add(addCategoryButton);
        mainPanel.add(addProductButton);
        mainPanel.add(addItemsButton);

        mainPanel.add(textPanel1, BorderLayout.NORTH);
        mainPanel.add(sideBar);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);

    }

    private boolean isNumeric(String productMinAmount) {
        try {
            Integer.parseInt(productMinAmount);
        } catch (NumberFormatException nfe) {
            return true;
        }
        return false;
    }


    private void storeManager(JPanel mainPanel, String jobTitle) {
        int widthBottom = 265;
        int heightBottom = 50;
        InventoryTableView inventoryTableView = new InventoryTableView();
        // Create the main container panel
        mainPanel.setLayout(null);
        Color color = Color.decode("#ff6d83");
        Color opacityColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
        // Create the textPanel1 and textPanelD panels with null layout
        JPanel textPanel1 = new JPanel(new BorderLayout());
        // Create the titleLabel1
        JLabel titleLabel1 = new JLabel(checkTime() + jobTitle+" and Welcome to the Inventory-Supplier Management System");
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel1.setForeground(Color.BLACK);
        titleLabel1.setPreferredSize(new Dimension(800, 80));
        titleLabel1.setBackground(opacityColor);
        titleLabel1.setOpaque(true);
        textPanel1.add(titleLabel1, BorderLayout.CENTER);
        textPanel1.setBounds(0, 0, 1200, 80);
        //we would like to create a sidebar for the buttons
        JPanel sideBar = new JPanel();
        sideBar.setLayout(null);
        sideBar.setBounds(0, 80, widthBottom, 820);
        sideBar.setBackground(opacityColor);


        // Create the inventoryReportButton
        JButton inventoryReportButton = addjButton(widthBottom, heightBottom, color,"Inventory Report");
        inventoryReportButton.addActionListener(e -> {
            inventoryTableView.displayInventoryReport();

        });

        // Set the bounds of inventoryReportButton within textPanel1
        inventoryReportButton.setBounds(0, 120, widthBottom, heightBottom);

        // Create the defectiveReportButton
       JButton defectiveReportButton = addjButton(widthBottom, heightBottom, color,"Defective Report");
        defectiveReportButton.addActionListener(e -> {
            inventoryTableView.displayDefectiveReport();
        });
        // Set the bounds of defectiveReportButton within textPanelD
        defectiveReportButton.setBounds(0, 171, widthBottom, heightBottom);

        // Add the expired report button
        JButton expiredReportButton = addjButton(widthBottom, heightBottom, color,"Expired Report");
        expiredReportButton.addActionListener(e -> {
            inventoryTableView.displayExpiredReport();
        });
        // Set the bounds of expiredReportButton within textPanelD
        expiredReportButton.setBounds(0, 222, widthBottom, heightBottom);

        // add the get discount by product ID button
         JButton getDiscountByProductIdButton = addjButton(widthBottom, heightBottom, color,"Discount By Product ID");
        //when click the button should open a small window to enter the product id
        //new text field and a button to enter the product id
        getDiscountByProductIdButton.addActionListener(e -> {
            String productId = JOptionPane.showInputDialog(getDiscountByProductIdButton, "Enter Product ID:");
            if(productId == null){
                return;
            }
            if (isNumeric(productId)){
                JOptionPane.showMessageDialog(getDiscountByProductIdButton, "Error: Please enter a valid number");
                return;
            }
            inventoryTableView.displayDiscountByProductId(productId);
        });

        // Set the bounds of expiredReportButton within textPanelD
        getDiscountByProductIdButton.setBounds(0, 273, widthBottom, heightBottom);

        // create the inventory report by category/ies button
        JButton inventoryReportByCategoryButton = addjButton(widthBottom, heightBottom, color,"Inventory Report By Category");
        inventoryReportByCategoryButton.addActionListener(e -> {
            //we should get the number of categories from the user
            try {
                //we should get a list of IDs from the user
                String categories1 = JOptionPane.showInputDialog(inventoryReportByCategoryButton, "Enter Category IDs separated by commas:");
                if(categories1 == null){
                    return;
                }
                String[] categoryIds = categories1.split(",");
                for (String categoryId : categoryIds) {
                    if (isNumeric(categoryId)) {
                        JOptionPane.showMessageDialog(inventoryReportByCategoryButton, "Please enter a valid number");
                        return;
                    }
                }
                ArrayList<Integer> categories = new ArrayList<>();
                for(String categoryId : categoryIds) {
                    categories.add(Integer.parseInt(categoryId));
                }

            try{
            inventoryTableView.displayInventoryReportByCategory(categories);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(inventoryReportByCategoryButton, "Please enter a valid number");
            }
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(inventoryReportByCategoryButton, "Please enter a valid number");
            }
        });
        // Set the bounds of expiredReportButton within textPanelD
        inventoryReportByCategoryButton.setBounds(0, 324, widthBottom, heightBottom);

        //Add the get product by category button
        JButton getProductByCategoryButton = addjButton(widthBottom, heightBottom, color,"Product By Category");
        getProductByCategoryButton.addActionListener(e -> {
            //we should get the category ID from the user
            String categoryId = JOptionPane.showInputDialog(getProductByCategoryButton, "Enter Category ID:");
            if(categoryId == null){
                return;
            }
            if (isNumeric(categoryId)){
                JOptionPane.showMessageDialog(getProductByCategoryButton, "Error: Please enter a valid number");
                return;
            }
            try{
            inventoryTableView.displayProductByCategory(categoryId);
        }catch (Exception exception){
                JOptionPane.showMessageDialog(getProductByCategoryButton, "Please enter a valid number");
            }
        });
        // Set the bounds of expiredReportButton within textPanelD
        getProductByCategoryButton.setBounds(0, 375, widthBottom, heightBottom);

        //Create to be expired report button
        JButton toBeExpiredReportButton = addjButton(widthBottom, heightBottom, color,"To Be Expired Report");
        toBeExpiredReportButton.addActionListener(e -> {
            //first we need to get the number of days from the user
            String numberOfDays = JOptionPane.showInputDialog(toBeExpiredReportButton, "Enter Number of Days:");
            if(numberOfDays == null){
                return;
            }
            if (isNumeric(numberOfDays)){
                JOptionPane.showMessageDialog(toBeExpiredReportButton, "Error: Please enter a valid number");
                return;
            }
            try {
                // then we need to send the input to the controller
                inventoryTableView.displayToBeExpiredReport(numberOfDays);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(toBeExpiredReportButton, "Please enter a valid number");
            }
        });
        //Create amount of product bottom
        JButton amountOfProductButton = addjButton(widthBottom, heightBottom, color,"Amount Of Product");
        amountOfProductButton.addActionListener(e -> {
            //first we need to get the number of days from the user
//            String productId = JOptionPane.showInputDialog(amountOfProductButton, "Enter Product ID:");
//            if(productId == null){
//                return;
//            }
//            if (isNumeric(productId)){
//                JOptionPane.showMessageDialog(amountOfProductButton, "Error: Please enter a valid number");
//                return;
//            }
//            //add option frame to choose branch
//            String productBranch = JOptionPane.showInputDialog(amountOfProductButton, "Enter Product Branch:(From A to I");
//            try {
//                // then we need to send the input to the controller.
//                inventoryTableView.displayAmountOfProduct(Integer.parseInt(productId), productBranch);
//            } catch (Exception exception) {
//                JOptionPane.showMessageDialog(amountOfProductButton, "Please enter a valid Branch");
//            }
            try{
                inventoryTableView.handleDisplayAmountOfProduct();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(amountOfProductButton, "Error: Please enter a valid information");
            }

        });
        // Set the bounds of expiredReportButton within textPanelD
        amountOfProductButton.setBounds(0, 426, widthBottom, heightBottom);
        // Set the bounds of expiredReportButton within textPanelD
        toBeExpiredReportButton.setBounds(0, 426, widthBottom, heightBottom);
        //add back to storeManagerMain  button
        JButton backToMainMenuButton = addjButton(widthBottom, heightBottom, color,"Back To Main Menu");
        backToMainMenuButton.addActionListener(e->{
            //would like to move to the store manager panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();
            storeManagerMain(mainPanel , jobTitle);
        });

        backToMainMenuButton.setBounds(0, 477, widthBottom, heightBottom);
        // Add the panels to the mainPanel
        mainPanel.add(backToMainMenuButton);
        mainPanel.add(getProductByCategoryButton);
        mainPanel.add(amountOfProductButton);
        mainPanel.add(toBeExpiredReportButton);
        mainPanel.add(inventoryReportByCategoryButton);
        mainPanel.add(textPanel1, BorderLayout.NORTH);
        mainPanel.add(defectiveReportButton);
        mainPanel.add(inventoryReportButton);
        mainPanel.add(expiredReportButton);
        mainPanel.add(getDiscountByProductIdButton);
        mainPanel.add(sideBar);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }


    // menu for the supplier manager
    private void supplierManager(JPanel mainPanel , String jobTitle) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle bounds = gc.getBounds();

        int screenWidth = bounds.width;
        int screenHeight = bounds.height;
        int windowHeight = screenHeight - 50;
        int widthBottom = 265;
        int buttonHeight = (windowHeight - 85)/14;
        int y = 85;
        SupplierDetailsView supplierDetailsView = new SupplierDetailsView();
        // Create the main container panel
        mainPanel.setLayout(null);
        Color color = Color.decode("#ff6d83");
        Color opacityColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
        // Create the textPanel1 and textPanelD panels with null layout
        JPanel textPanel1 = new JPanel(new BorderLayout());
        // Create the titleLabel1
        JLabel titleLabel1 = new JLabel(checkTime() + jobTitle +" and Welcome to the Inventory-Supplier Management System");
        titleLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel1.setForeground(Color.BLACK);
        titleLabel1.setPreferredSize(new Dimension(800, 80));
        titleLabel1.setBackground(opacityColor);
        titleLabel1.setOpaque(true);
        textPanel1.add(titleLabel1, BorderLayout.CENTER);
        textPanel1.setBounds(0, 0, 1200, 80);
        //we would like to create a sidebar for the buttons


        // Create a JScrollPane and set the sidebar panel as its view
//        JScrollPane scrollPane = new JScrollPane(sideBar);
//        scrollPane.setBounds(0, 80, widthBottom+20, getHeight());
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Customize the scrollbar appearance

// Create a listener for the scrollbar adjustment
//        AdjustmentListener adjustmentListener = e -> {
//            int scrollPosition = scrollPane.getVerticalScrollBar().getValue();
//            int maxScrollPosition = scrollPane.getVerticalScrollBar().getMaximum() - scrollPane.getVerticalScrollBar().getModel().getExtent();
//            int sidebarHeight = sideBar.getHeight();
//            int maxSidebarHeight = sideBar.getPreferredSize().height - scrollPane.getViewport().getHeight();
//
//            int newSidebarHeight = (int) (sidebarHeight * (double) scrollPosition / maxScrollPosition);
//            sideBar.setPreferredSize(new Dimension(sideBar.getWidth(), newSidebarHeight));
//
//            sideBar.revalidate();
////            sideBar.repaint();
//        };
//
//        // Add the adjustment listener to the scrollbar
//        scrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);

        // create button for adding new supplier
        JButton addSupplierButton = addjButton(widthBottom, buttonHeight, color, "Add New Supplier");
        addSupplierButton.addActionListener(e -> {
            supplierDetailsView.handleAddSupplier();
        });

        // Set the bounds of addSupplierButton within textPanel1
        addSupplierButton.setBounds(0, y, widthBottom, buttonHeight);


        // Create the removeSupplierButton
        JButton removeSupplierButton = addjButton(widthBottom, buttonHeight, color,"Remove Supplier");
        removeSupplierButton.addActionListener(e -> {
            supplierDetailsView.handleRemoveSupplier();
        });
        // Set the bounds of removeSupplierButton within textPanelD
        removeSupplierButton.setBounds(0, y+buttonHeight, widthBottom, buttonHeight);


        // Add the edit supplier button
        JButton editSupplierButton = addjButton(widthBottom, buttonHeight, color,"Edit Supplier Information");
        editSupplierButton.addActionListener(e -> {
            supplierDetailsView.handleEditSupplier();
        });
        // Set the bounds of expiredReportButton within textPanelD
        editSupplierButton.setBounds(0, y+2*buttonHeight, widthBottom, buttonHeight);

        // Add the add supply agreements button
        JButton addSAButton = addjButton(widthBottom, buttonHeight, color,"Add Supply Agreement");
        addSAButton.addActionListener(e -> {
            supplierDetailsView.handleAddSupplyAgreement();
        });

        // Set the bounds of expiredReportButton within textPanelD
        addSAButton.setBounds(0, y+3*buttonHeight, widthBottom, buttonHeight);

        // Add the remove supply agreements button
        JButton removeSAButton = addjButton(widthBottom, buttonHeight, color,"Remove Supply Agreement");
        removeSAButton.addActionListener(e -> {
            supplierDetailsView.handleRemoveSupplyAgreement();
        });

        // Set the bounds of expiredReportButton within textPanelD
        removeSAButton.setBounds(0, y+4*buttonHeight, widthBottom, buttonHeight);

        // Add the add order discount of supplier button
        JButton orderDiscountButton = addjButton(widthBottom, buttonHeight, color,"Add Order Discount");
        orderDiscountButton.addActionListener(e -> {
            supplierDetailsView.handleOrderDiscount();
        });

        // Set the bounds of expiredReportButton within textPanelD
        orderDiscountButton.setBounds(0, y+5*buttonHeight, widthBottom, buttonHeight);

        // Add the add set next delivery date of supplier button
        JButton nextDateButton = addjButton(widthBottom, buttonHeight, color,"Set Delivery Date");
        nextDateButton.addActionListener(e -> {
            supplierDetailsView.handleDeliveryDate();
        });

        // Set the bounds of expiredReportButton within textPanelD
        nextDateButton.setBounds(0, y+6*buttonHeight, widthBottom, buttonHeight);


        // Add the print supply agreements button
        JButton printSAButton = addjButton(widthBottom, buttonHeight, color, "Print Supply Agreements");
        printSAButton.addActionListener(e -> {
            supplierDetailsView.handlePrintSupplyAgreements();
        });

        // Set the bounds of printSAButton within sideBar
        printSAButton.setBounds(0, y+7*buttonHeight, widthBottom, buttonHeight);


        JButton addPeriodicOrderButton = addjButton(widthBottom, buttonHeight, color, "Add Periodic Order");
        addPeriodicOrderButton.addActionListener(e -> {
            supplierDetailsView.handleAddPeriodicOrder();
        });
        // Set the bounds of addPeriodicOrderButton within sideBar
        addPeriodicOrderButton.setBounds(0, y+8*buttonHeight, widthBottom, buttonHeight);

        // Add the delete order button
        JButton deleteOrderButton = addjButton(widthBottom, buttonHeight, color, "Remove Periodic Order");
        deleteOrderButton.addActionListener(e -> {
            supplierDetailsView.handleDeletePeriodicOrder();
        });
        // Set the bounds of deleteOrderButton within sideBar
        deleteOrderButton.setBounds(0, y+9*buttonHeight, widthBottom, buttonHeight);

        // Add the update order status button
        JButton updateOrderStatusButton = addjButton(widthBottom, buttonHeight, color, "Update Order Status");
        updateOrderStatusButton.addActionListener(e -> {
            supplierDetailsView.handleUpdateOrderStatus();
        });
        // Set the bounds of updateOrderStatusButton within sideBar
        updateOrderStatusButton.setBounds(0, y+10*buttonHeight, widthBottom, buttonHeight);

        // Add the print order button
        JButton printOrderButton = addjButton(widthBottom, buttonHeight, color, "Print Orders");
        printOrderButton.addActionListener(e -> {
            supplierDetailsView.handlePrintOrder();
        });
        // Set the bounds of printOrderButton within sideBar
        printOrderButton.setBounds(0, y+11*buttonHeight, widthBottom, buttonHeight);

        //add back to storeManagerMain  button
        JButton backToMainMenuButton = addjButton(widthBottom, buttonHeight, color,"Exit");
        backToMainMenuButton.addActionListener(e->{
            //would like to move to the store manager panel
            mainPanel.removeAll();
            mainPanel.repaint();
            mainPanel.revalidate();
            storeManagerMain(mainPanel, jobTitle);
        });

        backToMainMenuButton.setBounds(0, y+12*buttonHeight, widthBottom, buttonHeight);
        //add the buttons to the side bar:
//        sideBar.add(addSupplierButton);
//        sideBar.add(removeSupplierButton);
//        sideBar.add(editSupplierButton);
//        sideBar.add(addSAButton);
//        sideBar.add(removeSAButton);
//        sideBar.add(orderDiscountButton);
//        sideBar.add(printSAButton);
//        sideBar.add(nextDateButton);
//        sideBar.add(addPeriodicOrderButton);
//        sideBar.add(deleteOrderButton);
//        sideBar.add(updateOrderStatusButton);
//        sideBar.add(printOrderButton);
//        sideBar.add(backToMainMenuButton);
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBounds(0, 80, widthBottom, 820);
        sideBar.setBackground(opacityColor);

        // Add the panels to the mainPanel
        mainPanel.add(backToMainMenuButton);
        mainPanel.add(editSupplierButton);
        mainPanel.add(textPanel1, BorderLayout.NORTH);
        mainPanel.add(sideBar);
        mainPanel.add(addSupplierButton);
        mainPanel.add(removeSupplierButton);
        mainPanel.add(addSAButton);
        mainPanel.add(removeSAButton);
        mainPanel.add(orderDiscountButton);
        mainPanel.add(printSAButton);
        mainPanel.add(nextDateButton);
        mainPanel.add(addPeriodicOrderButton);
        mainPanel.add(deleteOrderButton);
        mainPanel.add(updateOrderStatusButton);
        mainPanel.add(printOrderButton);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);

//        mainPanel.add(scrollPane);
    }

    private static JButton addjButton(int widthBotton, int heightBotton, Color color , String name) {
        JButton inventoryReportButton = new JButton(name);
        inventoryReportButton.setPreferredSize(new Dimension(widthBotton, heightBotton));
        inventoryReportButton.setFont(new Font("Arial", Font.BOLD, 16));
        inventoryReportButton.setBackground(color);
        inventoryReportButton.setForeground(Color.WHITE);
        inventoryReportButton.setOpaque(true);
        return inventoryReportButton;
    }

    public String checkTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow(args[0]);
            mainWindow.setVisible(true);
        });
    }
}
