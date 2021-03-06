package dao;

import models.Speaker;

import java.util.List;

public interface SpeakerDao {

    //create
    void add(Speaker speaker);

    //read
    public Speaker findById(int id); //returns a specific Speaker
    public List<Speaker> getAll(); //return all Speakers

    //update
    public void update(int id, String newFirstName, String newLastName, String newBackground, int newEventId);

    //delete
    public void deleteById(int id); // deletes speakers by id
    public void clearAllSpeakers(); //deletes all speakers
}
