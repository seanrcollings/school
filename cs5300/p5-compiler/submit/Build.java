package submit;


import submit.ast.Node;

public class Build  {
    public static void line(StringBuilder builder, String string) {
        line(builder, string, null);
    }

    public static void line(StringBuilder builder, String string, String comment) {
        builder.append(String.format("%-20s", string));
        if (comment != null) builder.append(" # ").append(comment);
        builder.append("\n");
    }

    public static void sep(StringBuilder builder) {
        builder.append("# ---------------------------------------\n");
    }

    public static void comment(StringBuilder builder, String comment) {
        builder.append("# ").append(comment).append("\n");
    }

    public static void label(StringBuilder builder, String label) {
        Build.line(builder, label + ":");
    }

    public static void syscall(StringBuilder builder, Syscall call) {
        Build.line(builder, String.format("li $v0 %d", call.value), call.name());
        Build.line(builder, "syscall");
    }

    public static void word(StringBuilder builder, String label, int word) {
        Build.line(builder, String.format("%10s:    .word %d", label, word));
    }

    public static void ascii(StringBuilder builder, String label, String string) {
        Build.line(builder, String.format("%10s:    .asciiz \"%s\"", label, string));
    }

    public static String source(Node node) {
        StringBuilder builder = new StringBuilder();
        node.toCminus(builder, "");
        return builder.toString();
    }
}
