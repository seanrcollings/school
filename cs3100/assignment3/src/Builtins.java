import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public final class Builtins {
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";


    /** Lists the contents of a directory - list
     * @param dir Path object to list the contents of
     */
    static void listDir(Path dir) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        try {
            Files.list(dir).forEach(path -> {
                File file = new File(path.toString());
                char dirChr = file.isDirectory() ? 'd' : '-';
                char readChr = file.canRead() ? 'r' : '-';
                char writeChr = file.canWrite() ? 'w' : '-';
                char execChr = file.canExecute() ? 'x' : '-';
                long fileSize = file.length();
                String modifiedDate = sdf.format(file.lastModified());

                System.out.println(String.format(
                    "%c%c%c%c %10d %20s %s",
                    dirChr, readChr, writeChr, execChr,
                    fileSize, modifiedDate,  path.toString()
                ));
            });
        } catch (IOException e) {
            System.err.println("Directory does not exist");
        }
    }

    /** Returns a new Path object based on provided arguments - cd
     * @param commandArgs list of arguments, the directory moving to should be the first paramters
     * @param currentDir  current directory to base the new Path of off
     */
    static Path changeDir(String[] commandArgs, Path currentDir) {
        if (commandArgs.length == 0) {
            return Paths.get(System.getProperty("user.home"));
        } else {

            Path newPath = Paths.get(currentDir.resolve(commandArgs[0]).toString()).normalize();
            if (Files.exists(newPath)) {
                return newPath;
            } else {
                System.err.println(newPath.toString() + " is not a directory");
                return currentDir;
            }
        }
    }

    /** Creates a directory - mkdir
     * @param commandArgs list of arguments, name should be the first paramater
     * @param currentDir directoy to create the directory in
     */
    static void makeDir(String[] commandArgs, Path currentDir) {
        if (commandArgs.length == 0) {
            System.err.println("mdir requires a directory name");
        } else {
            String dirName = commandArgs[0];
            File file = new File(currentDir.resolve(dirName).toString());
            file.mkdir();
            System.out.println(dirName + " created successfully");
        }
    }

    /** Deletes a directory - rmdir
     * @param commandArgs list of arguments, name should be the first paramater
     * @param currentDir directoy to delete the directory in
     */
    static void removeDir(String[] commandArgs, Path currentDir) {
        if (commandArgs.length == 0) {
            System.err.println("rmdir requires a directory name");
        } else {
            String dirName = commandArgs[0];
            File file = new File(currentDir.resolve(dirName).toString());
            if (file.exists()) {
                file.delete();
                System.out.println(dirName + " deleted successfully");
            } else {
                System.err.println(dirName + " does not exist");
            }
        }
    }

    /** Prints out the history in a list
     * @param history to print
     */
    static void displayHistory(ArrayList<String> history) {
        System.out.println("-- Command History --");
        int arraySize = history.size();
        for (int i = 0; i < arraySize; i++) {
            System.out.println(String.format("%s%-2d%s: %s", GREEN, i + 1, RESET, history.get(i)));
        }
    }

    /** Executes a given command from the history
     * @param commandArgs list of arguments, index to execute should be the first value
     * @param history array list of history entries
     * @param shell reference to the shell object, to execute the history command
     */
    static void runHistory(String[] commandArgs, ArrayList<String> history, Shell shell) {
        if (commandArgs.length == 0) {
            System.err.println("Must provide a numer of which command to run from the history (^ 10)");
        }
        else {
            try {
                int index = Integer.parseInt(commandArgs[0]);
                if (index < history.size()) {
                    shell.handleInput(history.get(index - 1));
                } else{
                    System.err.println(
                        "History is " +
                        Integer.toString(history.size()) +
                        " items long, you entered: " +
                        Integer.toString(index)
                    );
                }
            } catch (NumberFormatException e) {
                System.err.println(commandArgs[0] + " is not a valid number");
            }
        }
    }
}
