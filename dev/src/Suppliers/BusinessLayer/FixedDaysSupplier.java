package Suppliers.BusinessLayer;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;

public class FixedDaysSupplier extends Supplier {

    // description of class: this is a class that represents the suppliers of superli, who delivers orders on fixed days. it inherits the class supplier.
    // in addition to parent class, it has a list of days. these days are the delivery days of the supplier.
    private ArrayList<ShipmentDays> days; // shipping days

    public FixedDaysSupplier(String name, int id, int bank, Payment pay) { // constructor
        super(name, id, bank, pay);
        days = new ArrayList<ShipmentDays>();
    }

    // function that takes in the enum of the new day, and adds it to the list of days
    public void addShipDay(int dayNumber) {
        ShipmentDays newDay = ShipmentDays.values()[dayNumber];
        days.add(newDay);
    }

    // function that takes in the string of the new day, and removes it from the list of days
    public void removeShipDay(int dayNumber) {
        // iterate list of days. when finds, delete
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).toString().equals(ShipmentDays.values()[dayNumber].toString()))
                days.remove(i);
        }
    }

    // function that takes in a day and returns true if supplier ships on that day
    public boolean canShipOnDay(ShipmentDays day) {
        for (ShipmentDays shipday : days) {
            if (shipday == day)
                return true;
        }
        return false;
    }


    // function that returns the closest date of the days in the list of days
    @Override
    public LocalDate getNextShippingDate() {
        LocalDate closest_date = null;
        // today's date
        LocalDate today = LocalDate.now();
        Date date = new java.util.Date();
        // get dates of all closest shipment days
        for (int i = 0; i < days.size(); i++) {
            // find closest date of the day on the list
            LocalDate current_day = today.with(TemporalAdjusters.next(DayOfWeek.valueOf(days.get(i).toString().toUpperCase())));
            // convert to Date object
            Date date_of_current_day = java.sql.Date.valueOf(String.valueOf(current_day));
            // get the distance between today and the date
            long distance = Math.abs(date_of_current_day.getTime() - date.getTime());
            // if closest day is null or closest day is further than current day checked
            if (closest_date == null || Math.abs(java.sql.Date.valueOf(String.valueOf(closest_date)).getTime() - date.getTime()) > distance) {
                // convert date to localdate
                closest_date = LocalDate.of(date_of_current_day.getYear()+1900, date_of_current_day.getMonth()+1, date_of_current_day.getDate());
            }
        }
        return closest_date;
    }
}
