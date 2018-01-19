package dao;


import models.Speaker;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import org.sql2o.Connection;

import java.util.List;

public class Sql2oSpeakerDao implements SpeakerDao {

    private final Sql2o sql2o;
    public Sql2oSpeakerDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Speaker speaker) {
        String sql = "INSERT INTO speakers (firstName, lastName, eventId, background) VALUES (:firstName, :lastName, :eventId, :background)";
        try (org.sql2o.Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(speaker)
                    .executeUpdate()
                    .getKey();
            speaker.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public Speaker findById(int id) {
        String sql = "SELECT * FROM speakers WHERE id = :id";
        try(Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Speaker.class);
        }
    }
    @Override
    public List<Speaker> getAll() {
        String sql = "SELECT * FROM speakers";
        try(Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Speaker.class);
        }
    }

    @Override
    public void update(int id, String firstName, String lastName, String background) {
        String sql = "UPDATE speakers SET firstName = :firstName, lastName = :lastName, background = :background WHER id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("firstName", firstName)
                    .addParameter("lastName", lastName)
                    .addParameter("background", background)
                    .executeAndFetch(Speaker.class);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public void deleteById(int id) {

    }



}
