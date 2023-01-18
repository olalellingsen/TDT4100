package diary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class SessionTest {
    private Session session;
    private Category category;

    @Test
    public void testSessionConstructor() {
        category = new Category("test");
        session = new Session("run", category, LocalDate.of(2022, 4, 15), 45);

        assertEquals(45, session.getCategory().getDuration());
        assertEquals("test", session.getCategory().getName());
        assertNotEquals(25, session.getCategory().getDuration());
        assertEquals(session, session.getCategory().getSessions().get(0));
    }

}
