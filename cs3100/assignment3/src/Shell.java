import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.io.*;
import java.lang.ProcessBuilder;


public class Shell {
    private Path workingDir;
    private boolean running = true;
    private ArrayList<String> history = new ArrayList<>();
    private long ptime = 0;

    public Shell(Path workingDir) {
        this.workingDir = workingDir;
    }

    /** Program's main loop
     * Executes until this.running == false
     * Prints the prompt and waits for input.
     * @return shell's exit code
     */
    public int run() {
        System.out.println("Welcome to JISH - Java Interactive SHell");
        Scanner input = new Scanner(System.in);
        while (running) {
            System.out.print("[" + workingDir.toString() + "]: ");
            String inputString = input.nextLine();
            if (!inputString.equals("")) {
                handleInput(inputString);
            }
        }
        input.close();
        return 1;
    }

    /** Core program functionality
     * pulls the command and arguments
     * out of the input string and executes
     * it accordingly
     * @param input string to execute
     */
    public void handleInput(String input) {
        String[] inputArray = splitCommand(input);
        String command = inputArray[0];
        String[] commandArgs = Arrays.copyOfRange(inputArray, 1, inputArray.length);
        boolean builtinRan = handleBuiltin(command, commandArgs);
        boolean externalCommandRan = false;
        if (!builtinRan) {
            externalCommandRan = handleExternalCommand(command, commandArgs);
        }
        if (builtinRan || externalCommandRan) {
            history.add(command + " " + String.join(" ", commandArgs));
        } else {
            System.out.println("Invalid Command: " + command);
        }
    }

    /** Handles the execution of builtin shell functionality
     * Is preferred over external commands
     * @param command to execute
     * @param commandArgs arguments to pass to the command
     * @return false if no builtin is found, true if one is
     */
    private boolean handleBuiltin(String command, String[] commandArgs) {
        switch (command) {
            case "cd":
                Path newWorkingDir = Builtins.changeDir(commandArgs, workingDir);
                System.setProperty("user.dir", "/tmp");
                workingDir = newWorkingDir;
                break;
            case "list":
                Builtins.listDir(workingDir);
                break;
            case "history":
                Builtins.displayHistory(history);
                break;
            case "mkdir":
                Builtins.makeDir(commandArgs, workingDir);
                break;
            case "rmdir":
                Builtins.removeDir(commandArgs, workingDir);
                break;
            case "^":
                Builtins.runHistory(commandArgs, history, this);
                break;
            case "ptime":
                System.out.println("Total time in child processes: " + Long.toString(ptime / 1000) + " seconds");
                break;
            case "exit":
                running = false;
                break;
            default:
                return false;
        }
        return true;
    }

    /** Handles the execution of external commands
     * while the piping is there, I don't beleive it works properly
     * @param command to execute
     * @param commandArgs arguments to pass to the command
     */
    private boolean handleExternalCommand(String command, String[] commandArgs) {
        int pipelocation = Util.getPipeLocation(commandArgs);

        long start = System.currentTimeMillis();
        try {
            if (pipelocation == -1) {
                // stdout is not being piped
                ProcessBuilder builder = new ProcessBuilder(command, String.join(" ", commandArgs));
                builder.directory(workingDir.toFile());
                Process process = builder.start();

                if (commandArgs[commandArgs.length - 1].equals("&")) {
                    // if we fork the process
                    return true;
                }

                InputStream is = process.getInputStream();
                Util.printInputStream(is);

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    InputStream err = process.getErrorStream();
                    Util.printInputStream(err);
                }
            } else {
                // stdout is  being piped
                final ProcessBuilder builder = new ProcessBuilder(command, String.join(" ", Arrays.copyOfRange(commandArgs, 0, pipelocation)));
                final ProcessBuilder builder2 = new ProcessBuilder(command, String.join(" ", Arrays.copyOfRange(commandArgs, pipelocation, commandArgs.length)));
                builder.directory(workingDir.toFile());
                builder2.directory(workingDir.toFile());

                Runnable runnable = () -> {
                    try {
                        redirectStreams(builder.start(), builder2.start());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };

                new Thread(runnable).start();
            }
        }
        catch (IOException e ) {
            // The command does not exist
            return false;
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        ptime += System.currentTimeMillis() - start;
        return true;
    }

    private static void redirectStreams(Process process1, Process process2){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process1.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process2.getOutputStream()));
        String lineToPipe;

        try {
            while ((lineToPipe = bufferedReader.readLine()) != null){
                System.out.println("Output process1 / Input process2:" + lineToPipe);
                bufferedWriter.write(lineToPipe + '\n');
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }

        return matchList.toArray(new String[matchList.size()]);
    }
}
