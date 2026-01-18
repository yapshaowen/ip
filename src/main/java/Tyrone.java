import java.util.*;

public class Tyrone {
    private static final String LINE = "____________________________________________________________\n";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
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

            if (input.equals("list")) {
                printLine();
                System.out.println("Here ya go dawg!\n");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                printLine();
                continue;
            }

            if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5).trim()) - 1;
                if (idx != -1) {
                    tasks[idx].mark();
                    printLine();
                    System.out.println("I gotchu! I've marked this task as done:");
                    System.out.println("  " + tasks[idx]);
                    printLine();
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim()) - 1;
                if (idx != -1) {
                    tasks[idx].unmark();
                    printLine();
                    System.out.println("Gotchu dawg, I've unmarked this task:");
                    System.out.println("  " + tasks[idx]);
                    printLine();
                }
                continue;
            }

            if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                taskCount = addTask(tasks, taskCount, new Todo(desc));
                continue;
            }

            if (input.startsWith("deadline ")) {
                int byPos = input.indexOf(" /by ");
                if (byPos == -1) {
                    printLine();
                    System.out.println("Invalid deadline bro! You gotta use: deadline <task> /by <time>");
                    printLine();
                    continue;
                }
                String desc = input.substring(9, byPos).trim();
                String by = input.substring(byPos + 5).trim();
                taskCount = addTask(tasks, taskCount, new Deadline(desc, by));
                continue;
            }

            if (input.startsWith("event ")) {
                int fromPos = input.indexOf(" /from ");
                int toPos = input.indexOf(" /to ");
                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    printLine();
                    System.out.println("Invalid format man! You gotta use: event <task> /from <start> /to <end>");
                    printLine();
                    continue;
                }

                String desc = input.substring(6, fromPos).trim();
                String from = input.substring(fromPos + 7, toPos).trim();
                String to = input.substring(toPos + 5).trim();
                taskCount = addTask(tasks, taskCount, new Event(desc, from, to));
                continue;
            }
        }

        sc.close();
    }

    private static int addTask(Task[] tasks, int taskCount, Task task) {
        if (taskCount >= MAX_TASKS) {
            printLine();
            System.out.println("My bad bro, I can only store up to " + MAX_TASKS + " tasks my man");
            printLine();
            return taskCount;
        }

        tasks[taskCount] = task;
        taskCount++;

        printLine();
        System.out.println("Gotchu my guy. I added this task:");
        System.out.println(" " + task);
        System.out.println("Now you got " + taskCount + " tasks in yo list");
        printLine();

        return taskCount;
    }

    private static void printLine() {
        System.out.println(LINE);
    }
}
