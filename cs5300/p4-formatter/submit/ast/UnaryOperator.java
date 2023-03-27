package submit.ast;

import java.util.ArrayList;
import java.util.List;

public class UnaryOperator implements Expression {

    private final Node child;
    private final List<UnaryOperatorType> types;
    private final String side;

    public UnaryOperator(Node child, UnaryOperatorType type, String side) {
        this.child = child;
        this.types = new ArrayList<>();
        this.types.add(type);
        this.side = side;
    }

    public UnaryOperator(Node child, List<UnaryOperatorType> types, String side) {
        this.child = child;
        this.types = types;
        this.side = side;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if (side.equals("left")) addOperators(builder);
        child.toCminus(builder, "");
        if (side.equals("right")) addOperators(builder);
    }

    private void addOperators(StringBuilder builder) {
        for (UnaryOperatorType type: types) {
            builder.append(type);
        }
    }
}
