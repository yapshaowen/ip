import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public enum CommandType {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE
    }

    public static class Command {
        public final CommandType type;
        public final String description;
        public final LocalDate by;
        public final String from;
        public final String to;

        // Used by MARK/UNMARK/DELETE (0-based)
        public final int index;

        private Command(CommandType type, String description, LocalDate by, String from, String to, int index) {
            this.type = type;
            this.description = description;
            this.by = by;
            this.from = from;
            this.to = to;
            this.index = index;
        }

        public static Command simple(CommandType type) {
            return new Command(type, null, null, null, null, -1);
        }

        public static Command todo(String desc) {
            return new Command(CommandType.TODO, desc, null, null, null, -1);
        }

        public static Command deadline(String desc, LocalDate by) {
            return new Command(CommandType.DEADLINE, desc, by, null, null, -1);
        }

        public static Command event(String desc, String from, String to) {
            return new Command(CommandType.EVENT, desc, null, from, to, -1);
        }

        public static Command indexCmd(CommandType type, int idx) {
            return new Command(type, null, null, null, null, idx);
        }
    }

    public static Command parse(String input, int taskCount) throws TyroneException {
        String trimmed = input.trim();

        if (trimmed.equals("bye")) {
            return Command.simple(CommandType.BYE);
        }
        if (trimmed.equals("list")) {
            return Command.simple(CommandType.LIST);
        }

        // todo <desc>
        if (trimmed.equals("todo") || trimmed.startsWith("todo ")) {
            String desc = trimmed.length() == 4 ? "" : trimmed.substring(5).trim();
            if (desc.isEmpty()) {
                throw new TyroneException("The description of a todo cannot be empty.");
            }
            return Command.todo(desc);
        }

        // deadline <desc> /by yyyy-mm-dd
        if (trimmed.equals("deadline") || trimmed.startsWith("deadline ")) {
            int byPos = trimmed.indexOf(" /by ");
            if (byPos == -1) {
                throw new TyroneException("Invalid deadline format. Use: deadline <task> /by yyyy-mm-dd");
            }

            String desc = trimmed.substring(9, byPos).trim();
            String byStr = trimmed.substring(byPos + 5).trim();

            if (desc.isEmpty()) {
                throw new TyroneException("The description of a deadline cannot be empty.");
            }
            if (byStr.isEmpty()) {
                throw new TyroneException("The /by part of a deadline cannot be empty.");
            }

            try {
                LocalDate by = LocalDate.parse(byStr); // expects yyyy-MM-dd
                return Command.deadline(desc, by);
            } catch (DateTimeParseException e) {
                throw new TyroneException("Invalid date format. Use yyyy-mm-dd (e.g., 2019-10-15).");
            }
        }

        // event <desc> /from <start> /to <end>  (still strings)
        if (trimmed.equals("event") || trimmed.startsWith("event ")) {
            int fromPos = trimmed.indexOf(" /from ");
            int toPos = trimmed.indexOf(" /to ");
            if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                throw new TyroneException("Invalid event format. Use: event <task> /from <start> /to <end>");
            }

            String desc = trimmed.substring(6, fromPos).trim();
            String from = trimmed.substring(fromPos + 7, toPos).trim();
            String to = trimmed.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw new TyroneException("The description of an event cannot be empty.");
            }
            if (from.isEmpty()) {
                throw new TyroneException("The /from part of an event cannot be empty.");
            }
            if (to.isEmpty()) {
                throw new TyroneException("The /to part of an event cannot be empty.");
            }

            return Command.event(desc, from, to);
        }

        // mark <index>
        if (trimmed.startsWith("mark ")) {
            int idx = parseIndex(trimmed.substring(5), taskCount);
            return Command.indexCmd(CommandType.MARK, idx);
        }

        // unmark <index>
        if (trimmed.startsWith("unmark ")) {
            int idx = parseIndex(trimmed.substring(7), taskCount);
            return Command.indexCmd(CommandType.UNMARK, idx);
        }

        // delete <index>
        if (trimmed.startsWith("delete ")) {
            int idx = parseIndex(trimmed.substring(7), taskCount);
            return Command.indexCmd(CommandType.DELETE, idx);
        }

        throw new TyroneException("I'm sorry, but I don't know what that means.");
    }

    private static int parseIndex(String raw, int taskCount) throws TyroneException {
        String s = raw.trim();
        if (s.isEmpty()) {
            throw new TyroneException("Task number cannot be empty.");
        }

        final int oneBased;
        try {
            oneBased = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new TyroneException("Task number must be a number.");
        }

        int idx = oneBased - 1;
        if (idx < 0 || idx >= taskCount) {
            throw new TyroneException("Task number out of range. Use 1 to " + taskCount + ".");
        }
        return idx;
    }
}