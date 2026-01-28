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
