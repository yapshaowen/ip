package tyrone;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDate by;
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Creates a deadline task with a due date.
     *
     * @param desc
     * @param by
     */
    public Deadline(String desc, LocalDate by) {
        super(desc);
        this.by = by;
    }

    public LocalDate getBy() {
        return this.by;
    }

    @Override
    public boolean isSameTask(Task other) {
        if (!(other instanceof Deadline)) {
            return false;
        }
        Deadline task = (Deadline) other;
        return this.getDesc().equalsIgnoreCase(task.getDesc())
                && this.by.equals(task.by);
    }

    /**
     * Returns the string shown to the user for this task type.
     *
     * @return
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + "(by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
