/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.Build;
import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolTable;

/**
 *
 * @author edwajohn
 */
public class Break extends AbstractNode implements Statement {

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("break;\n");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    Build.line(code, String.format("j %s", symbolTable.getEndLabel()), "break");
    return MIPSResult.createVoidResult();
  }
}
