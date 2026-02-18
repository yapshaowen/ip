package tyrone;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        assert filePath != null: "Storage: filePath shouldn't be null";
        this.filePath = filePath;
    }

    /**
     * Creates an index-based command (e.g., mark/unmark/delete).
     *
     * @return
     */
    public ArrayList<Task> load() {
        assert filePath != null: "Storage.load: filePath should not be null";
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(filePath)) {
                return tasks;
            }

            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                Task t = parseLine(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }

        return tasks;
    }

    /**
     * Loads tasks from disk; returns an empty list if the file is missing.
     *
     * @param tasks
     * @throws TyroneException
     */
    public void save(ArrayList<Task> tasks) throws TyroneException {
        assert tasks != null: "Storage.save: tasks list shouldn't be null";
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
                for (Task t : tasks) {
                    bw.write(serializeTask(t));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new TyroneException("Couldn't save tasks to disk bro.");
        }
    }

    private String serializeTask(Task t) throws TyroneException {
        int done = t.isDone() ? 1 : 0;

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.getDesc();
        }

        if (t instanceof Deadline d) {
            return "D | " + done + " | " + t.getDesc() + " | " + d.getBy().toString();
        }

        if (t instanceof Event e) {
            return "E | " + done + " | " + t.getDesc() + " | " + e.getFrom() + " | " + e.getTo();
        }

        throw new TyroneException("Unknown task type can't be saved bro!");
    }

    /**
     * Parses a single line from the storage file into a task
     *
     * @param line
     * @return
     */
    private Task parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.trim().split("\\|", -1);
        if (parts.length < 3) {
            return null;
        }

        boolean done = parseDone(parts[1]);
        if (done == false && !parts[1].trim().equals("0")) {
            return null;
        }

        try {
            Task task = createTask(parts);
            if (task != null && done) {
                task.mark();
            }
            return task;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts the done flag string into a boolean
     *
     * @param flag
     * @return
     */
    private boolean parseDone(String flag) {
        return flag.trim().equals("1");
    }

    private Task createTask(String[] parts) {
        String type = parts[0].trim();
        String desc = parts[2].trim();

        switch (type) {
            case "T":
                return new Todo(desc);
            case "D":
                return parts.length >= 4
                        ? new Deadline(desc, LocalDate.parse(parts[3].trim()))
                        : null;
            case "E":
                return parts.length >= 5
                        ? new Event(desc, parts[3].trim(), parts[4].trim())
                        : null;
            default:
                return null;
        }
    }
}
