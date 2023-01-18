package diary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    private Category category;
    private List<Session> sessions;

    @BeforeEach
    public void setup() {
        category = new Category("category");
        sessions = new ArrayList<Session>();
        Session session1 = new Session("sesh1", category, LocalDate.of(2020, 3, 12), 50);
        Session session2 = new Session("sesh2", category, LocalDate.of(2020, 3, 15), 20);
        sessions.add(session1);
        sessions.add(session2);
    }

    @Test
    public void testGetDuration() {
        assertEquals(70, category.getDuration());
        Session session3 = new Session("sesh3", category, LocalDate.of(2020, 3, 16), 20);
        sessions.add(session3);
        assertEquals(90, category.getDuration());
    }

}
