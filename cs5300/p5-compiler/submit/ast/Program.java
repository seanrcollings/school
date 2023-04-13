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
    int idx = 0;

    for (Declaration decl: declarations) {
      SymbolTable childSymbolTable = decl.getClass() == FunDeclaration.class
              ? symbolTable.getChild(idx++)
              : symbolTable;
      decl.toMIPS(code, data, childSymbolTable, regAllocator);
    }

    return MIPSResult.createVoidResult();
  }
}
