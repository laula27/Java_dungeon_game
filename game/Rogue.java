package game;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import files.*;


public class Rogue implements Runnable {

    private static final int DEBUG = 0;
    private boolean isRunning;
    public static final int FRAMESPERSECOND = 60;
    public static final int TIMEPERLOOP = 1000000000 / FRAMESPERSECOND;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static files.Dungeon dungeon = null;
    private int playerHP=0;
    private int playerScore = 0;
    private Thread PlayerControl;
    private int playerPosX;
    private int playerPosY;
    private int monsterPosX;
    private int monsterPosY;
    private int playerHpMoves;
    private Player player;
    private ArrayList<Monster> monsterList = new ArrayList<Monster>();
    private ArrayList<Item> itemList = new ArrayList<Item>();


    public Rogue(int width, int height) {
        displayGrid = new ObjectDisplayGrid(width, height);
    }

    @Override
    public void run() {
        //displayGrid.fireUp();
        /*for (int step = 1; step < WIDTH / 2; step *= 2) {
            for (int i = 0; i < WIDTH; i += step) {
                for (int j = 0; j < HEIGHT; j += step) {
                    displayGrid.addObjectToDisplay(new Char('X'), i, j);
                }
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            displayGrid.initializeDisplay();
        }*/
        ArrayList<Room> rooms = dungeon.getRoom();

        for(int room_index = 0;room_index<rooms.size();room_index++){
            int x = rooms.get(room_index).getPosX();
            int y = rooms.get(room_index).getPosY();
            int h = rooms.get(room_index).getHeight();
            int w = rooms.get(room_index).getWidth();
            display_room(x, y, w, h);
            ArrayList<Creature> creatures = rooms.get(room_index).getCreature();
            for(int creature_index = 0;creature_index<creatures.size();creature_index++){
                Creature creature = creatures.get(creature_index);
                int cx = creatures.get(creature_index).getPosX();
                int cy = creatures.get(creature_index).getPosY();
                int chp = creatures.get(creature_index).getHp();
                int maxh = creatures.get(creature_index).getMaxHit();
                int cHpMoves = creatures.get(creature_index).getHpMoves();
                if (creature instanceof Player){
                    player =(Player) creature;
                    displayGrid.addObjectToDisplay(new Char('@'), x+cx, y+cy-2);
                    playerHP = chp;
                    playerPosX = cx + x;
                    playerPosY = cy + y;
                    playerHpMoves = cHpMoves;
                }
                else{
                    
                    monsterPosX = cx + x;
                    monsterPosY = cy + y;
                    System.out.println("bf monster posx: "+cx+", posy: "+cy);
                    creature.setPosX(monsterPosX);
                    creature.setPosY(monsterPosY);
                    System.out.println("af monster posx: "+creature.getPosX()+", posy: "+creature.getPosY());
                    monsterList.add((Monster)creature);
                    char type = creatures.get(creature_index).getType();
                    displayGrid.addObjectToDisplay(new Char(type), x+cx, y+cy-2);
                }
                
            }
            ArrayList<Item> items = rooms.get(room_index).getItem();
            for(int item_index = 0;item_index<items.size();item_index++){
                Item item = items.get(item_index);
                itemList.add(item);
                int ix = items.get(item_index).getPosX();
                int iy = items.get(item_index).getPosY();
                if (item instanceof Sword){
                    displayGrid.addObjectToDisplay(new Char('|'), x+ix, y+iy-2);

                }
                else if (item instanceof Scroll){
                    displayGrid.addObjectToDisplay(new Char('?'), x+ix, y+iy-2);

                }
                else{
                    displayGrid.addObjectToDisplay(new Char(']'), x+ix, y+iy-2);

                }
            }

        }
        ArrayList<Passage> passages = dungeon.getPassage();
        for(int passages_index = 0;passages_index<passages.size();passages_index++){
            System.out.println(passages.size());
            Passage passage = passages.get(passages_index);
            

            ArrayList<Integer> px = passage.getPosXs();
            ArrayList<Integer> py = passage.getPosYs();
            System.out.println(px.get(0)+ "," + py.get(0));
            display_pass(px,py);

            
            
        }
        // Now draw top area
        String topText = "HP: " + playerHP + "  core:  " + playerScore;
        for (int col = 0; col < topText.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(topText.charAt(col)), col, 0);
        }

        // Now draw bottom area
        String packString = "Pack:";
        for ( int col = 0; col < packString.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(packString.charAt(col)), col, displayGrid.getHeight() - 5);
        }
        String infoString = "Info:";
        for (int col = 0; col < infoString.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
        }
    }
    public void display_pass(ArrayList<Integer> px, ArrayList<Integer> py){
        for(int i=0;i<px.size()-1;i++){
            int xp = px.get(i);
            int yp = py.get(i)+2;
            int xpnext = px.get(i+1);
            int ypnext = py.get(i+1)+2;
            if(xp==xpnext){
                if(yp < ypnext){
                    for(int j=yp;j<ypnext;j++){
                        displayGrid.addObjectToDisplay(new Char('#'), xp, j);
                    }
                }
                else{
                    for(int j=yp;j>ypnext;j--){
                        displayGrid.addObjectToDisplay(new Char('#'), xp, j);
                    }
                }
                
            }
            else if(yp==ypnext){
                for(int j=xp;j<xpnext;j++){
                    displayGrid.addObjectToDisplay(new Char('#'), j, yp);
                }
            }
        }
        
        displayGrid.addObjectToDisplay(new Char('+'), px.get(0), py.get(0)+2);
        
        displayGrid.addObjectToDisplay(new Char('+'), px.get(px.size()-1), py.get(py.size()-1)+2);
    }
    public void display_room(int x, int y, int w, int h){
        for(int i = x; i < x+w; i++){
            displayGrid.addObjectToDisplay(new Char('X'), i, y);
            displayGrid.addObjectToDisplay(new Char('X'), i, y+h-1);
        }
        for(int j = y; j < y+h; j++){
            displayGrid.addObjectToDisplay(new Char('X'), x, j);
            displayGrid.addObjectToDisplay(new Char('X'), x+w-1, j);
        }
        for (int i = x+1; i < x+w-1; i ++) {
            for (int j = y+1; j < y+h-1; j ++) {
                displayGrid.addObjectToDisplay(new Char('.'), i, j);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        /*Rogue test = new Rogue(WIDTH, HEIGHT);
        Thread testThread = new Thread(test);
        testThread.start();

        test.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid));
        test.keyStrokePrinter.start();

        testThread.join();
        test.keyStrokePrinter.join();*/
        String filename = null;
        if (args.length == 1) {
            filename = "xmlFiles/" + args[0];
        } else {
            System.out.println("java Test <xmlfilename>");
            return;
        }

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        
        SAXParser saxParser = saxParserFactory.newSAXParser();
        DungeonXMLHandler handler = new DungeonXMLHandler();
        saxParser.parse(new File(filename), handler);
        dungeon = handler.getDungeon();
        System.out.println("---width: "+dungeon.getWidth()+", Height: "+dungeon.getGameHeight());
        Rogue rogue = new Rogue(dungeon.getWidth(),dungeon.getGameHeight()+dungeon.getTopHeight()+dungeon.getBottomHeight());
        Thread rogueThread = new Thread(rogue);
        rogueThread.start();

        rogueThread.join();
        rogue.PlayerControl = new Thread(new PlayerControl(displayGrid, rogue.playerPosX, rogue.playerPosY-2, rogue.player, rogue.playerHpMoves, rogue.monsterList, rogue.itemList));
        rogue.PlayerControl.start();
        rogue.PlayerControl.join();
    }
}
