package diary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
- This class is an alternative file handler class that read and write binary files (.dat) 
instead for text files (.txt).
- It implements the FileHandler interface and can be used instead of the 
FileHandlerText-class (if needed) in a further development of the Diary application.
*/

public class FileHandlerBinary implements FileHandler {

    // write file
    @Override
    public void writeDiaryToFile(String filename, Diary diary) {
        File file = new File("src/main/java/diary/diaryFiles/" + filename + ".dat");
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(diary);
            objectOut.flush();
            objectOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read file
    @Override
    public Diary readFromDiaryFile(String filename) throws IOException, ClassNotFoundException {
        try {
            FileInputStream fileIn = new FileInputStream("src/main/java/diary/diaryFiles/" + filename + ".dat");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Diary readDiary = (Diary) objectIn.readObject();
            objectIn.close();
            return readDiary;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
