package submit.ast;

import java.util.List;

public class Call implements Expression {
    private String id;
    private List<Expression> params;

    public Call(String id, List<Expression> params) {
        this.id = id;
        this.params = params;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(id).append("(");

        boolean first = true;
        for (Expression param: params) {
            if (!first) {
                builder.append(", ");
            } else {
                first = false;
            }
            param.toCminus(builder, "");
        }

        builder.append(")");
    }
}
