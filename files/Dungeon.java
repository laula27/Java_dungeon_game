package files;

import java.util.ArrayList;
import java.util.List;
public class Dungeon {    

    private String name;
    private int width;
    private int gameHeight;
    private int topHeight;
    private int bottomHeight;
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<Creature> creatures = new ArrayList<Creature>();
    private ArrayList<Passage> passages = new ArrayList<Passage>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private static Dungeon dungeon = null;

    public Dungeon(String _name, int _gameHeight, int _width, int _topHeight, int _bottomHeight){
        name = _name;
        width = _width;
        gameHeight = _gameHeight;
        topHeight = _topHeight;
        bottomHeight = _bottomHeight;
        System.out.println("Dungeon: \n   name: " + name + "\n   width: " + width + "\n   gameHeight: " + gameHeight);
    }

    public static Dungeon getDungeon(String name, int gameHeight, int width, int topHeight, int bottomHeight) throws Exception {
        System.out.println("Getting dungeon...");
        if (dungeon == null) { // create dungeon with name, width, and gameHeight
            System.out.println("Dungeon has not been created yet...");
            dungeon = new Dungeon(name, gameHeight, width, topHeight, bottomHeight);
            return dungeon;
        } else if (dungeon.isSame(name, gameHeight, width, topHeight, bottomHeight)) {
            System.out.println("Matching dungeon exists...");
            return dungeon;
        } else {
            throw new Exception("Dungeon specs don't match!");
        }
    }

    public boolean isSame(String _name, int _gameHeight, int _width, int _topHeight, int _bottomHeight) {
        System.out.println("Checking if dungeon specs match current...");
        return (name == _name && width == _width && gameHeight == _gameHeight && topHeight == _topHeight && bottomHeight == _bottomHeight);
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public ArrayList<Room> getRoom(){
        return rooms;
    }

    public void addCreature(Creature creature){
        creatures.add(creature);
    }

    public void addPassage(Passage passage){
        passages.add(passage);
    }

    public ArrayList<Passage> getPassage(){
        return passages;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public int getGameHeight(){
        return gameHeight;
    }

    public int getTopHeight(){
        return topHeight;
    }

    public int getBottomHeight(){
        return bottomHeight;
    }

    public int getWidth(){
        return width;
    }
    
}
