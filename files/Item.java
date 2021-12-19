package files;
public class Item extends Displayable {
    private String name;
    private String id;
    private ItemAction itemAction;
    private int intValue;

    public Item() {
        System.out.println("Creating an Item");
    }

    public void setIntValue(int intval) {
        System.out.println("Setting intValue: " + intval);
        intValue = intval;
    }

    public int getIntValue(){
        return intValue;
    }

    public void setName(String name) {
        System.out.println("Setting item name: " + name);
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setID(int room, int serial) {
        System.out.println("Setting item ID: " + room + serial);
        id = room + "-" + serial;
    }

    public void setAction(ItemAction ia) {
        System.out.println("Setting ItemAction: " + ia);
        itemAction = ia;
    }

    public ItemAction getAction(){
        return itemAction;
    }
}
