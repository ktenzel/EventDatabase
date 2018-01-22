import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.Sql2oSpeakerDao;
import dao.Sql2oEventDao;

import models.Event;
import models.Speaker;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
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
            model.put("editEvent", true);
            List<Event> allEvents = eventDao.getAll();
            model.put("events", allEvents);

            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/events/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int idOfEventToEdit = Integer.parseInt(request.params("editEventId"));
            String newEventName = request.queryParams("newEventName");
            String newDescription = request.queryParams("newDescription");
            eventDao.update(eventDao.findById(idOfEventToEdit).getId(), newEventName, newDescription);

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


        //get: a single event and list of speakers
        get("/events/:eventId/speakers/:speakerId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int idOfSpeakerToFind = Integer.parseInt(request.params("speakerId"));
            Speaker foundSpeaker = speakerDao.findById(idOfSpeakerToFind);
            model.put("speakers", foundSpeaker);

            return new ModelAndView(model, "speaker-detail.hbs");
        }, new HandlebarsTemplateEngine());


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

            Speaker editSpeaker = speakerDao.findById(newSpeakerToEdit);
            speakerDao.update(newEventToEdit, newFirstName, newLastName, newBackground);

            return new ModelAndView(model, "update.hbs");
        }, new HandlebarsTemplateEngine());

        get("/events/:eventId/speakers/:speakerId/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSpeakerToDelete = Integer.parseInt(request.params("speakerId"));
            Speaker deleteSpeaker = speakerDao.findById(idOfSpeakerToDelete);
            speakerDao.deleteById(idOfSpeakerToDelete);
            return new ModelAndView(model, "delete.hbs");
        }, new HandlebarsTemplateEngine());





        //read
        //get: show all events
//        get("/events", (request, response) -> {
//            Map<String, Object> model = new HashMap<>();
//            int idOfEventToFind = Integer.parseInt(request.params("eventId"));
//
//
//            List<Event> events = eventDao.getAll();
//            model.put("events", events);
//
//
//            Event foundEvent = eventDao.findById(idOfEventToFind);
//            model.put("events", foundEvent);
//            List<Speaker> allSpeakersByEvent = eventDao.getAllSpeakersByEvent(idOfEventToFind);
//            model.put("speakers", allSpeakersByEvent);
//
//            return new ModelAndView(model, "events-details.hbs");
//        }, new HandlebarsTemplateEngine());





        //update










    }
}
