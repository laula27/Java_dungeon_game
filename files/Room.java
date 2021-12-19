package files;

import java.util.ArrayList;

public class Room extends Structure{

    private int id;
    private ArrayList<Creature> creatures = new ArrayList();
    private ArrayList<Item> items = new ArrayList();

    
    public Room(String _name){        
        setName(_name);
        System.out.println("Room:\n   room: " + _name);
    }

    public void setID(int room1) {
        System.out.println("Setting ID: " + room1);
        id = room1;
    }

    public void addCreature(Creature creature) {
        System.out.println("Adding creature: " + creature);
        creatures.add(creature);
    }

    public ArrayList<Creature> getCreature(){
        return creatures;
    }

    public ArrayList<Item> getItem(){
        return items;
    }

    public void addItem(Item item) {
        System.out.println("Adding item to room: " + item);
        items.add(item);
    }
  
}
