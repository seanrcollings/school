package submit.ast;

import java.util.List;

public class CompoundStatement implements Statement {
    private final List<VarDeclaration> declarations;
    private final List<Statement> statements;

    public CompoundStatement(List<VarDeclaration> declarations, List<Statement> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        String parentPrefix = prefix.length() > 0 ?  prefix.substring(0, prefix.length()-1) : prefix;
        builder.append(parentPrefix).append("{\n");

        String childPrefix = prefix + "  ";

        for (VarDeclaration v: declarations) {
            v.toCminus(builder, childPrefix);
        }

        for (Statement s: statements) {
            // TODO: remove this null check
            if (s != null)
                s.toCminus(builder, childPrefix);
        }

        builder.append(parentPrefix).append("}\n");
    }
}
