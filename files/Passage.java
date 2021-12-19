package files;
import java.util.ArrayList;

public class Passage extends Structure {
    private String id;
    private ArrayList<Integer> posXs = new ArrayList();
    private ArrayList<Integer> posYs = new ArrayList();

    public Passage() {
        System.out.println("Creating a Passage");
    }

    public void setID(int room1, int room2) {
        System.out.println("Setting ID: " + room1 + room2);
        id = room1 + "-" + room2;
    }

    public ArrayList<Integer> getPosXs(){
        return posXs;
    }

    public ArrayList<Integer> getPosYs(){
        return posYs;
    }

    @Override
    public void setPosX(int posX) {
        System.out.println("Setting PosX for passage: " + posX);
        posXs.add(posX);
    }
    

    @Override
    public void setPosY(int posY) {
        System.out.println("Setting PosY for passage: " + posY);
        posYs.add(posY);
    }
}
