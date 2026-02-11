package tyrone;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public enum CommandType {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, FIND
    }

    /**
     * Represents a parsed user command and its associated arguments.
     */
    public static class Command {
        public final CommandType type;
        public final String description;
        public final LocalDate by;
        public final String from;
        public final String to;
        public final String keyword;
        public final int index;

        private Command(CommandType type, String description, LocalDate by, String from,
                        String to, int index, String keyword) {
            this.type = type;
            this.description = description;
            this.by = by;
            this.from = from;
            this.to = to;
            this.index = index;
            this.keyword = keyword;
        }

        /**
         * Creates a command with no extra arguments (e.g., bye/list).
         *
         * @param type
         * @return
         */
        public static Command simple(CommandType type) {
            return new Command(type, null, null, null, null, -1, null);
        }

        /**
         * Creates a command with no extra arguments (e.g., bye/list).
         *
         * @param desc
         * @return
         */
        public static Command todo(String desc) {
            return new Command(CommandType.TODO, desc, null, null, null, -1, null);
        }

        /**
         * Creates a todo command with the given description.
         *
         * @param desc
         * @param by
         * @return
         */
        public static Command deadline(String desc, LocalDate by) {
            return new Command(CommandType.DEADLINE, desc, by, null, null, -1, null);
        }

        /**
         * Creates an event command with the given description and time range.
         *
         * @param desc
         * @param from
         * @param to
         * @return
         */
        public static Command event(String desc, String from, String to) {
            return new Command(CommandType.EVENT, desc, null, from, to, -1, null);
        }

        /**
         * Creates an event command with the given description and time range.
         *
         * @param type
         * @param idx
         * @return
         */
        public static Command indexCmd(CommandType type, int idx) {
            return new Command(type, null, null, null, null, idx, null);
        }

        public static Command find(String keyword) {
            return new Command(CommandType.FIND, null, null, null, null, -1, keyword);
        }
    }

    /**
     * Parses raw user input into a structured command.
     *
     * @param input
     * @param taskCount
     * @return
     * @throws TyroneException
     */
    public static Command parse(String input, int taskCount) throws TyroneException {
        assert input != null: "Parser.parse: input shouldn't be null";
        assert taskCount >= 0: "Paerser.parse: taskCount should be non-negative";
        String trimmed = input.trim();

        if (trimmed.equals("bye")) {
            return Command.simple(CommandType.BYE);
        }
        if (trimmed.equals("list")) {
            return Command.simple(CommandType.LIST);
        }

        if (trimmed.equals("find") || trimmed.startsWith("find ")) {
            String keyword = trimmed.length() == 4 ? "" : trimmed.substring(5).trim();
            if (keyword.isEmpty()) {
                throw new TyroneException("The keyword of a find command cannot be empty.");
            }
            return Command.find(keyword);
        }

        if (trimmed.equals("todo") || trimmed.startsWith("todo ")) {
            String desc = trimmed.length() == 4 ? "" : trimmed.substring(5).trim();
            if (desc.isEmpty()) {
                throw new TyroneException("The description of a todo cannot be empty.");
            }
            return Command.todo(desc);
        }

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

        if (trimmed.startsWith("mark ")) {
            int idx = parseIndex(trimmed.substring(5), taskCount);
            return Command.indexCmd(CommandType.MARK, idx);
        }

        if (trimmed.startsWith("unmark ")) {
            int idx = parseIndex(trimmed.substring(7), taskCount);
            return Command.indexCmd(CommandType.UNMARK, idx);
        }

        if (trimmed.startsWith("delete ")) {
            int idx = parseIndex(trimmed.substring(7), taskCount);
            return Command.indexCmd(CommandType.DELETE, idx);
        }

        throw new TyroneException("I'm sorry, but I don't know what that means.");
    }

    /**
     * Converts a 1-based task number string into a validated 0-based index.
     *
     * @param raw
     * @param taskCount
     * @return
     * @throws TyroneException
     */
    private static int parseIndex(String raw, int taskCount) throws TyroneException {
        assert taskCount >= 0: "parseIndex: taskCount should be non-negative";
        assert raw != null: "parseIndex: raw index string shouldn't be null";
        String input = raw.trim();
        if (input.isEmpty()) {
            throw new TyroneException("tyrone.Task number cannot be empty.");
        }

        final int oneBased;
        try {
            oneBased = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new TyroneException("tyrone.Task number must be a number.");
        }

        int idx = oneBased - 1;
        if (idx < 0 || idx >= taskCount) {
            throw new TyroneException("tyrone.Task number out of range. Use 1 to " + taskCount + ".");
        }
        return idx;
    }
}