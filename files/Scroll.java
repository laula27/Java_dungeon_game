package files;
public class Scroll extends Item{;
    private int room;
    private int serial;

    public Scroll() {
        //System.out.println("Scroll: " + name);
    }

    public void setID(int _room, int _serial) {
        room = _room;
        serial = _serial;
        System.out.println("   room: " + room + "\n   serial: " + serial);
    }

    //@Override
    public String toString( ) {
        String str = "Scroll: \n";

        return str;
    }
}
