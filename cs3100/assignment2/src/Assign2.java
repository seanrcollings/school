import java.lang.Runtime;

public class Assign2 {

    public static void main(String[] args) {
        if (args.length == 0 ) {
			return;
        }

        for (String param : args) {
            switch(param) {
                case "-cpu":
                    System.out.println("Processor     : " + Runtime.getRuntime().availableProcessors());
                    break;
                case "-mem":
                    Runtime runtime = Runtime.getRuntime();
                    System.out.println("Free Memory   :  " + runtime.freeMemory());
                    System.out.println("Total Memory  :  " + runtime.totalMemory());
                    System.out.println("Max Memory    :  " + runtime.maxMemory());
                    break;
                case "-dirs":
                    System.out.println("Working Directory    : " + System.getProperty("user.dir"));
                    System.out.println("User Home Directory  : " + System.getProperty("user.home"));
                    break;
                case "-os":
                    System.out.println("OS Name      : " + System.getProperty("os.name"));
                    System.out.println("OS Version   : " + System.getProperty("os.version"));
                    break;
                case "-java":
                    System.out.println("Java Vendor     :  " + System.getProperty("java.vendor"));
                    System.out.println("Java Runtime    :  " + System.getProperty("java.runtime.name"));
                    System.out.println("Java Version    :  " + System.getProperty("java.version"));
                    System.out.println("Java VM Version :  " + System.getProperty("java.vm.version"));
                    System.out.println("Java VM Name    :  " + System.getProperty("java.vm.name"));
                    break;
                default:
                    break;
            }
            System.out.println("------------------------------------");
        }
    }
}