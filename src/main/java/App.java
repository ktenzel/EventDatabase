import com.sun.java.swing.plaf.motif.MotifBorders;
import com.sun.org.apache.xpath.internal.operations.Mod;
import dao.Sql2oEventDao;
import dao.Sql2oSpeakerDao;
import org.omg.CORBA.INTERNAL;
import org.omg.PortableInterceptor.INACTIVE;
import org.sql2o.Sql2o;
import models.Speaker;
import models.Event;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ObjDoubleConsumer;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/televisionreviews.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oSpeakerDao speakerDao = new Sql2oSpeakerDao(sql2o);
        Sql2oEventDao eventDao = new Sql2oEventDao(sql2o);

        //get: home
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> events = eventDao.getAll();
            model.put("events", events);
            List<Speaker> speakers = speakerDao.getAll();
            model.put("speakers", speakers);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: input for new event
        get("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> events = eventDao.getAll();
            model.put("events", events);

            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: input to event
        post("/events", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            String description = request.queryParams("description");
            Event newEvent = new Event(name, description);
            eventDao.add(newEvent);

            List<Event> events = eventDao.getAll();
            model.put("events", events);
            return new ModelAndView(model, "success-event.hbs");
        }, new HandlebarsTemplateEngine());


        //get: delete all events
        get("/events/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            eventDao.deleteAllEvents();
            speakerDao.clearAllSpeakers();

            List<Event> events = eventDao.getAll();
            model.put("events", events);

            return new ModelAndView(model, "delete.hbs");
        }, new HandlebarsTemplateEngine());

        //get: show a form to update an event
        get("/events/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("events", true);
//            int eventId = Integer.parseInt(request.params("eventId"));
//            Event eventToEdit = eventDao.findById(eventId);
            List<Event> eventToEdit = eventDao.getAll();
            model.put("events", eventToEdit);

            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/events/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int eventIdToEdit = Integer.parseInt(request.params("editEventId"));
            String newName = request.queryParams("newName");
            String newDescription = request.queryParams("newDescription");
            eventDao.update(eventDao.findById(eventIdToEdit).getId(), newName, newDescription);

            List<Event> events = eventDao.getAll();
            model.put("events", events);
            return new ModelAndView(model, "update.hbs");
        }, new HandlebarsTemplateEngine());


        //get: get a single event and speakers
        get("/events/:eventId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int idOfEventToFind = Integer.parseInt(request.params("eventId"));

            List<Event> events = eventDao.getAll();
            model.put("events", events);

            Event foundEvent = eventDao.findById(idOfEventToFind);
            model.put("events", foundEvent);
            List<Speaker> allSpeakersByEvent = eventDao.getAllSpeakersByEvent(idOfEventToFind);
            model.put("speakers", allSpeakersByEvent);

            return new ModelAndView(model, "event-deatails.hbs");
        }, new HandlebarsTemplateEngine());



        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<Speaker> speakers = speakerDao.getAll();
            model.put("speakers", speakers);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());


        //get: delete all speakers
        get("/speakers/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> allEvents = eventDao.getAll();
            model.put("events", allEvents);

            speakerDao.clearAllSpeakers();
            return new ModelAndView(model, "delete.hbs");
        }, new HandlebarsTemplateEngine());



        //get: input for new speaker
        get("/speakers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> allEvents = eventDao.getAll();
            model.put("events", allEvents);

            return new ModelAndView(model, "speaker-form.hbs");
        }, new HandlebarsTemplateEngine());





        //post: input for new speaker
        post("/speakers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> allEvents = eventDao.getAll();
            model.put("events", allEvents);

            String firstName = request.queryParams("firstName");
            String lastName = request.queryParams("lastName");
            String background = request.queryParams("background");
            int eventId = Integer.parseInt(request.params("eventId"));

            Speaker newSpeaker = new Speaker(firstName, lastName, eventId, background);
            speakerDao.add(newSpeaker);
            model.put("speaker", newSpeaker);
            return new ModelAndView(model, "success-speaker.hbs");
        }, new HandlebarsTemplateEngine());















        //read
        //get: show all events
        get("/events", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfEventToFind = Integer.parseInt(request.params("eventId"));


            List<Event> events = eventDao.getAll();
            model.put("events", events);


            Event foundEvent = eventDao.findById(idOfEventToFind);
            model.put("events", foundEvent);
            List<Speaker> allSpeakersByEvent = eventDao.getAllSpeakersByEvent(idOfEventToFind);
            model.put("speakers", allSpeakersByEvent);

            return new ModelAndView(model, "events-details.hbs");
        }, new HandlebarsTemplateEngine());




        //get: a single event and list of speakers
        get("/events/:eventId/speakers/:speakerId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int idOfSpeakerToFind = Integer.parseInt(request.params("speakerId"));
            Speaker foundSpeaker = speakerDao.findById(idOfSpeakerToFind);
            model.put("speakers", foundSpeaker);

            return new ModelAndView(model, "speaker-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //update





        get("/speakers/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();


            List<Event> allEvents = eventDao.getAll();
            model.put("events", allEvents);

            List<Speaker> allSpeakers = speakerDao.getAll();
            model.put("speakers", allSpeakers);

            model.put("editSpeaker", true);
            return new ModelAndView(model, "speaker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/speakers/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> allEvents = eventDao.getAll();
            model.put("events", allEvents);


            String newFirstName = request.queryParams("newFirstName");
            String newLastName = request.queryParams("newLastName");
            String newBackground = request.queryParams("newBackground");
            int newEventToEdit = Integer.parseInt(request.params("eventId"));
            int newSpeakerToEdit = Integer.parseInt(request.params("speakerId"));

            Speaker editSpeaker = speakerDao.findById(newSpeakerToEdit)
            speakerDao.update(newEventToEdit, newFirstName, newLastName, newBackground);

            return new ModelAndView(model, "update.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
