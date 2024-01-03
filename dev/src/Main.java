import Inventory.PresentationLayer.CLIInventory;
import Suppliers.PresentationLayer.CLI;

import java.util.Scanner;

// this class is the main after integration of suppliers and inventory
public class Main {
    public static void main(String[] args) {
        // ask for user input if he wants the inventory or the suppliers
        if(args.length < 2){
            System.out.println("Invalid input");
            return;
        }
        switch (args[0].toUpperCase()) {
            case "CLI":
                // operate the CLI
                CLI1 cli = new CLI1(args[1]);
                break;
            case "GUI":
                // operate the GUI
                Inventory_Suppliers.PresentationLayer.View.MainWindow gui = new Inventory_Suppliers.PresentationLayer.View.MainWindow(args[1]);
                gui.setVisible(true);
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
}

class CLI1{
    public CLI1(String args){
        switch (args.toLowerCase()) {
            case "storemanager":
                // operate the CLI
                CLIInventory cliInventory = new CLIInventory();
                CLI cli = new CLI();
                System.out.println("Please enter 1 for inventory or 2 for suppliers");
                Scanner cliScanner = new Scanner(System.in);
                int choice = cliScanner.nextInt();
                if (choice == 1) {
                    cliInventory.start();
                }else {
                    cli.printMenu();
                }
                break;
            case "stockkeeper":
                // suppliers
                CLIInventory cliInventory1 = new CLIInventory();
                cliInventory1.printMenu();
                break;
            case "suppliermanager":
            // suppliers
            CLI cli1 = new CLI();
            cli1.printMenu();
            break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
}
