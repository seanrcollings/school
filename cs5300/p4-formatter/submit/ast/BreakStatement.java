package submit.ast;

public class BreakStatement  implements  Statement {

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix).append("break;\n");
    }
}
