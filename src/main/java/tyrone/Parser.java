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
     * Parses user input into a command
     *
     * @param input
     * @param taskCount
     * @return
     * @throws TyroneException
     */
    public static Command parse(String input, int taskCount) throws TyroneException {
        assert input != null : "Parser.parse: input shouldn't be null";
        assert taskCount >= 0 : "Parser.parse: taskCount should be non-negative";

        String trimmed = input.trim();

        Command simple = parseSimple(trimmed);
        if (simple != null) {
            return simple;
        }

        if (trimmed.equals("find") || trimmed.startsWith("find ")) {
            return Command.find(parseRequiredArg(trimmed, "find", "The keyword of a find command cannot be empty."));
        }

        if (trimmed.equals("todo") || trimmed.startsWith("todo ")) {
            return Command.todo(parseRequiredArg(trimmed, "todo", "The description of a todo cannot be empty."));
        }

        if (trimmed.equals("deadline") || trimmed.startsWith("deadline ")) {
            return parseDeadline(trimmed);
        }

        if (trimmed.equals("event") || trimmed.startsWith("event ")) {
            return parseEvent(trimmed);
        }

        Command idxCmd = parseIndexCommand(trimmed, taskCount);
        if (idxCmd != null) {
            return idxCmd;
        }

        throw new TyroneException("I'm sorry, but I don't know what that means.");
    }

    /**
     * Parses commands that have no arguments
     *
     * @param trimmed
     * @return
     */
    private static Command parseSimple(String trimmed) {
        switch (trimmed) {
            case "bye":
                return Command.simple(CommandType.BYE);
            case "list":
                return Command.simple(CommandType.LIST);
            default:
                return null;
        }
    }

    /**
     * Extracts a required argument after a command word
     *
     * @param trimmed
     * @param keyword
     * @param emptyMsg
     * @return
     * @throws TyroneException
     */
    private static String parseRequiredArg(String trimmed, String keyword, String emptyMsg) throws TyroneException {
        String arg = trimmed.length() == keyword.length() ? "" : trimmed.substring(keyword.length() + 1).trim();
        if (arg.isEmpty()) {
            throw new TyroneException(emptyMsg);
        }
        return arg;
    }

    /**
     * Parses a deadline command
     *
     * @param trimmed
     * @return
     * @throws TyroneException
     */
    private static Command parseDeadline(String trimmed) throws TyroneException {
        int byPos = trimmed.indexOf(" /by ");
        if (byPos == -1) {
            throw new TyroneException("Invalid deadline format. Use: deadline <task> /by yyyy-mm-dd");
        }

        String desc = trimmed.substring("deadline".length() + 1, byPos).trim();
        String byStr = trimmed.substring(byPos + " /by ".length()).trim();

        if (desc.isEmpty()) {
            throw new TyroneException("The description of a deadline cannot be empty.");
        }
        if (byStr.isEmpty()) {
            throw new TyroneException("The /by part of a deadline cannot be empty.");
        }

        try {
            return Command.deadline(desc, LocalDate.parse(byStr));
        } catch (DateTimeParseException e) {
            throw new TyroneException("Invalid date format. Use yyyy-mm-dd (e.g., 2019-10-15).");
        }
    }

    /**
     * Parses an event command
     */
    private static Command parseEvent(String trimmed) throws TyroneException {
        int fromPos = trimmed.indexOf(" /from ");
        int toPos = trimmed.indexOf(" /to ");
        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new TyroneException("Invalid event format. Use: event <task> /from <start> /to <end>");
        }

        String desc = trimmed.substring("event".length() + 1, fromPos).trim();
        String from = trimmed.substring(fromPos + " /from ".length(), toPos).trim();
        String to = trimmed.substring(toPos + " /to ".length()).trim();

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

    /**
     * Parses commands that require an index
     *
     * @param trimmed
     * @param taskCount
     * @return
     * @throws TyroneException
     */
    private static Command parseIndexCommand(String trimmed, int taskCount) throws TyroneException {
        if (trimmed.startsWith("mark ")) {
            return Command.indexCmd(CommandType.MARK, parseIndex(trimmed.substring(5), taskCount));
        }
        if (trimmed.startsWith("unmark ")) {
            return Command.indexCmd(CommandType.UNMARK, parseIndex(trimmed.substring(7), taskCount));
        }
        if (trimmed.startsWith("delete ")) {
            return Command.indexCmd(CommandType.DELETE, parseIndex(trimmed.substring(7), taskCount));
        }
        return null;
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