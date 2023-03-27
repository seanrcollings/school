package submit.ast;

import java.util.List;

public class FunctionDeclaration implements Declaration, Node {
    private String id;
    private VarType returnType;
    private List<String> paramIds, paramTypes;
    private Statement body;

    public FunctionDeclaration(String id, VarType returnType, List<String> paramIds, List<String> paramTypes, Statement body) {
        this.id = id;
        this.returnType = returnType;
        this.paramIds = paramIds;
        this.paramTypes = paramTypes;
        this.body = body;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append("\n");

        if (returnType == null) {
            builder.append("void");
        } else {
            builder.append(returnType);
        }

        builder.append(" ").append(id).append("(");

        for (int i = 0; i < paramIds.size(); ++i) {
            String id = paramIds.get(i);
            String type = paramTypes.get(i);
            builder.append(type).append(" ").append(id);
            if (i != paramIds.size() - 1) {
                builder.append(", ");
            }
        }

        builder.append(")\n");
        body.toCminus(builder, prefix);
//        builder.append("\n");
    }
}
