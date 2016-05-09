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

import movies.entities.Trailer;

/**
 * Created by a7medM on 12/25/2015.
 */

public class TrailersTask extends AsyncTask<String, Void, List<Trailer>> {

    private final String TAG = "TrailersTask";
    TrailersCallback trailersCallback;

    public TrailersTask(TrailersCallback trailersCallback) {
        this.trailersCallback = trailersCallback;
    }

    public interface TrailersCallback {
        public void preExecuteTrailers();

        public void postExecuteTrailers(List<Trailer> trailerList);

    }

    private List<Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
        JSONObject trailerJson = new JSONObject(jsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray("results");
        List<Trailer> results = new ArrayList<>();
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject trailerObject = trailerArray.getJSONObject(i);
            results.add(new Trailer(trailerObject));
        }
        return results;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (trailersCallback != null)
            trailersCallback.preExecuteTrailers();
    }

    @Override
    protected List<Trailer> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStr = null;

        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THEMOVIEDB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.d(TAG, url.toString());
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
            Log.e(TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getTrailersDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(TAG, "Error in Json Parsing " + e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        // call back to return trailers data to MovieFragment
        if (this.trailersCallback != null) {
            trailersCallback.postExecuteTrailers(trailers);
        }
    }
}