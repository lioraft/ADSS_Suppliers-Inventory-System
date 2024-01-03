package Suppliers.BusinessLayer;


import java.time.LocalDate;

public class NoTransportSupplier extends Supplier {
    // description of class: this is a class that represents the suppliers of superli, who can't deliver their orders, and needs delivery assistance.
    // it inherits the class supplier. in addition to parent class attributes, it has an address, which is the address that the orders will be picked up from.
    private String address; // pick up address
    private LocalDate nextDeliveryDate;

    public NoTransportSupplier(String name, int id, int bank, Payment pay, String shipaddress) { // constructor
        super(name, id, bank, pay);
        address = shipaddress;
        // set as null when first initialize
        nextDeliveryDate = null;
    }

    // getter and setter for address
    public String getAddress(){return address;}
    public void setAddress(String newAddress) {address=newAddress;}

    // function that takes in arguments for next delivery arrangement, and set it in next delivery date
    public void setNextDeliveryDate(int day, int month, int year) {
        nextDeliveryDate = LocalDate.of(year, month, day);
    }


    // function that returns the next shipping date - because we arrange delivery, the date is received by other module in system
    @Override
    public LocalDate getNextShippingDate() {
        return nextDeliveryDate;
    }
}
