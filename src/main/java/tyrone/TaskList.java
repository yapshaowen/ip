package tyrone;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Ensures the data directory and file exist before reading/writing.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates an empty task list.
     *
     * @param tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Creates a task list from an initial collection of tasks.
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
        assert index >= 0 && index < tasks.size(): "TaskList.get: index out of bounds";
        return tasks.get(index);
    }

    /**
     * Returns the backing list for saving purposes.
     *
     * @param task
     */
    public void add(Task task) {
        assert task != null: "TaskList.add: task shouldn't be null";
        tasks.add(task);
    }

    /**
     * Adds a task to the list.
     *
     * @param index
     * @return
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size(): "TaskList.remove: index out of bounds";
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as done.
     *
     * @param index
     */
    public void mark(int index) {
        assert index >= 0 && index < tasks.size(): "TaskList.mark: index out of bounds";
        tasks.get(index).mark();
    }

    /**
     * Marks the task at the given index as not done.
     *
     * @param index
     */
    public void unmark(int index) {
        assert index >= 0 && index < tasks.size(): "TaskList.unmark: index out of bounds";
        tasks.get(index).unmark();
    }

    /**
     * Returns the backing list for saving purposes.
     *
     * @return
     *
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
