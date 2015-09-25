package student.rmit.edu.au.s3110401mad_assignment.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.MovieDetailActivity;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.MovieArrayAdapter;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;

public class MovieListFragment extends Fragment {
    public static final String DRAWABLE = "drawable";
    public static final int IMDB_ID = 0;
    public static final int IMDB_TITLE = 1;
    public static final int IMDB_YEAR = 2;
    public static final int IMDB_SHORT_PLOT = 3;
    public static final int IMDB_FULL_PLOT = 4;

    private static MovieModel theModel = MovieModel.getSingleton();
    private ListView movieListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_movie_list, container, false);

        movieListView = (ListView) layout.findViewById(R.id.movie_list_fragment);
        this.retrieveMovies(); // load from movies_sample.xml

        MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(getActivity(), theModel.getAllMovies());
        movieListView.setAdapter(movieArrayAdapter);

        AdapterView.OnItemClickListener listener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movieSelected = (Movie) movieListView.getItemAtPosition(position);
                goToMovieDetailActivity(movieSelected);
            }
        };
        movieListView.setOnItemClickListener(listener);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        movieListView.setAdapter(new MovieArrayAdapter(
                getActivity(),
                theModel.getAllMovies()));
    }

    private void goToMovieDetailActivity(Movie movieSelected) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieSelected.getId());
        startActivityForResult(intent, 1);
    }

    /**
     * Get hard coded values from /res/values/movies_sample.xml
     */
    private void retrieveMovies() {
        if(theModel.getAllMovies().size() > 0)
            return;
        TypedArray movieArray  = getResources().obtainTypedArray(R.array.movie_array);
        int movieArrayLength = movieArray.length();
        for (int i = 0; i < movieArrayLength; ++i) {
            int id = movieArray.getResourceId(i, 0);
            if (id > 0) {
                String[] newArray = getResources().getStringArray(id);
                int imageResourceId = getResources().getIdentifier(
                        newArray[IMDB_ID],
                        DRAWABLE,
                        getActivity().getPackageName());

                theModel.addMovie(new MovieStruct(
                        newArray[IMDB_ID],
                        newArray[IMDB_TITLE],
                        newArray[IMDB_YEAR],
                        newArray[IMDB_SHORT_PLOT],
                        newArray[IMDB_FULL_PLOT],
                        imageResourceId));
            }
        }
        movieArray.recycle(); // Important!
    }
}
