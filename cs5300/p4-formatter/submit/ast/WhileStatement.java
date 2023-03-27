package submit.ast;

public class WhileStatement implements Statement {

    private final Expression condition;
    private final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix).append("while (");
        condition.toCminus(builder, "");
        builder.append(")\n");
        body.toCminus(builder, prefix + " ");
    }
}
