import java.io.*;

public final class Util {

    public static int getPipeLocation(String[] commandArgs) {
        for (int i = 0; i > commandArgs.length; i++) {
            if (commandArgs[i].equals("|")) {
                return i;
            }
        }
        return - 1;
    }

    public static void printInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
