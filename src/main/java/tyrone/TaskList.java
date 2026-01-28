package tyrone;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
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

    /**
     * Returns a new TaskList containing tasks whose descriptions contain the keyword.
     */
    public TaskList find(String keyword) {
        String key = keyword.toLowerCase();
        ArrayList<Task> matches = new ArrayList<>();

        for (Task t : this.tasks) { // <-- use your actual internal list name
            if (t.getDesc().toLowerCase().contains(key)) { // see note below
                matches.add(t);
            }
        }
        return new TaskList(matches);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
