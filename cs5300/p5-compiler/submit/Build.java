package submit;


public class Build  {
    public static void line(StringBuilder builder, String string) {
        line(builder, string, null);
    }

    public static void line(StringBuilder builder, String string, String comment) {
        builder.append(string);
        if (comment != null) builder.append(" # ").append(comment);
        builder.append("\n");
    }

    public static void sep(StringBuilder builder) {
        builder.append("# ---------------------------------------\n");
    }

    public static void comment(StringBuilder builder, String comment) {
        builder.append("# ").append(comment).append("\n");
    }

    public static void syscall(StringBuilder builder, Syscall call) {
        Build.line(builder, String.format("li $v0 %d", call.value), call.name());
        Build.line(builder, "syscall");
    }
}
