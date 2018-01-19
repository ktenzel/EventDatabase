package models;

/**
 * Created by Guest on 1/19/18.
 */
public class Speaker {

    private int id;
    private String firstName;
    private String lastName;
    private String background;

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
        if (!firstName.equals(speaker.firstName)) return false;
        if (!lastName.equals(speaker.lastName)) return false;
        return background.equals(speaker.background);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + background.hashCode();
        return result;
    }
}
