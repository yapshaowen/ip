package tyrone;

public class Task {
    private final String desc;
    private boolean done;

    /**
     * Creates a task with the given description.
     *
     * @param desc
     */
    public Task(String desc) {
        this.desc = desc;
        this.done = false;
    }

    /**
     * Marks this task as done.
     */
    public void mark() {
        this.done = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmark() {
        this.done = false;
    }

    /**
     * Returns whether the task is marked done.
     *
     * @return
     */
    public boolean isDone() {
        return this.done;
    }

    public String getDesc() {
        return this.desc;
    }

    @Override
    public String toString() {
        return "[" + (isDone() ? "X" : " ") + "] " + desc;
    }
}
