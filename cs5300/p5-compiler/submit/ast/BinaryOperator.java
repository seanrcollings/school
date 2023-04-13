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
      case OR -> null;
      case AND -> null;
      case LE -> null;
      case LT -> null;
      case GT -> null;
      case GE -> null;
      case EQ -> null;
      case NE -> null;
      case MOD -> null;
      case PLUS -> "add";
      case MINUS -> "sub";
      case TIMES -> "mul";
      case DIVIDE -> "div";
    };

    MIPSResult lhsRes = lhs.toMIPS(code, data, symbolTable, regAllocator);
    MIPSResult rhsRes = rhs.toMIPS(code, data, symbolTable, regAllocator);

    regAllocator.clearAll();
    String reg = regAllocator.getAny();

    Build.line(code, String.format("%s %s, %s, %s", instruction, reg, lhsRes.getRegister(), rhsRes.getRegister()));

    return MIPSResult.createRegisterResult(reg, lhsRes.getType());
  }
}
