package diary;

import java.time.LocalDate;

public class SessionWithReps implements SessionIF {

    private String name;
    private LocalDate date;
    private Category category;
    private int reps;

    public SessionWithReps(String name, LocalDate date, Category category, int reps) {
        this.name = name;
        this.date = date;
        this.category = category;
        this.reps = reps;
    }

    public String getName() {
        return name;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

}
