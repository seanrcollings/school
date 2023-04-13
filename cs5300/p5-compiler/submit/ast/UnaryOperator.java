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
public class UnaryOperator extends AbstractNode implements Expression {

  private final UnaryOperatorType type;
  private final Expression expression;

  public UnaryOperator(String type, Expression expression) {
    this.type = UnaryOperatorType.fromString(type);
    this.expression = expression;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(type);
    expression.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    if (type.equals(UnaryOperatorType.NEG)) {
      Build.comment(code, "Negate expression");
      MIPSResult res = expression.toMIPS(code, data, symbolTable, regAllocator);
      String reg = regAllocator.getAny();
      Build.line(code, String.format("li %s -1", reg));
      Build.line(code, String.format("mul %s, %s, %s", res.getRegister(), res.getRegister(), reg));
      regAllocator.clear(reg);
      return MIPSResult.createRegisterResult(res.getRegister(), res.getType());
    } else {
      throw new RuntimeException("Unsupported operator: " + type);
    }
  }
}
