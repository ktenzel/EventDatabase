package dao;

import models.Event;
import models.Speaker;

import java.util.List;

public interface EventDao {

    //create
    void add(Event event);


    //read
    public Event findById(int id); //return specific event
    public List<Event> getAll(); //return all speakers
    public List<Speaker> getAllSpeakersByEvent(int eventId);  //return all speakers from an event

    //update
    public void update(int id, String name, String description); //pushes updates to events
    //delete
    public void deleteById(int id); //deletes a single event
    public void deleteAllEvents(); //deletes all events

}
