package Suppliers.BusinessLayer;


import java.time.LocalDate;

public class OnOrderSupplier extends Supplier {
    // description of class: this is a class that represents the suppliers of superli, who delivers whenever an order is made.
    // it inherits the class supplier.
    int numberOfDaysToNextOrder; // the number of dates til next order

    public OnOrderSupplier(String name, int id, int bank, Payment pay) {
        super(name, id, bank, pay);
        numberOfDaysToNextOrder = 0;
    }

    public void setNumberOfDaysToNextOrder(int days) {
        numberOfDaysToNextOrder = days;
    }


    // function that takes input from user of days until shipping, updates the date and returns it
    @Override
    public LocalDate getNextShippingDate() {
        // return date
        return LocalDate.now().plusDays(numberOfDaysToNextOrder);
    }
}
