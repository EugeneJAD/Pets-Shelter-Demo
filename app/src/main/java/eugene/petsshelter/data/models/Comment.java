package eugene.petsshelter.data.models;

/**
 * Created by Eugene on 3/5/2018.
 */

public class Comment {

    public String uid;
    public String userImageURL;
    public String author;
    public String date;
    public String text;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String userImageURL, String author, String date, String text) {
        this.uid = uid;
        this.userImageURL = userImageURL;
        this.author = author;
        this.date = date;
        this.text = text;
    }
}
