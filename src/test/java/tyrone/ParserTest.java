package tyrone;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_deadline_validDate_parsesLocalDate() throws Exception {
        Parser.Command cmd = Parser.parse("deadline return book /by 2019-10-15", 0);

        assertEquals(Parser.CommandType.DEADLINE, cmd.type);
        assertEquals("return book", cmd.description);
        assertEquals(LocalDate.of(2019, 10, 15), cmd.by);
    }

    @Test
    public void parse_deadline_invalidDate_throws() {
        TyroneException e = assertThrows(
                TyroneException.class,
                () -> Parser.parse("deadline return book /by 2019/10/15", 0)
        );
        assertTrue(e.getMessage().toLowerCase().contains("invalid date format"));
    }

    @Test
    public void parse_mark_outOfRange_throws() {
        TyroneException e = assertThrows(
                TyroneException.class,
                () -> Parser.parse("mark 2", 1)
        );
        assertTrue(e.getMessage().toLowerCase().contains("out of range"));
    }
}