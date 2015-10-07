package student.rmit.edu.au.s3110401mad_assignment.view;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.MovieDetailActivity;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.MovieArrayAdapter;
import student.rmit.edu.au.s3110401mad_assignment.model.db.DatabaseHelper;
import student.rmit.edu.au.s3110401mad_assignment.model.Movie;
import student.rmit.edu.au.s3110401mad_assignment.model.MovieModel;
import student.rmit.edu.au.s3110401mad_assignment.model.async_task.ListToMatrixCursorTask;
import student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility.MovieMemoryManagementClient;

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView movieListView;

    private CursorAdapter mAdapter;
    private String mCurFilter;
    private Context context;
    private AsyncTask<String, Void, List<Movie>> asyncTask;

    @Override
    public void onResume() {
        super.onResume();
        movieListView.setAdapter(new MovieArrayAdapter(getActivity(), getMovieCursor()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_movie_list, container, false);

        layout.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        movieListView = (ListView) layout.findViewById(R.id.movie_list_fragment);
        mAdapter = new MovieArrayAdapter(getActivity(), getMovieCursor());
        movieListView.setAdapter(mAdapter);

        AdapterView.OnItemClickListener listener = new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MatrixCursor cursor = (MatrixCursor) movieListView.getItemAtPosition(position);
                Movie movieSelected = MovieModel.getSingleton().getMovieById(
                        "tt" + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOVIE_ID)));
                goToMovieDetailActivity(movieSelected);
            }
        };
        movieListView.setOnItemClickListener(listener);

        if(movieListView.getCount() > 0)
            layout.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        return layout;
    }

    private void goToMovieDetailActivity(Movie movieSelected) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(getString(R.string.movie_id), movieSelected.getId());
        startActivityForResult(intent, 1);
    }

    @Nullable
    private Cursor getMovieCursor() {
        asyncTask = new MovieMemoryManagementClient(context);
        asyncTask.execute(mCurFilter);
        try {
            List<Movie> asyncTaskGet = asyncTask.get();
            return new ListToMatrixCursorTask(asyncTaskGet).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(context == null)
            context = getActivity();
        String sortOrder = DatabaseHelper.MOVIE_TITLE + " COLLATE LOCALIZED ASC";

        /**
         * https://stackoverflow.com/a/21325236
         * Don't want a shared database so overrider CursorLoader
         */
        return new CursorLoader(context, null, DatabaseHelper.MOVIE_SUMMARY_PROJECTION,
                null, null, sortOrder) {
            @Override
            public Cursor loadInBackground() {
                if(mCurFilter == null) return null;
                if(mCurFilter.equals("")) return null;
                return getMovieCursor();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    public void setCurFilter(Context context,String curFilter) {
        resetCursor();
        this.context = context;
        this.mCurFilter = curFilter;
    }

    public void resetCursor() {
        // Destroys variables and references, and catches Exceptions
        try {
            if (mAdapter != null) {
                mAdapter.changeCursor(null);
                mAdapter = null;
            }
            if (asyncTask != null &&
                    asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                asyncTask.cancel(true);
            }
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }
}
