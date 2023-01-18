package diary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class FileHandlerText implements FileHandler {

    @Override
    public void writeDiaryToFile(String filename, Diary diary) {
        if (filename.contains(" ")) {
            throw new IllegalArgumentException("No whitespace in filename! ");
        }
        try {
            PrintWriter writer = new PrintWriter("src/main/resources/diaryFiles/" + filename + ".txt");
            writer.println(diary.getGoal());
            for (Session s : diary.getSessions()) {
                writer.println(
                        s.getDate().toString() + ";" + s.getName() + ";" + s.getCategory() + ";" + s.getDuration());
            }
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Diary readFromDiaryFile(String filename) throws IOException, ClassNotFoundException {
        if (filename.contains(" ")) {
            throw new IllegalArgumentException("No whitespace in filename! ");
        }
        Scanner scanner = new Scanner(new File("src/main/resources/diaryFiles/" + filename + ".txt"));
        String goal = scanner.nextLine();

        Diary d = new Diary(filename);
        d.setGoal(Integer.parseInt(goal));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineInfo = line.split(";");

            LocalDate date = LocalDate.parse(lineInfo[0]);
            String sessionName = lineInfo[1];
            String categoryName = lineInfo[2];
            int duration = Integer.parseInt(lineInfo[3]);
            d.addSession(sessionName, categoryName, date, duration);
        }
        return d;
    }
}
