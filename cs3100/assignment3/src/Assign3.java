import java.nio.file.Paths;

public class Assign3 {
    public static void main(String[] args) {
        Shell shell = new Shell(Paths.get(System.getProperty("user.dir")));
        int exitCode = shell.run();
        System.exit(exitCode);
    }
}