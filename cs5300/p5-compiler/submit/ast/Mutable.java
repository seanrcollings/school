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
public class Mutable extends AbstractNode implements Expression {

  private final String id;
  private final Expression index;

  public Mutable(String id, Expression index) {
    this.id = id;
    this.index = index;
  }

  public String getId() { return id; }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(id);
    if (index != null) {
      builder.append("[");
      index.toCminus(builder, prefix);
      builder.append("]");
    }
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    String reg = regAllocator.getAny();
    Build.line(
            code,
            String.format("li %s -%d", reg, symbolTable.getOffset(id)),
            String.format("Load the offset of %s", id)
    );

    Build.line(
            code,
            String.format("add %s %s $sp", reg, reg),
            "Add stack pointer to the offset for absolute address"
    );

    Build.line(
            code,
            String.format("lw %s 0(%s)", reg, reg),
            String.format("Load the value of %s from memory", id)
    );

    return MIPSResult.createRegisterResult(reg, symbolTable.find(id).getType());
  }
}
