package tyrone;

public class Event extends Task{
    protected String from;
    protected String to;

    /**
     * Creates an event task with a start and end time.
     *
     * @param desc
     * @param from
     * @param to
     */
    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    /**
     * Returns the string shown to the user for this task type.
     *
     * @return
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
