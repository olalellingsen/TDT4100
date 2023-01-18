package diary;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<Session> sessions;

    public Category(String name) {
        this.name = name;
        sessions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Session> getSessions() {
        return new ArrayList<Session>(sessions);
    }

    public int getDuration() {
        return sessions.stream().mapToInt(sesh -> sesh.getDuration()).sum();
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void deleteSession(Session session) {
        sessions.remove(session);
    }

    @Override
    public String toString() {
        return name;
    }
}
