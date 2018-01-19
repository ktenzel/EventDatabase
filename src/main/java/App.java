import com.sun.java.swing.plaf.motif.MotifBorders;
import com.sun.org.apache.xpath.internal.operations.Mod;
import dao.Sql2oEventDao;
import dao.Sql2oSpeakerDao;
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


        //get: delete all events
        get("/events/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            eventDao.deleteAllEvents();
            return new ModelAndView(model, "delete.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete all speakers
        get("/speakers/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            speakerDao.clearAllSpeakers();
            return new ModelAndView(model, "delete.hbs");
        }, new HandlebarsTemplateEngine());

        //create

        //get: input for new event
        get("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: input to event
        post("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            String description = request.queryParams("description");
            Event newEvent = new Event(name, description);
            eventDao.add(newEvent);
            return new ModelAndView(model, "success-event.hbs");
        }, new HandlebarsTemplateEngine());

        //get: input for new speaker
        get("/events/:eventId/speakers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "speaker-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: input for new speaker
        post("/events/:eventId/speakers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String firstName = request.queryParams("firstName");
            String lastName = request.queryParams("lastName");
            String background = request.queryParams("background");
            int eventId = Integer.parseInt(request.params("eventId"));

            Speaker newSpeaker = new Speaker(firstName, lastName, eventId, background);
            speakerDao.add(newSpeaker);
            return new ModelAndView(model, "success-speaker.hbs");
        }, new HandlebarsTemplateEngine());


        //read
        //get: show all events
        get("/events", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            List<Event> events = eventDao.getAll();
            model.put("events", events);

            return new ModelAndView(model, "events.hbs");
        }, new HandlebarsTemplateEngine());


        //get: get a single event and speakers
        get("/events/:eventId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int eventId = Integer.parseInt(request.params("eventId"));
            Event events = eventDao.findById(eventId);
            model.put("events", events);

            List<Speaker> speakers = eventDao.getAllSpeakersByEvent(eventId);
            model.put("speakers", speakers);

            return new ModelAndView(model, "event-deatails.hbs");

        }, new HandlebarsTemplateEngine());

        //get: a single event and list of speakers
        get("/events/:eventId/speakers/:speakerId", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int eventId = Integer.parseInt(request.params("eventId"));
            Event events = eventDao.findById(eventId);
            model.put("events", events);

            int speakerId = Integer.parseInt(request.params("speakerId"));
            Speaker speakers = speakerDao.findById(speakerId);
            model.put("speakers", speakers);

            return new ModelAndView(model, "speaker-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //update

        //get: show a form to update an event
        get("/events/:eventId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int eventId = Integer.parseInt(request.params("eventId"));
            Event eventToEdit = eventDao.findById(eventId);
            model.put("eventToEdit", eventToEdit);

            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/events/:eventId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int eventId = Integer.parseInt(request.params("eventId"));
            String newName = request.queryParams("newName");
            String newDescription = request.queryParams("newDescription");
            eventDao.update(eventId, newName, newDescription);

            return new ModelAndView(model, "update.hbs");
        }, new HandlebarsTemplateEngine());

        get("/events/:eventId/speakers/:speakerId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int eventId = Integer.parseInt(request.params("eventId"));
            int speakerId = Integer.parseInt(request.params("speakerId"));
            Event eventToEdit = eventDao.findById(eventId);
            model.put("eventToEdit", eventToEdit);
            Speaker speakerToEdit = speakerDao.findById(speakerId);
            model.put("speakerToEdit", speakerToEdit);

            return new ModelAndView(model, "speaker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/events/:eventId/speakers/:speakerId/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();

            int speakerId = Integer.parseInt(request.params("speakerId"));
            String newFirstName = request.queryParams("newFirstName");
            String newLastName = request.queryParams("newLastName");
            String newBackground = request.queryParams("newBackground");

            speakerDao.update(speakerId, newFirstName, newLastName, newBackground);
            return new ModelAndView(model, "update.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
