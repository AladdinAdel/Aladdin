package movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import movies.entities.Movie;


/**
 * Created by a7medM on 12/26/15.
 */

public class DataSource {

    Context context;
    String TAG = "DataSource";

    // Database fields
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {DBHelper.COLUMN_ID,
            DBHelper.MOVIE_ID, DBHelper.MOVIE_JSONOBJECT};

    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void deleteDB() {
        context.deleteDatabase(DBHelper.DATABASE_NAME);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addMovie(Movie movie) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(DBHelper.MOVIE_ID, movie.getId());
            String movieData = movie.getJsonObject();
            values.put(DBHelper.MOVIE_JSONOBJECT, movieData);
            id = database.insert(DBHelper.TABLE_Networks, null, values);
        } catch (Exception e) {
            Toast.makeText(context, "Something Error in Adding..", Toast.LENGTH_SHORT).show();

        }
        return id;
    }

    public int deleteMovie(int movie_id) {
        int id = -1;
        try {
            id = database.delete(DBHelper.TABLE_Networks, DBHelper.MOVIE_ID
                    + " = " + movie_id, null);
        } catch (Exception e) {
            Toast.makeText(context, "Something Error in deleting..", Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    public List<Movie> getMovies() {
        List<Movie> movieList = new ArrayList<>();
        try {
            Cursor cursor = database.query(DBHelper.TABLE_Networks,
                    allColumns, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String movieObject = cursor.getString(2);
                JSONObject jsonObject = new JSONObject(movieObject);
                Movie movie = new Movie(jsonObject);
                movieList.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Something wrong in get Favourites");
        }
        return movieList;
    }

    public boolean isFavorite(int id) {
        try {
            Cursor cursor = database.query(DBHelper.TABLE_Networks,
                    allColumns, DBHelper.MOVIE_ID + "='" + id + "'",
                    null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isNull(0)) {
                return true;
            }
            cursor.close();
        } catch (Exception e) {

        }
        return false;
    }
}