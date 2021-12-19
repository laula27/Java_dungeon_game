package files;
public class Armor extends Item{
    private int room;
    private int serial;

    public Armor() {
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;
        System.out.println("   room: " + room + "\n   serial: " + serial);
    }
    

    //@Override
    public String toString( ) {
        String str = "Armor: \n";
        return str;
    }
}
