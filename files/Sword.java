package files;
public class Sword extends Item{
    private int room;
    private int serial;

    public Sword() {
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;
        System.out.println("   room: " + room + "\n   serial: " + serial);
    }

    //@Override
    public String toString( ) {
        String str = "Sword: \n";

        return str;
    }
}
