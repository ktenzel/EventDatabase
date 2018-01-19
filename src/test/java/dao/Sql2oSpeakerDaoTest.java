package dao;

import models.Speaker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;


public class Sql2oSpeakerDaoTest {

    private Sql2oSpeakerDao speakerDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        speakerDao = new Sql2oSpeakerDao(sql2o); //ignore me for now

        //keep connection open through entire test so it does not get erased.
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Speaker setupSpeaker() {
        return new Speaker("firstName", "lastName", 1,"background");
    }

    @Test
    public void add_placesObjectInDBAssignedId_true() throws Exception {
        Speaker speaker = setupSpeaker();
        int originalId = speaker.getId();
        speakerDao.add(speaker);
        assertNotEquals(originalId, speaker.getId());
    }

}