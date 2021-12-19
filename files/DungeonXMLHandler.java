package files;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

public class DungeonXMLHandler extends DefaultHandler {
    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "DungeonXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    // When the parser parses the file it will add references to
    // Student objects to this array so that it has a list of
    // all specified students.  Had we covered containers at the
    // time I put this file on the web page I would have made this
    // an ArrayList of Students (ArrayList<Student>) and not needed
    // to keep tract of the length and maxStudents.  You should use
    // an ArrayList in your project.
    private Dungeon dungeon = null;
    private ObjectDisplayGrid odg = null;

    // The XML file contains a list of Students, and within each
    // Student a list of activities (clubs and classes) that the
    // student participates in.  When the XML file initially
    // defines a student, many of the fields of the object have
    // not been filled in.  Additional lines in the XML file
    // give the values of the fields.  Having access to the
    // current Student and Activity allows setters on those
    // objects to be called to initialize those fields.
    private Room roomBeingParsed = null;
    private Passage passageBeingParsed = null;
    private Creature creatureBeingParsed = null;
    private Action actionBeingParsed = null;
    private Item itemBeingParsed = null;
    private Stack<Displayable> displayableBeingParsed = new Stack();

    // The bX fields here indicate that at corresponding field is
    // having a value defined in the XML file.  In particular, a
    // line in the xml file might be:
    // <instructor>Brook Parke</instructor>
    // The startElement method (below) is called when <instructor>
    // is seen, and there we would set bInstructor.  The endElement
    // method (below) is called when </instructor> is found, and
    // in that code we check if bInstructor is set.  If it is,
    // we can extract a string representing the instructor name
    // from the data variable above.
    private boolean bVisible = false;
    private boolean bPosX = false;
    private boolean bPosY = false;
    private boolean bWidth = false;
    private boolean bHeight = false;
    private boolean bActionMessage = false;
    private boolean bActionIntValue = false;
    private boolean bActionCharValue = false;
    private boolean bHP = false;
    private boolean bHPMoves = false;
    private boolean bMaxHit = false;
    private boolean bItemIntValue = false;
    private boolean bType = false;

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its
    // functionality by deriving from it!
    public DungeonXMLHandler() {
        System.out.println("Creating a DungeonXMLHandler");
    }

    // Used by code outside the class to get the list of Student objects
    // that have been constructed.
    public Dungeon getDungeon() {
        return dungeon;
    }

    public ObjectDisplayGrid getObjectDisplayGrid() {
        return odg;
    }

    // startElement is called when a <some element> is called as part of
    // <some element> ... </some element> start and end tags.
    // Rather than explain everything, look at the xml file in one screen
    // and the code below in another, and see how the different xml elements
    // are handled.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if (qName.equalsIgnoreCase("Dungeon")) {
            // Create dungeon and object display grid with provided properties
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            try {
                dungeon = Dungeon.getDungeon(name, gameHeight, width, topHeight, bottomHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                odg = ObjectDisplayGrid.getObjectDisplayGrid(gameHeight, width, topHeight, bottomHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (qName.equalsIgnoreCase("Room")) {
            int id = Integer.parseInt(attributes.getValue("room"));
            Room room = new Room("");
            room.setID(id);
            roomBeingParsed = room;
            displayableBeingParsed.push(room);
            dungeon.addRoom(room);
        } else if (qName.equalsIgnoreCase("Passage")) {
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));
            Passage passage = new Passage();
            passage.setID(room1, room2);
            passageBeingParsed = passage;
            dungeon.addPassage(passageBeingParsed);
            displayableBeingParsed.push(passage);
        } else if (qName.equalsIgnoreCase("Player")
                    || qName.equalsIgnoreCase("Monster")) {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));

            Creature creature = null;
            if (qName.equalsIgnoreCase("Player")) {
                creature = new Player();
            } else if (qName.equalsIgnoreCase("Monster")) {
                creature = new Monster();
            }
            creature.setName(name);
            creature.setID(room, serial);
            roomBeingParsed.addCreature(creature);
            creatureBeingParsed = creature;
            displayableBeingParsed.push(creature);
        } else if (qName.equalsIgnoreCase("Armor")
                    || qName.equalsIgnoreCase("Scroll")
                    || qName.equalsIgnoreCase("Sword")) {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));

            Item item = null;
            if (qName.equalsIgnoreCase("Armor")) {
                item = new Armor();
                item.setName(name);
            } else if (qName.equalsIgnoreCase("Scroll")) {
                item = new Scroll();
                item.setName(name);
            } else if (qName.equalsIgnoreCase("Sword")) {
                item = new Sword();
                item.setName(name);
            }
            item.setID(room, serial);
            itemBeingParsed = item;
            if (displayableBeingParsed.peek() instanceof Room) {
                roomBeingParsed.addItem(item);
            } else if (displayableBeingParsed.peek() instanceof Player) {
                if (itemBeingParsed instanceof Sword) {
                    ((Player) creatureBeingParsed).setSword(itemBeingParsed);
                } else if (itemBeingParsed instanceof Armor) {
                    ((Player) creatureBeingParsed).setArmor(itemBeingParsed);
                }
            }

            displayableBeingParsed.push(item);
        } else if (qName.equalsIgnoreCase("CreatureAction")
                    || qName.equalsIgnoreCase("ItemAction")) {
            String name = attributes.getValue("name");
            
            String type = attributes.getValue("type");
            Action action = new Action();
            //System.out.println("cccccccccccreature name: " + action.getName());
            if (qName.equalsIgnoreCase("CreatureAction")) {
                if (name.equals("Remove")) {
                    action = new Remove(name, creatureBeingParsed);
                } else if (name.equals("YouWin")) {
                    action = new YouWin(name, creatureBeingParsed);
                } else if (name.equals("UpdateDisplay")) {
                    action = new UpdateDisplay(name, creatureBeingParsed);
                } else if (name.equals("Teleport")) {
                    action = new Teleport(name, creatureBeingParsed);
                } else if (name.equals("ChangeDisplayedType")) {
                    action = new ChangeDisplayedType(name, creatureBeingParsed);
                } else if (name.equals("DropPack")) {
                    action = new DropPack(name, creatureBeingParsed);
                } else if (name.equals("EndGame")) {
                    action = new EndGame(name, creatureBeingParsed);
                }
                if (type.equals("death")) {
                    creatureBeingParsed.addDeathAction((CreatureAction) action);
                } else if (type.equals("hit")) {
                    creatureBeingParsed.addHitAction((CreatureAction) action);
                }
            } else if (qName.equalsIgnoreCase("ItemAction")) {
                if (name.equals("BlessArmor")) {
                    action = new BlessArmor(itemBeingParsed);
                } else if (name.equals("BlessCurseOwner")) {
                    action = new BlessCurseOwner(itemBeingParsed);
                } else if (name.equals("Hallucinate")) {
                    action = new Hallucinate(itemBeingParsed);
                }
                itemBeingParsed.setAction((ItemAction) action);
            }
            actionBeingParsed = action;
            actionBeingParsed.setName(name);
        } else if (qName.equalsIgnoreCase("visible")) {
            bVisible = true;
        } else if (qName.equalsIgnoreCase("posX")) {
            bPosX = true;
        } else if (qName.equalsIgnoreCase("posY")) {
            bPosY = true;
        } else if (qName.equalsIgnoreCase("width")) {
            bWidth = true;
        } else if (qName.equalsIgnoreCase("height")) {
            bHeight = true;
        } else if (qName.equalsIgnoreCase("actionMessage")) {
            bActionMessage = true;
        } else if (qName.equalsIgnoreCase("actionIntValue")) {
            bActionIntValue = true;
        } else if (qName.equalsIgnoreCase("actionCharValue")) {
            bActionCharValue = true;
        } else if (qName.equalsIgnoreCase("hp")) {
            bHP = true;
        } else if (qName.equalsIgnoreCase("hpMoves")) {
            bHPMoves = true;
        } else if (qName.equalsIgnoreCase("maxhit")) {
            bMaxHit = true;
        } else if (qName.equalsIgnoreCase("ItemIntValue")) {
            bItemIntValue = true;
        } else if (qName.equalsIgnoreCase("type")) {
            bType = true;
        } else {
            System.out.println("Unknown qname: " + qName);
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Room")) {
            roomBeingParsed = null;
            displayableBeingParsed.pop();
        } else if (qName.equalsIgnoreCase("Passage")) {
            passageBeingParsed = null;
            displayableBeingParsed.pop();
        } else if (qName.equalsIgnoreCase("Player")
                || qName.equalsIgnoreCase("Monster")) {
            creatureBeingParsed = null;
            displayableBeingParsed.pop();
        } else if (qName.equalsIgnoreCase("CreatureAction")
                || qName.equalsIgnoreCase("ItemAction")) {
            actionBeingParsed = null;
        } else if (qName.equalsIgnoreCase("Armor")
                || qName.equalsIgnoreCase("Scroll")
                || qName.equalsIgnoreCase("Sword")) {
            itemBeingParsed = null;
            displayableBeingParsed.pop();
        }

        if (bVisible) {
            if (data.toString().equals("1")) {
                displayableBeingParsed.peek().setVisible();
            } else {
                displayableBeingParsed.peek().setInvisible();
            }
            bVisible = false;
        } else if (bPosX) {
            displayableBeingParsed.peek().setPosX(Integer.parseInt(data.toString()));
            bPosX = false;
        } else if (bPosY) {
            displayableBeingParsed.peek().setPosY(Integer.parseInt(data.toString()));
            bPosY = false;
        } else if (bWidth) {
            displayableBeingParsed.peek().setWidth(Integer.parseInt(data.toString()));
            bWidth = false;
        } else if (bHeight) {
            displayableBeingParsed.peek().setHeight(Integer.parseInt(data.toString()));
            bHeight = false;
        } else if (bActionMessage) {
            actionBeingParsed.setMessage(data.toString());
            bActionMessage = false;
        } else if (bActionIntValue) {
            actionBeingParsed.setIntValue(Integer.parseInt(data.toString()));
            bActionIntValue = false;
        } else if (bActionCharValue) {
            actionBeingParsed.setCharValue(data.toString().charAt(0));
            bActionCharValue = false;
        } else if (bHP) {
            creatureBeingParsed.setHp(Integer.parseInt(data.toString()));
            bHP = false;
        } else if (bHPMoves) {
            ((Player) creatureBeingParsed).setHpMoves(Integer.parseInt(data.toString()));
            bHPMoves = false;
        } else if (bMaxHit) {
            creatureBeingParsed.setMaxHit(Integer.parseInt(data.toString()));
            bMaxHit = false;
        } else if (bItemIntValue) {
            itemBeingParsed.setIntValue(Integer.parseInt(data.toString()));
            bItemIntValue = false;
        }else if (bType) {
            creatureBeingParsed.setType(data.toString().charAt(0));
            bType = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

}
