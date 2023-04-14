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
public class BinaryOperator extends AbstractNode implements Expression {

  private final Expression lhs, rhs;
  private final BinaryOperatorType type;

  public BinaryOperator(Expression lhs, BinaryOperatorType type, Expression rhs) {
    this.lhs = lhs;
    this.type = type;
    this.rhs = rhs;
  }

  public BinaryOperator(Expression lhs, String type, Expression rhs) {
    this.lhs = lhs;
    this.type = BinaryOperatorType.fromString(type);
    this.rhs = rhs;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    lhs.toCminus(builder, prefix);
    builder.append(" ").append(type).append(" ");
    rhs.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {

    String instruction = switch (type) {
      // Logical
      case OR -> "or";
      case AND -> "and";
      case LT -> "slt";
      case GT -> "sgt";
      case EQ -> "seq";
      case NE -> "sne";
      case LE -> "sle";
      case GE -> "sge";
      // Arithmetic
      case PLUS -> "add";
      case MINUS -> "sub";
      case TIMES -> "mul";
      case DIVIDE -> "div";
      case MOD -> "rem";
    };


    MIPSResult lhsRes = lhs.toMIPS(code, data, symbolTable, regAllocator);
    MIPSResult rhsRes = rhs.toMIPS(code, data, symbolTable, regAllocator);

    regAllocator.clear(lhsRes.getRegister());
    regAllocator.clear(rhsRes.getRegister());

    String reg = regAllocator.getAny();

    Build.line(
            code,
            String.format("%s %s, %s, %s", instruction, reg, lhsRes.getRegister(), rhsRes.getRegister()),
            Build.source(this)
    );

    return MIPSResult.createRegisterResult(reg, lhsRes.getType());
  }
}
