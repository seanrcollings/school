package scanner;

import java.util.HashMap;
import java.util.Stack;

/**
 * This is the file you will modify.
 */
public class Scanner {
  HashMap<Character, String> catMap = new HashMap<>();
  HashMap<String, HashMap<String, String>> transMap = new HashMap<>();
  HashMap<String, String> tokenTypeMap = new HashMap<>();
  String initialState = null;

  /**
   * Builds the tables needed for the scanner.
   */
  public Scanner(TableReader tableReader) {
    // Build catMap, mapping a character to a category.
    catMap.put('\0', "other"); // eof

    for (TableReader.CharCat cat : tableReader.getClassifier()) {
//      System.out.println("Character " + cat.getC() + " is of category "
//              + cat.getCategory());
      catMap.put(cat.getC(), cat.getCategory());
    }

    // Build the transition table. Given a state and a character category,
    // give a new state.
    for (TableReader.Transition t : tableReader.getTransitions()) {
//      System.out.println(t.getFromStateName() + " -- " + t.getCategory()
//              + " --> " + t.getToStateName());
      if (initialState == null) {
        initialState = t.getFromStateName();
      }
      var map = transMap.computeIfAbsent(t.getFromStateName(), k -> new HashMap<>());
      map.put(t.getCategory(), t.getToStateName());
    }

    // Build the token types table
    for (TableReader.TokenType tt : tableReader.getTokens()) {
//      System.out.println("State " + tt.getState()
//              + " accepts with the lexeme being of type " + tt.getType());
      tokenTypeMap.put(tt.getState(), tt.getType());
    }

  }

  /**
   * Returns the category for c or "not in alphabet" if c has no category. Do not hardcode
   * this. That is, this function should have nothing more than a table lookup
   * or two. You should not have any character literals in here such as 'r' or '3'.
   */
  public String getCategory(Character c) {
    return catMap.getOrDefault(c, "not in alphabet");
  }

  /**
   * Returns the new state given a current state and category. This models
   * the transition table. Returns "error" if there is no transition.
   * Do not hardcode any state names or categories. You should have only
   * table lookups here.
   */
  public String getNewState(String state, String category) {
    var map =  transMap.get(state);
    if (map == null) return "error";
    return map.getOrDefault(category, "error");
  }

  /**
   * Returns the type of token corresponding to a given state. If the state
   * is not accepting then return "error".
   * Do not hardcode any state names or token types.
   */
  public String getTokenType(String state) {
    return tokenTypeMap.getOrDefault(state, "error");
  }

  public boolean isAcceptingState(String state) {
    return tokenTypeMap.containsKey(state);
  }

  /**
   * Return the next token or null if there's a lexical error.
   */
  public Token nextToken(ScanStream ss) {
    // TODO: get a single token. This is an implementation of the nextToken
    // algorithm given in class. You may *not* use TableReader in this
    // function. Return null if there is a lexical error.

    String state = initialState;
    StringBuilder lexeme = new StringBuilder();
    Stack<String> stack = new Stack<>();
    stack.push("bad");

    while (!state.equals("error")) {
      char c = ss.eof() ? '\0' : ss.next();
      lexeme.append(c);
      if (isAcceptingState(state)) {
        stack.clear();
      }

      stack.push(state);
      state = getNewState(state, getCategory(c));
    }

    while (!isAcceptingState(state) && !state.equals("bad")) {
      state = stack.pop();
      if (lexeme.length() > 0) lexeme.deleteCharAt(lexeme.length() - 1);
      ss.rollback();
    }

    if (isAcceptingState(state)) {
      return new Token(getTokenType(state), lexeme.toString());
    }

    return null;
  }

}
