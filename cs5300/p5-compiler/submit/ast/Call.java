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
public class Call extends AbstractNode implements Expression {

  private final String id;
  private final List<Expression> args;

  public Call(String id, List<Expression> args) {
    this.id = id;
    this.args = new ArrayList<>(args);
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    builder.append(id).append("(");
    for (Expression arg : args) {
      arg.toCminus(builder, prefix);
      builder.append(", ");
    }
    if (!args.isEmpty()) {
      builder.setLength(builder.length() - 2);
    }
    builder.append(")");
  }

  @Override
  public MIPSResult toMIPS(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    if (id.equals("println")) { // println() is implemented in the compiler
      printlnToMips(code, data, symbolTable, regAllocator);
    } else {
      customFuncCallToMips(code, data, symbolTable, regAllocator);
    }

    return MIPSResult.createVoidResult();
  }

  private void printlnToMips(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    // More than a single arg for this function would be considered a syntax / semantic error
    MIPSResult res = args.get(0).toMIPS(code, data, symbolTable, regAllocator);

    if (res.getRegister() != null) {
      Build.line(code, String.format("move $a0 %s", res.getRegister()));
    } else {
      if (res.getType().equals(VarType.INT)) {
        Build.line(code, String.format("lw $a0 %s", res.getAddress()));
      } else {
        Build.line(code, String.format("la $a0 %s", res.getAddress()));
      }
    }

    Syscall call = res.getType() == VarType.INT ? Syscall.PRINT_INTEGER : Syscall.PRINT_STRING;

    Build.syscall(code, call);
    Build.line(code, "la $a0 newline");
    Build.syscall(code, Syscall.PRINT_STRING);
  }

  private void customFuncCallToMips(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    String raReg = regAllocator.getAny();
    Build.line(code, String.format("move %s $ra", raReg), "Put Return Address in a temp");
    Build.comment(code, "Store off temporaries");
    int offset = regAllocator.saveT(code, 0);

    Build.line(code, String.format("addi $sp $sp -%d", offset + symbolTable.size()));
    Build.line(code, String.format("jal %s", id), "Call the function");
    Build.line(code, String.format("addi $sp $sp %d", offset + symbolTable.size()));
    
    Build.comment(code, "Load temporaries");
    regAllocator.restoreT(code, 0);
    Build.line(code, String.format("move $ra %s", raReg), "Load return address back into $ra");
  }
}
