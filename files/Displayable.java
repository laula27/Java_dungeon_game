package files;

public class Displayable {

    private boolean visible;
    private boolean invisible;
    private int posX;
    private int posY;
    private char type;
    private int hp;
    private int hpMoves;
    private int maxHit;
    private int intValue;
    private int width;
    private int height;

    public void setVisible() {
        visible = true;
        System.out.println("   visible");
    }

    public void setInvisible() {
        invisible = false;
        System.out.println("   invisible");

    }

    public void setPosX(int _posX) {
        posX = _posX;
        //System.out.println("   posX: " + posX);
    }

    public int getPosX(){
        return posX;
    }

    public void setPosY(int _posY) {
        posY = _posY;
        //System.out.println("   posY: " + posY);
    }

    public int getPosY(){
        return posY+2;
    }

    public void setType(char t) {
        type = t;
        System.out.println("   type: " + type);
    }

    public char getType(){
        return type;
    }

    public void setHp(int _hp) {
        hp = _hp;
    }

    public int getHp(){
        return hp;
    }

    public void setHpMoves(int _hpMoves) {
        hpMoves = _hpMoves;
        System.out.println("   hpMoves: " + hpMoves);
    }

    public int getHpMoves(){
        return hpMoves;
    }

    public void setMaxHit(int _maxHit) {
        maxHit = _maxHit;
        System.out.println("   maxHit: " + maxHit);
    }

    public int getMaxHit(){
        return maxHit;
    }

    public void setIntValue(int _intValue) {
        intValue = _intValue;
        System.out.println("   intValue: " + intValue);
    }

    public void setWidth(int _width) {
        width = _width;
        System.out.println("   width: " + width);
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int _height) {
        height = _height;
        System.out.println("   height: " + height);
    }

    public int getWidth(){
        return width;
    }
}
