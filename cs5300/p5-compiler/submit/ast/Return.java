/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.*;

/**
 *
 * @author edwajohn
 */
public class Return extends AbstractNode implements  Statement {

  private final Expression expr;

  public Return(Expression expr) {
    this.expr = expr;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix);
    if (expr == null) {
      builder.append("return;\n");
    } else {
      builder.append("return ");
      expr.toCminus(builder, prefix);
      builder.append(";\n");
    }
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    MIPSResult res = expr.toMIPS(code, data, symbolTable, regAllocator);
    Build.line(
            code,
            String.format("sw %s %d($sp)", res.getRegister(), symbolTable.getOffset("return")),
            "Store return value"
    );

    regAllocator.clear(res.getRegister());

    int size = 0;
    SymbolTable curr = symbolTable;

    while (curr.getParent() != null) {
      size += curr.size();
      curr = curr.getParent();
    }

    Build.line(
            code,
            String.format("addi $sp $sp %d", size),
            "Add back offset relative to all enclosing scopes"
    );

    Build.line(code, "jr $ra");

    return MIPSResult.createVoidResult();
  }
}
