/*
 * Look for TODO comments in this file for suggestions on how to implement
 * your parser.
 */
package parser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lexer.ExprLexer;
import lexer.ParenLexer;
import lexer.SimpleLexer;
import lexer.TinyLexer;
import org.antlr.v4.runtime.*;

/**
 *
 */
public class Parser {

  final Grammar grammar;

  /**
   * All states in the parser.
   */
  private final States states;

  /**
   * Action table for bottom-up parsing. Accessed as
   * actionTable.get(state).get(terminal). You may replace
   * the Integer with a State class if you choose.
   */
  private final HashMap<State, HashMap<String, Action>> actionTable;
  /**
   * Goto table for bottom-up parsing. Accessed as gotoTable.get(state).get(nonterminal).
   * You may replace the Integers with State classes if you choose.
   */
  private final HashMap<State, HashMap<String, Integer>> gotoTable;

  public Parser(String grammarFilename) throws IOException {
    actionTable = new HashMap<>();
    gotoTable = new HashMap<>();

    grammar = new Grammar(grammarFilename);

    states = new States();

    // TODO: Call methods to compute the states and parsing tables here.
    Item startItem = new Item(grammar.startRule, 0, Util.EOF);
    State startState = computeClosure(startItem, grammar);
    computeStates(startState);
    System.out.println(states);
  }

  public States getStates() {
    return states;
  }

  static public State computeClosure(Item I, Grammar grammar) {
    State closure = new State();
    Stack<Item> stack = new Stack<>();
    closure.add(I);
    stack.push(I);

    while (!stack.empty()) {
      Item curr = stack.pop();

      if (grammar.isNonterminal(curr.getNextSymbol())) {
        List<Rule> rules = grammar.rules
                .stream()
                .filter((rule) -> rule.getLhs().equals(curr.getNextSymbol()) )
                .collect(Collectors.toList());

        if (rules.isEmpty()) continue;

        ArrayList<String> lookaheads =  new ArrayList<>( grammar.first.getOrDefault(curr.getNextNextSymbol(), new HashSet<>()));
        if (lookaheads.isEmpty()) lookaheads.addAll(grammar.first.getOrDefault(curr.getLookahead(), new HashSet<>()));
        if (lookaheads.isEmpty()) lookaheads.add(Util.EOF);

        for (Rule rule: rules) {
          for (String a: lookaheads) {
            Item item = new Item(rule, 0, a);
            if (!closure.contains(item)) {
              closure.add(item);
              stack.add(item);
            }
          }
        }
      }
    }

//    System.out.println(closure.toString());
    return closure;
  }

  // TODO: Implement this method.
  //   This returns a new state that represents the transition from
  //   the given state on the symbol X.
  static public State GOTO(State state, String X, Grammar grammar) {
    State ret = new State();
    for (Item item : state.getItems()) {
      String next = item.getNextSymbol();
      if (next != null && next.equals(X)) {
        State closure = computeClosure(item.advance(), grammar);
        ret.addAll(closure.getItems());
      }
    }
    return ret;
  }

  private void computeStates(State state) {
    states.add(state);

    for (Item item: state.getItems()) {
      State newState = GOTO(state, item.getNextSymbol(), grammar);
      if (!states.contains(newState) && newState.size() > 0) {
        computeStates(newState);
      }
    }
  }

  // TODO: Implement this method
  // You will want to use StringBuilder. Another useful method will be String.format: for
  // printing a value in the table, use
  //   String.format("%8s", value)
  // How much whitespace you have shouldn't matter with regard to the tests, but it will
  // help you debug if you can format it nicely.
  public String actionTableToString() {
    StringBuilder builder = new StringBuilder();
    return builder.toString();
  }

  // TODO: Implement this method
  // You will want to use StringBuilder. Another useful method will be String.format: for
  // printing a value in the table, use
  //   String.format("%8s", value)
  // How much whitespace you have shouldn't matter with regard to the tests, but it will
  // help you debug if you can format it nicely.
  public String gotoTableToString() {
    StringBuilder builder = new StringBuilder();
    return builder.toString();
  }

  // TODO: Implement this method
  // You should return a list of the actions taken.
  public List<Action> parse(Lexer scanner) throws ParserException {
    // tokens is the output from the scanner. It is the list of tokens
    // scanned from the input file.
    // To get the token type: v.getSymbolicName(t.getType())
    // To get the token lexeme: t.getText()
    ArrayList<? extends Token> tokens = new ArrayList<>(scanner.getAllTokens());
    Vocabulary v = scanner.getVocabulary();

    Stack<String> input = new Stack<>();
    Collections.reverse(tokens);
    input.add(Util.EOF);
    for (Token t : tokens) {
      input.push(v.getSymbolicName(t.getType()));
    }
    Collections.reverse(tokens);
//    System.out.println(input);

    // TODO: Parse the tokens. On an error, throw a ParseException, like so:
    //    throw ParserException.create(tokens, i)
    List<Action> actions = new ArrayList<>();
    return actions;
  }

  //-------------------------------------------------------------------
  // Convenience functions
  //-------------------------------------------------------------------

  public List<Action> parseFromFile(String filename) throws IOException, ParserException {
//    System.out.println("\nReading input file " + filename + "\n");
    final CharStream charStream = CharStreams.fromFileName(filename);
    Lexer scanner = scanFile(charStream);
    return parse(scanner);
  }

  public List<Action> parseFromString(String program) throws ParserException {
    Lexer scanner = scanFile(CharStreams.fromString(program));
    return parse(scanner);
  }

  private Lexer scanFile(CharStream charStream) {
    // We use ANTLR's scanner (lexer) to produce the tokens.
    Lexer scanner = null;
    switch (grammar.grammarName) {
      case "Simple":
        scanner = new SimpleLexer(charStream);
        break;
      case "Paren":
        scanner = new ParenLexer(charStream);
        break;
      case "Expr":
        scanner = new ExprLexer(charStream);
        break;
      case "Tiny":
        scanner = new TinyLexer(charStream);
        break;
      default:
        System.out.println("Unknown scanner");
        break;
    }

    return scanner;
  }

}
