package files;
public class ItemAction extends Action{

    private Item owner;

    public ItemAction(Item _owner){
        owner = _owner;
        System.out.println("Creating an ItemAction: " + owner);
    }



}
