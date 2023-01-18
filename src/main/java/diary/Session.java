package diary;

import java.time.LocalDate;

public class Session implements SessionIF {
    private String name;
    private LocalDate date;
    private Category category;
    private int duration; // in minutes

    public Session(String name, Category category, LocalDate date, int duration) {
        this.name = name;
        this.date = date;
        this.category = category;
        this.duration = duration;
        category.addSession(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return getDate() + "; " + getName() + "; " + getCategory().getName() + "; " + getDuration() + " min";
    }

}
