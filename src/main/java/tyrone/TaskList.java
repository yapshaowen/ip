package tyrone;

import java.util.ArrayList;

public record TaskList(ArrayList<Task> tasks) {
    public TaskList() {
        this(new ArrayList<>());
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public void mark(int index) {
        tasks.get(index).mark();
    }

    public void unmark(int index) {
        tasks.get(index).unmark();
    }
}
