package eugene.petsshelter.data.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

@IgnoreExtraProperties
public class Shelter{

    private String _id;

	@SerializedName("country")
	private String country;

	@SerializedName("hours")
	private String hours;

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("phone")
	private String phone;

	@SerializedName("imageURL")
	private String imageURL;

	@SerializedName("title")
	private String title;

	@SerializedName("email")
	private String email;

	@SerializedName("geoLocation")
	private GeoLocation geoLocation;

	@SerializedName("web")
	private String web;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setHours(String hours){
		this.hours = hours;
	}

	public String getHours(){
		return hours;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}

	public String getImageURL(){
		return imageURL;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public GeoLocation getGeoLocation() {return geoLocation;}

	public void setGeoLocation(GeoLocation geoLocation) {this.geoLocation = geoLocation;}

	public String getWeb() {return web;}

	public void setWeb(String web) {this.web = web;}
}