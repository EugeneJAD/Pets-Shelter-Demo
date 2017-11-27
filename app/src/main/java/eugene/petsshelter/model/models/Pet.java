package eugene.petsshelter.model.models;


public interface Pet {

    String getId();
    void setId(String _id);

    String getName();
    void setName(String name);

    String getAge();
    void setAge(String age);

    String getGender();
    void setGender(String gender);

    String getShelter();
    void setShelter(String shelter);

    boolean isAdopted();
    void setAdopted(boolean adopted);

    String getImageURL();
    void setImageURL(String imageURL);

    String getDescription();
    void setDescription(String description);

    String getBreed();
    void setBreed(String breed);

}
