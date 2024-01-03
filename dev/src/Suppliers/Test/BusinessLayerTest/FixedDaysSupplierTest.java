package Suppliers.Test.BusinessLayerTest;
import Suppliers.BusinessLayer.FixedDaysSupplier;
import Suppliers.BusinessLayer.Payment;
import Suppliers.BusinessLayer.ShipmentDays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedDaysSupplierTest { // testing methods implemented in fixed days supplier class

    // create a new fixed days supplier that will be tested on methods
    FixedDaysSupplier fd_supplier = new FixedDaysSupplier("lior", 209259993, 1234567, Payment.Credit);

    @Test
    void addShipDay() {
        // adds shipping days
        fd_supplier.addShipDay(5);
        fd_supplier.addShipDay(1);
        // tests if method returns correct days
        assertEquals(true, fd_supplier.canShipOnDay(ShipmentDays.Friday));
        assertEquals(false, fd_supplier.canShipOnDay(ShipmentDays.Sunday));
        assertEquals(true, fd_supplier.canShipOnDay(ShipmentDays.Monday));
    }

    @Test
    void canShipOnDay() {
        // adds shipping day
        fd_supplier.addShipDay(0);
        // tests if method returns correct days
        assertEquals(true, fd_supplier.canShipOnDay(ShipmentDays.Sunday));
    }

    @Test
    void removeShipDay() {
        // adds shipping days
        fd_supplier.addShipDay(5);
        fd_supplier.addShipDay(1);
        // tests if method returns correct days
        assertEquals(true, fd_supplier.canShipOnDay(ShipmentDays.Friday));
        assertEquals(true, fd_supplier.canShipOnDay(ShipmentDays.Monday));
        // remove friday from shipping days
        fd_supplier.removeShipDay(5);
        // tests if method returns false as it should
        assertEquals(false, fd_supplier.canShipOnDay(ShipmentDays.Friday));
    }
}