/*
 * CS 4481 Compilers
 * Project 1 - scanner part 1
 *
 * This file contains the method runTests(). You will uncomment tests
 * as you develop your scanner code
 */
package scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A different Main file will be used for grading, so you may make modifications as needed, but before
 * submitting you should download a fresh copy of this file, uncomment the tests and make sure you pass
 * them all.
 */
public class Main {

  public static void main(String[] args)
          throws FileNotFoundException, IOException {
    if (args.length > 0) {
      scanInputFile(args[0], args[1]);
    } else {
      runPascaTests();

      System.out.println(tests.getSuccesses() + "/" + tests.getN()
              + " tests succeeded");
      int failures = tests.getFailures();

      System.exit(failures);
    }
  }

  private static void runPascaTests() throws FileNotFoundException, IOException {
    // TODO: uncomment tests as you develop code

    //------------------------------------------------------------
    // Test register.table reading, table building and token
    // parsing. These tests should pass once you have the Scanner
    // constructor implemented.
    //------------------------------------------------------------

    String tableFile = "data/pasca.table";
    TableReader tableReader = new TableReader(tableFile);
    Scanner scanner = new Scanner(tableReader);

    {
      ScanStream ss = getDataStream("data/test1-pasca.txt");

      testToken(scanner, ss, "identifier", "begin");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "program");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "a1");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "delimiter", "{");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "var0");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "is");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "integer", "8");
      testToken(scanner, ss, "delimiter", ";");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "var1");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "var2");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "variable");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "var1able");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "delimiter", "}");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "integer", "123");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "integer", "1203");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "string", "' a crazy string abc(){}*''4013'");
      testToken(scanner, ss, "ignore");
      testToken(scanner, ss, "identifier", "end");
      testToken(scanner, ss, "delimiter", ".");
    }
    {
      ScanStream ss = getDataStream("data/test2-pasca.txt");
      testToken(scanner, ss, "string", "'alks dfl;alsk k''jk{}'");
    }
    {
      ScanStream ss = getDataStream("data/err1-pasca.txt");
      testToken(scanner, ss, "ignore", "(* a comment *)");
      testToken(scanner, ss, "ignore", " ");
      testToken(scanner, ss, null);
    }
    {
      ScanStream ss = getDataStream("data/err2-pasca.txt");
      testToken(scanner, ss, null);
    }
    {
      ScanStream ss = getDataStream("data/err3-pasca.txt");
      testToken(scanner, ss, null);
    }
  }

  //--------------------------------------------------------------------------------
  // Utility stuff
  // You can ignore code from here to the end of the file.
  //--------------------------------------------------------------------------------

  static Tests tests = new Tests();

  private static void testToken(Scanner scanner, ScanStream ss, String type, String lexeme) {
    try {
      Token t = scanner.nextToken(ss);
      if (type == null) {
        tests.test(t == null, "expected lexical error");
      } else {
        tests.test(t.getType().equals(type), t.getType() + " not equal to " + type);
      }

      if (lexeme != null) {
        tests.test(t.getLexeme().equals(lexeme), t.getLexeme() + " not equal to " + lexeme);
      }
    } catch(Exception e) {
      tests.addFailure(e);
    }
  }

  private static void testToken(Scanner scanner, ScanStream ss, String type) {
    testToken(scanner, ss, type, null);
  }

 private static ScanStream getDataStream(String testFile) throws IOException {
    File file = new File(testFile);
    FileInputStream fis = new FileInputStream(file);
    byte[] data = new byte[(int) file.length()];
    fis.read(data);
    fis.close();
    ScanStream ss = new ScanStream(data);
    return ss;
  }

  public static void scanInputFile(String tableFile, String testFile)
          throws FileNotFoundException, IOException {
    TableReader tableReader = new TableReader(tableFile);

    File file = new File(testFile);
    FileInputStream fis = new FileInputStream(file);
    byte[] data = new byte[(int) file.length()];
    fis.read(data);
    fis.close();

    PrintWriter writer = new PrintWriter(testFile + ".out", "UTF-8");

    Scanner scanner = new Scanner(tableReader);
    ScanStream ss = new ScanStream(data);
    Token t;
    do {
      t = scanner.nextToken(ss);
      if (t == null) {
        System.out.println("Lexical error: " + ss.next());
        ss.rollback();
        writer.println("Lexical error: " + ss.next());
      } else if (!t.getType().equals("ignore")) {
        System.out.println(t.toString());
        writer.println(t.toString());
      }
    } while (t != null && !ss.eof());

    writer.close();
  }


}
