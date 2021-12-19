package files;
public class Player extends Creature{

    private Item sword;
    private Item armor;

    public Player() {}

    public void setSword(Item _sword){
        sword = _sword;
        System.out.println("Setting sword: " + sword);
    }

    public Item getSword(){
        return sword;
    }

    public void setArmor(Item armor){
        this.armor = armor;
        System.out.println("Setting armor: " + armor);
    }

    public Item getArmor(){
        return armor;
    }
    
}
