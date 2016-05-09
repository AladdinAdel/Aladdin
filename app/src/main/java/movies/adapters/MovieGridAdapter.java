package movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesapp.R;
import com.squareup.picasso.Picasso;

import movies.entities.Movie;

/**
 * Created by a7medM on 12/15/15.
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {

    public MovieGridAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Movie movie = getItem(position);
        String image_url = "http://image.tmdb.org/t/p/w185" + movie.getImage(); // setup image url

        viewHolder = (ViewHolder) view.getTag();

        Picasso.with(getContext())
                .load(image_url)
                .into(viewHolder.movieImgV); // load image from url into ImageView
        viewHolder.movieTitleTv.setText(movie.getTitle()); // set movie title to TextView
        return view;
    }

    public static class ViewHolder {
        public final ImageView movieImgV;
        public final TextView movieTitleTv;

        public ViewHolder(View view) {
            movieImgV = (ImageView) view.findViewById(R.id.movieImgV);
            movieTitleTv = (TextView) view.findViewById(R.id.movieTitleTv);
        }
    }
}