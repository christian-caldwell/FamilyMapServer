package Model;

public class Person 
{
	private String descendant;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;


    public Person() {
    }

    public Person(String descendant, String personID,
                  String firstName, String lastName,
                  String gender, String father,
                  String mother, String spouse) {
        this.descendant = descendant;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }
}
