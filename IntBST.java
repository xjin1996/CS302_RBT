// Binary search trees of nodes that contain integers
//
// CS 201 Exam 2

public class IntBST {
        
    // Instance variables
        
    protected int value;
    protected IntBST left;
    protected IntBST right;
    protected IntBST parent;
        
    // Constructors
                
    public IntBST(int val) {
        value = val;
        left = null;
        right = null;
        parent = null;
    }
                
    public IntBST(int val, IntBST lt, IntBST rt) {
        value = val;
        setLeft(lt);    // also takes care of parent pointer of lt
        setRight(rt);   // also takes care of parent pointer of rt
    }
        
    // Instance methods: 
        
    public int value() {
        // returns the integer stored at the current root node of the tree
        return value;
    }
        
    public IntBST left() {
        // returns the left subtree of the tree
        return left;
    }
        
    public IntBST right() {
        // returns the right subtree of the tree
        return right;
    }
        
    public IntBST parent() {
        // returns the parent node of the tree
        return parent;
    }

    
    public boolean isLeaf() {
        // returns true if tree has no children
        return left() == null && right() == null;
    }

    public boolean isLeftChild() {
        // returns true if this is a left child of parent.
        return parent() != null && this == parent().left();
    }

    public boolean isRightChild() {
        // returns true if this is a right child of parent.
        return parent() != null && this == parent().right();
    }

    
    public void setValue(int newValue) {
        // sets the integer in the current node of the tree to a new value
        value = newValue;
    }
        
    public void setLeft(IntBST newLeft) {
        // changes to the left subtree to a new tree and
        // re-parents newLeft if not null
        if (left != null && (left.parent() == this))
            left.setParent(null); // remove old left's parent pointer
        left = newLeft;
        if (left != null)
            left.setParent(this); // set new left's parent pointer
    }
        
    public void setRight(IntBST newRight) {
        // changes the right subtree to a new tree and
        // re-parents newRight if not null
        if (right != null && (right.parent() == this))
            right.setParent(null); // remove old right's parent pointer
        right = newRight;
        if (right != null)
            right.setParent(this); // set new right's parent pointer
    }

    protected void setParent(IntBST newParent) {
        // re-parents this node to newParent
        parent = newParent;
    }
        
    public IntBST copy() {
        // returns a copy of this tree
        IntBST lt = left();
        IntBST rt = right();
        if (lt != null)
            lt = lt.copy();
        if (rt != null)
            rt = rt.copy();
        return new IntBST(value(), lt, rt);
    }

    
    // ------------------------------------------------------------
    // contains, add
    
    public boolean contains(int wanted) {
        // returns true iff wanted is a value found within the tree
        IntBST node = locate(wanted);
        return wanted == node.value();
    }
    
    public void add(int val) {
        // adds val to the binary search tree.
        IntBST newNode = new IntBST(val);

        IntBST insertLocation = locate(val);
        int nodeValue = insertLocation.value();
        if (nodeValue < val) {
            insertLocation.setRight(newNode);
        } else {
            if (insertLocation.left() != null)
                // if value is in tree we insert just before
                insertLocation.predecessor().setRight(newNode);
            else
                insertLocation.setLeft(newNode);
        }
    }
    
    protected IntBST locate(int wanted) {
        // returns: 1 - existing tree node with the desired value, or
        //          2 - the node to which value should be added.

        if (value() == wanted)
            return this;
        else {
            IntBST child;
            if (value() < wanted)
                child = right();
            else
                child = left();
            
            if (child == null)
                return this;
            else
                return child.locate(wanted);
        }
    }

    protected IntBST predecessor() {
        // returns pointer to predecessor of root
        Assert.pre(left() != null, "Root has left child.");
        IntBST result = left();
        while (result.right() != null) {
            result = result.right();
        }
        return result;
    }
    

    // ------------------------------------------------------------
    // rotations


    protected void rotateRight() {
        // pre: this node has a left subtree
        // post: rotates local portion of tree so left child is root
        Assert.pre(left() != null, "Root has left child.");
        IntBST parent = parent();
        IntBST newRoot = left();
        boolean wasLeftChild = isLeftChild();

        // hook in new root (sets newRoot's parent, as well)
        setLeft(newRoot.right());

        // put pivot below it (sets this's parent, as well)
        newRoot.setRight(this);

        if (parent != null) {
            if (wasLeftChild) parent.setLeft(newRoot);
            else              parent.setRight(newRoot);
        }
    }

    protected void rotateLeft() {
        // pre: this node has a right subtree
        // post: rotates local portion of tree so right child is root
        Assert.pre(right() != null, "Root has right child.");
        IntBST parent = parent();
        IntBST newRoot = right();
        boolean wasRightChild = isRightChild();

        // hook in new root (sets newRoot's parent, as well)
        setRight(newRoot.left());

        // put pivot below it (sets this's parent, as well)
        newRoot.setLeft(this);

        if (parent != null) {
            if (wasRightChild) parent.setRight(newRoot);
            else               parent.setLeft(newRoot);
        }
    }

    // ------------------------------------------------------------
    // printing

    
    public static void printBST(IntBST t) {
        // displays a printed representation of tree t using printBST1
        printBST1(t, 0, true);
    }
        
    public static void printBST1(IntBST t, int level, boolean indent) {
        if (indent) {
            for (int i=0; i<level; i++)
                System.out.print("    ");
        } else {
            System.out.print("  ");
        }
        if (t==null) {
            System.out.println(" .");
        } else {
            System.out.printf("%2d", t.value());
            if (t.isLeaf()) {
                System.out.println();
            } else {
                printBST1(t.right(), level+1, false);
                printBST1(t.left(), level+1, true);
            }
        }
    }

}
