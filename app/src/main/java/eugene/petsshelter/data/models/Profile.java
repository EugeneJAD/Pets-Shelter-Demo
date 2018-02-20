package eugene.petsshelter.data.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eugene on 03.02.2018.
 */

@IgnoreExtraProperties
public class Profile {

    @SerializedName("uId")
    private String uId;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("favoritePets")
    private Map<String, Boolean> favoritePets = new HashMap<>();

    // Firebase. Default constructor for calls to DataSnapshot.getValue(Profile.class)
    public Profile(){}

    public Profile(String uId, String name, String phone, String email, String imageUrl) {
        this.uId = uId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getUId() {return uId;}

    public void setUId(String uId) {this.uId = uId;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Boolean> getFavoritePets() {return favoritePets;}

    public void setFavoritePets(Map<String, Boolean> favoritePets) {this.favoritePets = favoritePets;}
}
