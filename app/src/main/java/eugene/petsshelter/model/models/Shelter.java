package eugene.petsshelter.model.models;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Shelter {

    private String _id;
    private String title;
    private String phone;
    private String email;
    private String hours;
    private String imageURL;
    private String web;
    private GeoLocation geoLocation;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public GeoLocation getGeoLocation() {return geoLocation;}

    public void setGeoLocation(GeoLocation geoLocation) {this.geoLocation = geoLocation;}
}

