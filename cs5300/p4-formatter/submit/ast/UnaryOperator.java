package submit.ast;

import java.util.ArrayList;
import java.util.List;

public class UnaryOperator implements Expression {

    private final Node child;
    private final UnaryOperatorType type;
    private final String side;

    public UnaryOperator(Node child, UnaryOperatorType type, String side) {
        this.child = child;
        this.type = type;
        this.side = side;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if (side.equals("left")) builder.append(type);
        child.toCminus(builder, "");
        if (side.equals("right")) builder.append(type);
    }
}
