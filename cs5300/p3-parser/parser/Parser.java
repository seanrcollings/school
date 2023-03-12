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
  private final HashMap<Integer, HashMap<String, Action>> actionTable;
  /**
   * Goto table for bottom-up parsing. Accessed as gotoTable.get(state).get(nonterminal).
   * You may replace the Integers with State classes if you choose.
   */
  private final HashMap<Integer, HashMap<String, State>> gotoTable;

  public Parser(String grammarFilename) throws IOException {
    actionTable = new HashMap<>();
    gotoTable = new HashMap<>();

    grammar = new Grammar(grammarFilename);

    states = new States();

    // TODO: Call methods to compute the states and parsing tables here.
    constructStatesAndTables();
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

    return closure;
  }

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

  private void constructStatesAndTables() {
    Item startItem = new Item(grammar.startRule, 0, Util.EOF);
    State startState = computeClosure(startItem, grammar);
    states.add(startState);
    constructStatesAndTables(startState);
  }

  private void constructStatesAndTables(State state) {
    HashMap<String, Action> stateAction = actionTable.computeIfAbsent(state.getName(), (s) -> new HashMap<>());
    HashMap<String, State> stateGoto = gotoTable.computeIfAbsent(state.getName(), (s) -> new HashMap<>());

    for (Item item: state.getItems()) {
      String symbol = item.getNextSymbol();

      // Compute a new state, and create shifts and gotos
      if (symbol != null) {
        State newState = GOTO(state, symbol, grammar);

        if (states.contains(newState))
          newState = states.getState(states.indexOf(newState));

        if (grammar.isNonterminal(symbol))
          stateGoto.put(symbol, newState);

        if (grammar.isTerminal(symbol))
          stateAction.put(symbol, Action.createShift(newState.getName()));

        if (!states.contains(newState)) {
          states.add(newState);
          constructStatesAndTables(newState);
        }
      }

      // Check for Accepting states
      else if (item.getLookahead().equals(Util.EOF)
              && grammar.startRule.equals(item.getRule())) {
        stateAction.put(Util.EOF, Action.createAccept());
      }

      // Check for reduces
      else {
        stateAction.put(item.getLookahead(), Action.createReduce(item.getRule()));
      }
    }
  }

  public String actionTableToString() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("%-5s", ""));
    builder.append(" | ");
    List<String> terminals = actionTable
            .values()
            .stream()
            .map(HashMap::keySet)
            .flatMap(Collection::stream)
            .sorted(Comparator.reverseOrder())
            .distinct()
            .collect(Collectors.toList());

    for (String key : terminals) {
      builder.append(String.format("%11s", key));
      builder.append(" | ");
    }

    builder.append("\n");
    int len = builder.lastIndexOf("\n");
    builder.append(Stream.generate(() -> "-").limit(len).collect(Collectors.joining()));
    builder.append("\n");

    for (State state: states.getStates()) {
      builder.append(String.format("%-5d", state.getName()));
      builder.append(" | ");

      HashMap<String, Action> actions = actionTable.get(state.getName());
      for (String terminal: terminals) {
        if (actions != null) {
          Action action = actions.get(terminal);

          if (action != null) {
            builder.append(String.format("%11s", action));
          } else {
            builder.append(String.format("%11s", ""));
          }
          builder.append(" | ");
        } else {
          builder.append(String.format("%11s", ""));
          builder.append(" | ");
        }
      }

      builder.append("\n");
    }

    return builder.toString();
  }

  public String gotoTableToString() {
    StringBuilder builder = new StringBuilder();
    builder.append(String.format("%-5s", ""));
    builder.append(" | ");
    List<String> nonTerminals = gotoTable
            .values()
            .stream()
            .map(HashMap::keySet)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());

    for (String key : nonTerminals) {
      builder.append(String.format("%11s", key));
      builder.append(" | ");
    }

    builder.append("\n");
    int len = builder.lastIndexOf("\n") ;
    builder.append(Stream.generate(() -> "-").limit(len).collect(Collectors.joining()));
    builder.append("\n");

    for (State state: states.getStates()) {
      builder.append(String.format("%-5d", state.getName()));
      builder.append(" | ");

      HashMap<String, State> next = gotoTable.get(state.getName());
      for (String nonTerminal: nonTerminals) {
        if (next != null) {
          State gotoState = next.get(nonTerminal);

          if (gotoState != null) {
            builder.append(String.format("%11d", gotoState.getName()));
          } else {
            builder.append(String.format("%11s", ""));
          }
          builder.append(" | ");
        } else {
          builder.append(String.format("%11s", ""));
          builder.append(" | ");
        }
      }

      builder.append("\n");
    }

    return builder.toString();
  }

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
    System.out.println(input);

    List<Action> actions = new ArrayList<>();
    Stack<State> stack  = new Stack<>();
    stack.push(states.getState(0));
    int i = 0;
    String a = input.pop();

    while (true) {
      State s = stack.peek();
      Action action = actionTable.get(s.getName()).get(a);

      if (action == null) throw ParserException.create(tokens, i);

      actions.add(action);

      if (action.isShift()) {
        stack.push(states.getState(action.getState()));
        a = input.pop();
        i++;
      } else if (action.isReduce()) {
        int magnitude = action.getRule().getRhs().size();
        for(int j =0; j < magnitude; j++) {
          stack.pop();
        }
        State t = stack.peek();
        stack.push(gotoTable.get(t.getName()).get(action.getRule().getLhs()));

      } else if (action.isAccept()) {
        break;
      }
    }

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
