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
    public void update(int id, String firstName, String lastName, String background);

    //delete
//    public void deleteById(int id);
//    public void clearAllSpeakers();
}
