package Suppliers.BusinessLayer;


public class SupplierContact {
    // description of class: this class represents a contact of a supplier. it has name of contact, and its phone number.
    private String contact_name; // contact name
    private String cellphone; // phone number

    public SupplierContact(String name, String phone) { // constructor
        contact_name = name;
        cellphone = phone;
    }

    // getters of attributes
    public String getContactName() {return this.contact_name;}
    public String getPhone() {return this.cellphone;}

}
