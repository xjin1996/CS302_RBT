// Operations on binary search trees of integers
//
// for CS 201 Exam 2
//
// Name: Guanghan Pan

public class IntBSTops {

	// problem 1 --------------------------------------------------

	public static int height(IntBST t) {
		// returns height of t  (==1 if t is leaf!)
		if (t == null) {
			return 0;
		} else {
			return 1 + Math.max(height(t.left()), height(t.right()));
		}
	}

	public static int balanceFact(IntBST t) {
		// returns balance factor of t, i.e. height(right) - height(left)

		return height(t.right)-height(t.left);

	}

	public static boolean isBalanced(IntBST t) {
		// returns whether t is balanced
		if (t == null || t.isLeaf()) {
			return true;
		} else {
			return Math.abs(balanceFact(t)) <= 1
					&& isBalanced(t.right())
					&& isBalanced(t.left());
		}
	}

	// problem 2 --------------------------------------------------
	// depends on answers from problem 1

	public static void balance(IntBST t) {
		// finds unbalanced nodes in tree and performs rotation
		if (!isBalanced(t.left())) {
			balance(t.left());
			balance(t);
		} else if (!isBalanced(t.right())) {
			balance(t.right());
			balance(t);
		} else if (!isBalanced(t)) {
			if (balanceFact(t)<-1) {
				if(balanceFact(t.left())>0) 
					t.left.rotateLeft();
				t.rotateRight();
			} else { //balanceFact(t) > 1
				if(balanceFact(t.right())<0) 
					t.right.rotateRight();
				t.rotateLeft();
			}
		}

	}


	// problem 3 --------------------------------------------------

	public static int width(IntBST t) {
		// returns width of t:
		// 0 if t is null
		// 2 if t is a leaf
		// if one child is null, 1 + width of the other child
		// otherwise, the sum of the widths of t's subtrees
		if (t == null) {
			return 0;
		} else if (t.isLeaf()) {
			return 2;
		} else if (t.left() == null) {
			return 1 + width(t.right());
		} else if (t.right() == null) {
			return 1 + width(t.left());
		} else {
			return width(t.left()) + width(t.right());
		}

	}

	public static int leftWidth(IntBST t) {
		// returns width of left "half" of t:
		// 0 if t is null
		// otherwise, the width of t's left subtree (but at least 1)

		if (t == null) {
			return 0;
		} else {
			int i= width(t.left());
			if (i > 1) return i;
			return 1;
		}
	}

	public static int rightWidth(IntBST t) {
		// returns width of right "half" of t:
		// 0 if t is null
		// otherwise, the width of t's right subtree (but at least 1)
		if (t == null) {
			return 0;
		} else {
			int i= width(t.right());
			if (i > 1) return i;
			return 1;
		}
	}

	// problem 4 --------------------------------------------------

	public static IntBST smallest(IntBST t) {
		// returns first node in t
		IntBST temp = t;
		while(temp.left() != null) {
			temp = temp.left();			
		}
		return temp;

	}

	public static IntBST largest(IntBST t) {
		// returns last node in t
		IntBST temp = t;
		while(temp.right() != null) {
			temp = temp.right();			
		}
		return temp;

	}

	// problem 5 --------------------------------------------------

	public static IntBST next(IntBST t) {
		// returns next node after current according to in-order traversal
		if (t.right() != null) {
			return smallest(t.right());
		} else if(t.isLeftChild()) {
			return t.parent();
		} else { // t is right child
			IntBST temp = t;
			while (temp.isRightChild()) {
				temp = temp.parent();
			}
			return temp.parent();
		}

	}

	// -------------------------------------------------------------

	public static String traverseRec(IntBST t) {
		// creates string with values in t using recursive in-order traversal
		StringBuffer sb = new StringBuffer(" ");
		traverseRec1(sb, t);
		return sb.toString();
	}   
	private static void traverseRec1(StringBuffer sb, IntBST t) {
		if (t != null) {
			traverseRec1(sb, t.left());
			sb.append(t.value() + " ");
			traverseRec1(sb, t.right());
		}
	}
	public static String traverseLoop(IntBST t) {
		// creates string with values in t using loop
		StringBuffer sb = new StringBuffer(" ");
		for (IntBST node = smallest(t); node != null; node = next(node))
			sb.append(node.value() + " ");
		return sb.toString();
	}
}
