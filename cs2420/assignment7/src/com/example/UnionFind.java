package com.example;

import java.util.ArrayList;
import java.util.Arrays;

public class UnionFind {

    /**
     * Stores all the groups associated with this union find
     * - If the number stored is a positive integer, it is the index of it's parent
     * - If it's a negative number, it does not have a parent and the absolute value is
     *   equal to the size of the group. -1 Would mean it's in a group by itself
     */
    private int[] groups;
    private int size;


    public UnionFind(int size) {
        this.groups = new int[size];
        this.size = size;
        Arrays.fill(groups, -1); // put them all into their own groups
    }

    /**
     * Finds the group associated with the provided item
     * Preforms path compression on each it passes over
     * @param item the item to find the group of
     * @return the group's root or null if the item is not in the set
     */
    public Integer find(int item) {
        int parent = groups[item];
        if (parent < 0) return item;

        int root = find(parent);
        // Path compress by setting the found root
        // as the root of each item as we exit the recursion
        groups[item] = root;
        return root;
    }

    /**
     * Test if two values are within the same group
     * @param val1
     * @param val2
     * @return whether or not the provided values are in the same group
     */
    public boolean inSameGroup(int val1, int val2) {
        return find(val1).equals(find(val2));
    }

    /**
     * Unions two groups together by size
     * @param val1
     * @param val2
     */
    public void union(int val1, int val2) {
        int root1 = find(val1);
        int root2 = find(val2);
        if (root2 == root1) return;

        if (groupSize(root2) > groupSize(root1)) {
            int temp = root1;
            root1 = root2;
            root2 = temp;
        }
        groups[root1] = groups[root1] + groups[root2];
        groups[root2]  = root1;
    }

    @Override
    public String toString() {
        return "UnionFind{" +
                "\ngroups=" + Arrays.toString(groups) +
                "\nsize=" + size +
                "\n}";
    }

    public ArrayList<Integer> getGroupRoots() {
        ArrayList<Integer> roots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (groups[i] < 0) roots.add(i);
        }

        return roots;
    }

    /**
     * @param item - find the size of this item's containing group
     * @return size of group
     */
    public int groupSize(int item) {
        int root = find(item);
        return Math.abs(groups[root]);
    }

    // Private

    /**
     * Checks if an item is within the groups length
     * @param item item to check the bounds against
     * @return True in the bounds | False out of the bounds
     */
    private boolean inBounds(int item) {
        return item >= 0 && item < size;
    }


    public static void main(String[] args) {
        UnionFind set = new UnionFind(10);
        System.out.println(set.find(4));
        System.out.println(set.find(3));
        System.out.println(set.find(2));
        System.out.println(set.toString());

        set.union(1, 5);
        set.union(1, 6);
        set.union(5, 8);

        System.out.println(set.toString());

        set.union(2, 3);
        set.union(4, 3);
        set.union(7, 4);
        System.out.println(set.toString());

        set.union(1, 2);
        System.out.println(set.toString());
    }
}
