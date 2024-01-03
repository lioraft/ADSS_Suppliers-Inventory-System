package Inventory.BusinessLayer;

import java.time.LocalDate;
import java.util.ArrayList;

public class Category {
    //dictionary contains the product by category
   // Dictionary<Integer,Product> productByCategory;
    private String name;
    private int id;
    private LocalDate startDiscount;
    private LocalDate endDiscount;
    private float discount;
    public Category(String name, int id) {
        this.name = name;
        this.id = id;
    }


    public void setDiscount(String start, String end, float discount){
        try{
            LocalDate startDiscount = LocalDate.parse(start);
            LocalDate endDiscount = LocalDate.parse(end);
            this.discount=discount;
            this.startDiscount=startDiscount;
            this.endDiscount=endDiscount;
        }
        catch (Exception e){
            System.out.println("Error in date format");
        }
    }
    public float getDiscount() {
        if(discount==0)
            return 0;
        LocalDate now = LocalDate.now();
        if(now.isAfter(startDiscount) && now.isBefore(endDiscount))
            return discount;
        else
            return 0;

    }
    public String toString(){
        return "Category name: "+name+" Category id: "+id;
    }

}
