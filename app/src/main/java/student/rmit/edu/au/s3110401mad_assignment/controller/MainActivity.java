package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.view.MovieListFragment;
import student.rmit.edu.au.s3110401mad_assignment.view.PartyMapFragment;


public class MainActivity extends AppCompatActivity {
    private EditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialFragment();

        editText = (EditText) findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Fragment myFragment = getFragmentManager().findFragmentByTag(
                        MovieListFragment.class.getSimpleName()
                );
                if (myFragment != null && myFragment.isVisible() &&
                        editText.getText().length() > 0) {
                    Log.i("asdgdgs s.toString: ", s.toString());
                    Log.i("asdgdgs edit.toString: ", editText.getText().toString());
                    ((MovieListFragment)myFragment).retrieveMovies(editText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Fragment fragment = new PartyMapFragment();
                if(editText.getText().length() > 1) {
                    fragment = new MovieListFragment();
                }
                selectFragment(fragment);
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editText.setText("");
                initialFragment();
            }
        });
    }

    private void initialFragment() {
        Fragment fragment = new PartyMapFragment();
        selectFragment(fragment);
    }

    private void selectFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()) != null &&
                fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()).isVisible()) {
            return;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName()).commit();
    }
}