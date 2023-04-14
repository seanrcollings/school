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
public class If extends AbstractNode implements Statement {

  private final Expression expression;
  private final Statement trueStatement;
  private final Statement falseStatement;

  public If(Expression expression, Statement trueStatement, Statement falseStatement) {
    this.expression = expression;
    this.trueStatement = trueStatement;
    this.falseStatement = falseStatement;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("if (");
    expression.toCminus(builder, prefix);
    builder.append(")\n");
    if (trueStatement instanceof CompoundStatement) {
      trueStatement.toCminus(builder, prefix);
    } else {
      trueStatement.toCminus(builder, prefix + " ");
    }
    if (falseStatement != null) {
      builder.append(prefix).append("else\n");
//      falseStatement.toCminus(builder, prefix);
      if (falseStatement instanceof CompoundStatement) {
        falseStatement.toCminus(builder, prefix);
      } else {
        falseStatement.toCminus(builder, prefix + " ");
      }
    }
//    builder.append(prefix).append("}");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    Build.comment(code, "if (" + Build.source(expression) + ")");

    MIPSResult res = expression.toMIPS(code, data, symbolTable, regAllocator);
    regAllocator.clear(res.getRegister());
    String reg = regAllocator.getAny();
    // MIPS sets 1 for true and 0 for false, but the instruction say to consider
    // 0 a true value, and otherwise it's false, so we subtract one here
    // to follow that guideline
    Build.line(code, String.format("subi %s %s 1", reg, res.getRegister()));

    String falseLabel = symbolTable.getUniqueLabel();
    String endLabel = symbolTable.getUniqueLabel();

    // If
    Build.line(code, String.format("bne %s $zero %s", reg, falseLabel));
    trueStatement.toMIPS(code, data, symbolTable, regAllocator);
    Build.line(code, String.format("j %s", endLabel));


    // Else
    Build.label(code, falseLabel);
    if (falseStatement != null) {
      falseStatement.toMIPS(code, data, symbolTable, regAllocator);
    }

    Build.label(code, endLabel);

    regAllocator.clear(reg);
    return MIPSResult.createVoidResult();
  }
}
