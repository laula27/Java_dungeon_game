package files;
public class UpdateDisplay extends CreatureAction {
    private String name;

    public UpdateDisplay(String _name, Creature _owner) {
        super(_owner);
        System.out.println("Creating an UpdateDisplay: " + name + _owner);
        name = _name;
    }
}