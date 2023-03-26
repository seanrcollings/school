package submit.ast;

public class UnaryOperator implements Expression {

    private Mutable lhs;
    private UnaryOperatorType type;

    public UnaryOperator(Mutable lhs, UnaryOperatorType type) {
        this.lhs = lhs;
        this.type = type;
    }


    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        lhs.toCminus(builder, "");
        builder.append(type);
    }
}
