// Applet for CS 201 Exam 2
//
// Visualizes operations on balanced (or not) binary search trees

import java.applet.*;
import java.awt.*;
import java.util.*; // for Random
import java.awt.event.*;

public class IntBSTApplet extends Applet
    implements ActionListener, ItemListener {
    private static final long serialVersionUID = 1L; // to avoid warning
    
    // instance variables for all components that we need access to
    TextField addNum;
    Choice addChoice, drawChoice;
    Button addButton, removeButtonL, removeButtonR, balanceButton, clearButton;
    IntBSTcanvas c;
    Random rand = new Random();
    
    // this initializes the applet
    public void init() {
        // Font to use in applet
        setFont(new Font("TimesRoman", Font.BOLD, 14));

        setLayout(new BorderLayout());
        add("North", makeTitleLabel());
        add("South", makeBottomPanel());
        c = new IntBSTcanvas();
        c.setBackground(Color.white);
        add("Center", c);
    }

    // Creates label to be displayed at top
    public Label makeTitleLabel() {
        Label titleLabel = new Label("CS 201 Exam 2 Applet");
        titleLabel.setAlignment(Label.CENTER);
        titleLabel.setBackground(Color.blue);
        titleLabel.setForeground(Color.white);
        return titleLabel;
    }

    // Creates panel to be displayed at bottom
    public Panel makeBottomPanel() {
        Panel p = new Panel();
        p.setLayout(new GridLayout(2, 1));
        p.add(makeButtonPanel());
        p.add(makeChoicePanel());
        return p;
    }

    // Creates panel with buttons
    public Panel makeButtonPanel() {
        Panel p = new Panel();
        p.setLayout(new GridLayout(1, 5));
        
        // add button (also contains a text field)
        addNum = new TextField("50", 3);
        addButton = new Button("add");
        addButton.setBackground(Color.yellow);
        addButton.addActionListener(this);

        Panel addPanel = new Panel();
        addPanel.setLayout(new BorderLayout());
        addPanel.add("West", addNum);
        addPanel.add("Center", addButton);
        p.add(addPanel);
        
        removeButtonL = new Button("remove L");
        removeButtonL.setBackground(Color.orange);
        p.add(removeButtonL);
        removeButtonL.addActionListener(this);

        removeButtonR = new Button("remove R");
        removeButtonR.setBackground(Color.orange);
        p.add(removeButtonR);
        removeButtonR.addActionListener(this);
        
        balanceButton = new Button("balance");
        balanceButton.setBackground(Color.green);
        p.add(balanceButton);
        balanceButton.addActionListener(this);
        
        clearButton = new Button("clear");
        clearButton.setBackground(Color.red);
        p.add(clearButton);
        clearButton.addActionListener(this);

        return p;
    }

    // Creates panel with menus
    public Panel makeChoicePanel() {
        Panel p = new Panel();
        p.setLayout(new GridLayout(1, 2));
        p.setBackground(Color.cyan);

        Panel p1 = new Panel();
        p1.setLayout(new BorderLayout());
        p1.add("West", new Label("add sequence:"));
        addChoice = new Choice();
        addChoice.addItem("random");
        addChoice.addItem("increasing");
        addChoice.addItem("decreasing");
        p1.add("Center", addChoice);
        p.add(p1);
        
        Panel p2 = new Panel();
        p2.setLayout(new BorderLayout());
        p2.add("West", new Label("drawing style:"));
        drawChoice = new Choice();
        drawChoice.addItem("fixed");
        drawChoice.addItem("variable");
        p2.add("Center", drawChoice);
        p.add(p2);
        drawChoice.addItemListener(this);
        
        return p;
    }
        

    // This method is called by java to handle events on buttons
    public void actionPerformed(ActionEvent e)  {
        if (e.getSource() == addButton) {
            int n = Integer.parseInt(addNum.getText()); // get number
            c.add(n);
            if (addChoice.getSelectedItem().equals("random"))
                n = Math.abs(rand.nextInt()) % 100; // random number in 0..99
            else if (addChoice.getSelectedItem().equals("increasing"))
                n++;
            else
                n--;
            addNum.setText(Integer.toString(n));
        } else if (e.getSource() == removeButtonL) {
            c.removeFirst();
        } else if (e.getSource() == removeButtonR) {
            c.removeLast();
        } else if (e.getSource() == balanceButton) {
            c.balance();
        } else if (e.getSource() == clearButton) {
            c.clear();
        }
    }

    
    // This method is called by java to handle events on choice menus
    public void itemStateChanged(ItemEvent e)  {
        if (e.getSource() == drawChoice) {
            int drawStyle = drawChoice.getSelectedIndex();
            c.setDrawStyle(drawStyle);
        }
    }
}


// IntBSTcanvas:  an object that displays a BST of integers

class IntBSTcanvas extends Canvas {
    private static final long serialVersionUID = 1L; // to avoid warning

    // instance variables
    IntBST tree;        // pointer to root of tree
    int drawStyle;      // how tree is drawn (0 - fixed, 1 - variable)

    // constructor:
    
    public IntBSTcanvas() {
        tree = null;
        drawStyle = 0;
    }

    public void setDrawStyle(int n) {
        // set draw style
        drawStyle = n;
        repaint();
    }
    
    public void add(int n) {
        // add n to tree and redraw the picture
        if (tree == null)
            tree = new IntBST(n);
        else
            tree.add(n);
        repaint();
    }

    public void removeFirst() {
        // if possible, remove left-most element and redraw the picture
        if (tree != null) {
            if (tree.left() == null) { // special case if root is left-most
                IntBST oldroot = tree;
                tree = tree.right();
                oldroot.setRight(null); // also removes tree's parent pointer
            } else { // "regular" case: left-most element has parent
                IntBST leftmost = smallest(tree);
                if (leftmost != null) { // i.e. problem 4 has been solved
                    IntBST parent = leftmost.parent();
                    parent.setLeft(leftmost.right());
                }
            }
            repaint();
        }
    }

    public void removeLast() {
        // if possible, remove right-most element and redraw the picture
        if (tree != null) {
            if (tree.right() == null) { // special case if root is right-most
                IntBST oldroot = tree;
                tree = tree.left();
                oldroot.setLeft(null); // also removes tree's parent pointer
            } else { // "regular" case: right-most element has parent
                IntBST rightmost = largest(tree);
                if (rightmost != null) { // i.e. problem 4 has been solved
                    IntBST parent = rightmost.parent();
                    parent.setRight(rightmost.left());
                }
            }
            repaint();
        }
    }

    public void balance() {
        // balance tree and redraw the picture
        IntBST temp = new IntBST(0, tree, null); // create temporary parent
        balance(tree);
        tree = temp.left(); // reset root of tree in case it has changed
        temp.setLeft(null); // also remove's tree's parent pointer
        repaint();
    }

    public void clear() {
        // clear the tree and redraw the picture
        tree = null;
        repaint();
    }

    public void paint (Graphics g) {
        // This method is called by Java when the window is changed (e.g.,
        // uncovered or resized), or when "repaint()" is called.
        Dimension d = getSize();   // size of canvas

        // print string at top
        g.setColor(Color.black);
        if (isBalanced(tree))
            centerString(g, "Balanced", d.width/2, 10);
        else
            centerString(g, "NOT Balanced", d.width/2, 10);

        // draw tree
        int radius = 10;
        int dy = 40;
        int y0 = 30;
        if (drawStyle == 0) { // draw with fixed width
            int l = radius;
            int r = d.width-radius;
            drawTree(g, tree, l, r, y0, dy, radius);
        } else { // draw with variable width
            int spacing = 3*radius/2;
            int offset =  spacing*width(tree)/2 - spacing*leftWidth(tree);
            int x =  d.width/2 - offset;
            drawTree2(g, tree, x, y0, dy, radius, spacing);
        }

        // print values at bottom
        String seq1 = "rec.: ";
        String seq2 = "loop: ";
        if (tree != null) {
            seq1 += traverseRec(tree);  // recursive
            seq2 += traverseLoop(tree); // loop
        }
        g.setColor(Color.black);
        g.drawString(seq1, 5, d.height - 25);
        g.drawString(seq2, 5, d.height - 10);
    }


    public static void drawTree(Graphics g, IntBST t, int l, int r,
                                int y, int dy, int rad) {
        // FIXED-WIDTH DRAWING
        //
        // Draws the tree t at height y with root centered between l and r.
        // The next level is drawn at height y+dy.
        // Each leaf is drawn as a circle with radius rad.
        int x = (l+r)/2;
        g.setColor(Color.black);
        if (t == null)
            g.fillOval(x-3, y-3, 7, 7);
        else {
            int y2 = y+dy;
            if (t.left() != null) {
                g.setColor(Color.black);
                g.drawLine(x, y, (l+x)/2, y2);
                drawTree(g, t.left(),  l, x, y2, dy, rad);
            }
            if (t.right() != null) {
                g.setColor(Color.black);
                g.drawLine(x, y, (x+r)/2, y2);
                drawTree(g, t.right(), x, r, y2, dy, rad);
            }
            drawNode(g, t, x, y, rad);
        }
    }

    public static void drawTree2(Graphics g, IntBST t, int x,
                                 int y, int dy, int rad, int spacing) {
        // VARIABLE-WIDTH DRAWING
        //
        // Draws the tree t centered around x with its root at height y.
        // The next level is drawn at height y+dy.
        // Each leaf is drawn as a circle with radius rad.
        // subtrees are separated no more space than necessary.  two
        // adjacent leaves are separated by 'spacing'
        g.setColor(Color.black);
        if (t == null)
            g.fillOval(x-3, y-3, 7, 7);
        else {
            int y2 = y+dy;
            if (t.left() != null) {
                int xl = x - spacing * rightWidth(t.left());
                g.setColor(Color.black);
                g.drawLine(x, y, xl, y2);
                drawTree2(g, t.left(), xl, y2, dy, rad, spacing);
            }
            if (t.right() != null) {
                int xr = x + spacing * leftWidth(t.right());
                g.setColor(Color.black);
                g.drawLine(x, y, xr, y2);
                drawTree2(g, t.right(), xr, y2, dy, rad, spacing);
            }
            drawNode(g, t, x, y, rad);
        }
    }

    public static void drawNode(Graphics g, IntBST t, int x, int y, int rad) {
        // Draws circle representing root of t.  print balance factor
        // underneath the node; also use color coding.
        int bal = balanceFact(t);
        g.setColor(balanceColor(bal));
        g.fillOval(x-rad, y-rad, 2*rad, 2*rad);
        g.setColor(Color.black);
        g.drawOval(x-rad, y-rad, 2*rad, 2*rad);
        centerString(g, (new Integer(bal)).toString(), x, y+2*rad);
        if (Math.abs(bal) > 1)
            g.setColor(Color.white);
        centerString(g, (new Integer(t.value())).toString(), x, y);
    }

    public static void centerString(Graphics g, String s, int x, int y) {
        // draw a String centered at x, y
        FontMetrics fm = g.getFontMetrics(g.getFont());
        int xs = x - fm.stringWidth(s)/2 + 1;
        int ys = y + fm.getAscent()/3 + 1;
        g.drawString(s, xs, ys);
    }

    public static Color balanceColor(int n) {
        // returns a color representing "imbalance" of tree:
        // 0  (balanced)    yellow
        // <0 (left heavy)  red   (the more out of balance, the darker)
        // >0 (right heavy) green (the more out of balance, the darker)

        Color result = Color.yellow;
        if (n<0) { // red
            result = new Color(255, Math.max(0, 320+n*120), 0);
            n = -n-2;
        } else if (n>0) { // green
            result = new Color(Math.max(0, 255-n*120), 255, 0);
            n = n-1;
        }
        while (n>0) {
            result = result.darker();
            n--;
        }
        return result;
    }

    // local copies of some IntBSTops methods
    
    public static IntBST smallest(IntBST t) {
        return IntBSTops.smallest(t); }
    public static IntBST largest(IntBST t) {
        return IntBSTops.largest(t); }
    public static String traverseRec(IntBST t) {
        return IntBSTops.traverseRec(t); }
    public static String traverseLoop(IntBST t) {
        return IntBSTops.traverseLoop(t); }
    public static int balanceFact(IntBST t) {
        return IntBSTops.balanceFact(t); }
    public static boolean isBalanced(IntBST t) {
        return IntBSTops.isBalanced(t); }
    public static void balance(IntBST t) {
        IntBSTops.balance(t); }
    public static int width(IntBST t) {
        return IntBSTops.width(t); }
    public static int leftWidth(IntBST t) {
        return IntBSTops.leftWidth(t); }
    public static int rightWidth(IntBST t) {
        return IntBSTops.rightWidth(t); }
    
}
