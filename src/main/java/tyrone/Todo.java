package tyrone;

public class Todo extends Task{

    /**
     * Creates a todo task.
     *
     * @param desc
     */
    public Todo(String desc) {
        super(desc);
    }

    /**
     * Returns the string shown to the user for this task type.
     *
     * @return
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
