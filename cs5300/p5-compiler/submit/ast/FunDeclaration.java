/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import submit.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edwajohn
 */
public class FunDeclaration extends AbstractNode implements Declaration {

  private final VarType returnType;
  private final String id;
  private final ArrayList<Param> params;
  private final Statement statement;

  public FunDeclaration(VarType returnType, String id, List<Param> params,
          Statement statement) {
    this.returnType = returnType;
    this.id = id;
    this.params = new ArrayList<>(params);
    this.statement = statement;
  }

  public void toCminus(StringBuilder builder, final String prefix) {
    String rt = (returnType != null) ? returnType.toString() : "void";
    builder.append("\n").append(rt).append(" ");
    builder.append(id);
    builder.append("(");

    for (Param param : params) {
      param.toCminus(builder, prefix);
      builder.append(", ");
    }
    if (!params.isEmpty()) {
      builder.delete(builder.length() - 2, builder.length());
    }
    builder.append(")\n");
    statement.toCminus(builder, prefix);
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    code.append("\n").append(id).append(":\n");

    statement.toMIPS(code, data, symbolTable.getChild(0), regAllocator);

    regAllocator.clearAll();

    if (id.equals("main")) {
      Build.syscall(code, Syscall.EXIT);
    } else {
      Build.line(code, "jr $ra");
    }

    if (returnType != null) {
      return MIPSResult.createAddressResult(symbolTable.getOffset("return").toString(), returnType);
    } else {
      return MIPSResult.createVoidResult();
    }
  }
}