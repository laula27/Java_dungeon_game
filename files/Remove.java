package files;
public class Remove extends CreatureAction {
    private String name;

    public Remove(String _name, Creature _owner) {
        super(_owner);
        System.out.println("Creating a Remove: " + name + _owner);
        name = _name;
    }
}