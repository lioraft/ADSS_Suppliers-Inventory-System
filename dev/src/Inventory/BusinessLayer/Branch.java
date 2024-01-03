package Inventory.BusinessLayer;

public enum Branch {
    INVENTORY("Inventory"),
    A("Site A"),
    B("Site B"),
    C("Site C"),
    D("Site D"),
    E("Site E"),
    F("Site F"),
    G("Site G"),
    H("Site H"),
    I("Site I"),
    J("Site J");

    private final String branch;

    Branch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

}