package files;
public class Hallucinate extends ItemAction {
    public Hallucinate(Item _owner) {
        super(_owner);
        System.out.println("Creating a Hallucinate: " + _owner);
    }
}