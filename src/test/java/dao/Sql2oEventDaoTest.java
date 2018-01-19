package dao;

import models.Event;
import models.Speaker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

import org.sql2o.Connection;

import static org.junit.Assert.*;

/**
 * Created by Guest on 1/19/18.
 */
public class Sql2oEventDaoTest {

    private Sql2oEventDao eventDao;
    private Sql2oSpeakerDao speakerDao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        eventDao = new Sql2oEventDao(sql2o);
//        speakerDao = new Sql2oSpeakerDao(sql2o);


        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    public Event setupEvent() {
        return new Event("name", "description");
    }

    public Speaker setupSpeaker() {
        return new Speaker("firstName", "lastName", 1, "background");
    }

    @Test
    public void add_writesToDBAndAssignsId_true() throws Exception {
        Event event = setupEvent();
        int originalId = event.getId();
        eventDao.add(event);
        assertNotEquals(originalId, event.getId());
    }

    @Test
    public void findById_returnsCorrectInstance_true() throws Exception {
        Event event = setupEvent();
        event.setName("NewName");
        Event secondEvent = setupEvent();
        eventDao.add(event);
        eventDao.add(secondEvent);
        assertEquals("NewName", eventDao.findById(1).getName());
    }
}