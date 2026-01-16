import java.util.*;

public class Tyrone {
    private static final String LINE = "____________________________________________________________\n";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;

        String logo =
                      " _______   ______   ___  _   _ _____ \n"
                    + "|_   _\\ \\ / /  _ \\ / _ \\| \\ | | ____|\n"
                    + "  | |  \\ V /| |_) | | | |  \\| |  _|  \n"
                    + "  | |   | | |  _ <| |_| | |\\  | |___ \n"
                    + "  |_|   |_| |_| \\_\\\\___/|_| \\_|_____|\n";
        printLine();
        System.out.println("Wassup dawg? It's ya boy\n" + logo);
        System.out.println("Watchu want?\n");
        printLine();

        while (sc.hasNextLine()) {
            String input = sc.nextLine();

            if (input.equals("bye")) {
                printLine();
                System.out.println("Bye dawg!\n");
                printLine();
                break;
            }

            /*if (input.equals("list")) {
                printLine();
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                printLine();
                continue;
            }*/

            tasks[taskCount] = input;
            taskCount++;

            printLine();
            System.out.println("added: " + input);
            printLine();
        }

        sc.close();
    }

    private static void printLine() {
        System.out.println(LINE);
    }
}
