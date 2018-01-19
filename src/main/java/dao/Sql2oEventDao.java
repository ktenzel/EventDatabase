package dao;

import models.Event;
import models.Speaker;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oEventDao implements EventDao {
    private final Sql2o sql2o;

    public Sql2oEventDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Event event) {
        String sql = "INSERT INTO events (name, description) VALUES (:name, :description)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(event)
                    .executeUpdate()
                    .getKey();
            event.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Event findById(int id) {
        String sql = "SELECT * FROM events WHERE id = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Event.class);
        }
    }

    @Override
    public List<Event> getAll() {
        String sql = "SELECT * FROM events";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Event.class);
        }
    }

    @Override
    public List<Speaker> getAllSpeakersByEvent(int eventId) {
        String sql = "SELECT * FROM speakers WHERE eventId = :eventId";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("eventId", eventId)
                    .executeAndFetch(Speaker.class);
        }
    }

    @Override
    public void update(int id, String name, String description) {
        String sql = "UPDATE events SET name = :name, description = :descroipting WHERE id = :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("description", description)
                    .executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM events WHERE id= :id";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAllEvents() {
        String sql = "DELETE FROM events";
        try (Connection con = sql2o.open()){
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
