package dao;

import models.Event;
import models.Speaker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

import org.sql2o.Connection;

import static org.junit.Assert.*;


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

    @Test
    public void getAll_returnsAllInstancesOfEventsAddedToDao() throws Exception {
        Event event = setupEvent();
        Event secondEvent = setupEvent();
        Event notAdded = setupEvent();
        eventDao.add(event);
        eventDao.add(secondEvent);
        assertEquals(2, eventDao.getAll().size());
    }

    @Test
    public void update_makesChangesToDbVaues_true() throws Exception {
        Event event = setupEvent();
        Event secondEvent = setupEvent();
        eventDao.add(event);
        eventDao.add(secondEvent);
        eventDao.update(1, "NewName", "New description");
        assertEquals("name", eventDao.findById(2).getName());
        assertEquals("NewName", eventDao.findById(1).getName());
        assertEquals("New description", eventDao.findById(1).getDescription());
    }

    @Test
    public void deleteById_removesEntryById_true() throws Exception {
        Event event = setupEvent();
        Event secondEvent = setupEvent();
        eventDao.add(event);
        eventDao.add(secondEvent);
        eventDao.deleteById(1);
        assertEquals(1, eventDao.getAll().size());
        assertTrue(eventDao.getAll().contains(secondEvent));
    }

    @Test
    public void deleteAllEvents_removesAllEvents_true() throws Exception {
        Event event = setupEvent();
        Event secondEvent = setupEvent();
        eventDao.add(event);
        eventDao.add(secondEvent);
        assertEquals(2, eventDao.getAll().size());
        eventDao.deleteAllEvents();
        assertEquals(0, eventDao.getAll().size());
    }
}