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
public class While extends AbstractNode implements Statement {

  private final Expression expression;
  private final Statement statement;

  public While(Expression expression, Statement statement) {
    this.expression = expression;
    this.statement = statement;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("while (");
    expression.toCminus(builder, prefix);
    builder.append(")\n");
    if (statement instanceof CompoundStatement) {
      statement.toCminus(builder, prefix);
    } else {
      statement.toCminus(builder, prefix + " ");
    }
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    String startLabel = symbolTable.getUniqueLabel();
    String endLabel = symbolTable.getUniqueLabel();

    Build.label(code, startLabel);

    // Comparison
    MIPSResult res = expression.toMIPS(code, data, symbolTable, regAllocator);
    regAllocator.clear(res.getRegister());
    String reg = regAllocator.getAny();
    // MIPS sets 1 for true and 0 for false, but the instruction say to consider
    // 0 a true value, and otherwise it's false, so we subtract one here
    // to follow that guideline
    Build.line(code, String.format("subi %s %s 1", reg, res.getRegister()));
    Build.line(code, String.format("bne %s $zero %s", reg, endLabel));
    symbolTable.setEndLabel(endLabel);

    statement.toMIPS(code, data, symbolTable, regAllocator);

    Build.line(code, String.format("j %s", startLabel));
    Build.label(code, endLabel);

    return MIPSResult.createVoidResult();
  }
}
