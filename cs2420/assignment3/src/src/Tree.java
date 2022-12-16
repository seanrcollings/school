// ******************ERRORS********************************
// Throws UnderflowException as appropriate
package src;
import javax.swing.tree.TreeNode;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    final String ENDLINE = "\n";
    private BinaryNode<E> root;  // Root of tree
    private BinaryNode<E> curr;  // Last node accessed in tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     *
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create BST from Array
     *
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.length; i++) {
            bstInsert(arr[i]);
        }
    }

    public static void getLeaves(Integer[] preorder, int beg, int end, ArrayList<Integer> leaves) {
        if (beg > end) return;
        if (beg == end) {
            leaves.add(preorder[beg]);
            return;
        }
        if (preorder[beg] < preorder[beg + 1]) leaves.add(preorder[beg]);
        getLeaves(preorder, beg + 1, end, leaves);
    }

    /**
     * Create Tree By Level.  Parents are set.
     * This runs in  O(n)
     * @param arr   List of elements to be added
     * @param label Name of tree
     */
    public void createTreeByLevel(E[] arr, String label) {
        treeName = label;
        if (arr.length <= 0) {
            root = null;
            return;
        }

        ArrayList<BinaryNode<E>> nodes = new ArrayList<BinaryNode<E>>();
        root = new BinaryNode<E>(arr[0]);
        nodes.add(root);
        BinaryNode<E> newr = null;
        for (int i = 1; i < arr.length; i += 2) {
            BinaryNode<E> curr = nodes.remove(0);
            BinaryNode<E> newl = new BinaryNode<E>(arr[i], null, null, curr);
            nodes.add(newl);
            newr = null;
            if (i + 1 < arr.length) {
                newr = new BinaryNode<E>(arr[i + 1], null, null, curr);
                nodes.add(newr);
            }

            curr.left = newl;
            curr.right = newr;
        }
    }

    /**
     * Change name of tree
     *
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents as a tree with one node per line
     */
    public String toString() {
        if (root == null)
            return (treeName + " Empty tree\n");
        else
            return treeName + "\n" + toString(root, 0);
    }

    /**
     * This runs in  O(n)
     * Return a string displaying the tree contents as a single line
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root);
    }

    /**
     * Find successor of "curr" node in tree
     *
     * @return String representation of the successor
     */
    public String successor() {
        if (curr == null) curr = root;
        curr = successor(curr, curr.element);
        if (curr == null) return "null";
        else return curr.toString();
    }

    private BinaryNode<E> successor(BinaryNode<E> t, E value) {
        if (t == null) return null;

        if (t.element == value && t.right != null) { // successor is leftmost in right subtree
            t = t.right;
            while (t.left != null) {
                t = t.left;
            }
            return t;
        }

        // Traverse up the tree until p.left = t
        if (t.parent.left == t) { return t.parent; }

        return successor(t.parent, value);
    }

    /**
     * Print all paths from root to leaves
     */
    public void printAllPaths() {
        printAllPaths(root, "");
    }

    private void printAllPaths(BinaryNode<E> t, String currentPath) {
        if (t == null) return;
        if (isLeaf(t)) {
            System.out.println(currentPath + " " + t.element);
        }
        printAllPaths(t.left, currentPath + " " + t.element);
        printAllPaths(t.right, currentPath + " " + t.element);
    }


    /**
     * Counts all non-null binary search trees embedded in tree
     *
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        return countBST(root);
    }

    private Integer countBST(BinaryNode<E> t) {
        if (t == null) return 0;
        if (isLeaf(t)) return 1;
        int subTreeNum = countBST(t.left) + countBST(t.right);
        if (subTreeNum == 2) return subTreeNum + 1;
        return subTreeNum;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     *
     * @param x the item to insert.
     */
    public void bstInsert(E x) {

        root = bstInsert(x, root, null);
    }

    /**
     * Determines if item is in tree
     *
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return bstContains(item, root);
    }

    /**
     * Remove all paths from tree that sum to less than given value
     *
     * @param sum: minimum path sum allowed in final tree
     */
    public void pruneK(Integer sum) {
        pruneK(root, sum);
    }

    private int pruneK(BinaryNode<E> t, int sum) {
        if (t == null) return sum;

        int diff = sum - (Integer)t.element;
        int leftDiff = pruneK(t.left, diff);
        if (leftDiff > 0) {
            t.left = null;
        }

        int rightDiff = pruneK(t.right, diff);
        if (rightDiff > 0) {
            t.right = null;
        }

        return Math.min(leftDiff, rightDiff);
    }

    /**
     * Find the least common ancestor of two nodes
     *
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     */
    public String lca(E a, E b) {
        BinaryNode<E> ancestor = null;
        if (a.compareTo(b) < 0) {
            ancestor = lca(root, a, b);

        }  else {
            ancestor = lca(root, b, a);
        }
        if (ancestor == null) return "none";
        else return ancestor.toString();
    }

    private BinaryNode<E> lca(BinaryNode<E> t, E smaller, E bigger) {
        if (t == null) return null;
        if (t.element.compareTo(smaller) > 0 && t.element.compareTo(bigger) < 0) {
            return t;
        }
        if (t.element.compareTo(smaller) == 0 || t.element.compareTo(bigger) == 0) {
            return t;
        }
        else if (t.element.compareTo(smaller) < 0) {
            return lca(t.right, smaller, bigger);
        } else {
            return lca(t.left, smaller, bigger);
        }
    }

    /**
     * Balance the tree
     */
    public void balanceTree() {
        ArrayList<E> inorder = inorder();
        root = null;
        root = balanceTree(inorder, 0, inorder.size() - 1, null);
    }

    private BinaryNode<E> balanceTree(ArrayList<E> items, int low, int high, BinaryNode<E> parent) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> newNode =  bstInsert(items.get(mid), root, parent);
        newNode.left = balanceTree(items,low, mid - 1, newNode);
        newNode.right = balanceTree(items, mid + 1, high, newNode);
        return newNode;
    }

    public ArrayList<E> inorder() {
        return inorder(root);
    }

    private ArrayList<E> inorder(BinaryNode<E> curr) {
        if (curr == null) return null;
        ArrayList<E> list = new ArrayList<>();
        if (curr.left != null) list.addAll(inorder(curr.left));
        list.add(curr.element);
        if (curr.right != null) list.addAll(inorder(curr.right));
        return list;
    }

    /**
     * In a BST, keep only nodes between range
     *
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        root = keepRange(root, a, b);
    }

    private BinaryNode<E> keepRange(BinaryNode<E> curr, E min, E max) {
        if (curr == null) return null;

        curr.left = keepRange(curr.left, min, max);
        curr.right = keepRange(curr.right, min, max);

        if (curr.element.compareTo(min) < 0) {
            if (curr.right != null) curr.right.parent = curr.parent;
            return curr.right;
        }
        if (curr.element.compareTo(max) > 0) {
            if (curr.left != null) curr.left.parent = curr.parent;
            return curr.left;
        }

        return curr;
    }

    /**
     * @return for the level with maximum sum, print the sum of the nodes
     */
    public void maxLevelSum() {
        System.out.println("Max level sum is: " + maxLevelSum(root, 1));
    }

    private int maxLevelSum(BinaryNode<E> t, int level) {
        if (t == null) return 0;
        if (level == 1) {
            int maxSum = (Integer)t.element;
            int height = getHeight();
            for (level = 2; level <= height; level++) {
                int sum = maxLevelSum(t.left, level) + maxLevelSum(t.right, level);
                System.out.println("Max Sum: " + maxSum + " New Sum: " + sum);
                if (sum > maxSum) maxSum = sum;
            }
            return maxSum;
        }

        int myLevel = getLevel(t.element);
        if (level == myLevel) {
            return (Integer)t.element;
        }
        return maxLevelSum(t.left, level) + maxLevelSum(t.right, level);
    }

    public int getLevel(E search) {
        return getLevel(root, search, 1);
    }

    private int getLevel(BinaryNode<E> curr, E search, int level) {
        if (curr == null) return -1;
        if (curr.element.compareTo(search) == 0) return level;

        int leftLevel = getLevel(curr.left, search, level + 1);
        return leftLevel != -1 ? leftLevel : getLevel(curr.right, search, level + 1);
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(BinaryNode<E> curr) {
        if (curr == null) return 0;
        return Math.max(getHeight(curr.left) + 1, getHeight(curr.right) + 1);
    }



    /**
     * @return true if the tree is a Sum Tree (every non-leaf node is sum of nodes in subtree)
     */
    public boolean isSum() {
        return isSum(root);
    }

    private boolean isSum(BinaryNode<E> curr) {
        if (curr == null) { return true; }
        if (isLeaf(curr)) { return true;}
        int value = (Integer)curr.element;
        // Not really that efficient. Have to compute some of the sums a bunch of times
        int childSum = sum(curr.left) + sum(curr.right);

        return value == childSum && isSum(curr.left) && isSum(curr.right);
    }

    private int sum(BinaryNode<E> curr) {
        if (curr == null) return 0;
        return sum(curr.left) + (Integer)curr.element + sum(curr.right);
    }

    // Having this "global" max value feels icky
    // would love to see a different way to do this
    // I think that it would be easier in a language with multiple
    // return values (like Python). I initially tried to implement this
    // using a length 2 array with the larger sub-traversal and the max
    // But honestly that become grosser quicker and I don't think it would have
    // even worked in the same ways this does.
    private class Max {public int value = -1111111111;}

    /**
     * @return the sum of the maximum path between any two leaves
     */
    public Integer maxPath() {
        Max max = new Max();
        maxPath(root, max);
        return max.value;
    }

    private Integer maxPath(BinaryNode<E> curr, Max max) {
        if (curr == null) return 0;
        if (isLeaf(curr)) return (Integer)curr.element;

        int leftSum = maxPath(curr.left, max);
        int rightSum = maxPath(curr.right, max);
        int value = (Integer)curr.element;

        if (curr.isFull()) {
            // Set the max if it's traversal is bigger
            max.value = Math.max(max.value, leftSum + rightSum + value);
            // return the larger sub-traversal
            return Math.max(leftSum, rightSum) + value;
        }

        return ((curr.left == null) ? rightSum : leftSum ) + value;
    }

    private static class Positions {
        int pre = 0;
        int post = 0;
    }

    public void createTreeTraversals(E[] preorder, E[] postorder, String name) {
        this.treeName = name;
        this.root = createTreeTraversals(preorder, postorder, new Positions(), null);
    }

    private BinaryNode<E> createTreeTraversals(E[] preorder, E[] postorder, Positions pos, BinaryNode<E> parent) {
        if (pos.pre >= preorder.length) return null;

        BinaryNode<E> newNode = new BinaryNode<>(preorder[pos.pre], null, null, parent);
        if (postorder[pos.post].compareTo(preorder[pos.pre]) != 0) {
            pos.pre++;
            newNode.left = createTreeTraversals(preorder, postorder, pos, newNode);
            pos.pre++;
            newNode.right = createTreeTraversals(preorder, postorder, pos, newNode);
            if (newNode.element.compareTo(postorder[pos.post]) == 0) {
                pos.post++;
            }
        } else {
            pos.post++;
        }

        return newNode;
    }


    //PRIVATE
    private boolean isLeaf(BinaryNode<E> node) {return node != null && node.left == null && node.right == null;}

    /**
     * @param preorderT  preorder traversal of tree
     * @param postorderT postorder traversal of tree
     * @param name       of tree
     *                   create a full tree (every node has 0 or 2 children) from its traversals.  This is not a BST.
     */


    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t, BinaryNode<E> parent) {
        if (t == null)
            return new BinaryNode<>(x, null, null, parent);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0) {
            t.left = bstInsert(x, t.left, t);
        } else {
            t.right = bstInsert(x, t.right, t);
        }

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     *          SIDE EFFECT: Sets local variable curr to be the node that is found
     * @return node containing the matched item.
     */
    private boolean bstContains(E x, BinaryNode<E> t) {
        curr = null;
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return bstContains(x, t.left);
        else if (compareResult > 0)
            return bstContains(x, t.right);
        else {
            curr = t;
            return true;    // Match
        }
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     *
     * @param t the node that roots the subtree.
     */
    private String toString2(BinaryNode<E> t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.left));
        sb.append(t.element.toString() + " ");
        sb.append(toString2(t.right));
        return sb.toString();
    }

    /**
     * Internal method to return a string of items in the tree in order
     * This routine runs in O(??)
     *
     * @param t the node that roots the subtree.
     */
    private String toString(BinaryNode<E> t, int level) {
        if (t == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(toString(t.right, level + 1));
        sb
                .append("   ".repeat(level))
                .append(t.toString())
                .append("\n");
        sb.append(toString(t.left, level + 1));
        return sb.toString();
    }

    private static class BinaryNode<AnyType> {
        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
        BinaryNode<AnyType> parent; //  Parent node

        // Constructors
        BinaryNode(AnyType theElement) {
            this(theElement, null, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt, BinaryNode<AnyType> pt) {
            element = theElement;
            left = lt;
            right = rt;
            parent = pt;
        }

        public boolean isFull() {
            return left != null && right != null;
        }

        public boolean isEmpty() {
            return left == null && right == null;
        }

        public boolean isLeaf() {
            return isEmpty();
        }


        public String toString() {
            StringBuilder sb = new StringBuilder();
            //sb.append("Node:");
            sb.append(element);
            if (parent == null) {
                sb.append("[]");
            } else {
                sb.append("[");
                sb.append(parent.element);
                sb.append("]");
            }

            return sb.toString();
        }

    }

    // Test program
    public static void main(String[] args) {

        final String ENDLINE = "\n";

        // Assignment Problem 1
        Integer[] v1 = {25, 10, 60, 55, 58, 56, 14, 63, 8, 50, 6, 9, 61};
        Tree<Integer> tree1 = new Tree<>(v1, "Tree1:");
        System.out.println(tree1.toString());
        System.out.println(tree1.toString2());

        // Assignment Problem 2
        long seed = 436543;
        Random generator = new Random(seed);  // Don't use a seed if you want the numbers to be different each time
        int val = 60;
        final int SIZE = 8;

        List<Integer> v2 = new ArrayList<Integer>();
        for (int i = 0; i < SIZE * 2; i++) {
            int t = generator.nextInt(200);
            v2.add(t);
        }
        v2.add(val);
        Integer[] v = v2.toArray(new Integer[v2.size()]);
        Tree<Integer> tree2 = new Tree<Integer>(v, "Tree2");
        tree2.contains(val);  //Sets the current node inside the tree class.
        int succCount = 5;  // how many successors do you want to see?
        System.out.println("In Tree2, starting at " + val + ENDLINE);
        for (int i = 0; i < succCount; i++) {
            System.out.println("The next successor is " + tree2.successor());
        }

//
        // Assignment Problem 3
        System.out.println();
        System.out.println(tree1.toString());
        System.out.println("All paths from tree1");
        tree1.printAllPaths();


        // Assignment Problem 4
        Integer[] v4 = {66, 75, -15, 3, 65, -83, 83, -10, 16, -7, 70, 200, 71, 90};
        Tree<Integer> treeA = new Tree<Integer>("TreeA");
        treeA.createTreeByLevel(v4, "TreeA");
        System.out.println();
        System.out.println(treeA.toString());
        System.out.println("treeA Contains BST: " + treeA.countBST());

        Integer[] a = {21, 8, 5, 6, 7, 19, 10, 40, 43, 52, 12, 60};
        Tree<Integer> treeB = new Tree<Integer>("TreeB");
        treeB.createTreeByLevel(a, "TreeB");
        System.out.println();
        System.out.println(treeB.toString());
        System.out.println("treeB Contains BST: " + treeB.countBST());

        // Assignment Problem 5

        treeB.pruneK(60);
        treeB.changeName("treeB after pruning 60");
        System.out.println(treeB.toString());
        treeA.pruneK(200);
        treeA.changeName("treeA after pruning 200");
        System.out.println(treeA.toString());

        // Assignment Problem 6

        System.out.println(tree1.toString());
        System.out.println("tree1 Least Common Ancestor of (56,61) " + tree1.lca(56, 61) + ENDLINE);

        System.out.println("tree1 Least Common Ancestor of (6,25) " + tree1.lca(6, 25) + ENDLINE);

        // Assignment Problem 7
        Integer[] v7 = {20, 15, 10, 5, 8, 2, 100, 28, 42};
        Tree<Integer> tree7 = new Tree<>(v7, "Tree7:");

        System.out.println(tree7.toString());
        tree7.balanceTree();
        tree7.changeName("tree7 after balancing");
        System.out.println(tree7.toString());

        // Assignment Problem 8
        System.out.println(tree1.toString());
        tree1.keepRange(10, 50);
        tree1.changeName("tree1 after keeping only nodes between 10 and 50");
        System.out.println(tree1.toString());

        tree7.changeName("Tree 7");
        System.out.println(tree7.toString());
        tree7.keepRange(8, 85);
        tree7.changeName("tree7 after keeping only nodes between 8  and 85");
        System.out.println(tree7.toString());

        // Assignment Problem 9
        Integer[] v9 = {66, -15, -83, 3, -10, -7, 65, 16, 75, 70, 71, 83, 200, 90};
        Tree<Integer> tree4 = new Tree<Integer>(v9, "Tree4:");
        System.out.println(tree4.toString());
        tree4.maxLevelSum();

        // Assignment Problem 10
        ArrayList<Integer> leaves = new ArrayList<Integer>();
        Integer[] preorder1 = {10, 3, 1, 7, 18, 13};

        getLeaves(preorder1, 0, preorder1.length - 1, leaves);
        System.out.print("Leaves are ");
        for (int leaf : leaves) {
            System.out.print(leaf + " ");
        }
        System.out.println();

        leaves = new ArrayList<Integer>();
        Integer[] preorder2 = {66, -15, -83, 3, -10, -7, 65, 16, 75, 70, 71, 83, 200, 90};

        getLeaves(preorder2, 0, preorder2.length - 1, leaves);
        System.out.print("Leaves are ");
        for (int leaf : leaves) {
            System.out.print(leaf + " ");
        }
        System.out.println();

        // Assignment Problem 11
        Tree<Integer> treeC = new Tree<Integer>("TreeC");
        Integer[] data = {44, 9, 13, 4, 5, 6, 7};
        treeC.createTreeByLevel(data, "Sum Tree1 ?");
        if (treeC.isSum()) {
            System.out.println(treeC.toString() + " is Sum Tree");
        } else {
            System.out.println(treeC.toString() + " is NOT a Sum Tree");
        }
        Integer[] data1 = {52, 13, 13, 4, 5, 6, 7, 0, 4};
        treeC.createTreeByLevel(data1, "Sum Tree2 ?");
        if (treeC.isSum()) {
            System.out.println(treeC.toString() + " is Sum Tree");
        } else {
            System.out.println(treeC.toString() + " is NOT a Sum Tree");
        }
        Integer[] data2 = {44, 13, 13, 4, 5, 6, 7, 1, 4};
        treeC.createTreeByLevel(data2, "Sum Tree3?");
        if (treeC.isSum()) {
            System.out.println(treeC.toString() + " is Sum Tree");
        } else {
            System.out.println(treeC.toString() + " is NOT a Sum Tree");
        }

        // Assignment Problem 12
        treeC.changeName("Tree12");
        System.out.println(treeC.toString() + "MaxPath=" + treeC.maxPath());


        Integer[] data12 = {1, 3, 2, 5, 6, -3, -4, 7, 8};
        treeC.createTreeByLevel(data12, "Another Tree");
        System.out.println(treeC.toString() + "MaxPath=" + treeC.maxPath());

        // Assignment Problem 13
        Integer[] preorderT = {1, 2, 4, 5, 3, 6, 8, 9, 7};
        Integer[] postorderT = {4, 5, 2, 8, 9, 6, 7, 3, 1};
        tree1.createTreeTraversals(preorderT, postorderT, "Tree13 from preorder & postorder");
        System.out.println(tree1.toString());
        Integer[] preorderT2 = {5, 10, 25, 1, 57, 6, 15, 20, 3, 9, 2};
        Integer[] postorderT2 = {1, 57, 25, 6, 10, 20, 9, 2, 3, 15, 5};
        tree1.createTreeTraversals(preorderT2, postorderT2, "Tree from preorder & postorder");
        System.out.println(tree1.toString());
    }
}
