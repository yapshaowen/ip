import java.util.*;

public class Tyrone {
    private static final String LINE = "____________________________________________________________\n";
    //private static final String LINE = "_".repeat(50) + "\n";

    public static void main(String[] args) {
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

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                printLine();
                System.out.println("Bye dawg!\n");
                printLine();
                break;
            }

            printLine();
            System.out.println(input);
            printLine();
        }

        sc.close();
    }

    private static void printLine() {
        System.out.println(LINE);
    }
}
