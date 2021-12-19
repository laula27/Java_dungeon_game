package game;

import asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject {

    private static final int DEBUG = 0;
    private static final String CLASSID = ".ObjectDisplayGrid";

    private static AsciiPanel terminal;
    private Char[][] objectGrid = null;

    private List<InputObserver> inputObservers = null;

    private static int height;
    private static int width;

    public ObjectDisplayGrid(int _width, int _height) {
        width = _width;
        height = _height;

        terminal = new AsciiPanel(width, height);

        objectGrid = new Char[width][height];

        //initializeDisplay();

        super.add(terminal);
        super.setSize(width * 9, height * 16);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // super.repaint();
        // terminal.repaint( );
        super.setVisible(true);
        terminal.setVisible(true);
        terminal.addKeyListener(this);
        super.addKeyListener(this);
        inputObservers = new ArrayList<>();
        super.repaint();
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
            if (DEBUG > 0) {
                System.out.println(CLASSID + ".notifyInputObserver " + ch);
            }
        }
    }

    // we have to override, but we don't use this
    @Override
    public void keyPressed(KeyEvent even) {
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public final void initializeDisplay() {
        Char ch = new Char('.');
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                addObjectToDisplay(ch, i, j);
            }
        }
        terminal.repaint();
    }

    public void fireUp() {
        if (this.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Char ch, int x, int y) {
        if ((0 <= x) && (x < objectGrid.length)) {
            if ((0 <= y) && (y < objectGrid[0].length)) {
                objectGrid[x][y] = ch;
                writeToTerminal(x, y);
            }
        }
    }
    public Char getObjectFromDisplay(int x, int y) {
        if ((0 <= x) && (x < objectGrid.length)) {
            if ((0 <= y) && (y < objectGrid[0].length)) {
                return objectGrid[x][y];
            }
        }
        return new Char('0');
    }


    private void writeToTerminal(int x, int y) {
        char ch = objectGrid[x][y].getChar();
        terminal.write(ch, x, y);
        terminal.repaint();
    }

    public Char[][] getObjectGrid(){
        return objectGrid;
    }

    public final void copyDisplay(Char[][] og) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(og[i][j] != null){
                    addObjectToDisplay(og[i][j], i, j);
                }
            }
        }
        terminal.repaint();
    }
}
