package tyrone;

import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";

    private final Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays the greeting message on startup.
     */
    public void showWelcome() {
        String logo =
                " _______   ______   ___  _   _ _____ \n"
                        + "|_   _\\ \\ / /  _ \\ / _ \\| \\ | | ____|\n"
                        + "  | |  \\ V /| |_) | | | |  \\| |  _|  \n"
                        + "  | |   | | |  _ <| |_| | |\\  | |___ \n"
                        + "  |_|   |_| |_| \\_\\\\___/|_| \\_|_____|\n";
        showLine();
        System.out.println("Wassup dawg? It's ya boy\n" + logo);
        System.out.println("Watchu want?");
        showLine();
    }

    /**
     * Reads one command line from the user.
     *
     * @return
     */
    public String readCommand() {
        return sc.nextLine().trim();
    }

    /**
     * Displays the exit message.
     */
    public void showBye() {
        showLine();
        System.out.println("Bye bro!");
        showLine();
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg
     */
    public void showError(String msg) {
        showLine();
        System.out.println("BROOOO!!!! " + msg);
        showLine();
    }

    /**
     * Displays all tasks currently in the list.
     *
     * @param taskList
     */
    public void showList(TaskList taskList) {
        showLine();
        System.out.println("Here are your tasks bro:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + "." + taskList.get(i));
        }
        showLine();
    }

    /**
     * Displays confirmation after adding a task.
     *
     * @param task
     * @param count
     */
    public void showAdd(Task task, int count) {
        showLine();
        System.out.println("Gotchu bro! I added this task:");
        System.out.println(" " + task);
        System.out.println("Now you got " + count + " tasks in yo list bro");
        showLine();
    }

    /**
     * Displays confirmation after marking a task.
     *
     * @param task
     */
    public void showMark(Task task) {
        showLine();
        System.out.println("Nice! I marked this task as done bro:");
        System.out.println(" " + task);
        showLine();
    }

    /**
     * Displays confirmation after marking a task.
     *
     * @param task
     */
    public void showUnmark(Task task) {
        showLine();
        System.out.println("Aight, I unmarked this task bro:");
        System.out.println(" " + task);
        showLine();
    }

    /**
     * Displays confirmation after deleting a task.
     *
     * @param removed
     * @param count
     */
    public void showDelete(Task removed, int count) {
        showLine();
        System.out.println("Aight, I marked this task as not done yet: ");
        System.out.println(" " + removed);
        System.out.println("Now you get " + count + " tasks in the list bro!");
        showLine();
    }

    /**
     * Releases UI resources (e.g., closes the scanner).
     */
    public void close() {
        sc.close();
    }
}
