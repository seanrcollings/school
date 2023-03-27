package submit.ast;

public class Immutable implements  Expression {

    private final boolean isExpr;
    private final Node child;

    public Immutable(Node child, boolean isExpr) {
        this.child = child;
        this.isExpr = isExpr;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if (isExpr) builder.append("(");
        if (child != null)
            child.toCminus(builder, prefix);
        else
            builder.append("NULL");
        if (isExpr) builder.append(")");
    }
}
