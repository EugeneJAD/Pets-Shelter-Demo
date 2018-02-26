package eugene.petsshelter.data.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2/25/2018.
 */

@IgnoreExtraProperties
public class NewsItem {

    public String key;
    public String author;
    public String authorPost;
    public String title;
    public String body;
    public String date;
    public String imageUrl;
    public String imageCredits;
    public int starCount = 0;
    public String webUrl;
    public boolean isFavorite;
    public Map<String, Boolean> stars = new HashMap<>();

    public NewsItem() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    //for new items creating
    public NewsItem(String author, String authorPost, String title, String body, String date, String imageUrl, String imageCredits, int starCount, String webUrl) {
        this.author = author;
        this.authorPost = authorPost;
        this.title = title;
        this.body = body;
        this.date = date;
        this.imageUrl = imageUrl;
        this.imageCredits = imageCredits;
        this.starCount = starCount;
        this.webUrl = webUrl;
    }
}
