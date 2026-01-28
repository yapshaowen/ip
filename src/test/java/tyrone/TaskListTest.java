package tyrone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void remove_middleItem_shiftsAndReturnsRemoved() {
        TaskList list = new TaskList();
        list.add(new Todo("a"));
        list.add(new Todo("b"));
        list.add(new Todo("c"));

        Task removed = list.remove(1);

        assertEquals("[T][ ] b", removed.toString());
        assertEquals(2, list.size());
        assertEquals("[T][ ] a", list.get(0).toString());
        assertEquals("[T][ ] c", list.get(1).toString());
    }

    @Test
    public void mark_then_unmark_updatesStatus() {
        TaskList list = new TaskList();
        list.add(new Todo("read book"));

        list.mark(0);
        assertTrue(list.get(0).toString().contains("[X]"));

        list.unmark(0);
        assertTrue(list.get(0).toString().contains("[ ]"));
    }
}