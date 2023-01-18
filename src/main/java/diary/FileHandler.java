package diary;

import java.io.IOException;

public interface FileHandler {
    Diary readFromDiaryFile(String filename) throws IOException, ClassNotFoundException;

    void writeDiaryToFile(String filename, Diary diary);

}
