package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.model.chain_of_responsibility.PartyMemoryManagementClient;
import student.rmit.edu.au.s3110401mad_assignment.view.MovieListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.PartyMapFragment;


public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    public static final int LENGTH_ONCHANGE_FRAGMENT = 2;
    private PartyMemoryManagementClient partyTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialFragment();
    }

    private void initialFragment() {
        partyTask = new PartyMemoryManagementClient(this);
        partyTask.execute();

        PartyMapFragment fragment = new PartyMapFragment();
        fragment.setParties(partyTask);
        selectFragment(fragment);
    }

    private void selectFragment(Fragment fragment) {
        selectFragment(fragment,false);
    }

    private void selectFragment(Fragment fragment, boolean forceReset) {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()) != null &&
                fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()).isVisible()
                && !forceReset)
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
                newText.length() > LENGTH_ONCHANGE_FRAGMENT) {
            Fragment fragment = useMovieListFragment(newText);
            selectFragment(fragment,true);

            return true;
        }

        PartyMapFragment fragment = new PartyMapFragment();
        fragment.setParties(partyTask);
        selectFragment(fragment);
        return false;
    }

    private Fragment useMovieListFragment(String newText) {
        MovieListFragment fragment = new MovieListFragment();
        fragment.setCurFilter(this,
                !TextUtils.isEmpty(newText) ? newText : null);

        return fragment;
    }
}