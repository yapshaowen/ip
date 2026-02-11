package tyrone;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Tyrone {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a Tyrone instance that loads/saves tasks from the given file.
     *
     * @param filePath
     */
    public Tyrone(Path filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(storage.load());
    }

    /**
     * Starts the chatbot and handles user commands until exit.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            if (input.isBlank()) {
                continue;
            }

            try {
                Parser.Command cmd = Parser.parse(input, tasks.size());

                switch (cmd.type) {
                case BYE:
                    ui.showBye();
                    ui.close();
                    return;

                case LIST:
                    ui.showList(tasks);
                    break;

                case TODO:
                    tasks.add(new Todo(cmd.description));
                    storage.save(tasks.getTasks());
                    ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                    break;

                case DEADLINE:
                    tasks.add(new Deadline(cmd.description, cmd.by));
                    storage.save(tasks.getTasks());
                    ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                    break;

                case EVENT:
                    tasks.add(new Event(cmd.description, cmd.from, cmd.to));
                    storage.save(tasks.getTasks());
                    ui.showAdd(tasks.get(tasks.size() - 1), tasks.size());
                    break;

                case MARK:
                    tasks.mark(cmd.index);
                    storage.save(tasks.getTasks());
                    ui.showMark(tasks.get(cmd.index));
                    break;

                case UNMARK:
                    tasks.unmark(cmd.index);
                    storage.save(tasks.getTasks());
                    ui.showUnmark(tasks.get(cmd.index));
                    break;

                case DELETE:
                    Task removed = tasks.remove(cmd.index);
                    storage.save(tasks.getTasks());
                    ui.showDelete(removed, tasks.size());
                    break;

                case FIND:
                    TaskList matches = tasks.find(cmd.keyword);
                    ui.showFindResults(matches);
                    break;

                default:
                    throw new TyroneException("Unknown command.");
                }
            } catch (TyroneException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public String getResponse(String input) {
        assert input != null: "Tyrone.getResponse: input shouldn't be null";
        try {
            Parser.Command cmd = Parser.parse(input, tasks.size());

            switch (cmd.type) {
                case BYE:
                    return "bye";

                case LIST:
                    return tasks.toString();

                case TODO:
                    tasks.addUnique(new Todo(cmd.description));
                    storage.save(tasks.getTasks());
                    return "Gotchu bro! I added this task:\n  " + tasks.get(tasks.size() - 1)
                            + "\nNow you got " + tasks.size() + " tasks in yo list bro";

                case DEADLINE:
                    tasks.addUnique(new Deadline(cmd.description, cmd.by));
                    storage.save(tasks.getTasks());
                    return "Gotchu bro! I added this task:\n  " + tasks.get(tasks.size() - 1)
                            + "\nNow you got " + tasks.size() + " tasks in yo list bro";

                case EVENT:
                    tasks.addUnique(new Event(cmd.description, cmd.from, cmd.to));
                    storage.save(tasks.getTasks());
                    return "Gotchu bro! I added this task:\n  " + tasks.get(tasks.size() - 1)
                            + "\nNow you got " + tasks.size() + " tasks in yo list bro";

                case MARK:
                    tasks.mark(cmd.index);
                    storage.save(tasks.getTasks());
                    return "Aight, marked:\n  " + tasks.get(cmd.index);

                case UNMARK:
                    tasks.unmark(cmd.index);
                    storage.save(tasks.getTasks());
                    return "Aight, unmarked:\n  " + tasks.get(cmd.index);

                case DELETE:
                    Task removed = tasks.remove(cmd.index);
                    storage.save(tasks.getTasks());
                    return "Removed:\n  " + removed
                            + "\nNow you got " + tasks.size() + " tasks in yo list bro";

                case FIND:
                    TaskList matches = tasks.find(cmd.keyword);
                    return matches.toString();

                default:
                    return "Unknown command.";
            }

        } catch (TyroneException e) {
            return e.getMessage();
        }
    }


    /**
     * Launches the chatbot using the default data file path.
     *
     * @param args
     */
    public static void main(String[] args) {
        Path dataPath = Paths.get("data", "tyrone.txt");
        new Tyrone(dataPath).run();
    }
}