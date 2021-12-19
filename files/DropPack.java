package files;
public class DropPack extends CreatureAction {
    private String name;

    public DropPack(String _name, Creature _owner) {
        super(_owner);
        System.out.println("Creating DropPack: " + name + _owner);
        name = _name;
    }
}
