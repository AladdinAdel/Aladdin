package movies.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by a7medM on 12/25/2015.
 */
public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(JSONObject reviewObject) throws JSONException {
        this.id = reviewObject.getString("id");
        this.author = reviewObject.getString("author");
        this.content = reviewObject.getString("content");
        this.url = reviewObject.getString("url");
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}
