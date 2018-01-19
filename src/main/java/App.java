import com.sun.java.swing.plaf.motif.MotifBorders;
import dao.Sql2oEventDao;
import dao.Sql2oSpeakerDao;
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
            return new ModelAndView(model, "delete.hbs"));
        }, new HandlebarsTemplateEngine();

        //get: delete all speakers
        get("/speakers/delete", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            speakerDao.clearAllSpeakers();
            return new ModelAndView(model, "delete.hbs"));
        }, new HandlebarsTemplateEngine());

        //create

        get("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "event-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/events/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            String description = request.queryParams("description");
            Event newEvent = new Event(name, description);
            eventDao.add(newEvent);
            return new ModelAndView(model, "success-event.hbs");
        }, new HandlebarsTemplateEngine());








    }
}
