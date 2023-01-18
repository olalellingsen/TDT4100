package diary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Diary {
    private String name;
    private int goal; // in minutes
    private ArrayList<Session> sessions = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();

    public Diary(String name) {
        this.name = name;
    }

    public void addSession(String sessionName, String categoryName, LocalDate date, int duration) {
        validateSessionInput(sessionName, date, duration + "");

        if (categoryName == null || categoryName.isEmpty()) {
            categoryName = "No category";
        }

        Category category = null;
        for (Category c : getCategories()) {
            if (c.getName().equals(categoryName)) {
                category = c;
            }
        }
        if (category == null) {
            category = new Category(categoryName);
        }

        Session s = new Session(sessionName, category, date, duration);
        this.addSession(s);
    }

    // validation of user input
    public void validateSessionInput(String sessionName, LocalDate date, String duration) {
        if (sessionName == "" || sessionName.isEmpty()) {
            throw new IllegalArgumentException("Please set a session name");
        }
        try {
            Integer.parseInt(duration);
        } catch (Exception e) {
            throw new IllegalArgumentException("Please set a duration number");
        }
        if (Integer.parseInt(duration) < 0) {
            throw new IllegalArgumentException("Duration must be a positive number");
        }
        if (date == null) {
            throw new IllegalArgumentException("Please pick a date");
        }
    }

    public void addSession(Session session) {
        sessions.add(session);
        addCategory(session.getCategory());
    }

    public void deleteSession(int index) {
        if ((index + 1 > sessions.size()) || index < 0) {
            throw new IndexOutOfBoundsException("Wrong list index");
        }
        Session s = sessions.get(index);
        sessions.remove(s);
        s.getCategory().deleteSession(s);

        // delete category
        if (s.getCategory().getSessions().size() == 0) {
            categories.remove(s.getCategory());
        }
    }

    public void addCategory(Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public ArrayList<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public ArrayList<Session> getSessions() {
        return new ArrayList<>(sessions);
    }

    public String getName() {
        return name;
    }

    // goal
    // in hours and minutes
    public void setGoal(String hour, String min) {
        validateGoal(hour, min);
        if (Integer.parseInt(hour) < 0 || Integer.parseInt(min) < 0) {
            throw new IllegalArgumentException("Goal must be a positive number");
        }
        this.goal = getHourToMin(Integer.parseInt(hour), Integer.parseInt(min));
    }

    public void validateGoal(String h, String m) {
        try {
            Integer.parseInt(h);
            Integer.parseInt(m);
        } catch (Exception e) {
            throw new IllegalArgumentException("Please set hour and min as numbers");
        }
    }

    // in minutes
    public void setGoal(int min) {
        if (min < 0) {
            throw new IllegalArgumentException("Goal must be a positive number");
        }
        this.goal = min;
    }

    public int getGoal() {
        int g = goal;
        return g;
    }

    // time format methods
    public String getMinToHour(int min) {
        if (min > 60) {
            return min / 60 + "h, " + min % 60 + "min";
        }
        return min + "min";
    }

    public int getHourToMin(int hour, int min) {
        if (hour > 0) {
            return 60 * hour + min;
        }
        return min;
    }

    // duration
    public int getTotalDuration() {
        return sessions.stream().mapToInt(sesh -> sesh.getDuration()).sum();
    }

    public int getCompletionStatus() {
        if (getGoal() <= 0) {
            return 0;
        }
        return Math.round(((float) getTotalDuration() / (float) getGoal()) * 100);
    }

    // for JavaFX progressBar to work
    public Double getCompletionDbl() {
        Float f;
        f = ((float) ((float) getCompletionStatus() / 100));
        return f.doubleValue();
    }

    // category distribution
    public HashMap<Category, Float> getDistributionStatus() {
        HashMap<Category, Float> distribution = new HashMap<>();

        Float percentage;
        for (Category category : categories) {
            percentage = ((float) category.getDuration() / getTotalDuration());
            distribution.put(category, percentage);
        }
        return distribution;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                "\nGoal: " + getGoal() +
                "\nTotal duration: " + getTotalDuration() +
                "\nCompleted: " + getCompletionStatus() + "%" +
                "\nCategories: " + getCategories() +
                "\nSessions: " + getSessions() +
                "\nDistribution: " + getDistributionStatus();
    }

    // general sorting method with comparator
    public List<Session> sortSessions(Comparator<Session> comparator) {
        return sessions.stream().sorted(comparator).toList();
    }

    // sort by duration
    public void setSessionsByDuration() {
        this.sessions = new ArrayList<>(
                sortSessions((s1, s2) -> ((Integer) s2.getDuration()).compareTo((Integer) s1.getDuration())));
    }

    // sort by date
    public void setSessionsByDate() {
        this.sessions = new ArrayList<>(
                sortSessions((s1, s2) -> s2.getDate().compareTo(s1.getDate())));
    }

    // sort by category name
    public void setSessionsByCategoryName() {
        this.sessions = new ArrayList<>(
                sortSessions((s1, s2) -> s1.getCategory().getName().compareTo(s2.getCategory().getName())));
    }

    // sort by name
    public void setSessionsByName() {
        this.sessions = new ArrayList<>(
                sortSessions((s1, s2) -> s1.getName().compareTo(s2.getName())));
    }

    /*
     * NB!
     * following methods is not used, but can be added in further development of the
     * application
     */

    // sorting method using Predicate
    public List<Session> sortSessions(Predicate<Session> predicate) {
        return sessions.stream().filter(predicate).collect(Collectors.toList());
    }

    // filter by category
    public void setSessionsByCategory(String categoryName) {
        this.sessions = new ArrayList<>(
                sortSessions(((session) -> session.getCategory().getName().equals(categoryName))));
    }

    public static void main(String[] args) {
        Diary diary = new Diary("Piano diary");
        diary.addSession("D major", "scales", LocalDate.of(2022, 02, 11), 10);
        diary.addSession("Etude 1", "Etudes", LocalDate.of(2022, 02, 10), 10);
        diary.addSession("G harmonic minor", "scales", LocalDate.of(2022, 02, 14),
                5);
        diary.addSession("Let it be", "Beatles tunes", LocalDate.of(2022, 02, 12),
                15);

        diary.setGoal("3", "30");
        System.out.println(diary);
    }
}
