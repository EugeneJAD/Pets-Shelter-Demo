package eugene.petsshelter.model.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Cat implements Pet {

    private String _id;
    private String name;
    private String age;
    private String description;
    private String gender;
    private String breed;
    private String shelter;
    private boolean adopted;
    private String imageURL;
    private int foodCount;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {this.age = age;}

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {this.gender = gender;}

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }

    public boolean isAdopted() {
        return adopted;
    }

    public void setAdopted(boolean adopted) {
        this.adopted = adopted;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getFoodCount() {return foodCount;}

    public void setFoodCount(int foodCount) {this.foodCount = foodCount;}
}
