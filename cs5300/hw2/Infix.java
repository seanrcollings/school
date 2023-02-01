public class Infix {

    public static void main(String[] args) {
        String str = "+++12-835";
        if (args.length > 0) {
            str = args[0];
        }
        try {
            Infix parser = new Infix(str);
            parser.parse();
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private String terminals;
    private int pos;

    public Infix(String terminals) {
        this.terminals = terminals;
        this.pos = 0;
    }

    public void parse() throws Exception {
        list();

        if (pos < terminals.length()) {
            throw new Exception("syntax error");
        }
    }

    private void list() throws Exception {
        char current = lookahead();
        if (current == '+') {
            match('+');
            emit("(");
            list();
            emit("+");
            list();
            emit(")");
        } else if (current == '-') {
            match('-');
            emit("(");
            list();
            emit("-");
            list();
            emit(")");
        } else {
            digit();
        }
    }

    private void digit() throws Exception {
        char curr = lookahead();
        if (Character.isDigit(lookahead())) {
            match(curr);
            emit(curr);
            return;
        }

        if (curr == 0) {
            throw new Exception("Unexpected EOF");
        }

        throw new Exception("Invalid digit " + curr);
    }

    private void match(char t) throws Exception {
        if (lookahead() == t) {
            pos++;
        } else {
            throw new Exception("Could not match character " + t);
        }
    }

    private char lookahead() {
        if (pos < terminals.length()) return terminals.charAt(pos);
        return 0; // 0 As a character isn't valid for this program
    }

    private void emit(Object o) {
        System.out.print(o);
    }
}
