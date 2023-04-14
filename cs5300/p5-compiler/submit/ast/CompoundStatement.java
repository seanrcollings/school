/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.Build;
import submit.MIPSResult;
import submit.RegisterAllocator;
import submit.SymbolTable;

import java.util.List;

/**
 *
 * @author edwajohn
 */
public class CompoundStatement extends AbstractNode implements Statement {

  private final List<Statement> statements;
  private final SymbolTable symbolTable;

  public CompoundStatement(List<Statement> statements, SymbolTable symbolTable) {
    this.statements = statements;
    this.symbolTable = symbolTable;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(prefix).append("{\n");
    for (Statement s : statements) {
      s.toCminus(builder, prefix + "  ");
    }
    builder.append(prefix).append("}\n");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    Build.sep(code);
    Build.comment(code, "Entering a new Scope");
    Build.line(
            code,
            String.format("addi $sp $sp -%d", this.symbolTable.getParent().size()),
            "Update the stack pointer"
    );

    statements.forEach((s) -> s.toMIPS(code, data, this.symbolTable, regAllocator));

    Build.comment(code, "Exiting scope");
    Build.line(
            code,
            String.format("addi $sp $sp %d", this.symbolTable.getParent().size()),
            "Update the stack pointer"
    );
    Build.sep(code);
    return MIPSResult.createVoidResult();
  }
}
