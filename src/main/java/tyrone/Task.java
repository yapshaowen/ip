package tyrone;

public class Task {
    private final String desc;
    private boolean done;

    public Task(String desc) {
        this.desc = desc;
        this.done = false;
    }

    public void mark() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

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
