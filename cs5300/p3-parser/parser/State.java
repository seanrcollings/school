package parser;

import java.util.*;

public class State implements Comparable<State> {
    private Set<Item> itemSet;
    private List<Item> items;
    private int name;

    public State(int name) {
        this.itemSet = new HashSet<>();
        this.items = new ArrayList<>();
        this.name = name;
    }

    public State() {
        this(0);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setName(int name) {
        this.name = name;
    }

    public void add(Item item) {
        if (!itemSet.contains(item)) {
            itemSet.add(item);
            items.add(item);
        }
    }

    public void addAll(Iterable<Item> items) {
        for (Item item: items) {
            add(item);
        }
    }

    public boolean contains(Item item) {
        return itemSet.contains(item);
    }

    public Integer size() {
        return items.size();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        ArrayList<Item> sortedList = new ArrayList<>(items);
        sortedList.sort(Comparator.comparingInt(Item::hashCode));
        for (Item item : sortedList) {
            hash = 37 * hash + Objects.hashCode(item);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (items.size() != other.items.size()) {
            return false;
        }
        for (Item item : items) {
            if (!other.itemSet.contains(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name + ": " + items.toString();
    }

    @Override
    public int compareTo(State o) {
        return new Integer(this.name).compareTo(new Integer(o.name));
    }

}
