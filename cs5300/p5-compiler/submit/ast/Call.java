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
      return printlnToMips(code, data, symbolTable, regAllocator);
    } else {
      return customFuncCallToMips(code, data, symbolTable, regAllocator);
    }
  }

  private MIPSResult printlnToMips(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    // More than a single arg for this function would be considered a syntax / semantic error
    MIPSResult res = args.get(0).toMIPS(code, data, symbolTable, regAllocator);

    if (res.getRegister() != null) {
      Build.line(code, String.format("move $a0 %s", res.getRegister()));
      regAllocator.clear(res.getRegister());
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
    return MIPSResult.createVoidResult();
  }

  private MIPSResult customFuncCallToMips(StringBuilder code, StringBuilder data, SymbolTable symbolTable, RegisterAllocator regAllocator) {
    String raReg = regAllocator.getAny();
    Build.line(code, String.format("move %s $ra", raReg), "Put Return Address in a temp");
    Build.comment(code, "Store off temporaries");
    int offset = regAllocator.saveT(code, symbolTable.size()) + symbolTable.size();

    Build.comment(code, "Store arguments for function on the stack");

    int argsOffset = offset;
    for (Expression arg : args) {
      MIPSResult res = arg.toMIPS(code, data, symbolTable, regAllocator);
      argsOffset += res.getType().getSize();
      Build.line(code, String.format("sw %s -%d($sp)", res.getRegister(), argsOffset));
      regAllocator.clear(res.getRegister());
    }

    Build.line(code, String.format("addi $sp $sp -%d", offset));
    Build.line(code, String.format("jal %s", id), "Call the function");
    Build.line(code, String.format("addi $sp $sp %d", offset));

    Build.comment(code, "Load temporaries");
    regAllocator.restoreT(code, symbolTable.size());
    SymbolInfo funcInfo = symbolTable.find(id);

    Build.line(code, String.format("move $ra %s", raReg), "Load return address back into $ra");
    regAllocator.clear(raReg);

    if (funcInfo.getType() != null) {
      String retReg = regAllocator.getAny();
      Build.line(
              code,
              String.format("lw %s -%d($sp)", retReg, argsOffset + funcInfo.getType().getSize()),
              "Load return value"
      );
      return MIPSResult.createRegisterResult(retReg, funcInfo.getType());
    }

    return MIPSResult.createVoidResult();
  }
}
