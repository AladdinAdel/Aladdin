package movies.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.moviesapp.R;

import movies.fragments.DetailFragment;

/**
 * Created by a7medM on 12/15/15.
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // add fragment on this Activity and send movie data to it.
        Bundle arguments = new Bundle();

        arguments.putParcelable(DetailFragment.MOVIE_DATA,
                getIntent().getParcelableExtra(DetailFragment.MOVIE_DATA));

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.movie_detail_container, detailFragment);
        fragmentTransaction.commit();
    }
}