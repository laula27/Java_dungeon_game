package files;
public class Action{

    private String name = "";
    private String msg;
    private int v;
    private char c;
    
    public Action() {
        System.out.println("Creating action");
    }

    public void setName(String _name){
        name = _name;
    }

    public String getName(){
        return name;
    }

    public void setMessage(String _msg){
        msg = _msg;
        System.out.println("   actionMessage: " + msg);
    }

    public String getMessage(){
        return msg;
    }

    public void setIntValue(int _v){
        v = _v;
        System.out.println("   intValue: " + v);
    }

    public int getIntValue(){
        return v;
    }

    public void setCharValue(char _c){
        c = _c;
        System.out.println("   charValue: " + c);
    }

    public char getCharValue(){
        return c;
    }
}
