package dao;

import models.Event;
import models.Speaker;

import java.util.List;

/**
 * Created by Guest on 1/19/18.
 */
public interface EventDao {

    //create
    void add(Event event);


    //read
    public Event findById(int id); //return specific event
//    public List<Event> getAll(); //return all speakers
//    public List<Speaker> getAllSpeakersByEvent(int eventId);  //return all speakers from an event

    //update

    //delete
}
