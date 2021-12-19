package files;
public class ChangeDisplayedType extends CreatureAction{
    private String name;

    public ChangeDisplayedType(String _name, Creature owner) {
        super(owner);
        name = _name;
        System.out.println("Creating ChangedDisplayType: " + _name + owner);
    }
}
