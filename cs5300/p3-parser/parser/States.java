package parser;

import java.util.*;

public class States {
    private Set<State> stateSet;
    private List<State> states;
    private int id = 0;

    public States() {
        this.stateSet = new HashSet<>();
        this.states = new ArrayList<>();
    }

    public List<State> getStates() {
        return states;
    }

    public State getState(int name) {
        if (name >= states.size()) {
            return null;
        }
        return states.get(name);
    }

    public void add(State state) {
        if (!stateSet.contains(state)) {
            state.setName(id++);
            this.states.add(state);
            this.stateSet.add(state);
        }
    }

    public boolean contains(State state) {
        return stateSet.contains(state);
    }

    public int indexOf(State state) {
        return states.indexOf(state);
    }


    public Integer size() { return states.size(); }

    @Override
    public String toString() {
        return states.toString();
    }

}
