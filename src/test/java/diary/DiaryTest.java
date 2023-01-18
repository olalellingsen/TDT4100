package diary;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DiaryTest {
    private Diary diary;

    @BeforeEach
    public void setup() {
        diary = new Diary("Piano diary");
        diary.addSession("D major", "scales", LocalDate.of(2022, 02, 11), 10);
        diary.addSession("Etude 1", "Etudes", LocalDate.of(2022, 02, 10), 10);
        diary.addSession("G harmonic minor", "scales", LocalDate.of(2022, 02, 14), 5);
        diary.addSession("Let it be", "Beatles tunes", LocalDate.of(2022, 02, 12), 15);
    }

    @DisplayName("Test set a diary goal")
    @Test
    public void testSetGoal() {
        assertEquals(0, diary.getGoal());

        // hour and minute input
        diary.setGoal("2", "20");
        assertEquals(140, diary.getGoal());
        diary.setGoal(50);
        assertEquals(50, diary.getGoal());

        assertEquals(80, diary.getCompletionStatus());// percentage of goal completed
        assertEquals(0.8, diary.getCompletionDbl(), 1.000000001);// double, percentage of goal completed / 100

        assertNotEquals(50, diary.getCompletionStatus());

        assertThrows(IllegalArgumentException.class, () -> {
            diary.setGoal("-30", "-2");
        }, "Test: set goal to a negative number.");

        assertThrows(IllegalArgumentException.class, () -> {
            diary.setGoal(-50);
        }, "Test: set goal to a negative number.");

    }

    @DisplayName("Testing session inputs")
    @Test
    public void testSessionInputs() {
        // testing name
        assertEquals("D major", diary.getSessions().get(0).getName());
        assertEquals("Etude 1", diary.getSessions().get(1).getName());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            diary.getSessions().get(4);
        });

        // testing categories
        assertEquals("scales", diary.getCategories().get(0).getName());
        assertNotEquals("scales", diary.getCategories().get(1).getName());
        assertEquals("Etudes", diary.getCategories().get(1).getName());
        assertThrows(IndexOutOfBoundsException.class, () -> {
            diary.getCategories().get(3).getName();
        });

        // testing duration
        assertEquals(10, diary.getSessions().get(0).getDuration());
        assertEquals(10, diary.getSessions().get(1).getDuration());

        assertEquals(40, diary.getTotalDuration());

        // testing category duration
        assertEquals(15, diary.getCategories().get(0).getDuration());
        assertEquals(10, diary.getCategories().get(1).getDuration());
    }

    @DisplayName("Test adding and deleting sessions in diary")
    @Test
    public void testAddAndDeleteSession() {
        diary.setGoal("2", "40");
        assertEquals(25, diary.getCompletionStatus());

        // add sessions
        diary.addSession("Yesterday", "Beatles tunes", LocalDate.of(2022, 02, 13), 20);
        diary.addSession("Db major", "scales", LocalDate.of(2022, 02, 13), 5);
        diary.addSession("Prelude 1", "Etudes", LocalDate.of(2022, 02, 13), 10);
        diary.addSession("Prelude 2", "Etudes", LocalDate.of(2022, 02, 15), 5);

        assertEquals(8, diary.getSessions().size());
        assertEquals(3, diary.getCategories().size());
        assertEquals("Beatles tunes", diary.getCategories().get(2).getName());

        assertEquals(80, diary.getTotalDuration());
        assertEquals(50, diary.getCompletionStatus());

        // deletes sessions
        diary.deleteSession(4);
        diary.deleteSession(4);
        diary.deleteSession(4);
        diary.deleteSession(4);

        assertEquals(4, diary.getSessions().size());
        assertEquals(3, diary.getCategories().size());
        assertEquals("scales", diary.getCategories().get(0).getName());
        assertEquals("Beatles tunes", diary.getSessions().get(3).getCategory().getName());

        assertEquals(25, diary.getCompletionStatus());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            diary.deleteSession(5);// wrong list index
        });
    }

    @DisplayName("Test invalid session inputs")
    @Test
    public void testInvalidSessionInput() {
        // no date
        assertThrows(IllegalArgumentException.class, () -> {
            diary.addSession("test", "test", null, 10);
        });

        // no category
        // ok with null as category, it will be displayed as "no category" in the app.
        assertDoesNotThrow(() -> {
            diary.addSession("intervals", null, LocalDate.of(2022, 4, 10), 20);
        });
        assertDoesNotThrow(() -> {
            diary.addSession("intervals", "", LocalDate.of(2022, 4, 10), 20);
        });

        // no name
        assertThrows(IllegalArgumentException.class, () -> {
            diary.addSession("", "test", LocalDate.of(2022, 4, 10), 10);
        });

        // negative duration
        assertThrows(IllegalArgumentException.class, () -> {
            diary.addSession("test", "test", LocalDate.of(2022, 4, 10), -20);
        });

        // invalid duration input type
        assertThrows(IllegalArgumentException.class, () -> {
            diary.validateSessionInput("test", LocalDate.of(2022, 4, 10), "test");
        });
    }

    @DisplayName("Test HashMap of categories and their percentage of the total duration")
    @Test
    public void testCategoryDistribution() {

        // test if number of categories is equal to keys in HashMap
        assertEquals(diary.getCategories().size(), diary.getDistributionStatus().keySet().size());

        // test if sum of values in HashMap is 1.0
        float sum = 0;
        for (float value : diary.getDistributionStatus().values()) {
            sum += value;
        }
        assertEquals(1.0, sum, 0.00001);

        List<String> expectedCategories = new ArrayList<>(Arrays.asList("scales", "Etudes", "Beatles tunes"));

        List<String> actualCategories = diary.getDistributionStatus()
                .keySet().stream().map(category -> category.getName()).toList();

        for (String name : expectedCategories) {
            assertTrue(actualCategories.contains(name));
        }
        assertEquals(expectedCategories.size(), diary.getCategories().size());

        diary.addSession("test With Same Category", "scales", LocalDate.of(2022, 02, 13), 5);

        assertEquals(expectedCategories.size(), diary.getCategories().size());

        diary.addSession("test With New Category", "new Category", LocalDate.of(2022, 02, 13), 5);

        assertEquals(expectedCategories.size() + 1, diary.getCategories().size());

        // removing sessions so there is one category left
        diary.deleteSession(0);
        diary.deleteSession(0);
        diary.deleteSession(0);
        diary.deleteSession(0);
        diary.deleteSession(0);

        assertEquals("{new Category=1.0}", diary.getDistributionStatus().toString());
    }

    @DisplayName("Test sorting by session name")
    @Test
    public void testSortByName() {
        Diary testSorting = new Diary("test");

        diary.setSessionsByName();

        testSorting.addSession("D major", "scales", LocalDate.of(2022, 02, 11), 10);
        testSorting.addSession("Etude 1", "Etudes", LocalDate.of(2022, 02, 10), 10);
        testSorting.addSession("G harmonic minor", "scales", LocalDate.of(2022, 02, 14), 5);
        testSorting.addSession("Let it be", "Beatles tunes", LocalDate.of(2022, 02, 12), 15);

        String a = diary.getSessions().toString();
        String e = testSorting.getSessions().toString();
        assertEquals(e, a);
    }

    @DisplayName("Test sorting by category name")
    @Test
    public void testSortByCategoryName() {
        Diary testSorting = new Diary("test");

        diary.setSessionsByCategoryName();

        testSorting.addSession("Let it be", "Beatles tunes", LocalDate.of(2022, 02, 12), 15);
        testSorting.addSession("Etude 1", "Etudes", LocalDate.of(2022, 02, 10), 10);
        testSorting.addSession("D major", "scales", LocalDate.of(2022, 02, 11), 10);
        testSorting.addSession("G harmonic minor", "scales", LocalDate.of(2022, 02, 14), 5);

        String a = diary.getSessions().toString();
        String e = testSorting.getSessions().toString();
        assertEquals(e, a);
    }

    @DisplayName("Test sorting by session date")
    @Test
    public void testSortByDate() {
        Diary testSorting = new Diary("test");

        diary.setSessionsByDate();

        testSorting.addSession("G harmonic minor", "scales", LocalDate.of(2022, 02, 14), 5);
        testSorting.addSession("Let it be", "Beatles tunes", LocalDate.of(2022, 02, 12), 15);
        testSorting.addSession("D major", "scales", LocalDate.of(2022, 02, 11), 10);
        testSorting.addSession("Etude 1", "Etudes", LocalDate.of(2022, 02, 10), 10);

        String a = diary.getSessions().toString();
        String e = testSorting.getSessions().toString();
        assertEquals(e, a);
    }

    @DisplayName("Test sorting by session duration")
    @Test
    public void testSortByDuration() {
        Diary testSorting = new Diary("test");

        diary.setSessionsByDuration();

        testSorting.addSession("Let it be", "Beatles tunes", LocalDate.of(2022, 02, 12), 15);
        testSorting.addSession("D major", "scales", LocalDate.of(2022, 02, 11), 10);
        testSorting.addSession("Etude 1", "Etudes", LocalDate.of(2022, 02, 10), 10);
        testSorting.addSession("G harmonic minor", "scales", LocalDate.of(2022, 02, 14), 5);

        String a = diary.getSessions().toString();
        String e = testSorting.getSessions().toString();
        assertEquals(e, a);
    }

}
