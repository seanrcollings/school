package scanner;

import java.util.HashMap;
import java.util.Stack;

/**
 * This is the file you will modify.
 */
public class Scanner {

  //------------------------------------------------------------
  // TODO: declare the HashMaps that you will use to store
  // your tables. Also declare the start state.
  //------------------------------------------------------------

  // Category table
  final HashMap<Character, String> char2category = new HashMap<>();
  // Transition table
  final HashMap<String, HashMap<String, String>> state2category2state = new HashMap<>();
  // Token type table
  final HashMap<String, String> state2tokenType = new HashMap<>();
  // Utility
  final HashMap<String, String> name2state = new HashMap<>();
  // Start state
  private String s0;

  //------------------------------------------------------------
  // TODO: build your tables in the constructor and implement
  // the get methods.
  //------------------------------------------------------------

  /**
   * Builds the tables needed for the scanner.
   */
  public Scanner(TableReader tableReader) {
    // TODO: starting with the skeleton code below, build the
    // classifer, transition and token type tables. You will need
    // to also implement the test functions below once you have your
    // tables built.

    // Build catMap, mapping a character to a category.
    for (TableReader.CharCat cat : tableReader.getClassifier()) {
      System.out.println("Character " + cat.getC() + " is of category "
              + cat.getCategory());
      char2category.put(cat.getC(), cat.getCategory());
    }

    // Build the transition table. Given a state and a character category,
    // give a new state.
    for (TableReader.Transition t : tableReader.getTransitions()) {
      String from = t.getFromStateName();
      String to = t.getToStateName();
      System.out.println(from + " -- " + t.getCategory()
              + " --> " + to);

      if (s0 == null)
        s0 = from;

      HashMap<String, String> cat2state;
      if (state2category2state.containsKey(from)) {
        cat2state = state2category2state.get(from);
      } else {
        cat2state = new HashMap<>();
        state2category2state.put(from, cat2state);
      }
      cat2state.put(t.getCategory(), to);
    }

    // Build the token types
    for (TableReader.TokenType tt : tableReader.getTokens()) {
      System.out.println("State " + tt.getState()
              + " accepts with the lexeme being of type " + tt.getType());

      state2tokenType.put(tt.getState(), tt.getType());
    }

  }

  /**
   * Returns the category for c or "not in alphabet" if c has no category. Do not hardcode
   * this. That is, this function should have nothing more than a table lookup
   * or two. You should not have any character literals in here such as 'r' or '3'.
   */
  public String getCategory(Character c) {
    if (!char2category.containsKey(c)) return "not in alphabet";
    return char2category.get(c);
  }

  /**
   * Returns the new state given a current state and category. This models
   * the transition table. Returns "error" if there is no transition.
   * Do not hardcode any state names or categories. You should have only
   * table lookups here.
   */
  public String getNewState(String state, String category) {
    if (!state2category2state.containsKey(state)) return "error";
    if (!state2category2state.get(state).containsKey(category)) return "error";
    return state2category2state.get(state).get(category);
  }

  /**
   * Returns the type of token corresponding to a given state. If the state
   * is not accepting then return "error".
   * Do not hardcode any state names or token types.
   */
  public String getTokenType(String state) {
    if (!state2tokenType.containsKey(state)) return "error";
    return state2tokenType.get(state);
  }

  //------------------------------------------------------------
  // TODO: implement nextToken
  //------------------------------------------------------------

  /**
   * Return the next token or null if there's a lexical error.
   */
  public Token nextToken(ScanStream ss) {
    // TODO: get a single token. This is an implementation of the nextToken
    // algorithm given in class. You may *not* use TableReader in this
    // function. Return null if there is a lexical error.

    String state = s0;
    String lexeme = "";
    Stack<String> stack = new Stack<>();
    String bad = "bad";
    String error = "error";
    stack.push(bad);
    System.out.println("-----------------------------------------");
    while (state != error) {
      char c;
      try {
        c = ss.next();
      } catch(Exception e) {
        state = error;
        continue;
      }
      lexeme += c;

      if (state2tokenType.containsKey(state))
        stack.clear();
      stack.push(state);
      String category = char2category.get(c);
      if (state2category2state.containsKey(state)) {
        state = state2category2state.get(state).get(category);
      } else {
        state = null;
      }
      if (state == null) {
        state = error;
      }
      System.out.println(c + " --> " + state + " : " + lexeme);
    }

    while (state != bad && !state2tokenType.containsKey(state)) {
      state = stack.pop();
      if (!lexeme.isEmpty()) {
        lexeme = lexeme.substring(0, lexeme.length() - 1);
        ss.rollback();
      }
    }
    if (state2tokenType.containsKey(state)) {
      return new Token(state2tokenType.get(state), lexeme);
    }
    return null;
  }

}
