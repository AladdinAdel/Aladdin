package movies.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.moviesapp.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.entities.Movie;

/**
 * Created by a7medM on 12/15/2015.
 */
public class MoviesTask extends AsyncTask<String, Void, List<Movie>> {
    Callback callback;
    private final String TAG = "MoviesTask";

    public interface Callback {
        public void preExecute();

        public void postExecute(List<Movie> movieList);
    }

    public MoviesTask(Callback callback) {
        this.callback = callback;
    }

    private final String LOG_TAG = MoviesTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.callback != null) {
            callback.preExecute();
        }

    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THEMOVIEDB_API_KEY)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.d(TAG, builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getMoviesDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private List<Movie> getMoviesDataFromJson(String jsonStr) throws JSONException {
        if (jsonStr.isEmpty() || jsonStr == null)
            return null;
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");
        List<Movie> results = new ArrayList<>();
        if (movieArray != null && !jsonStr.isEmpty())
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movie movieModel = new Movie(movie);
                results.add(movieModel);
            }
        return results;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        // call back to return movies data to MovieFragment
        if (callback != null) {
            callback.postExecute(movies);
        }
    }
}