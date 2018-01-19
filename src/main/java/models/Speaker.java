package models;

public class Speaker {

    private int id;
    private String firstName;
    private String lastName;
    private int eventId;
    private String background;

    public Speaker(String firstName, String lastName, int eventId, String background) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eventId = eventId;
        this.background = background;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Speaker speaker = (Speaker) o;

        if (id != speaker.id) return false;
        if (eventId != speaker.eventId) return false;
        if (!firstName.equals(speaker.firstName)) return false;
        if (!lastName.equals(speaker.lastName)) return false;
        return background.equals(speaker.background);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + eventId;
        result = 31 * result + background.hashCode();
        return result;
    }
}
