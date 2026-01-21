import java.util.*;
import java.nio.file.*;

public class Tyrone {
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Path dataPath = Paths.get("data", "tyrone.txt");
        Storage storage = new Storage(dataPath);
        ArrayList<Task> tasks = storage.load();

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
                handleCommand(input, tasks, storage);
                if (input.equals("bye")) {
                    break;
                }
            } catch (TyroneException e) {
                printError(e.getMessage());
            }
        }

        sc.close();
    }

    private static void handleCommand(String input, ArrayList<Task> tasks, Storage storage) throws TyroneException {
        if (input.equals("bye")) {
            printLine();
            System.out.println("Bye dawg!");
            printLine();
            return;
        }

        if (input.equals("list")) {
            printLine();
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
            printLine();
            return;
        }

        if (input.startsWith("mark ")) {
            int idx = parseIndex(input.substring(5), tasks.size());
            tasks.get(idx).mark();
            storage.save(tasks);

            printLine();
            System.out.println("I gotchu! I've marked this task as done:");
            System.out.println("  " + tasks.get(idx));
            printLine();
            return;
        }

        if (input.startsWith("unmark ")) {
            int idx = parseIndex(input.substring(7), tasks.size());
            tasks.get(idx).unmark();
            storage.save(tasks);

            printLine();
            System.out.println("Gotchu dawg, I've unmarked this task:");
            System.out.println("  " + tasks.get(idx));
            printLine();
            return;
        }

        if (input.startsWith("delete ")) {
            int idx = parseIndex(input.substring(7), tasks.size());
            Task removed = tasks.remove(idx);
            storage.save(tasks);

            printLine();
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removed);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            printLine();
            return;
        }

        if (input.equals("todo") || input.startsWith("todo ")) {
            String desc = (input.length() == 4) ? "" : input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new TyroneException("Todo cannot be empty. Use: todo <task>");
            }
            addTask(tasks, new Todo(desc), storage);
            return;
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

            addTask(tasks, new Deadline(desc, by), storage);
            return;
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

            addTask(tasks, new Event(desc, from, to), storage);
            return;
        }

        throw new TyroneException("I ain't understand that. Try: todo, deadline, event, list, mark, unmark, delete, bye");
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

    private static void addTask(ArrayList<Task> tasks, Task task, Storage storage) throws TyroneException {
        tasks.add(task);
        storage.save(tasks);

        printLine();
        System.out.println("Gotchu my guy. I added this task:");
        System.out.println("  " + task);
        System.out.println("Now you got " + tasks.size() + " tasks in yo list");
        printLine();
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
