package Inventory_Suppliers.PresentationLayer.View;

import Inventory.BusinessLayer.Product;
import Inventory_Suppliers.PresentationLayer.Controller.InventoryController;
//import com.github.lgooddatepicker.components.*;
import Inventory_Suppliers.PresentationLayer.Controller.SupplierControllerMVC;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

public class InventoryTableView {
    private final InventoryController inventoryController = InventoryController.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void handleAddCategory() {
        // Create the frame
        JFrame frame = new JFrame("Add Category");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        JLabel categoryNameLabel = new JLabel("Category Name:");
        JTextField categoryNameField = new JTextField();

        JLabel categoryIdLabel = new JLabel("Category ID:");
        JTextField categoryIdField = new JTextField();

        inputPanel.add(categoryNameLabel);
        inputPanel.add(categoryNameField);
        inputPanel.add(categoryIdLabel);
        inputPanel.add(categoryIdField);

        // Create the add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int categoryId;
                try{
                    categoryId = Integer.parseInt(categoryIdField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the category ID.");
                    return;
                }
                // Check if category name is empty
                if (categoryNameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter the category name.");
                    return;
                }

                // Check if category ID is empty
                if (categoryIdField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter the category ID.");
                    return;
                }
                // Check if category ID is a bigger number than 0
                if (categoryId <= 0) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the category ID.");
                    return;
                }

                try {
                    //Integer.parseInt(categoryIdField.getText());
                    // Call the method to add the category
                   // addCategory(categoryNameField.getText(), categoryIdField.getText());
                    JOptionPane.showMessageDialog(frame, addCategory(categoryNameField.getText(), categoryIdField.getText()));
                    frame.dispose(); // Close the window
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the category ID.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex);
                }
            }
        });

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(addButton, BorderLayout.SOUTH);

        // Set the size of the frame
        frame.setSize(300, 150);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Make the frame visible
        frame.setVisible(true);
    }

    public String addCategory(String categoryName, String categoryId) {
        JFrame reportWindow = new JFrame(" Add Category");
        reportWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        // Get the inventory report from the business layer
        String feedback = inventoryController.addCategory(categoryName, Integer.parseInt(categoryId));

        // Set the report text in the text area
        reportTextArea.setText(feedback);

        // Add the text area to the report window
        reportWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        reportWindow.setVisible(false);
        // Add the category to the database
        return feedback;
    }

    public void inventoryReport() {
        inventoryController.getInventoryReport();
    }
    // Display the inventory data to the user
    public void displayInventoryReport() {
        // Create a new window
        JFrame reportWindow = new JFrame("Inventory Report");
        reportWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        // Get the inventory report from the business layer
        String inventoryReport = inventoryController.publishInventoryReport();

        // Set the report text in the text area
        reportTextArea.setText(inventoryReport);

        // Add the text area to the report window
        reportWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        reportWindow.setVisible(true);
    }


    public void displayDefectiveReport() {
        JFrame reportWindow = new JFrame("Defective Report");
        reportWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        // Get the inventory report from the business layer
        String inventoryReport = inventoryController.publishDefectiveReport();

        // Set the report text in the text area
        reportTextArea.setText(inventoryReport);

        // Add the text area to the report window
        reportWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        reportWindow.setVisible(true);

    }

    public void displayExpiredReport() {
        JFrame reportWindow = new JFrame("Expired Report");
        reportWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        // Get the inventory report from the business layer
        String inventoryReport = inventoryController.publishExpiredReport();

        // Set the report text in the text area
        reportTextArea.setText(inventoryReport);

        // Add the text area to the report window
        reportWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        reportWindow.setVisible(true);
    }

    public void displayDiscountByProductId(String productId) {
        // Create a new window
        //first we should get from the user the product id
        //then we should get the discount from the controller
        //then we should display the discount
        JFrame inputWindow = new JFrame("Discount By Product Id");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the discount from the controller
        String discount = inventoryController.getDiscountByProductId(productId);

        // Set the report text in the text area
        reportTextArea.setText(discount);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(true);
    }

    public void displayInventoryReportByCategory(ArrayList<Integer> categories) {
        // Create a new window
        // we Got the categories ID from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        ArrayList<StringBuilder> report = inventoryController.publishInventoryReportByCategory(categories);

        // Set the report text in the text area
        reportTextArea.setText(String.valueOf(report));

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(true);
    }

    public void displayProductByCategory(String categoryId) {
        // Create a new window
        // we Got the categories ID from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        ArrayList<Product> report = inventoryController.publishProductByCategory(Integer.parseInt(categoryId));

        // Set the report text in the text area

        for (Product product : report) {
            reportTextArea.append(product.toString());
            reportTextArea.append("\n");
        }
        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(true);
    }

    public void displayToBeExpiredReport(String numberOfDays) {
        // Create a new window
        // we Got the number of days from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        String report = inventoryController.publishToBeExpiredReport(Integer.parseInt(numberOfDays));

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(true);
    }

    public void displayAmountOfProduct(Integer productId, String productBranch) {
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        String report = String.valueOf(inventoryController.publishAmountOfProduct(productId, productBranch));

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(true);
    }
    public void handleAddProduct() {
        // Create the frame
        JFrame frame = new JFrame("Add New Product");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(15);
        inputPanel.add(nameLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel minAmountLabel = new JLabel("Minimum Amount:");
        JTextField minAmountField = new JTextField(15);
        inputPanel.add(minAmountLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(minAmountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel makatLabel = new JLabel("Makat:");
        JTextField makatField = new JTextField(15);
        inputPanel.add(makatLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(makatField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel categoryIDLabel = new JLabel("Category ID:");
        JTextField categoryIDField = new JTextField(15);
        inputPanel.add(categoryIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(categoryIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        // the supplier ID should be from a list of suppliers

        JLabel supplierIdLabel = new JLabel("Supplier ID:");
        String[] supplierOptions = SupplierControllerMVC.getInstance().getSupplierList();
        JComboBox<String> supplierComboBox = new JComboBox<>(supplierOptions);
        inputPanel.add(supplierIdLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(supplierComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;


        // add the subCategory as an optional string
        JLabel subCategoryLabel = new JLabel("Sub Category:");
        JTextField subCategoryField = new JTextField(15);
        inputPanel.add(subCategoryLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(subCategoryField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;


        // Create the list component to display suppliers
        JList<String> supplierList = new JList<>();

        // Create the ActionListener
//        ActionListener supplierUpdateListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Retrieve the category ID and makat values
//                String makat = makatField.getText();
//                // Update the supplierList component with the new supplier information
//                // Retrieve the updated supplier information
//                String[] updatedSupplierArray = SupplierControllerMVC.getInstance().getSupplierList(makat);
//                supplierList.setListData(updatedSupplierArray);
//            }
//        };

//        // Attach the ActionListener to the category ID and makat fields
//        makatField.addActionListener(supplierUpdateListener);

        // add the supplier list to the input panel
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(supplierList, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        buttonPanel.add(addButton);

        // Add action listener to the "Add" button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // check if all the fields are filled
                if (nameField.getText().equals("") || minAmountField.getText().equals("") || makatField.getText().equals("") || categoryIDField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                //Check if the numeric fields are numbers
                if (!isNumeric(minAmountField.getText()) || !isNumeric(makatField.getText()) || !isNumeric(categoryIDField.getText())) {
                    JOptionPane.showMessageDialog(null, "Please fill the numeric fields with numbers");
                    return;
                }
                // Retrieve the input values
                String name = nameField.getText();
                int minAmount = Integer.parseInt(minAmountField.getText());
                int makat = Integer.parseInt(makatField.getText());
                int categoryID = Integer.parseInt(categoryIDField.getText());
                int supplierID = Integer.parseInt(Objects.requireNonNull(supplierComboBox.getSelectedItem()).toString());
                String sub_category = subCategoryField.getText();



                // Add the new product to the inventory
                inventoryController.addProduct(name, minAmount,categoryID, sub_category, makat, supplierID);

                // Display a success message
                JOptionPane.showMessageDialog(null, "Product added successfully");

                // Clear the input fields
                nameField.setText("");
                minAmountField.setText("");
                makatField.setText("");
                categoryIDField.setText("");
                subCategoryField.setText("");
        };
        });

        // Add the input panel and button panel to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        //set preferred location to the center of the screen
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX() - 100, frame.getY() - 100);
        frame.setResizable(false);
        // Set the frame size and make it visible
        frame.pack();
        frame.setVisible(true);
    }


    public Boolean addProduct(String name, int minAmount, int categoryID,String sub_category, int makat , int supplierID){
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        String report = String.valueOf(inventoryController.addProduct(name, minAmount, categoryID, sub_category, makat, supplierID));

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window not visible
        inputWindow.setVisible(false);
        return Boolean.parseBoolean(report);
    }
    public void handleAddItems() {
        // Create the frame
        JFrame frame = new JFrame("Add Items");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel amountLabel = new JLabel("Amount of Items:");
        JTextField amountField = new JTextField(15);
        inputPanel.add(amountLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(amountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel manufacturerLabel = new JLabel("Manufacturer:");
        JTextField manufacturerField = new JTextField(15);
        inputPanel.add(manufacturerLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(manufacturerField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel barcodeLabel = new JLabel("Barcode:");
        JTextField barcodeField = new JTextField(15);
        inputPanel.add(barcodeLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(barcodeField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        inputPanel.add(nameLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel expirationDateLabel = new JLabel("Expiration Date:");
        inputPanel.add(expirationDateLabel, gbc);
        gbc.gridx += 50;
        JDatePickerImpl datePicker = createDatePicker();
        inputPanel.add(datePicker, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel costPriceLabel = new JLabel("Cost Price:");
        JTextField costPriceField = new JTextField(15);
        inputPanel.add(costPriceLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(costPriceField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel categoryIDLabel = new JLabel("Category ID:");
        JTextField categoryIDField = new JTextField(15);
        inputPanel.add(categoryIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(categoryIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel productIDLabel = new JLabel("Product ID:");
        JTextField productIDField = new JTextField(15);
        inputPanel.add(productIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(productIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel sizeLabel = new JLabel("Size (optional):");
        JTextField sizeField = new JTextField(15);
        inputPanel.add(sizeLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(sizeField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel branchLabel = new JLabel("Branch:");
        String[] branchOptions = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        JComboBox<String> branchComboBox = new JComboBox<>(branchOptions);
        inputPanel.add(branchLabel, gbc);
        gbc.gridx++;
        inputPanel.add(branchComboBox, gbc);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        buttonPanel.add(addButton);

        // Add action listener to the "Add" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amount = amountField.getText();
                String manufacturer = manufacturerField.getText();
                String barcode = barcodeField.getText();
                String name = nameField.getText();
                Date selectedDate = (Date) datePicker.getModel().getValue();
                String expirationDate = (selectedDate != null) ? new SimpleDateFormat("yyyy-MM-dd").format(selectedDate) : "";
                String costPrice = costPriceField.getText();
                String categoryID = categoryIDField.getText();
                String productID = productIDField.getText();
                String size = sizeField.getText();
                String branch = Objects.requireNonNull(branchComboBox.getSelectedItem()).toString();

                // Perform input validation
                if (amount.isEmpty() || manufacturer.isEmpty() || barcode.isEmpty() || name.isEmpty() || costPrice.isEmpty()
                        || categoryID.isEmpty() || productID.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                // Check if amount is a valid number
                if (!isNumeric(amount)) {
                    JOptionPane.showMessageDialog(frame, "Amount must be a number.");
                    return;
                }

                // Check if manufacturer is a valid number
                if (!isNumeric(manufacturer)) {
                    JOptionPane.showMessageDialog(frame, "Manufacturer must be a number.");
                    return;
                }

                // Check if barcode is a valid number
                if (!isNumeric(barcode)) {
                    JOptionPane.showMessageDialog(frame, "Barcode must be a number.");
                    return;
                }

                // Check if cost price is a valid number
                if (!isNumeric(costPrice)) {
                    JOptionPane.showMessageDialog(frame, "Cost Price must be a number.");
                    return;
                }

                // Check if category ID is a valid number
                if (!isNumeric(categoryID)) {
                    JOptionPane.showMessageDialog(frame, "Category ID must be a number.");
                    return;
                }

                // Check if product ID is a valid number
                if (!isNumeric(productID)) {
                    JOptionPane.showMessageDialog(frame, "Product ID must be a number.");
                    return;
                }
                // Check if date is in the future
                if (selectedDate != null && !selectedDate.after(new Date())) {
                    JOptionPane.showMessageDialog(frame, "Expiration date must be in the future.");
                    return;
                }

                // Call the method to add the item to the inventory
                String added =addItems(Integer.parseInt(manufacturer), Integer.parseInt(barcode), name, expirationDate, Integer.parseInt(costPrice), Integer.parseInt(categoryID), Integer.parseInt(productID), size, branch, Integer.parseInt(amount));
                JOptionPane.showMessageDialog(frame, added);
                if(added.equals("Items added successfully")){
                    frame.dispose();
                }


            }
        });

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the preferred size of the frame
//        frame.setPreferredSize(new Dimension(500, 400));

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX()-200, frame.getY() - 200);
        frame.setResizable(false);

        // Make the frame pack and visible
        frame.pack();
        frame.setVisible(true);

    }


    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, null);
    }

    private boolean isNumeric(String amount) {
        try {
            Integer.parseInt(amount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String addItems(int manufacturer, int barcode, String name, String expirationDate, int costPrice, int categoryID2, int productID, String size, String branch,int amount){
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        String report = String.valueOf(inventoryController.addItems(manufacturer, barcode, name, expirationDate, costPrice, categoryID2, productID, size, branch, amount));

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(false);
        return report;
    }
    public void handleSetMinimumAmount() {
        // Create the frame
        JFrame frame = new JFrame("Set Minimum Amount");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        JLabel productIDLabel = new JLabel("Product ID:");
        JTextField productIDField = new JTextField();

        JLabel deliveryTimeLabel = new JLabel("Days to Next Delivery:");
        JTextField deliveryTimeField = new JTextField();

        JLabel demandLabel = new JLabel("Demand:");
        JTextField demandField = new JTextField();

        inputPanel.add(productIDLabel);
        inputPanel.add(productIDField);
        inputPanel.add(deliveryTimeLabel);
        inputPanel.add(deliveryTimeField);
        inputPanel.add(demandLabel);
        inputPanel.add(demandField);

        // Create the set minimum amount button
        JButton setMinimumAmountButton = new JButton("Set Minimum Amount");
        setMinimumAmountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Check if user inserted valid input
                if (productIDField.getText().isEmpty() || deliveryTimeField.getText().isEmpty() || demandField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }
                try {
                    Integer.parseInt(productIDField.getText());
                    Integer.parseInt(deliveryTimeField.getText());
                    Integer.parseInt(demandField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid input for numeric fields.");
                    return;
                }
                int productID = Integer.parseInt(productIDField.getText());
                int deliveryTime = Integer.parseInt(deliveryTimeField.getText());
                int demand = Integer.parseInt(demandField.getText());

                try {
                    setMinimumAmount(deliveryTime, demand, productID);
                    JOptionPane.showMessageDialog(frame, "Minimum amount set successfully.");
                    frame.dispose(); // Close the window
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(frame, "Error: " + exception.getMessage());
                }
            }
        });

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        // Create empty border for padding between button and window border
        Border bottomBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        // Add the button to the SOUTH position of the frame with the bottom border
        frame.add(setMinimumAmountButton, BorderLayout.SOUTH);
        ((JComponent) frame.getContentPane()).setBorder(bottomBorder);
        frame.setResizable(false);


        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }

    public void setMinimumAmount(int deliveryTime, int demand ,int productID) {
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        inventoryController.setMinimumAmount(deliveryTime, demand, productID);
        String report = "Minimum amount was set successfully";

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(false);
    }
    public void handleSetDiscountByProduct() {
        // Create the frame
        JFrame frame = new JFrame("Set Discount");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel productIDLabel = new JLabel("Product ID:");
        JTextField productIDField = new JTextField(15);
        inputPanel.add(productIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(productIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountField = new JTextField(15);
        inputPanel.add(discountLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(discountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel startDateLabel = new JLabel("Start Date:");
        JDatePickerImpl startDatePanel = createDatePicker();
        inputPanel.add(startDateLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(startDatePanel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel endDateLabel = new JLabel("End Date:");
        JDatePickerImpl endDatePanel = createDatePicker();
        inputPanel.add(endDateLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(endDatePanel, gbc);
        gbc.gridy++;


        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton setDiscountButton = new JButton("Set Discount");
        buttonPanel.add(setDiscountButton);

        // Add action listener to the "Set Discount" button
        setDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productID = productIDField.getText();
                String discount = discountField.getText();
                Date startDate ;
                Date endDate ;
                String startDate1 ;
                String endDate1 ;
                try {
                     startDate = (Date) startDatePanel.getModel().getValue();
                     endDate = (Date) endDatePanel.getModel().getValue();
                     startDate1 = dateFormat.format(startDate);
                     endDate1 = dateFormat.format(endDate);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }


                // Perform input validation
                if (productID.isEmpty() || discount.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                // Check if product ID is a valid number
                if (!isNumeric(productID)) {
                    JOptionPane.showMessageDialog(frame, "Product ID must be a number.");
                    return;
                }

                // Check if discount is a valid number
                if (!isNumeric(discount)) {
                    JOptionPane.showMessageDialog(frame, "Discount must be a number.");
                    return;
                }

                // Check if categoryID is a bigger then 0
                if (Float.parseFloat(productID) <= 0) {
                    JOptionPane.showMessageDialog(frame, "Discount must be bigger then 0.");
                    return;
                }
                // Check if discount is bigger then 0
                if(Float.parseFloat(discount) <= 0){
                    JOptionPane.showMessageDialog(frame, "Discount must be bigger then 0.");
                    return;
                }


                //make sure that a date that has passed can not be selected
                if (startDate.before(new Date()) || endDate.before(new Date())) {
                    JOptionPane.showMessageDialog(frame, "Start and end dates must be in the future.");
                    return;
                }
                if(startDate.after(endDate)){
                    JOptionPane.showMessageDialog(frame, "Start date must be before end date.");
                    return;
                }
                // Check if discount is lower then 100
                if (Float.parseFloat(discount) > 100) {
                    JOptionPane.showMessageDialog(frame, "Discount must be lower then 100.");
                    return;
                }


                // Call the method to set the discount
                try {
                    setDiscountByProduct(Integer.parseInt(productID), Float.parseFloat(discount), startDate1, endDate1);
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error setting the discount.");
                    return;
                }

                // Display success message
                JOptionPane.showMessageDialog(frame, "Discount set successfully.");
            }
        });

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the preferred size of the frame
//        frame.setPreferredSize(new Dimension(350, 300));

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX()-100, frame.getY() - 150);
        frame.setResizable(false);


        // Make the frame pack and visible
        frame.pack();
        frame.setVisible(true);
    }

    public void setDiscountByProduct(int productID, float discount , String start, String end)  {
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(200, 100);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        inventoryController.setDiscountByProduct(productID, discount, start, end);
        String report = "Discount was set successfully";

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(false);
    }
    public void handleSetDiscountByCategory() {
        JFrame frame = new JFrame("Set Discount by Category");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel categoryIDLabel = new JLabel("Category ID:");
        JTextField categoryIDField = new JTextField(15);
        inputPanel.add(categoryIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(categoryIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel discountLabel = new JLabel("Discount (%):");
        JTextField discountField = new JTextField(15);
        inputPanel.add(discountLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(discountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel startDateLabel = new JLabel("Start Date:");
        JDatePickerImpl startDatePanel = createDatePicker();
        inputPanel.add(startDateLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(startDatePanel, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel endDateLabel = new JLabel("End Date:");
        JDatePickerImpl endDatePanel = createDatePicker();
        inputPanel.add(endDateLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(endDatePanel, gbc);
        gbc.gridy++;


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton setDiscountButton = new JButton("Set Discount");
        buttonPanel.add(setDiscountButton);

        setDiscountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String categoryID = categoryIDField.getText();
                String discount = discountField.getText();
                Date startDate;
                Date endDate;
                String startDate1;
                String endDate1;
                try {
                    startDate = (Date) startDatePanel.getModel().getValue();
                    endDate = (Date) endDatePanel.getModel().getValue();
                    startDate1 = dateFormat.format(startDate);
                    endDate1 = dateFormat.format(endDate);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                if (categoryID.isEmpty() || discount.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                if (!isNumeric(categoryID)) {
                    JOptionPane.showMessageDialog(frame, "Category ID must be a number.");
                    return;
                }
                try{
                    Float.parseFloat(categoryID);
                    try{
                        Float.parseFloat(discount);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, "Discount must be a number.");
                        return;
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(frame, "Category ID must be a number.");
                    return;
                }
                // Check if categoryID is a bigger then 0
                if (Float.parseFloat(categoryID) <= 0) {
                    JOptionPane.showMessageDialog(frame, "Discount must be bigger then 0.");
                    return;
                }
                // Check if discount is bigger then 0
                if(Float.parseFloat(discount) <= 0){
                    JOptionPane.showMessageDialog(frame, "Discount must be bigger then 0.");
                    return;
                }

                if (!isNumeric(discount)) {
                    JOptionPane.showMessageDialog(frame, "Discount must be a number.");
                    return;
                }
                // Check if discount is lower then 100
                if (Float.parseFloat(discount) > 100) {
                    JOptionPane.showMessageDialog(frame, "Discount must be lower then 100.");
                    return;
                }
                //make sure that a date that has passed can not be selected
                if (startDate.before(new Date()) || endDate.before(new Date())) {
                    JOptionPane.showMessageDialog(frame, "Start and end dates must be in the future.");
                    return;
                }
                if(startDate.after(endDate)){
                    JOptionPane.showMessageDialog(frame, "Start date must be before end date.");
                    return;
                }

                try {
                    setDiscountByCategory(Integer.parseInt(categoryID), Float.parseFloat(discount), startDate1, endDate1);
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error setting the discount.");
                    return;
                }

                JOptionPane.showMessageDialog(frame, "Discount set successfully.");
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

//        frame.setPreferredSize(new Dimension(350, 300));
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX()-100, frame.getY() - 150);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }

    public void setDiscountByCategory(int categoryID, float discount , String start, String end)  {
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        inventoryController.setDiscountByCategory(categoryID, discount, start, end);
        String report = "Discount was set successfully";

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(false);
    }

    public void setHowOftenToGetDefectiveReport(int days) {
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        inventoryController.setHowOftenToGetDefectiveReport(days);
        String report = "Set How Often To Get Defective Report was set successfully";

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(true);
    }

    public String sellItem(int CategoryID, int ItemID){
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        String report = inventoryController.sellItem(CategoryID, ItemID);
        if(report.equals("")){
            report = "Item was sold successfully";
        }

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(false);
        return report;
    }

    public String setDefectiveItem(int barcode, int categoryID, String reason) {
        // Create a new window
        // we Got the product id and branch from the user
        // then we should get the report from the controller
        // then we should display the report
        JFrame inputWindow = new JFrame("Inventory Report By Category");
        inputWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputWindow.setSize(1000, 750);

        // Create a text area to display the report
        JTextArea reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);

        //get the report from the controller
        String report = inventoryController.setDefectiveItem(barcode, categoryID, reason);
        if(report.equals("")){
            report = "Item was set as defective successfully";
        }

        // Set the report text in the text area

        reportTextArea.append(report);

        // Add the text area to the report window
        inputWindow.getContentPane().add(new JScrollPane(reportTextArea));

        // Make the report window visible
        inputWindow.setVisible(false);
        return report;
    }
    private void addFields(ArrayList<Component> comp, JPanel panel){
        for (Component c : comp) {
            panel.add(c);
        }
    }

    public void handleSetDefectiveReport() {
        JFrame frame = new JFrame("Set Defective Item");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel barcodeLabel = new JLabel("Barcode:");
        JTextField barcodeField = new JTextField(10);
        inputPanel.add(barcodeLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(barcodeField, gbc);
        gbc.gridx = 0;
        gbc.gridy+=5;

        JLabel categoryIDLabel = new JLabel("Category ID:");
        JTextField categoryIDField = new JTextField(10);
        inputPanel.add(categoryIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(categoryIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy+=5;

        JLabel reasonLabel = new JLabel("Reason:");
        JTextField reasonField = new JTextField(10);
        inputPanel.add(reasonLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(reasonField, gbc);
        gbc.gridy+=5;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton setDefectiveButton = new JButton("Set Defective");
        buttonPanel.add(setDefectiveButton);

        setDefectiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String barcode = barcodeField.getText();
                String categoryID = categoryIDField.getText();
                String reason = reasonField.getText();

                if (barcode.isEmpty() || categoryID.isEmpty() || reason.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                if (!isNumeric(barcode)) {
                    JOptionPane.showMessageDialog(frame, "Barcode must be a number.");
                    return;
                }

                if (!isNumeric(categoryID)) {
                    JOptionPane.showMessageDialog(frame, "Category ID must be a number.");
                    return;
                }

                try {
                    setDefectiveItem(Integer.parseInt(barcode), Integer.parseInt(categoryID), reason);
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error setting the defective item.");
                    return;
                }

                JOptionPane.showMessageDialog(frame, "Defective item set successfully.");
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

      //  frame.setPreferredSize(new Dimension(300, 250));
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX()-50, frame.getY() - 100);
        frame.pack();
        frame.setVisible(true);

    }

    public void handleSellItem() {
        JFrame frame = new JFrame("Sell Item");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel categoryIDLabel = new JLabel("Category ID:");
        JTextField categoryIDField = new JTextField(10);
        inputPanel.add(categoryIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(categoryIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy+=5;

        JLabel itemIDLabel = new JLabel("Item ID:");
        JTextField itemIDField = new JTextField(10);
        inputPanel.add(itemIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(itemIDField, gbc);
        gbc.gridy+=5;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton sellItemButton = new JButton("Sell Item");
        buttonPanel.add(sellItemButton);

        sellItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String categoryID = categoryIDField.getText();
                String itemID = itemIDField.getText();
                String ret = "";

                if (categoryID.isEmpty() || itemID.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                if (!isNumeric(categoryID)) {
                    JOptionPane.showMessageDialog(frame, "Category ID must be a number.");
                    return;
                }

                if (!isNumeric(itemID)) {
                    JOptionPane.showMessageDialog(frame, "Item ID must be a number.");
                    return;
                }

                try {
                    ret = sellItem(Integer.parseInt(categoryID), Integer.parseInt(itemID));
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error selling the item.");
                    return;
                }

                JOptionPane.showMessageDialog(frame, ret);
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX()-50, frame.getY() - 100);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);

    }

    public void handleSetItemAsDefective() {
        JFrame frame = new JFrame("Set Item As Defective");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel categoryIDLabel = new JLabel("Category ID:");
        JTextField categoryIDField = new JTextField(10);
        inputPanel.add(categoryIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(categoryIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy+=5;

        JLabel itemIDLabel = new JLabel("Barcode:");
        JTextField itemIDField = new JTextField(10);
        inputPanel.add(itemIDLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(itemIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy+=5;

        JLabel reasonLabel = new JLabel("Reason:");
        JTextField reasonField = new JTextField(10);
        inputPanel.add(reasonLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(reasonField, gbc);
        gbc.gridy+=5;


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton setItemAsDefectiveButton = new JButton("Set Item As Defective");
        buttonPanel.add(setItemAsDefectiveButton);

        setItemAsDefectiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String categoryID = categoryIDField.getText();
                String barcode = itemIDField.getText();
                String reason = reasonField.getText();
                String setAsDefect = "";

                if (categoryID.isEmpty() || barcode.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                if (!isNumeric(categoryID)) {
                    JOptionPane.showMessageDialog(frame, "Category ID must be a number.");
                    return;
                }

                if (!isNumeric(barcode)) {
                    JOptionPane.showMessageDialog(frame, "Item ID must be a number.");
                    return;
                }
                if(reason.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Please enter a reason.");
                    return;
                }

                try {
                    setAsDefect = setDefectiveItem(Integer.parseInt(barcode),Integer.parseInt(categoryID) , reason);
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error setting the item as defective.");
                    return;
                }
                JOptionPane.showMessageDialog(frame, setAsDefect);
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
  //      frame.setPreferredSize(new Dimension(250, 200));
        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX()-50, frame.getY() - 100);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);

    }

    public void handleDisplayAmountOfProduct() {
        JFrame frame = new JFrame("Display Amount of Product");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel productIdLabel = new JLabel("Product ID:");
        JTextField productIdField = new JTextField(15);
        inputPanel.add(productIdLabel, gbc);
        gbc.gridx += 50;
        inputPanel.add(productIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel branchLabel = new JLabel("Branch:");
        String[] branchOptions = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        JComboBox<String> branchComboBox = new JComboBox<>(branchOptions);
        inputPanel.add(branchLabel, gbc);
        gbc.gridx++;
        inputPanel.add(branchComboBox, gbc);


        // Create a "Go Back" button
//        JButton goBackButton = new JButton("Go Back");
//        inputPanel.add(goBackButton, gbc);

//        goBackButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//                // Additional code for the "Go Back" functionality
//            }
//        });

        gbc.gridy++;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton displayAmountButton = new JButton("Display Amount");
        buttonPanel.add(displayAmountButton);

        displayAmountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productId = productIdField.getText();
                String productBranch = (String) branchComboBox.getSelectedItem();

                if (productId.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the required fields.");
                    return;
                }

                if (!isNumeric(productId)) {
                    JOptionPane.showMessageDialog(frame, "Product ID must be a number.");
                    return;
                }

                // Add additional validation for productBranch if needed

                try {
                    displayAmountOfProduct(Integer.parseInt(productId), productBranch);
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error displaying amount of product.");
                    return;
                }

                // Additional success message or code if needed
            }
        });

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);


        frame.setLocationRelativeTo(null);
        frame.setLocation(frame.getX() - 100, frame.getY() - 150);
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }

}
