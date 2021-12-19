package game;
import files.*;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.Templates;

import java.util.Random;
import java.util.Scanner;

public class PlayerControl implements InputObserver, Runnable {

    private static int DEBUG = 1;
    private int posX;
    private int posY;
    private static String CLASSID = "KeyStrokePrinter";
    private static Queue<Character> inputQueue = null;
    private ObjectDisplayGrid displayGrid;
    private Char prevChar = new Char('.');
    private Item prevItem;
    private char chagedisplaytype = '.';
    private char ScrollCharValue = ' ';
    private Char prev_changeDisplayType = new Char('.');
    private Player player;
    private ArrayList<Monster> monsterList = new ArrayList<Monster>();
    private ArrayList<Item> itemList = new ArrayList<Item>();
    private int hpMoves;
    private int playerScore = 0;
    private ArrayList<Char> pack = new ArrayList<Char>();
    private char scroll_char;
    private ArrayList<Integer> pack_item = new ArrayList<Integer>();
    private ArrayList<String> pack_names = new ArrayList<String>();
    private ArrayList<Item> real_pack = new ArrayList<Item>();
    private ArrayList<hallu_rec> hallu_record = new ArrayList<hallu_rec>();
    private char[] symbol_set = {'#','.','+','T','S','H','X'};
    private int scroll_int = 0;
    private boolean scroll_hallu = false; // ture is hallucination
    private int queue = 0;
    private int moveCounter = 0;
    private boolean Sword_use = false;
    private int Sword_use_val = 0;
    private int Sword_use_slot = 0;
    private boolean Armor_use = false;
    private int Armor_use_val = 0;
    private int Armor_use_slot = 0;
    private String Scroll_name;
    private boolean reading = false;
    private boolean ending = false;
    private boolean helping = false;
    private int actionIntValue = 0;
    private boolean dropping = false;
    private boolean swording = false;
    private boolean armoring = false;
    private boolean taking_off_armor = false;
    private boolean overlaping = false;
    private int overlap_X = 0;
    private int overlap_Y = 0;
    private ArrayList<Item> stack = new ArrayList<Item>();
    private ArrayList<Char> stack_char = new ArrayList<Char>();
    private static int hallu_count = 0;
    private Char[][] tempODG;
    private String packString;

    public PlayerControl(ObjectDisplayGrid grid, int _posX, int _posY,Player _player, int _hpMoves, ArrayList<Monster> _monsterList, ArrayList<Item> _itemList) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        posX = _posX;
        posY = _posY;
        player = _player;
        monsterList = _monsterList;
        itemList = _itemList;
        hpMoves = _hpMoves;
        init_weapon(player);
        tempODG = new Char[displayGrid.getWidth()][displayGrid.getHeight()];

        System.out.println(posX+","+posY);
    }
    class hallu_rec {
        int pX = 0;
        int pY = 0;
        Char c;

        hallu_rec(int _pX, int _pY, Char _c){
            pX = _pX;
            pY = _pY;
            c = _c;
        }
        
        public int get_pX(){
            return pX;
        }
        public int get_pY(){
            return pY;
        }
    }

    public void init_weapon(Player player){
        Item sword = player.getSword();
        Item armor = player.getArmor();
        try{
            System.out.println("-------" + sword.getName());
            Char c = new Char('|');
            pack.add(c);
            pack_names.add(sword.getName());
            pack_item.add(sword.getIntValue());
            itemList.add(sword);
            real_pack.add(sword);
        }
        catch(Exception e){}
        try{
            System.out.println("-------" + armor.getName());
            Char c = new Char(']');
            pack.add(c);
            pack_names.add(armor.getName());
            pack_item.add(armor.getIntValue());
            itemList.add(armor);
            real_pack.add(armor);
        }
        catch(Exception e){}
    }

    public void showPack(){
        packString = "Pack: ";
        for(int i=0;i<pack.size();i++){
            packString+=pack.get(i).getChar();
        }
        packString += "                            ";
        for ( int col = 0; col < packString.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(packString.charAt(col)), col, displayGrid.getHeight() - 5);
        }
    }

    public Monster getMonster(int x, int y){
        Monster temp;
        int mx, my;
        //System.out.println("x: " + x + ", y: "+y+4);
        for(int i = 0; i < monsterList.size(); i++){
            temp = monsterList.get(i);
            mx = temp.getPosX();
            my = temp.getPosY();
            //System.out.println("monster "+ i+", x: "+mx+", y: "+my);
            if(mx == x && my == y+4){
                return temp;
            }

        }
        return monsterList.get(0);
    }

    public Item getItem(int x, int y){
        Item temp;
        int mx, my;
        System.out.println("x: " + x + ", y: "+ y);
        for(int i = 0; i < itemList.size(); i++){
            temp = itemList.get(i);
            System.out.println("name: "+ temp.getName());
            mx = temp.getPosX();
            my = temp.getPosY();
            System.out.println("item "+ i+", x: "+mx+", y: "+my);
            if(mx == x && my == y){
                return temp;
            }

        }
        System.out.println("------------");
        return itemList.get(0);
    }

    public void clear_info(){
        String infoString1 = "Info:                                                                   ";
        for (int col = 0; col < infoString1.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
        }
    }
    
    public void help(int action){
        helping = false;
        if(action == 'c'){
            String infoString1 = "Info: Change, or take off armor.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'd'){
            String infoString1 = "Info: Drop item <integer> from the pack.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'E'){
            String infoString1 = "Info: End game (E) <Y | y>: end the game.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'i'){
            String infoString1 = "Info: Show or display the inventory.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'p'){
            String infoString1 = "Info: Pick up an item from the dungeon floor.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'r'){
            String infoString1 = "Info: Read an item.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'T'){
            String infoString1 = "Info: Take out a sword.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
        else if(action == 'w'){
            String infoString1 = "Info: Wear armor.";
            for (int col = 0; col < infoString1.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString1.charAt(col)), col, displayGrid.getHeight() - 3);
            }
        }
    }

    public void teleport(Creature creature){
        Random random = new Random();
        int randX = 0, randY = 0;
        int orgX = creature.getPosX(), orgY = creature.getPosY();
        System.out.println("orgX: "+orgX+", orgY: "+(orgY-2));

        while(true){
            randX = random.nextInt(displayGrid.getWidth());
            randY = random.nextInt(displayGrid.getHeight());
            Char C = displayGrid.getObjectFromDisplay(randX, randY-4);
            if(C == null){
                //System.out.println("-----"+C+"-----");
            }
            else if(C.getChar() == '.' || C.getChar() == '#' || C.getChar() == '+'){
                //System.out.println("-----"+C.getChar()+"-----");
                break;
            }
            else{
                //System.out.println("-----"+C.getChar()+"-----");
            }

        }
        creature.setPosX(randX);
        creature.setPosY(randY-2);
        displayGrid.addObjectToDisplay(prev_changeDisplayType, orgX, orgY-4);
        prev_changeDisplayType = displayGrid.getObjectFromDisplay(randX, randY-4);
        System.out.println("----Prevchange"+prev_changeDisplayType.getChar());
        System.out.println("randX: "+randX+", randY: "+(randY-4));
        displayGrid.addObjectToDisplay(new Char(creature.getType()), randX, randY-4);
    }

    public boolean is_symbol(Char C){
        for(int i = 0; i < 7; i++){
            if(C.getChar() == symbol_set[i]) return true;
        }
        return false;
    }

    public void hallu(){
        System.out.println("-------------hallu function");
        int count = 10;
        Random random = new Random();
        int randX = 0, randY = 0, randS = 0;
        

        while(count > 0){
            randX = random.nextInt(displayGrid.getWidth());
            randY = random.nextInt(displayGrid.getHeight());
            randS = random.nextInt(7);
            Char C = displayGrid.getObjectFromDisplay(randX, randY-4);
            if(C != null){
                if(is_symbol(C)){
                    System.out.println("randX: "+randX+", randY: "+randY);
                    hallu_record.add(new hallu_rec(randX, randY-4, C));
                    displayGrid.addObjectToDisplay(new Char(symbol_set[randS]), randX, randY-4);
                    count--;
                }
            }
        }
        hallu_count--;
        String infoString="Info: ";
        infoString += "Hallucinations will continue for " + (hallu_count-1) + " moves.";
        for (int col = 0; col < infoString.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
        }
        for (int j = 0; j < displayGrid.getHeight(); j++) {
            for (int i = 0; i < displayGrid.getWidth(); i++) {
                if(tempODG[i][j] != null){
                    System.out.print(tempODG[i][j].getChar());
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.print("\n");
        }

        if(hallu_count == 0){
            System.out.println("------reset display");
            displayGrid.copyDisplay(tempODG);
        }
        
    }

    public boolean attack(Player player,int x, int y){
        boolean alive = true;
        //monster attack 
        Random random = new Random();
        Monster temp = getMonster(x, y);
        int mHP = temp.getHp();
        
        int m_damage = random.nextInt(getMonster(x, y).getMaxHit() + 1);
        int p_damage = random.nextInt(player.getMaxHit()+1);
        if(Armor_use == true){
            player.setHp(player.getHp()-m_damage+pack_item.get(Armor_use_slot-1));
        }
        else{
            player.setHp(player.getHp()-m_damage);
        }

        if(Sword_use == true){
            temp.setHp(temp.getHp()-p_damage-pack_item.get(Sword_use_slot-1));
        }
        else{
            temp.setHp(temp.getHp()-p_damage);
        }

        System.out.println("------Player HP: "+player.getHp());
        
        String topText = "HP: " + player.getHp() + "  score:  " + playerScore;
        for (int col = 0; col < topText.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(topText.charAt(col)), col, 0);
        }

        // Check player hitaction
        ArrayList<CreatureAction> hitActions = player.getHitAction();
        for(int i = 0; i < hitActions.size(); i++){
            String name = hitActions.get(i).getName();
            if(name.equals("DropPack")){
                if(pack.size() > 0){
                    prevChar = pack.remove(0);
                    pack_item.remove(0);
                    pack_names.remove(0);
                    Item temp_drop = real_pack.remove(0);
                    temp_drop.setPosX(player.getPosX());
                    temp_drop.setPosY(player.getPosY());
                }
            }
        }


        System.out.println("damage: " + p_damage);
        System.out.println("player: " + player.getHp()+ ", monster: " + temp.getHp());
        String infoString = "";

        if(player.getHp() <= 0){    // Player died
            infoString = "Info: ";
            ArrayList<CreatureAction> deathactions = player.getDeathAction();
            for(int i = 0; i < deathactions.size(); i++){
                String name = deathactions.get(i).getName();
                //System.out.println(name);
                //System.out.println("----------Action message: " + deathactions.get(1).getMessage());
                //if(name"YouWin") System.out.println("same!!!");
                if(name.equals("EndGame")){
                    System.out.println("EndGame");
                    String acitonMsg = deathactions.get(i).getMessage();
                    System.out.println("----- message: "+ acitonMsg);
                    infoString += acitonMsg;
                }
                else if(name.equals("ChangeDisplayedType")){
                    System.out.println("ChangeDisplayedType");
                    chagedisplaytype = deathactions.get(i).getCharValue();
                }
                else if(name.equals("UpdateDisplay")){
                    System.out.println("UpdateDisplay");
                    displayGrid.addObjectToDisplay(new Char(chagedisplaytype), player.getPosX(), player.getPosY());
                }
            }
            for (int col = 0; col < infoString.length(); col++) {
                displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
            }
            return true;
        }

        if(temp.getHp() <= 0){//monster died
            infoString = "Info: ";
            ArrayList<CreatureAction> deathactions = temp.getDeathAction();
            for(int i = 0; i < deathactions.size(); i++){
                String name = deathactions.get(i).getName();
                //System.out.println(name);
                //System.out.println("----------Action message: " + deathactions.get(1).getMessage());
                //if(name"YouWin") System.out.println("same!!!");
                if(name.equals("YouWin")){
                    //System.out.println("youwin");
                    String acitonMsg = deathactions.get(i).getMessage();
                    infoString += acitonMsg;
                }
                else if(name.equals("ChangeDisplayedType")){
                    chagedisplaytype = deathactions.get(i).getCharValue();
                }
                else if(name.equals("Remove")){
                    alive = false;
                }
                
                actionIntValue = deathactions.get(i).getIntValue();
                
            }
        }
        else{
            ArrayList<CreatureAction> hitactions = temp.getHitAction();
            for(int i = 0; i < hitactions.size(); i++){
                String name = hitactions.get(i).getName();
                //System.out.println("name is " + name);
                if(name.equals("Teleport")){
                    System.out.println("---------teleport");
                    String acitonMsg = hitactions.get(i).getMessage();
                    infoString += acitonMsg;
                    teleport(temp);
                }
                else if(name.equals("ChangeDisplayedType")){
                    chagedisplaytype = hitactions.get(i).getCharValue();
                }
                
                actionIntValue = hitactions.get(i).getIntValue();
                
            }
            infoString = "Info: Damage " + p_damage + "               ";
        }

        //System.out.println(infoString);
        for (int col = 0; col < infoString.length(); col++) {
            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
        }
        return alive;
    }
    public void move(int x, int y, int action){
        Char ch;
        char c;
        clear_info();
        if(helping != true){
            if(action == 'j'){//down
                moveCounter++;
                if(moveCounter == hpMoves){
                    moveCounter = 0;
                    player.setHp(player.getHp()+1);
                }
                System.out.println("----hallcount: "+hallu_count);
                if(hallu_count > 0){
                    hallu();
                }
                String topText = "HP: " + player.getHp() + "  score:  " + playerScore;
                for (int col = 0; col < topText.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(topText.charAt(col)), col, 0);
                }
                ch = displayGrid.getObjectFromDisplay(x, y+1);
                c = ch.getChar();
                if(c == '#' || c == '+' || c == '.' || c == ']' || c == '?' || c == '|'){
                    displayGrid.addObjectToDisplay(prevChar, x, y);
                    displayGrid.addObjectToDisplay(new Char('@'), x, y+1);
                    player.setPosY(y+1);
                    prevChar = ch;
                    posY = y+1;
                }
                else if(c == 'T' || c == 'H' || c == 'S'){
                    Monster m = getMonster(x, y+1);
                    if(m.getHp() <= 0){
                        displayGrid.addObjectToDisplay(prevChar, x, y);
                        displayGrid.addObjectToDisplay(new Char('@'), x, y+1);
                        prevChar = ch;
                        posY = y+1;
                    } else if(attack(player, x, y+1) == false){
                        displayGrid.addObjectToDisplay(new Char(chagedisplaytype), x, y+1);
                    }
                }
                showPack();
                
            }
            else if(action == 'k'){//up
                moveCounter++;
                if(moveCounter == hpMoves){
                    moveCounter = 0;
                    player.setHp(player.getHp()+1);
                }
                System.out.println("----hallcount: "+hallu_count);
                if(hallu_count > 0){
                    hallu();
                }
                String topText = "HP: " + player.getHp() + "  score:  " + playerScore;
                for (int col = 0; col < topText.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(topText.charAt(col)), col, 0);
                }
                ch = displayGrid.getObjectFromDisplay(x, y-1);
                c = ch.getChar();
                if(c == '#' || c == '+' || c == '.' || c == ']' || c == '?' || c == '|'){
                    displayGrid.addObjectToDisplay(prevChar, x, y);
                    displayGrid.addObjectToDisplay(new Char('@'), x, y-1);
                    player.setPosY(y-1);
                    prevChar = ch;
                    posY = y-1;
                }
                else if(c == 'T' || c == 'H' || c == 'S'){
                    Monster m = getMonster(x, y-1);
                    if(m.getHp() <= 0){
                        displayGrid.addObjectToDisplay(prevChar, x, y);
                        displayGrid.addObjectToDisplay(new Char('@'), x, y-1);
                        prevChar = ch;
                        posY = y-1;
                    } else if(attack(player, x, y-1) == false){
                        displayGrid.addObjectToDisplay(new Char('.'), x, y-1);
                    }
                }
                showPack();
            }
            else if(action == 'h'){ //left
                moveCounter++;
                if(moveCounter == hpMoves){
                    moveCounter = 0;
                    player.setHp(player.getHp()+1);
                }
                System.out.println("----hallcount: "+hallu_count);
                if(hallu_count > 0){
                    hallu();
                }
                String topText = "HP: " + player.getHp() + "  score:  " + playerScore;
                for (int col = 0; col < topText.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(topText.charAt(col)), col, 0);
                }
                ch = displayGrid.getObjectFromDisplay(x-1, y);
                c = ch.getChar();
                if(c == '#' || c == '+' || c == '.' || c == ']' || c == '?' || c == '|'){
                    displayGrid.addObjectToDisplay(prevChar, x, y);
                    displayGrid.addObjectToDisplay(new Char('@'), x-1, y);
                    player.setPosX(x-1);
                    prevChar = ch;
                    posX = x-1;
                }
                else if(c == 'T' || c == 'H' || c == 'S'){
                    Monster m = getMonster(x-1, y);
                    if(m.getHp() <= 0){
                        displayGrid.addObjectToDisplay(prevChar, x, y);
                        displayGrid.addObjectToDisplay(new Char('@'), x-1, y);
                        prevChar = ch;
                        posX = x-1;
                    } else if(attack(player, x-1, y) == false){
                        displayGrid.addObjectToDisplay(new Char('.'), x-1, y);
                    }
                }
                showPack();
            }
            else if(action == 'l'){//right
                moveCounter++;
                if(moveCounter == hpMoves){
                    moveCounter = 0;
                    player.setHp(player.getHp()+1);
                }
                System.out.println("----hallcount: "+hallu_count);
                if(hallu_count > 0){
                    hallu();
                }
                String topText = "HP: " + player.getHp() + "  score:  " + playerScore;
                for (int col = 0; col < topText.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(topText.charAt(col)), col, 0);
                }
                ch = displayGrid.getObjectFromDisplay(x+1, y);
                c = ch.getChar();
                if(c == '#' || c == '+' || c == '.' || c == ']' || c == '?' || c == '|'){
                    displayGrid.addObjectToDisplay(prevChar, x, y);
                    displayGrid.addObjectToDisplay(new Char('@'), x+1, y);
                    player.setPosX(x+1);
                    prevChar = ch;
                    posX = x+1;
                }
                else if(c == 'T' || c == 'H' || c == 'S'){
                    Monster m = getMonster(x+1, y);
                    if(m.getHp() <= 0){
                        displayGrid.addObjectToDisplay(prevChar, x, y);
                        displayGrid.addObjectToDisplay(new Char('@'), x+1, y);
                        prevChar = ch;
                        posX = x+1;
                    } else if(attack(player, x+1, y) == false){
                        displayGrid.addObjectToDisplay(new Char('.'), x+1, y);
                    }
                }
                showPack();
            }
            else if(action == 'p'){
                //System.out.println(c);
                Item temp;
                System.out.println("overlaping = "+overlaping);
                if(overlaping == true && overlap_X == x && overlap_Y == y){
                    temp = stack.remove(stack.size()-1);
                    stack_char.remove((stack_char.size()-1));
                    System.out.println("-----ouverlapping, name: "+temp.getName());
                }
                else{
                    temp = getItem(x, y);
                }
                ch = prevChar;
                c = prevChar.getChar();
                
                real_pack.add(temp);
                if(c == ']' || c == '?' || c == '|'){
                    pack.add(ch);
                    pack_names.add(temp.getName());
                    if(c == '?'){
                        String infoString = "Info: ";
                        Scroll_name = temp.getName();
                        ItemAction itemAction = temp.getAction();
                        String msg = itemAction.getMessage();
                        scroll_int = itemAction.getIntValue();
                        if(itemAction.getName().equals("Hallucinate")){
                            scroll_hallu = true;
                        }
                        else{
                            scroll_char = itemAction.getCharValue();
                        }
                        pack_item.add(scroll_int);
                        infoString += msg;
                        for (int col = 0; col < infoString.length(); col++) {
                            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                        }
                    }
                    else{
                        System.out.println("name: "+temp.getName());
                        pack_item.add(temp.getIntValue());
                    }

                    if(overlaping == true && overlap_X == x && overlap_Y == y){
                        if(stack.size() == 0){
                            overlaping = false;
                            prevChar  = new Char('.');
                        }
                        else{
                            prevChar = stack_char.get(stack_char.size()-1);
                        }
                    }
                    else{
                        prevChar  = new Char('.');
                    }
                    showPack();
                }
            }
            else if(action == 'd'){
                dropping = true;
            }
            else if(action == '1' || action == '2' || action == '3' || action == '4' || action == '5'){
                int val = action - '0';
                Item real;
                if(dropping == true){
                    System.out.println("action is : "+val);
                    if(val <= pack.size()){
                        if(prevChar.getChar() != '.'){  // overlaping
                            overlaping = true;
                            stack.add(real_pack.remove(val-1));
                            prevChar = pack.remove(val-1);
                            System.out.println("prevchar: "+prevChar.getChar());
                            stack_char.add(prevChar);
                            System.out.println("stack size: "+stack.size());
                            System.out.println("stack_char size: "+stack_char.size());
                            overlap_X = x;
                            overlap_Y = y;
                        }
                        else{
                            real = real_pack.remove(val-1);
                            prevChar = pack.remove(val-1);
                            
                            if(stack.size() != 0){
                                stack.remove(stack.size()-1);
                                stack_char.remove(stack_char.size()-1);
                            }
                            stack.add(real);
                            stack_char.add(prevChar);
                            real.setPosX(x);
                            real.setPosY(y-2);
                        }
                        
                        //System.out.println("name"+real.getName());
                        System.out.println("x:"+x+"y:"+y);
                        pack_names.remove((val-1));
                        c = prevChar.getChar();
                        //System.out.println("queue is " + queue);
                        //System.out.println("prevChar is " + c);
                        if(c == '|'){
                            Sword_use = false;
                        }
                        else if(c == ']'){
                            Armor_use = false;
                        }
                        showPack();
                        dropping = false;
                    }
                    else{
                        String infoString = "Info: Index out of range.                       ";
                        for (int col = 0; col < infoString.length(); col++) {
                            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                        }
                    }
                } else if(swording == true){
                    swording = false;
                    if(pack.get(val-1).getChar() == '|'){
                        Sword_use = true;
                        Sword_use_slot = val;
                        Sword_use_val = pack_item.get(val-1);
                    }
                    else{
                        String infoString = "Info: This slot does not contain sword.";
                        for (int col = 0; col < infoString.length(); col++) {
                            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                        }
                    }
                } else if(armoring == true){
                    armoring = false;
                    if(pack.get(val-1).getChar() == ']'){
                        Armor_use = true;
                        Armor_use_slot = val;
                        Armor_use_val = pack_item.get(val-1);
                    }
                    else{
                        String infoString = "Info: This slot does not contain armor.                        ";
                        for (int col = 0; col < infoString.length(); col++) {
                            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                        }
                    }
                } else if(taking_off_armor == true){
                    taking_off_armor = false;
                    System.out.println("slot: "+Armor_use_slot+", val: "+val);
                    if(pack.get(val-1).getChar() == ']' && Armor_use == true && Armor_use_slot == val){
                        Armor_use = false;
                    }
                    else{
                        String infoString = "Info: This armor/this item is not being used.";
                        for (int col = 0; col < infoString.length(); col++) {
                            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                        }
                    }
                } else if(reading == true){
                    String infoString = "Info: ";
                    if(val > pack.size()){
                        infoString += "Index out of range.               ";
                        for (int col = 0; col < infoString.length(); col++) {
                            displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                        }
                    }
                    else{
                        if(pack.get(val-1).getChar() == '?'){
                            if(scroll_hallu == true){    //is hallucinate
                                System.out.println("-----------is hallucinate");
                                hallu_count = scroll_int+1;
                                infoString += "Hallucinations will continue for " + (hallu_count-1) + " moves.";
                                
                                Char[][] t = displayGrid.getObjectGrid();
                                for (int i = 0; i < displayGrid.getWidth(); i++) {
                                    for (int j = 0; j < displayGrid.getHeight(); j++) {
                                        if(t[i][j] != null){
                                            if(t[i][j].getChar() == '@'){
                                                System.out.println("-----------@@@@@@@@@");
                                                tempODG[i][j] = new Char('.');
                                            }
                                            else{
                                                tempODG[i][j] = t[i][j];
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                c = scroll_char;
                                System.out.println("------ "+c);
                                if(c == 'w'){//affect sword
                                    if(Sword_use == true){
                                        System.out.println("---"+Sword_use_slot);
                                        infoString += pack_names.get(Sword_use_slot-1) + " cursed! " + scroll_int*(-1) + " taken from its effectiveness.";
                                        String pos = "";
                                        int new_val = pack_item.get(Sword_use_slot-1)+scroll_int;
                                        if(new_val >= 0) pos = "+";
                                        pack_item.set(Sword_use_slot-1, new_val);
                                        String new_name = pos + "" + pack_item.get(Sword_use_slot-1) +" Sword";
                                        pack_names.set(Sword_use_slot-1, new_name);
                                    }
                                    else{
                                        infoString += "Scroll of cursing does nothing because sword not being used.";
                                    }
                                }
                                else{//affect armor
                                    if(Armor_use == true){
                                        System.out.println("---"+Armor_use_slot);
                                        infoString += pack_names.get(Armor_use_slot-1) + " cursed! " + scroll_int*(-1) + " taken from its effectiveness.";
                                        String pos = "";
                                        int new_val = pack_item.get(Armor_use_slot-1)+scroll_int;
                                        if(new_val >= 0) pos = "+";
                                        pack_item.set(Armor_use_slot-1, pack_item.get(Armor_use_slot-1)+scroll_int);
                                        String new_name = pos + "" + pack_item.get(Armor_use_slot-1) +" Armor";
                                        pack_names.set(Armor_use_slot-1, new_name);
                                    }
                                    else{
                                        infoString += "Scroll of cursing does nothing because armor not being used.";
                                    }
                                }
                                
                            }
                            for(int i = 0; i < pack.size(); i++){
                                if(pack.get(i).getChar() == '?'){
                                    pack.remove(i);
                                    pack_item.remove(i);
                                    pack_names.remove(i);
                                }
                            }
                        }
                    }
                    for (int col = 0; col < infoString.length(); col++) {
                        displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                    }
                }
                showPack();
            }
            else if(action == 'T'){//use sword
                swording = true;
            }
            else if(action == 'w'){//use armor
                armoring = true;
            }
            else if(action == 'c'){ //take off armor
                taking_off_armor = true;
            }
            else if(action =='i'){ // info
                String packString = "Pack: ";
                for(int i=0;i<pack.size();i++){
                    char curr_item = pack.get(i).getChar();
                    Integer curr_item_val = pack_item.get(i);
                    if(curr_item == ']'){   //is armor
                        System.out.println("val: "+Armor_use_val+", pack_val: "+curr_item_val);
                        if(Armor_use == true && Armor_use_val == pack_item.get(i)){
                            packString+=pack_names.get(i) + "(a) ";
                        }
                        else{
                            packString+=pack_names.get(i) + " ";
                        }
                    }
                    else if(curr_item == '|'){   //is sword
                        System.out.println("val: "+Sword_use_val+", pack_val: "+curr_item_val);
                        if(Sword_use == true && Sword_use_val == pack_item.get(i)){
                            packString+=pack_names.get(i) +"(w) ";
                        }
                        else{
                            packString+=pack_names.get(i) + " ";
                        }
                    }
                    else if(curr_item == '?'){  // is scroll
                        packString+=Scroll_name + " ";
                    }
                }
                packString += "                    ";
                for ( int col = 0; col < packString.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(packString.charAt(col)), col, displayGrid.getHeight() - 5);
                }
            }
            else if(action == 'r'){ //read scroll
                reading = true;
            }
            else if(action == 'E'){ // end game
                String infoString = "Info: Press <Y | y> to end the game.";
                for (int col = 0; col < infoString.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                }
                ending = true;
            }
            else if((action == 'y' || action == 'Y') && ending == true){ // end game confirmation
                String infoString = "Info: Game ended.";
                for (int col = 0; col < infoString.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                }
            }
            else if (action == '?'){    // Help
                String infoString = "Info: (Commands) c d E i p r T w  (Press H <command> to know more)";
                for (int col = 0; col < infoString.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                }
            }
            else if(action == 'H'){
                helping = true;
                String infoString = "Info: Enter commands to know more.";
                for (int col = 0; col < infoString.length(); col++) {
                    displayGrid.addObjectToDisplay(new Char(infoString.charAt(col)), col, displayGrid.getHeight() - 3);
                }
            }
        }
        else{
            help(action);
        }
    }

    @Override
    public void observerUpdate(char ch) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".observerUpdate receiving character " + ch);
        }
        inputQueue.add(ch);
    }

    private void rest() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean processInput() {

        char ch;

        boolean processing = true;
        while (processing) {
            if (inputQueue.peek() == null) {
                processing = false;
            } else {
                ch = inputQueue.poll();
                if (DEBUG > 1) {
                    System.out.println(CLASSID + ".processInput peek is " + ch);
                }
                if (ch == 'X') {
                    System.out.println("got an X, ending input checking");
                    return false;
                } else {
                    if(ch == 'k' || ch == 'j' || ch == 'h' || ch == 'l'|| ch == 'p' || ch == 'd' || ch == '?' ||
                        ch == 'w' || ch == 'T' || ch == 'c' || ch == 'r' || ch == 'i' || ch == 'E' || 
                        ch == 'y' || ch == 'Y' || ch == 'H'){
                        move(posX, posY, ch);
                    }
                    else if(ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' ){
                        move(posX, posY, ch);
                    }
                    
                    if (DEBUG > 0) {
                        System.out.println("character " + ch + " entered on the keyboard");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        displayGrid.registerInputObserver(this);
        boolean working = true;
        while (working) {
            rest();
            working = (processInput( ));
        }
    }
}
