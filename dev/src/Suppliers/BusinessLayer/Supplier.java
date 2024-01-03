package Suppliers.BusinessLayer;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Supplier {

    // description of class: this is an abstract class that represents the suppliers of superli. each supplier has name, id (private company number),
    // number of bank account, preferred method of payment, and a list of supply agreements made with the supplier.
    protected String supplier_name; // supplier's name
    protected int supplier_id; // supplier's id
    protected int bank_account; // number of bank accounts
    protected Payment pay_system; // preferred method of payment
    protected ArrayList<SupplyAgreement> agreements; // list of supply agreements made with the supplier
    protected ArrayList<DiscountByOrder> discountsByOrder; // list of discounts relevant to entire order from supplier
    protected ArrayList<Order> ordersHistory; // list of orders made from supplier
    protected ArrayList<SupplierContact> contacts; // list of supplier's contacts


    public Supplier(String name, int id, int bank, Payment pay) { // constructor
        supplier_name = name;
        supplier_id = id;
        bank_account = bank;
        pay_system = pay;
        agreements = new ArrayList<SupplyAgreement>();
        discountsByOrder = new ArrayList<DiscountByOrder>();
        ordersHistory = new ArrayList<Order>();
        contacts = new ArrayList<SupplierContact>();
    }

    // abstract method that returns the closest possible shipping date from supplier
    public abstract LocalDate getNextShippingDate();

    // getters for the attributes of the class
    public int getPrivateCompanyNumber() {return this.supplier_id;}
    public String getName() {return this.supplier_name;}
    public int getBankAccount() {return this.bank_account;}
    public ArrayList<Order> getOrdersHistory() {return ordersHistory;}
    public ArrayList<DiscountByOrder> getDiscounts() {return discountsByOrder;}

    // function that takes in number of order and returns it. if cant find, returns null
    public Order getOrder(int order_number) {
        for (int i = 0; i < ordersHistory.size(); i++) {
            if (ordersHistory.get(i).getOrderNumber() == order_number) {
                return ordersHistory.get(i);
            }
        }
        return null;
    }

    // function takes in information of new agreement, initializes it and adds the new agreement to list of agreements, and returns the agreement created
    public SupplyAgreement addAgreement (int code, double price, int catalog, int amount) {
        SupplyAgreement newAgreement = new SupplyAgreement(code, price, catalog, amount);
        agreements.add(newAgreement);
        return newAgreement;
    }

    // return specific agreement from list of agreements based on product code. if cant find, return null
    public SupplyAgreement getAgreement (int product_code) {
        for (int i = 0; i < agreements.size(); i++) {
            if (agreements.get(i).getProductCode() == product_code) {
                return agreements.get(i);
            }
        }
        return null;
    }

    // remove agreement from list of agreements. if finds and deletes, return true. else, return false
    public boolean removeAgreement (int product_code) {
        for (int i = 0; i < agreements.size(); i++) {
            if (agreements.get(i).getProductCode() == product_code) {
                agreements.remove(i);
                return true;
            }
        }
        return false;
    }

    // function that returns the list of agreements
    public ArrayList<SupplyAgreement> getAllAgreements() {
        return agreements;
    }

    // function that prints all the supply agreements of supplier
    public void printSupplyAgreements() {
        for (SupplyAgreement sa : agreements) {
            System.out.println(sa.toString());
        }
    }

    // add new order discount to list of discounts. if argument is null, returns false. else, returns true
    public boolean addOrderDiscount(DiscountByOrder discount) {
        if (discount == null)
            return false;
        discountsByOrder.add(discount);
        return true;

    }

    // function takes in information of new order, initializes it, adds the new order to history of orders, and returns the order created
    public Order addNewOrder(String destination) {
        Order order = new Order(this.getNextShippingDate(), destination);
        ordersHistory.add(order);
        return order;
    }

    // add order that was already made
    public Order addExistingOrder(LocalDate date, String destination) {
        Order order = new Order(date, destination);
        ordersHistory.add(order);
        return order;
    }

    // function that applies order discount on the order by using a function from order class
    public void applyOrderDiscountsOnOrder(Order order) {
        order.applyOrderDiscount(discountsByOrder);
    }

    // function takes in information of new contact, initializes it, adds the new contact to list of contacts, and returns the contact created
    public SupplierContact addContact(String name, String phone) {
        SupplierContact contact = new SupplierContact(name, phone);
        contacts.add(contact);
        return contact;
    }

    // function takes in information of contact and removes it. if deletes, returns true. if can't find contact in list, returns false;
    public boolean removeContact(String name, String phone) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getContactName().equals(name) && contacts.get(i).getPhone().equals(phone)) {
                contacts.remove(i);
                return true;
            }
        }
        return false;
    }

    // function that takes in the product code and number of units, and returns the list price the supplier offers. if supplier doesn't
    // sell product, return -1
    public double getListPriceOfProducts(int product_code, int units) {
        for (int i = 0; i < agreements.size(); i++) {
            if (product_code == agreements.get(i).getProductCode())
                return agreements.get(i).getListPriceBeforeDiscounts(units);
        }
        return -1;
    }

    // function that takes in the product code, and returns the catalog number of the product in the supplier's systems. if supplier doesn't
    // sell product, return -1
    public int getCatalogNumber(int product_code) {
        for (int i = 0; i < agreements.size(); i++) {
            if (product_code == agreements.get(i).getProductCode())
                return agreements.get(i).getCatalogCode();
        }
        return -1;
    }

    // function that takes in the product code, and returns the max amount of units the supplier can deliver. if supplier doesn't
    // sell product, return -1
    public int getMaxAmountOfUnits(int product_code) {
        for (int i = 0; i < agreements.size(); i++) {
            if (product_code == agreements.get(i).getProductCode())
                return agreements.get(i).getMaxAmount();
        }
        return -1;
    }

    // function that takes in the product code and number of units, and returns the minimal price the supplier offers after discount. if supplier doesn't
    // sell product, return -1
    public double getBestPossiblePrice(int product_code, int units) {
        for (int i = 0; i < agreements.size(); i++) {
            if (product_code == agreements.get(i).getProductCode()) {
               double minimal_price = agreements.get(i).getMinimalPrice(units);
               return minimal_price;
            }
        }
        return -1;
    }

    // function takes in name of contact of supplier, and returns it. if can't find, returns null
    public SupplierContact getContact(String name, String phone) {
        for (int i = 0; i < contacts.size(); i++) {
            if (name.equals(contacts.get(i).getContactName()) && phone.equals(contacts.get(i).getPhone())) {
                return contacts.get(i);
            }
        }
        return null;
    }

    // setters for attributes
    public void setName(String name) {supplier_name = name;} // set name
    public void setBankAccount(int bank) {bank_account = bank;} // set bank account
    public void setPaymentMethod(Payment pay) {pay_system = pay;} // set payment method

    // print supplier's order history
    public void printOrderHistory() {
        for (int i = 0; i < ordersHistory.size(); i++) {
            System.out.println(ordersHistory.get(i).toString());
        }
    }
}
