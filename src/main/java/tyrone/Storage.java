package tyrone;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() {
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

    public void save(ArrayList<Task> tasks) throws TyroneException {
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

        if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + done + " | " + t.getDesc() + " | " + d.getBy().toString();
        }

        if (t instanceof Event) {
            Event e = (Event) t;
            return "E | " + done + " | " + t.getDesc() + " | " + e.getFrom() + " | " + e.getTo();
        }

        throw new TyroneException("Unknown task type can't be saved bro!");
    }

    private Task parseLine(String line) {
        if (line == null) {
            return null;
        }

        String trimmed = line.trim();

        if (trimmed.isEmpty()) {
            return null;
        }

        String[] parts = trimmed.split("\\|", -1);
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        String doneStr = parts[1].trim();
        String desc = parts[2].trim();

        boolean done;

        if (doneStr.equals("1")) {
            done = true;
        } else if (doneStr.equals("0")) {
            done = false;
        } else {
            return null;
        }

        try {
            Task t;
            if (type.contentEquals("T")) {
                t = new Todo(desc);
            } else if (type.equals("D")) {
                if (parts.length < 4) {
                    return null;
                }
                LocalDate by = LocalDate.parse(parts[3].trim());
                t = new Deadline(desc, by);
            } else if (type.equals("E")) {
                if (parts.length < 5) {
                    return null;
                }
                t = new Event(desc, parts[3].trim(), parts[4].trim());
            } else {
                return null;
            }

            if (done) {
                t.mark();
            } else {
                t.unmark();
            }

            return t;
        } catch (Exception e) {
            return null;
        }
    }
}
