package diary;

import java.time.LocalDate;

public interface SessionIF {

    String getName();

    LocalDate getDate();

    Category getCategory();

}
