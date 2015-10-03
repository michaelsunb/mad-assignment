package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.MovieDetailActivity;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.MovieArrayAdapter;
import student.rmit.edu.au.s3110401mad_assignment.db.DatabaseHelper;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieStruct;
import student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility.MovieMemoryManagementClient;

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String DRAWABLE = "drawable";
    public static final int IMDB_ID = 0;
    public static final int IMDB_TITLE = 1;
    public static final int IMDB_YEAR = 2;
    public static final int IMDB_SHORT_PLOT = 3;
    public static final int IMDB_FULL_PLOT = 4;

    private static MovieModel theModel = MovieModel.getSingleton();
    private ListView movieListView;
    private ProgressBar progressBar;

    private CursorAdapter mAdapter;
    private String mCurFilter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_movie_list, container, false);

        movieListView = (ListView) layout.findViewById(R.id.movie_list_fragment);
        movieListView.setAdapter(mAdapter);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout)layout.findViewById(R.id.fragment_movie_list)).addView(progressBar, params);

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

    private void goToMovieDetailActivity(Movie movieSelected) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieSelected.getId());
        startActivityForResult(intent, 1);
    }

    private MatrixCursor listToMatrixCursor(List<Movie> allMovies) {
        MatrixCursor matrixCursor = new MatrixCursor(DatabaseHelper.MOVIE_SUMMARY_PROJECTION);

        long i = 0;
        for(Movie movie : allMovies) {
            if(i++ > 10) break;
            Matcher movieIdNo = Pattern.compile("\\d+$").matcher(movie.getId());
            matrixCursor.addRow(new String[]{
                    ((movieIdNo.find()) ? movieIdNo.group(0) : i + ""),
                    movie.getId(),
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getShortPlot(),
                    movie.getFullPlot(),
                    MovieModel.bitMapToString(movie.getPoster()),
                    movie.getRating() + ""
            });
        }
        return matrixCursor;
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if(context == null)
            context = getActivity();
        String sortOrder = DatabaseHelper.MOVIE_TITLE + " COLLATE LOCALIZED ASC";

        return new CursorLoader(context, null, DatabaseHelper.MOVIE_SUMMARY_PROJECTION,
                null, null, sortOrder)
        {
            @Override
            public Cursor loadInBackground() {
                if(mCurFilter == null) return null;
                if(mCurFilter.equals("")) return null;

                AsyncTask<String, Void, List<Movie>> asyncTask = new MovieMemoryManagementClient(context);
                asyncTask.execute(mCurFilter);
                try {
                    List<Movie> asyncTaskGet = asyncTask.get();
                    return listToMatrixCursor(asyncTaskGet);
//                    Cursor rawQuery = new DatabaseHelper(context)
//                            .getReadableDatabase().rawQuery(
//                                    "SELECT * FROM " + DatabaseHelper.MOVIE_TABLE_NAME +
//                                            " WHERE " + DatabaseHelper.MOVIE_ID +
//                                            " LIKE '%" + mCurFilter + "%'", null);
//                    Log.e("Ayy lmao rawQuery", rawQuery.getCount() + "");
//                    return rawQuery;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(progressBar != null) progressBar.setVisibility(View.INVISIBLE);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(progressBar != null) progressBar.setVisibility(View.INVISIBLE);
        mAdapter.swapCursor(null);
    }

    public void setCurFilter(Context context,String curFilter) {
        this.context = context;
        this.mCurFilter = curFilter;
    }

    public void setmAdapter(CursorAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
