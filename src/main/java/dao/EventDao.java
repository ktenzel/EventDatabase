package dao;

import models.Event;
import models.Speaker;

import java.util.List;

public interface EventDao {

    //create
    void add(Event event);


    //read
    List<Event> getAll(); //return all speakers
    List<Speaker> getAllSpeakersByEvent(int eventId);  //return all speakers from an event
    Event findById(int id); //return specific event

    //update
    public void update(int id, String name, String description); //pushes updates to events


    //delete
    public void deleteById(int id); //deletes a single event
    public void deleteAllEvents(); //deletes all events

}
