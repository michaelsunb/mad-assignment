package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.adapter.MovieArrayAdapter;
import student.rmit.edu.au.s3110401mad_assignment.db.DatabaseHelper;
import student.rmit.edu.au.s3110401mad_assignment.view.MovieListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.PartyMapFragment;


public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    private CursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MovieArrayAdapter(this,
                new MatrixCursor(DatabaseHelper.MOVIE_SUMMARY_PROJECTION));

        initialFragment();
    }

    private void initialFragment() {
        Fragment fragment = new PartyMapFragment();
        selectFragment(fragment);
    }

    private void selectFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()) != null &&
                fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()).isVisible())
            return;

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName()).commit();
    }

    /** Menu stuff **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!newText.isEmpty() &&
                newText.length() > 2) {
            Fragment fragment = useMovieListFragment(newText);
            selectFragment(fragment);
            getLoaderManager().restartLoader(0, null,
                    ((LoaderManager.LoaderCallbacks)fragment));
            return true;
        }

        selectFragment(new PartyMapFragment());
        return false;
    }

    private Fragment useMovieListFragment(String newText) {
        MovieListFragment fragment = new MovieListFragment();
        fragment.setmAdapter(mAdapter);
        fragment.setCurFilter(this,
                !TextUtils.isEmpty(newText) ? newText : null);

        return fragment;
    }
}