package Inventory_Suppliers.PresentationLayer.View;

import Inventory_Suppliers.PresentationLayer.Controller.OrderControllerNVC;
import Inventory_Suppliers.PresentationLayer.Controller.SupplierControllerMVC;
import Suppliers.BusinessLayer.Payment;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;


public class SupplierDetailsView {
    // Display supplier information to the user
    private final SupplierControllerMVC supplierController = SupplierControllerMVC.getInstance();
    private final OrderControllerNVC orderController = OrderControllerNVC.getInstance();

    /*
    -------------------------------Add supplier handlers and window functions----------------------------------------
     */

    // handle add supplier
    public void handleAddSupplier() {
        // add frame
        JFrame frame = new JFrame("Add Supplier");
        // add panel
        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("Choose Supplier Type:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust the font properties as desired
        panel.add(titleLabel);
        // add combo box of supplier types
        String[] options = {"Fixed Days Supplier", "On Order Supplier", "No Transport Supplier"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(comboBox);
        // add continue button
        JButton chooseButton = new JButton("Continue");
        // add action listener for button
        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // get selected type
                String selectedType = (String) comboBox.getSelectedItem();
                if(selectedType == null) {
                    JOptionPane.showMessageDialog(null, "Please choose supplier type");
                    return;
                }
                // open add supplier window
                AddSupplierWindow(selectedType);
                frame.dispose();
            }
        });
        // add components to frame and set frame properties
        panel.add(chooseButton);
        panel.setLayout(new FlowLayout());
        frame.getContentPane().add(panel);
        frame.setSize(350, 100);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // window for getting input of new supplier: after choosing supplier's type, this window will open
    public void AddSupplierWindow(String type) {
        // create frame
        JFrame frame = new JFrame("Add Supplier");
        frame.setLayout(new BorderLayout());

        // create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // add form fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField(20);
        JLabel bankLabel = new JLabel("Bank:");
        JTextField bankField = new JTextField(20);
        JLabel paymentLabel = new JLabel("Payment:");
        JComboBox<Payment> paymentComboBox = new JComboBox<>(Payment.values());
        JPanel daysPanel = createDaysPanel();
        JTextField addressField = new JTextField(20);

        // add form fields to form panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(nameLabel, constraints);
        constraints.gridx = 1;
        formPanel.add(nameField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(idLabel, constraints);
        constraints.gridx = 1;
        formPanel.add(idField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(bankLabel, constraints);
        constraints.gridx = 1;
        formPanel.add(bankField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        formPanel.add(paymentLabel, constraints);
        constraints.gridx = 1;
        formPanel.add(paymentComboBox, constraints);

        // adjust layout based on supplier type
        if (type.equals("Fixed Days Supplier")) {
            constraints.gridx = 0;
            constraints.gridy = 4;
            formPanel.add(new JLabel("Days:"), constraints);
            constraints.gridx = 1;
            formPanel.add(daysPanel, constraints);
        } else if (type.equals("No Transport Supplier")) {
            constraints.gridx = 0;
            constraints.gridy = 4;
            formPanel.add(new JLabel("Address:"), constraints);
            constraints.gridx = 1;
            formPanel.add(addressField, constraints);
        }

        // add create button
        JButton createButton = new JButton("Create");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        formPanel.add(createButton, constraints);

        // add create button action listener
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // check none of the fields are empty
                if (nameField.getText().isEmpty() || idField.getText().isEmpty() || bankField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields");
                    return;
                }
                // Get form fields values
               try {
                    String name = nameField.getText();
                    int id = Integer.parseInt(idField.getText());
                    int bank = Integer.parseInt(bankField.getText());
                    Payment payment = (Payment) paymentComboBox.getSelectedItem();

                // Create empty result string
                String result = "";

                if (type.equals("Fixed Days Supplier")) {
                    ArrayList<Integer> days = getSelectedDays(daysPanel);
                    // Add fixed days supplier
                    result = supplierController.addFixedDaysSupplier(name, id, bank, payment, days);
                } else if (type.equals("On Order Supplier")) {
                    // Add on order supplier
                    result = supplierController.addOnOrderSupplier(name, id, bank, payment);
                } else if (type.equals("No Transport Supplier")) {
                    String address = addressField.getText();
                    // Add no transport supplier
                    result = supplierController.addNoTransportSupplier(name, id, bank, payment, address);
                }

                JOptionPane.showMessageDialog(frame, result);
                // Close frame
                frame.dispose();
               } catch (NumberFormatException ex) {
                   JOptionPane.showMessageDialog(frame, "Please enter valid values!");
               }
            }
        });

        // add form panel to frame
        frame.add(formPanel, BorderLayout.CENTER);

        // center the frame on the screen
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }


    // helper method for fixed days supplier: this function shows the days list in a panel of checkboxes and returns it
    private JPanel createDaysPanel() {
        // create days panel
        JPanel daysPanel = new JPanel();
        daysPanel.setLayout(new GridLayout(7, 1));

        // create days checkboxes
        JCheckBox sundayCheckBox = new JCheckBox("Sunday");
        JCheckBox mondayCheckBox = new JCheckBox("Monday");
        JCheckBox tuesdayCheckBox = new JCheckBox("Tuesday");
        JCheckBox wednesdayCheckBox = new JCheckBox("Wednesday");
        JCheckBox thursdayCheckBox = new JCheckBox("Thursday");
        JCheckBox fridayCheckBox = new JCheckBox("Friday");
        JCheckBox saturdayCheckBox = new JCheckBox("Saturday");

        // add days checkboxes to days panel
        daysPanel.add(sundayCheckBox);
        daysPanel.add(mondayCheckBox);
        daysPanel.add(tuesdayCheckBox);
        daysPanel.add(wednesdayCheckBox);
        daysPanel.add(thursdayCheckBox);
        daysPanel.add(fridayCheckBox);
        daysPanel.add(saturdayCheckBox);

        return daysPanel;
    }

    // helper method for fixed days supplier: this function returns the selected days from the days panel
    private ArrayList<Integer> getSelectedDays(JPanel daysPanel) {
        ArrayList<Integer> selectedDays = new ArrayList<>();

        Component[] components = daysPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) components[i];
                if (checkBox.isSelected()) {
                    selectedDays.add(i);
                }
            }
        }

        return selectedDays;
    }

        /*
    -------------------------------Remove supplier handlers and window functions----------------------------------------
     */

    public void handleRemoveSupplier() {
        // Initialize the frame and text field
        JFrame frame;
        JTextField idTextField;

        // Create the frame
        frame = new JFrame("Remove Supplier");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(250, 150);
        frame.setLayout(new BorderLayout());

        // Create the form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 5, 5)); // 3 rows, 1 column with spacing of 5 pixels
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding

        // Create the title label
        JLabel titleLabel = new JLabel("Please choose supplier:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust the font
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the title label
        formPanel.add(titleLabel);

        // Create a panel for the supplier ID label and text field
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel idLabel = new JLabel("Supplier ID:");
        idTextField = new JTextField(8); // Shorter text box
        idPanel.add(idLabel);
        idPanel.add(idTextField);
        formPanel.add(idPanel);

        // Create the delete button
        JButton deleteButton = new JButton("Remove from System");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idText = idTextField.getText();
                if (!idText.isEmpty()) {
                    try {
                        Integer.parseInt(idText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid Supplier ID.");
                        return;
                    }
                    int id = Integer.parseInt(idText);
                    String result = supplierController.removeSupplier(id);
                    JOptionPane.showMessageDialog(frame, result);
                    idTextField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                }
            }
        });
        // Add the delete button to the form panel
        formPanel.add(deleteButton);

        // Add the form panel to the frame
        frame.add(formPanel, BorderLayout.CENTER);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }




        /*
    -------------------------------Edit supplier handlers and window functions----------------------------------------
     */

    // handle edit supplier
    public void handleEditSupplier() {
        // Create the frame
        JFrame frame = new JFrame("Edit Supplier");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 135);
        frame.setLayout(new GridBagLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("What would you like to edit?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.anchor = GridBagConstraints.CENTER;
        titleConstraints.insets = new Insets(5, 0, 5, 0); // Reduce vertical spacing
        frame.add(titleLabel, titleConstraints);

        // Create the combo box panel
        JPanel comboPanel = new JPanel(new FlowLayout());
        GridBagConstraints comboConstraints = new GridBagConstraints();
        comboConstraints.gridx = 0;
        comboConstraints.gridy = 1;
        comboConstraints.anchor = GridBagConstraints.CENTER;
        comboConstraints.insets = new Insets(0, 0, 5, 0); // Reduce vertical spacing
        frame.add(comboPanel, comboConstraints);

        // Create the edit options combo box
        JComboBox<String> editOptionsComboBox = new JComboBox<>();
        editOptionsComboBox.addItem("Add Contact to Supplier");
        editOptionsComboBox.addItem("Remove Contact of Supplier");
        editOptionsComboBox.addItem("Change Supplier Name");
        editOptionsComboBox.addItem("Change Bank Account");
        editOptionsComboBox.addItem("Change Payment");
        comboPanel.add(editOptionsComboBox);

        // Create the edit button
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) editOptionsComboBox.getSelectedItem();
                if (selectedOption != null) {
                    openEditOptionWindow(selectedOption);
                }
            }
        });
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 2;
        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.insets = new Insets(0, 0, 5, 0); // Reduce vertical spacing
        frame.add(editButton, buttonConstraints);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void openEditOptionWindow(String option) {
        // create the edit option window based on the selected option
        JFrame optionFrame = new JFrame(option);
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionFrame.setLayout(new BorderLayout());

        // create the title label
        JPanel optionPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        optionPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7)); // Add padding to the panel
        optionFrame.add(optionPanel, BorderLayout.CENTER);

        JLabel idLabel = new JLabel("Supplier ID:");
        JTextField idField = new JTextField();
        optionPanel.add(idLabel);
        optionPanel.add(idField);

        // Add Contact to Supplier or Remove Contact of Supplier
        if (option.equals("Add Contact to Supplier") || option.equals("Remove Contact of Supplier")) {
            JLabel contactNameLabel = new JLabel("Contact Name:");
            JTextField contactNameField = new JTextField();
            JLabel phoneLabel = new JLabel("Phone:");
            JTextField phoneField = new JTextField();
            optionPanel.add(contactNameLabel);
            optionPanel.add(contactNameField);
            optionPanel.add(phoneLabel);
            optionPanel.add(phoneField);

            // Save button ActionListener
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Perform the appropriate action based on the selected option
                    // check user inserted valid input
                    if (idField.getText().isEmpty() || contactNameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(optionFrame, "Please fill all fields.");
                        return;
                    }
                    try {
                        Integer.parseInt(idField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(optionFrame, "Please enter a valid Supplier ID.");
                        return;
                    }
                    int id = Integer.parseInt(idField.getText());
                    String contactName = contactNameField.getText();
                    String phone = phoneField.getText();
                    String result = "";

                    if (option.equals("Add Contact to Supplier")) {
                        result = supplierController.addContactToSupplier(id, contactName, phone);
                    } else if (option.equals("Remove Contact of Supplier")) {
                        result = supplierController.removeContactOfSupplier(id, contactName, phone);
                    }

                    JOptionPane.showMessageDialog(optionFrame, result);
                    optionFrame.dispose();
                }
            });

            // Center the button within a panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(saveButton);
            optionFrame.add(buttonPanel, BorderLayout.SOUTH);
        } else if (option.equals("Change Supplier Name") || option.equals("Change Bank Account") || option.equals("Change Payment")) { // Change Supplier Name, Change Bank Account or Change Payment
            JLabel valueLabel;
            if (option.equals("Change Payment")) {
                valueLabel = new JLabel("New Payment Method:");
            }
            else if (option.equals("Change Bank Account")) {
                valueLabel = new JLabel("New Bank Account Number:");
            }
            else {
                valueLabel = new JLabel("New Supplier Name:");
            }
            JTextField valueField = new JTextField();
            optionPanel.add(valueLabel);
            optionPanel.add(valueField);

            // Save button ActionListener
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Perform the appropriate action based on the selected option
                    // check user inserted valid input
                    if (idField.getText().isEmpty() || valueField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(optionFrame, "Please fill all fields.");
                        return;
                    }
                    try {
                        Integer.parseInt(idField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(optionFrame, "Please enter a valid Supplier ID.");
                        return;
                    }
                    int id = Integer.parseInt(idField.getText());
                    String value = valueField.getText();
                    String result = "";

                    if (option.equals("Change Supplier Name")) {
                        result = supplierController.changeSupplierName(id, value);
                    } else if (option.equals("Change Bank Account")) {
                        // check user inserted valid input
                        try {
                            Integer.parseInt(value);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(optionFrame, "Please enter a valid Bank Account Number.");
                            return;
                        }
                        int bankAccount = Integer.parseInt(value);
                        result = supplierController.changeBankAccount(id, bankAccount);
                    } else if (option.equals("Change Payment")) {
                        // check user inserted valid input
                        try {
                            Payment.valueOf(value);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(optionFrame, "Please enter a valid Payment Method.");
                            return;
                        }
                        result = supplierController.changePayment(id, Payment.valueOf(value));
                    }

                    JOptionPane.showMessageDialog(optionFrame, result);
                    optionFrame.dispose();
                }
            });

            // Center the button within a panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(saveButton);
            optionFrame.add(buttonPanel, BorderLayout.SOUTH);
        }

        // center the option frame on the screen
        optionFrame.pack();
        optionFrame.setLocationRelativeTo(null);
        optionFrame.setVisible(true);
    }

        /*
    -------------------------------Add supply agreement handlers and window functions----------------------------------------
     */

    public void handleAddSupplyAgreement() {
        // Create the frame
        JFrame frame = new JFrame("Add New Supplier Agreement");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        JLabel supplierIDLabel = new JLabel("Supplier ID:");
        JTextField supplierIDField = new JTextField();

        JLabel productCodeLabel = new JLabel("Product Code:");
        JTextField productCodeField = new JTextField();

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();

        JLabel catalogLabel = new JLabel("Catalog:");
        JTextField catalogField = new JTextField();

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        inputPanel.add(supplierIDLabel);
        inputPanel.add(supplierIDField);
        inputPanel.add(productCodeLabel);
        inputPanel.add(productCodeField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(catalogLabel);
        inputPanel.add(catalogField);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);

        // Create the add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // check user inserted valid input
                if (supplierIDField.getText().isEmpty() || productCodeField.getText().isEmpty() || priceField.getText().isEmpty() || catalogField.getText().isEmpty() || amountField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }
                try {
                    Integer.parseInt(supplierIDField.getText());
                    Integer.parseInt(productCodeField.getText());
                    Double.parseDouble(priceField.getText());
                    Integer.parseInt(catalogField.getText());
                    Integer.parseInt(amountField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid input.");
                    return;
                }
                int supplierID = Integer.parseInt(supplierIDField.getText());
                int productCode = Integer.parseInt(productCodeField.getText());
                double price = Double.parseDouble(priceField.getText());
                int catalog = Integer.parseInt(catalogField.getText());
                int amount = Integer.parseInt(amountField.getText());

                String result = supplierController.addSupplyAgreement(supplierID, productCode, price, catalog, amount);
                JOptionPane.showMessageDialog(frame, result);
            }
        });

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        // Create empty border for padding between button and window border
        Border bottomBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);


        // Add the button to the SOUTH position of the frame with the bottom border
        frame.add(addButton, BorderLayout.SOUTH);
        ((JComponent) frame.getContentPane()).setBorder(bottomBorder);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);
        frame.pack();
    }

        /*
    -------------------------------Remove supply agreements handlers and window functions----------------------------------------
     */
    // Handle the remove supply agreement option
    public void handleRemoveSupplyAgreement() {
        // Create the frame
        JFrame frame = new JFrame("Remove Supply Agreement");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        // Create the panel for the input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        frame.add(inputPanel, BorderLayout.CENTER);
        // set some padding to the panel
        inputPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        // Create the labels
        JLabel supplierIDLabel = new JLabel("Supplier ID:");
        JLabel productCodeLabel = new JLabel("Product Code:");

        // Create the text fields
        JTextField supplierIDField = new JTextField();
        JTextField productCodeField = new JTextField();

        // Set preferred size for the text fields
        supplierIDField.setPreferredSize(new Dimension(150, 25));
        productCodeField.setPreferredSize(new Dimension(150, 25));

        // Add the labels and text fields to the panel
        inputPanel.add(supplierIDLabel);
        inputPanel.add(supplierIDField);
        inputPanel.add(productCodeLabel);
        inputPanel.add(productCodeField);

        // Create the remove button
        JButton removeButton = new JButton("Remove");
        // Create empty border for padding between button and window border
        Border bottomBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        // Add the button to the SOUTH position of the frame with the bottom border
        frame.add(removeButton, BorderLayout.SOUTH);
        ((JComponent) frame.getContentPane()).setBorder(bottomBorder);

        // Add action listener to the remove button
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Check if the user inserted valid input
                if (supplierIDField.getText().isEmpty() || productCodeField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }
                try {
                    Integer.parseInt(supplierIDField.getText());
                    Integer.parseInt(productCodeField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid input.");
                    return;
                }
                // Get the values from the text fields
                int supplierID = Integer.parseInt(supplierIDField.getText());
                int productCode = Integer.parseInt(productCodeField.getText());

                // Call the removeSupplyAgreement method
                String result = supplierController.removeSupplyAgreement(supplierID, productCode);

                // Show the result in a dialog box
                JOptionPane.showMessageDialog(frame, result);
            }
        });

        // Center the frame on the screen
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);
    }



    /*
-------------------------------Add discounts of orders handlers and window functions----------------------------------------
 */
    public void handleOrderDiscount() {
        // Create the frame
        JFrame frame = new JFrame("Add Discount by Order");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLayout(new BorderLayout());

        // Create the panel for the input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        frame.add(inputPanel, BorderLayout.CENTER);
        // set some padding to the panel
        inputPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        // Create the labels
        JLabel supplierIDLabel = new JLabel("Supplier ID:");
        JLabel discountValueLabel = new JLabel("Discount Value:");
        JLabel minPriceLabel = new JLabel("Minimum Price:");
        JLabel optionLabel = new JLabel("Discount Is By:");

        // Create the text fields
        JTextField supplierIDField = new JTextField();
        JTextField discountValueField = new JTextField();
        JTextField minPriceField = new JTextField();

        // Set the number of columns for the text fields
        supplierIDField.setColumns(10);
        discountValueField.setColumns(10);
        minPriceField.setColumns(10);

        // Create the radio buttons
        JRadioButton percentageButton = new JRadioButton("Percentage");
        JRadioButton priceButton = new JRadioButton("Price");

        // Create a button group for the radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(percentageButton);
        buttonGroup.add(priceButton);

        // Add the labels, text fields, and radio buttons to the panel
        inputPanel.add(supplierIDLabel);
        inputPanel.add(supplierIDField);
        inputPanel.add(discountValueLabel);
        inputPanel.add(discountValueField);
        inputPanel.add(minPriceLabel);
        inputPanel.add(minPriceField);
        inputPanel.add(optionLabel);
        inputPanel.add(percentageButton);
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(priceButton);

        // Create the add button
        JButton addButton = new JButton("Add");
        // Create empty border for padding between button and window border
        Border bottomBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

        // Add the button to the SOUTH position of the frame with the bottom border
        frame.add(addButton, BorderLayout.SOUTH);
        ((JComponent) frame.getContentPane()).setBorder(bottomBorder);

        // Add action listener to the add button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Check if the user inserted valid input
                if (supplierIDField.getText().isEmpty() || discountValueField.getText().isEmpty() || minPriceField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }
                try {
                    Integer.parseInt(supplierIDField.getText());
                    Double.parseDouble(discountValueField.getText());
                    Double.parseDouble(minPriceField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid input.");
                    return;
                }
                // Get the values from the text fields
                int supplierID = Integer.parseInt(supplierIDField.getText());
                double discountValue = Double.parseDouble(discountValueField.getText());
                double minPrice = Double.parseDouble(minPriceField.getText());
                int option = percentageButton.isSelected() ? 1 : 2;

                // Call the addDiscountByOrder method
                String result = supplierController.addDiscountByOrder(supplierID, discountValue, minPrice, option);

                // Show the result in a dialog box
                JOptionPane.showMessageDialog(frame, result);
            }
        });

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        frame.pack();
    }

    /*
------------------------------- Set delivery dates for suppliers handlers and window functions----------------------------------------
 */
    public void handleDeliveryDate() {
        JFrame optionFrame = new JFrame("Set Delivery Date");
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionFrame.setSize(200, 150);
        optionFrame.setLayout(new FlowLayout());

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.PAGE_AXIS));

        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"On Order Supplier", "No Transport Supplier", "Fixed Days Supplier"});

        // Create the choose button
        JButton continueButton = new JButton("Continue");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(continueButton);

        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (typeComboBox.getSelectedItem().equals("Fixed Days Supplier")) {
                    fixedSuppliersDaysEditor(optionPanel);
                }
                else {
                    handleTypeSelection(optionPanel, typeComboBox.getSelectedItem());
                }
            }
        });

        optionPanel.add(typeComboBox);
        optionPanel.add(buttonPanel);
        optionFrame.add(optionPanel);
        optionFrame.setLocationRelativeTo(null);
        optionFrame.setVisible(true);
        optionFrame.setResizable(false);

        optionFrame.pack();
    }


    // after clicking continue and choosing the type of supplier, this function will be called to show the relevant fields to the user
    private void handleTypeSelection(JPanel optionPanel, Object selectedType) {
        // remove all the components from the panel
        optionPanel.removeAll();
        // add the title label
        if (selectedType.equals("On Order Supplier")) {
            addLabelAndTextField(optionPanel, "Supplier ID:");
            addLabelAndTextField(optionPanel, "Number of Days:");
        } else if (selectedType.equals("No Transport Supplier")) {
            addLabelAndTextField(optionPanel, "Supplier ID:");
            addDateChooser(optionPanel, "Date:");
        }
        // add the save button
        JButton saveButton = new JButton("Save");
        // add action listener to the save button
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSave(selectedType, optionPanel);
            }
        });

        // add the save button to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        optionPanel.add(buttonPanel);
        // revalidate and repaint the panel
        optionPanel.revalidate();
        optionPanel.repaint();
        // get frame
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(optionPanel);
        // pack the frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }


    // this function adds a label and a text field to the panel based on given label
    private void addLabelAndTextField(JPanel optionPanel, String label) {
        JLabel jLabel = new JLabel(label);
        JTextField jTextField = new JTextField();
        optionPanel.add(jLabel);
        optionPanel.add(jTextField);
    }

    // this function adds a label and a date chooser to the panel based on given label
    private void addDateChooser(JPanel optionPanel, String label) {
        JLabel jLabel = new JLabel(label);
        SpinnerModel dateModel = new SpinnerDateModel();
        JSpinner jSpinner = new JSpinner(dateModel);
        optionPanel.add(jLabel);
        optionPanel.add(jSpinner);
    }

    // this function saves the changes in the database based on the selected type of supplier
    private void handleSave(Object selectedType, JPanel optionPanel) {
        // check if the user inserted valid input
        if (getTextFieldValue(optionPanel, "Supplier ID:").isEmpty()) {
            JOptionPane.showMessageDialog(optionPanel, "Please fill all fields.");
            return;
        }
        try {
            Integer.parseInt(getTextFieldValue(optionPanel, "Supplier ID:"));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(optionPanel, "Please enter valid input.");
            return;
        }
        int id = Integer.parseInt(getTextFieldValue(optionPanel, "Supplier ID:"));

        if (selectedType.equals("On Order Supplier")) {
            // check if the user inserted valid input
            if (getTextFieldValue(optionPanel, "Number of Days:").isEmpty()) {
                JOptionPane.showMessageDialog(optionPanel, "Please fill all fields.");
                return;
            }
            try {
                Integer.parseInt(getTextFieldValue(optionPanel, "Number of Days:"));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(optionPanel, "Please enter valid input.");
                return;
            }
            int days = Integer.parseInt(getTextFieldValue(optionPanel, "Number of Days:"));
            String result = supplierController.setNextDeliveryDateOfOnOrderSupplier(id, days);
            JOptionPane.showMessageDialog(optionPanel, result);
        } else if (selectedType.equals("No Transport Supplier")) {
            int day = getSelectedDay(optionPanel, "Date:");
            int month = getSelectedMonth(optionPanel, "Date:");
            int year = getSelectedYear(optionPanel, "Date:");
            String result = supplierController.setNextDeliveryDateOfNoTransportSupplier(id, day, month, year);
            JOptionPane.showMessageDialog(optionPanel, result);
        }
    }

    public void fixedSuppliersDaysEditor(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        // Create the combo box with options
        String[] options = {"Add Ship Day", "Remove Ship Day"};
        JComboBox<String> comboBox = new JComboBox<>(options);

        // Create the continue button
        JButton continueButton = new JButton("Continue");


        // Add action listener to the continue button
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();
                panel.removeAll();
                panel.setLayout(new GridLayout(3, 2)); // 3 rows, 2 columns

                // set labels and text fields for supplier id and day number
                JLabel supplierIdLabel = new JLabel("Supplier ID:");
                JTextField supplierIdTextField = new JTextField();
                JLabel dayNumberLabel = new JLabel("Day Number:");
                JTextField dayNumberTextField = new JTextField();
                JButton saveButton = new JButton("Save");

                // add the save button
                saveButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String supplierIdText = supplierIdTextField.getText();
                        String dayNumberText = dayNumberTextField.getText();

                        if (supplierIdText.isEmpty() || dayNumberText.isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "Please enter both Supplier ID and Day Number.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // try to parse the supplier id and day number and check if they are valid
                            try {
                                Integer.parseInt(supplierIdText);
                                Integer.parseInt(dayNumberText);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(panel, "Please enter valid Supplier ID.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            int id = Integer.parseInt(supplierIdText);
                            int dayNumber = Integer.parseInt(dayNumberText);

                            // Perform actions based on the selected option
                            if (selectedOption.equals("Add Ship Day")) {
                                // Call the add method
                                String result = supplierController.addShipDayToFixedSupplier(id, dayNumber);
                                JOptionPane.showMessageDialog(panel, result);
                            } else if (selectedOption.equals("Remove Ship Day")) {
                                // Call the remove method
                                String result = supplierController.removeShipDayFromFixedSupplier(id, dayNumber);
                                JOptionPane.showMessageDialog(panel, result);
                            }
                        }
                    }
                });

                // Add the components to the panel
                panel.add(supplierIdLabel);
                panel.add(supplierIdTextField);
                panel.add(dayNumberLabel);
                panel.add(dayNumberTextField);
                panel.add(saveButton);

                // Repaint the panel to update the changes
                panel.revalidate();
                panel.repaint();

                // Pack the frame
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });

        // Add the combo box and continue button to the panel
        panel.add(comboBox, BorderLayout.NORTH);
        panel.add(continueButton, BorderLayout.SOUTH);

        // Repaint the panel to update the changes
        panel.revalidate();
        panel.repaint();
    }



    // this function returns the value of the text field based on the given label
    private String getTextFieldValue(JPanel optionPanel, String label) {
        Component[] components = optionPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            Component component = components[i];

            if (component instanceof JLabel && ((JLabel) component).getText().equals(label)) {
                JTextField textField = (JTextField) components[i + 1];
                return textField.getText();
            }
        }

        return null;
    }

    // this function returns the selected day from the date chooser based on the given label
    private int getSelectedDay(JPanel optionPanel, String label) {
        Component[] components = optionPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            Component component = components[i];

            if (component instanceof JLabel && ((JLabel) component).getText().equals(label)) {
                JSpinner spinner = (JSpinner) components[i + 1];
                SpinnerDateModel dateModel = (SpinnerDateModel) spinner.getModel();
                return dateModel.getDate().getDate();
            }
        }

        return -1;
    }

    // this function returns the selected month from the date chooser based on the given label
    private int getSelectedMonth(JPanel optionPanel, String label) {
        Component[] components = optionPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            Component component = components[i];

            if (component instanceof JLabel && ((JLabel) component).getText().equals(label)) {
                JSpinner spinner = (JSpinner) components[i + 1];
                SpinnerDateModel dateModel = (SpinnerDateModel) spinner.getModel();
                return dateModel.getDate().getMonth() + 1; // Month is 0-based
            }
        }

        return -1;
    }

    // this function returns the selected year from the date chooser based on the given label
    private int getSelectedYear(JPanel optionPanel, String label) {
        Component[] components = optionPanel.getComponents();

        for (int i = 0; i < components.length; i++) {
            Component component = components[i];

            if (component instanceof JLabel && ((JLabel) component).getText().equals(label)) {
                JSpinner spinner = (JSpinner) components[i + 1];
                SpinnerDateModel dateModel = (SpinnerDateModel) spinner.getModel();
                return dateModel.getDate().getYear() + 1900; // Year is based on 1900
            }
        }

        return -1;
    }

    /*
    ------------------------------------Print Supply Agreements------------------------------------
     */

    // this function handles the print supply agreements option
    public void handlePrintSupplyAgreements() {
        // Create the option frame
        JFrame optionFrame = new JFrame("Print Supply Agreement");
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create the option panel
        JPanel optionPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel jLabel = new JLabel("Supplier ID:");
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(150, 20));
        // create the print button
        JButton printButton = new JButton("Print");

        // add the print button and set the action listener
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // try to parse the supplier id and check if it is valid
                try {
                    Integer.parseInt(jTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(optionFrame, "Please enter valid Supplier ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(jTextField.getText());
                String printResult;
                try {
                    printResult = supplierController.printAllSupplyAgreements(id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                String message;
                if (printResult != null) {
                    message =  printResult;
                } else {
                    message = "Failed to print Supply Agreement.";
                }

                JOptionPane.showMessageDialog(optionFrame, message);
            }
        });

        // add the components to the panel
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        optionPanel.add(jLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        optionPanel.add(jTextField, gridBagConstraints);

        buttonPanel.add(printButton);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        optionPanel.add(buttonPanel, gridBagConstraints);

        optionFrame.getContentPane().add(optionPanel);
        optionFrame.setLocationRelativeTo(null);
        optionFrame.setVisible(true);
        optionFrame.setResizable(false);
        optionFrame.pack();
    }

/*
------------------------------------------Add Periodic Order------------------------------------------
 */

    // function that opens window for modification of periodic order
    public void openPeriodicOrderWindow(String func) {
        JFrame frame = new JFrame();
        if (func == "add") {
            frame.setTitle("Add Periodic Order");
        } else if (func == "remove") {
            frame.setTitle("Remove Periodic Order");
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        frame.setContentPane(mainPanel);
        mainPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns, vertical and horizontal gaps
        mainPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));

        JLabel branchLabel = createLabel("Branch:");
        JComboBox<String> branchComboBox = createComboBox(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
        JLabel supplierIDLabel = createLabel("Supplier ID:");
        JTextField supplierIDTextField = createTextField(10);
        JLabel productCodeLabel = createLabel("Product Code:");
        JTextField productCodeTextField = createTextField(10);
        JLabel amountLabel = createLabel("Amount:");
        JTextField amountTextField = createTextField(10);
        JLabel dayLabel = createLabel("Day:");
        JRadioButton[] dayRadioButtons = createDayRadioButtons();

        JPanel dayPanel = new JPanel();
        dayPanel.setLayout(new GridLayout(0, 1, 5, 5)); // Column layout for radio buttons
        for (JRadioButton radioButton : dayRadioButtons) {
            dayPanel.add(radioButton);
        }

        JButton submitButton = createButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if all fields are filled
                if (branchComboBox.getSelectedItem() == null || supplierIDTextField.getText().equals("") ||
                        productCodeTextField.getText().equals("") || amountTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }
                // check if supplier id and product code are valid integers
                try {
                    Integer.parseInt(supplierIDTextField.getText());
                    Integer.parseInt(productCodeTextField.getText());
                    Integer.parseInt(amountTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid values.");
                    return;
                }
                String branchID = (String) branchComboBox.getSelectedItem();
                int supplierID = Integer.parseInt(supplierIDTextField.getText());
                int productCode = Integer.parseInt(productCodeTextField.getText());
                int selectedDay = getSelectedDayFromRadioButtons(dayRadioButtons);
                int amount = Integer.parseInt(amountTextField.getText());
                // if no day was selected, show an error message
                if (selectedDay == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a day.");
                    return;
                }
                // try to modify the periodic order according to the function
                if (func == "add") {
                    String message = orderController.addPeriodicOrder(branchID, supplierID, productCode, amount, selectedDay);
                    JOptionPane.showMessageDialog(frame, message);
                } else if (func == "remove") {
                    String message = orderController.removePeriodicOrder(branchID, supplierID, productCode, selectedDay);
                    JOptionPane.showMessageDialog(frame, message);
                }
            }
        });

        addComponentsToFrame(
                frame,
                branchLabel,
                branchComboBox,
                supplierIDLabel,
                supplierIDTextField,
                productCodeLabel,
                productCodeTextField,
                amountLabel,
                amountTextField,
                dayLabel,
                dayPanel,
                dayRadioButtons[0],
                dayRadioButtons[1],
                dayRadioButtons[2],
                dayRadioButtons[3],
                dayRadioButtons[4],
                dayRadioButtons[5],
                dayRadioButtons[6],
                submitButton
        );

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
    }

    // this function handles the add periodic order option
    public void handleAddPeriodicOrder() {
        openPeriodicOrderWindow("add");
    }

    private int getSelectedDayFromRadioButtons(JRadioButton[] dayRadioButtons) {
        for (int i = 0; i < 7; i++) {
            if (dayRadioButtons[i].isSelected()) {
                return i; // Return the numeric value of the selected day
            }
        }
        return -1; // Return a default value or handle the case when no day is selected
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        return textField;
    }

    private JComboBox<String> createComboBox(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        return comboBox;
    }

    private JRadioButton[] createDayRadioButtons() {
        JRadioButton[] dayRadioButtons = new JRadioButton[7];
        ButtonGroup dayButtonGroup = new ButtonGroup();
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        for (int i = 0; i < 7; i++) {
            dayRadioButtons[i] = new JRadioButton(days[i]);
            dayButtonGroup.add(dayRadioButtons[i]);
        }

        return dayRadioButtons;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        return button;
    }

    private void addComponentsToFrame(JFrame frame, JComponent... components) {
        for (JComponent component : components) {
            frame.add(component);
        }
    }

/*
------------------------------------------Remove Periodic Order------------------------------------------
 */
    public void handleDeletePeriodicOrder() {
        openPeriodicOrderWindow("remove");
    }

/*
------------------------------------------Update Order Status------------------------------------------
 */
    public void handleUpdateOrderStatus() {
        JFrame mainFrame;
        JComboBox<String> optionComboBox;
        JButton continueButton;
        mainFrame = new JFrame("Order Options");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel optionPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        optionComboBox = new JComboBox<>();
        optionComboBox.addItem("Cancel Order");
        optionComboBox.addItem("Confirm Order");

        continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) optionComboBox.getSelectedItem();
                mainFrame.dispose();
                openUpdateWindow(selectedOption);
            }
        });

        optionPanel.add(optionComboBox);
        buttonPanel.add(continueButton);

        mainPanel.add(optionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    private void openUpdateWindow(String selectedOption) {

        JFrame updateFrame = new JFrame("Update Order");
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel updatePanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel orderNumberLabel = new JLabel("Order Number:");
        JTextField orderNumberField = new JTextField(10);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // check if the input is valid
                if (orderNumberField.getText().equals("")) {
                    JOptionPane.showMessageDialog(updateFrame, "Please fill all the fields.");
                    return;
                }
                try {
                    Integer.parseInt(orderNumberField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(updateFrame, "Please enter a valid order number.");
                    return;
                }
                int orderNumber = Integer.parseInt(orderNumberField.getText());

                if (selectedOption.equals("Cancel Order")) {
                    String result = orderController.cancelOrder(orderNumber);
                    JOptionPane.showMessageDialog(updateFrame, result);
                } else if (selectedOption.equals("Confirm Order")) {
                    String result = orderController.confirmOrder(orderNumber);
                    JOptionPane.showMessageDialog(updateFrame, result);
                }

                updateFrame.dispose();
            }
        });

        inputPanel.add(orderNumberLabel);
        inputPanel.add(orderNumberField);
        buttonPanel.add(updateButton);

        updatePanel.add(inputPanel, BorderLayout.CENTER);
        updatePanel.add(buttonPanel, BorderLayout.SOUTH);

        updateFrame.getContentPane().add(updatePanel);
        updateFrame.pack();
        updateFrame.setLocationRelativeTo(null);
        updateFrame.setResizable(false);
        updateFrame.setVisible(true);
    }

    /*
    ------------------------------------------Print Orders------------------------------------------
     */
    public void handlePrintOrder() {
        JFrame mainFrame = new JFrame("Order Options");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel optionPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        JComboBox<String> optionComboBox = new JComboBox<>();
        optionComboBox.addItem("Print Orders by Supplier");
        optionComboBox.addItem("Print All Orders");

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) optionComboBox.getSelectedItem();
                if (selectedOption.equals("Print Orders by Supplier")) {
                    mainFrame.dispose();
                    printOrdersBySupplier();
                } else if (selectedOption.equals("Print All Orders")) {
                    mainFrame.dispose();
                    printAllOrders();
                }
            }
        });

        optionPanel.add(optionComboBox);
        buttonPanel.add(continueButton);

        mainPanel.add(optionPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.getContentPane().add(mainPanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void printOrdersBySupplier() {

        JFrame reportFrame = new JFrame("Order Report");
        // set the size of the frame
        reportFrame.setSize(600, 600);
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel reportPanel = new JPanel(new BorderLayout());
        JTextArea reportTextArea = new JTextArea(10, 30);
        reportTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        try {
            int supplierID = Integer.parseInt(JOptionPane.showInputDialog(reportFrame, "Enter Supplier ID:"));

            String report = orderController.getOrdersOfSupplier(supplierID);
            reportTextArea.setText(report);

            reportPanel.add(scrollPane, BorderLayout.CENTER);

            reportFrame.getContentPane().add(reportPanel);
            reportFrame.setLocationRelativeTo(null);
            reportFrame.setResizable(false);
            reportFrame.setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(reportFrame, "Please enter a valid supplier ID.");
        }
    }

    private void printAllOrders() {

        JFrame reportFrame = new JFrame("Order Report");
        // set the size of the frame
        reportFrame.setSize(600, 600);
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel reportPanel = new JPanel(new BorderLayout());
        JTextArea reportTextArea = new JTextArea(10, 30);
        reportTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportTextArea);

        String report = orderController.getAllOrders();
        reportTextArea.setText(report);

        reportPanel.add(scrollPane, BorderLayout.CENTER);

        reportFrame.getContentPane().add(reportPanel);
        //reportFrame.pack();
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setResizable(false);
        reportFrame.setVisible(true);
    }
}
