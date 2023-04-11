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
public class Assignment extends AbstractNode implements Expression {

  private final Mutable mutable;
  private final AssignmentType type;
  private final Expression rhs;

  public Assignment(Mutable mutable, String assign, Expression rhs) {
    this.mutable = mutable;
    this.type = AssignmentType.fromString(assign);
    this.rhs = rhs;
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    mutable.toCminus(builder, prefix);
    if (rhs != null) {
      builder.append(" ").append(type.toString()).append(" ");
      rhs.toCminus(builder, prefix);
    } else {
      builder.append(type.toString());
    }
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    String reg = regAllocator.getAny();

    Build.line(
            code,
            String.format("li %s -%d", reg, symbolTable.getOffset(mutable.getId())),
            "Load the offset"
    );
    Build.line(
            code,
            String.format("add %s %s $sp", reg, reg),
            "Add stack pointer to the offset for absolute address"
    );

    MIPSResult res = rhs.toMIPS(code, data, symbolTable, regAllocator);

    Build.line(
            code,
            String.format("sw %s 0(%s)", res.getRegister(), reg),
            "Store to memory"
    );

    regAllocator.clear(reg);
    regAllocator.clear(res.getRegister());

    return MIPSResult.createVoidResult();
  }
}
