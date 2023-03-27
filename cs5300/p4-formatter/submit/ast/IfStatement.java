package submit.ast;

public class IfStatement implements  Statement {

    private final Expression condition;
    private final Statement trueStatement;
    private final Statement falseStatement;

    public IfStatement(Expression condition, Statement trueStatement, Statement falseStatement) {
        this.condition = condition;
        this.trueStatement = trueStatement;
        this.falseStatement = falseStatement;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix).append("if (");
        condition.toCminus(builder, "");
        builder.append(")\n");
        String trueStatementPrefix = trueStatement.getClass().equals(CompoundStatement.class) ? prefix : prefix + " ";
        trueStatement.toCminus(builder, trueStatementPrefix);

        if (falseStatement != null) {
            String falseStatementPrefix = falseStatement.getClass().equals(CompoundStatement.class) ? prefix : prefix + " ";
            builder.append(prefix).append("else\n");
            falseStatement.toCminus(builder, falseStatementPrefix);
        }
    }
}
