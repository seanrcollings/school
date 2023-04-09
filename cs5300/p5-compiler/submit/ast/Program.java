/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.Build;
import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolTable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edwajohn
 */
public class Program extends AbstractNode implements Node {

  private ArrayList<Declaration> declarations;

  public Program(List<Declaration> declarations) {
    this.declarations = new ArrayList<>(declarations);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    toCminus(builder, "");
    return builder.toString();
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    for (Declaration declaration : declarations) {
      declaration.toCminus(builder, "");
    }
  }
  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    Build.ascii(data, "newline", "\\n");
    declarations.forEach((n) -> n.toMIPS(code, data, symbolTable, regAllocator));

    return MIPSResult.createVoidResult();
  }
}
