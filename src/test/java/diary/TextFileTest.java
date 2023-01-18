package diary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TextFileTest {
    Diary testDiary;
    FileHandlerText fileHandler;

    @BeforeEach
    public void setup() {
        testDiary = new Diary("test");
        fileHandler = new FileHandlerText();
    }

    @Test
    public void testWriteToFile() throws FileNotFoundException {

        testDiary.setGoal("3", "30");
        testDiary.addSession("sesh1", "categoryA", LocalDate.of(2022, 4, 26), 10);
        testDiary.addSession("sesh2", "categoryA", LocalDate.of(2022, 4, 26), 20);
        testDiary.addSession("sesh3", "categoryB", LocalDate.of(2022, 4, 26), 15);
        testDiary.addSession("sesh4", "categoryC", LocalDate.of(2022, 4, 26), 15);
        testDiary.addSession("sesh5", "categoryC", LocalDate.of(2022, 4, 26), 15);

        try {
            fileHandler.writeDiaryToFile("testFile1", testDiary);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // if there is whitespace in the filename
        assertThrows(IllegalArgumentException.class, () -> {
            fileHandler.writeDiaryToFile("test File 2", testDiary);
        });

        Scanner scanner = new Scanner(new File("src/main/resources/diaryFiles/testFile1.txt"));

        while (scanner.hasNext()) {
            assertEquals("210", scanner.nextLine());
            assertEquals("2022-04-26;sesh1;categoryA;10", scanner.nextLine());
            assertEquals("2022-04-26;sesh2;categoryA;20", scanner.nextLine());
            assertEquals("2022-04-26;sesh3;categoryB;15", scanner.nextLine());
            assertEquals("2022-04-26;sesh4;categoryC;15", scanner.nextLine());
            assertEquals("2022-04-26;sesh5;categoryC;15", scanner.nextLine());

            // no next line
            assertThrows(NoSuchElementException.class, () -> {
                scanner.nextLine();
            });
        }
    }

    @Test
    public void testReadFromFile() {
        try {
            testDiary = fileHandler.readFromDiaryFile("testFile2");
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals("testFile2", testDiary.getName());
        assertEquals(320, testDiary.getGoal());
        assertEquals(5, testDiary.getSessions().size());
        assertEquals(105, testDiary.getTotalDuration());
        assertEquals(33, testDiary.getCompletionStatus());
        assertEquals(3, testDiary.getCategories().size());

        assertEquals("2022-04-27", testDiary.getSessions().get(0).getDate().toString());
        assertEquals("2022-04-27", testDiary.getSessions().get(4).getDate().toString());

        assertEquals("sesh1", testDiary.getSessions().get(0).getName());
        assertEquals("sesh2", testDiary.getSessions().get(1).getName());

        assertThrows(IllegalArgumentException.class, () -> {
            fileHandler.readFromDiaryFile("wrong name");
        });
        assertThrows(IOException.class, () -> {
            fileHandler.readFromDiaryFile("wrongname");
        });

    }

}
