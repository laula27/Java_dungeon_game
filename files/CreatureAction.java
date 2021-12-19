package files;
public class CreatureAction extends Action{
    private Creature owner;


    public CreatureAction(Creature _owner){
        //super("");
        owner = _owner;
        System.out.println("CreatureAction:" + _owner);
    } 

    
}
