package eugene.petsshelter.data.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

@IgnoreExtraProperties
public class Cat implements Pet{

	private String _id;

	@SerializedName("foodCount")
	private int foodCount;

	@SerializedName("shelter")
	private String shelter;

	@SerializedName("gender")
	private String gender;

	@SerializedName("imageURL")
	private String imageURL;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("adopted")
	private boolean adopted;

	@SerializedName("age")
	private String age;

	@SerializedName("breed")
	private String breed;

	public void setFoodCount(int foodCount){
		this.foodCount = foodCount;
	}

	public int getFoodCount(){
		return foodCount;
	}

	public void setShelter(String shelter){
		this.shelter = shelter;
	}

	public String getShelter(){
		return shelter;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}

	public String getImageURL(){
		return imageURL;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setAdopted(boolean adopted){
		this.adopted = adopted;
	}

	public boolean isAdopted(){
		return adopted;
	}

	public void setAge(String age){
		this.age = age;
	}

	public String getAge(){
		return age;
	}

	public void setBreed(String breed){
		this.breed = breed;
	}

	public String getBreed(){
		return breed;
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}
}