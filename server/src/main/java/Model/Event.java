package Model;

public class Event// implements Comparable<Event>
{
    private String descendant;
	private String eventID; //unique ID
    private String personID; //personID of associated person
    private Double latitude;
    private Double longitude;
    private String country;
    private String city;
    private String description;
    private int year;


    public Event() {
    }

    public Event(String eventID, String personID,
                 Double latitude, Double longitude,
                 String country, String city,
                 String description, int year,
                 String descendant) {
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.description = description;
        this.year = year;
        this.descendant = descendant;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public String getDescendant() {
        return descendant;
    }
}
