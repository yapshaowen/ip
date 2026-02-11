package tyrone;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from an initial collection of tasks.
     *
     * @param tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the size of the task list.
     *
     * @return
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the given index.
     *
     * @param index
     * @return
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Adds a task to the list.
     *
     * @param task
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list.
     *
     * @param index
     * @return
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as done.
     *
     * @param index
     */
    public void mark(int index) {
        tasks.get(index).mark();
    }

    /**
     * Marks the task at the given index as not done.
     *
     * @param index
     */
    public void unmark(int index) {
        tasks.get(index).unmark();
    }

    /**
     * Finds a task based on the keyword.
     *
     * @return
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

    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "No tasks yet bro.";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
