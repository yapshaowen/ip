import java.util.*;

public class Tyrone {
    private static final String LINE = "____________________________________________________________";
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
        System.out.println("Watchu want?");
        printLine();

        while (sc.hasNextLine()) {
            String input = sc.nextLine().trim();

            try {
                taskCount = handleCommand(input, tasks, taskCount);
                if (input.equals("bye")) {
                    break;
                }
            } catch (TyroneException e) {
                printError(e.getMessage());
            }
        }

        sc.close();
    }

    private static int handleCommand(String input, Task[] tasks, int taskCount) throws TyroneException {
        if (input.equals("bye")) {
            printLine();
            System.out.println("Bye dawg!");
            printLine();
            return taskCount;
        }

        if (input.equals("list")) {
            printLine();
            System.out.println("Here ya go dawg!");
            for (int i = 0; i < taskCount; i++) {
                System.out.println((i + 1) + "." + tasks[i]);
            }
            printLine();
            return taskCount;
        }

        if (input.startsWith("mark ")) {
            int idx = parseIndex(input.substring(5), taskCount);
            tasks[idx].mark();
            printLine();
            System.out.println("I gotchu! I've marked this task as done:");
            System.out.println("  " + tasks[idx]);
            printLine();
            return taskCount;
        }

        if (input.startsWith("unmark ")) {
            int idx = parseIndex(input.substring(7), taskCount);
            tasks[idx].unmark();
            printLine();
            System.out.println("Gotchu dawg, I've unmarked this task:");
            System.out.println("  " + tasks[idx]);
            printLine();
            return taskCount;
        }

        if (input.equals("todo") || input.startsWith("todo ")) {
            String desc = (input.length() == 4) ? "" : input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new TyroneException("Todo cannot be empty. Use: todo <task>");
            }
            return addTask(tasks, taskCount, new Todo(desc));
        }

        if (input.equals("deadline") || input.startsWith("deadline ")) {
            int byPos = input.indexOf(" /by ");
            if (byPos == -1) {
                throw new TyroneException("Invalid deadline bro! Use: deadline <task> /by <time>");
            }
            String desc = input.substring(9, byPos).trim();
            String by = input.substring(byPos + 5).trim();
            if (desc.isEmpty()) {
                throw new TyroneException("Deadline task cannot be empty. Use: deadline <task> /by <time>");
            }
            if (by.isEmpty()) {
                throw new TyroneException("Deadline /by cannot be empty. Use: deadline <task> /by <time>");
            }
            return addTask(tasks, taskCount, new Deadline(desc, by));
        }

        if (input.equals("event") || input.startsWith("event ")) {
            int fromPos = input.indexOf(" /from ");
            int toPos = input.indexOf(" /to ");
            if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                throw new TyroneException("Invalid format man! Use: event <task> /from <start> /to <end>");
            }

            String desc = input.substring(6, fromPos).trim();
            String from = input.substring(fromPos + 7, toPos).trim();
            String to = input.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw new TyroneException("Event task cannot be empty. Use: event <task> /from <start> /to <end>");
            }
            if (from.isEmpty()) {
                throw new TyroneException("Event /from cannot be empty. Use: event <task> /from <start> /to <end>");
            }
            if (to.isEmpty()) {
                throw new TyroneException("Event /to cannot be empty. Use: event <task> /from <start> /to <end>");
            }

            return addTask(tasks, taskCount, new Event(desc, from, to));
        }

        // Minimal requirement: unknown commands like "blah"
        throw new TyroneException("I ain't understand that. Try: todo, deadline, event, list, mark, unmark, bye");
    }

    private static int parseIndex(String s, int taskCount) throws TyroneException {
        String trimmed = s.trim();
        if (trimmed.isEmpty()) {
            throw new TyroneException("Give me a task number, my guy.");
        }
        try {
            int oneBased = Integer.parseInt(trimmed);
            int idx = oneBased - 1;
            if (idx < 0 || idx >= taskCount) {
                throw new TyroneException("That task number ain't valid. Use 1 to " + taskCount + ".");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new TyroneException("Task number must be a number, bro.");
        }
    }

    private static int addTask(Task[] tasks, int taskCount, Task task) throws TyroneException {
        if (taskCount >= MAX_TASKS) {
            throw new TyroneException("My bad bro, I can only store up to " + MAX_TASKS + " tasks.");
        }

        tasks[taskCount] = task;
        taskCount++;

        printLine();
        System.out.println("Gotchu my guy. I added this task:");
        System.out.println("  " + task);
        System.out.println("Now you got " + taskCount + " tasks in yo list");
        printLine();

        return taskCount;
    }

    private static void printError(String msg) {
        printLine();
        System.out.println("OOPS!!! " + msg);
        printLine();
    }

    private static void printLine() {
        System.out.println(LINE);
    }
}
