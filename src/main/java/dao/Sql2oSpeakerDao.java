package dao;


import models.Speaker;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.Connection;

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

}
