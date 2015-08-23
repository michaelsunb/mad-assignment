package student.rmit.edu.au.s3110401mad_assignment.model;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Michaelsun Baluyos on 24/08/2015.
 * Reference for unique android id:
 * http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
 */
public class PersonalModel {
    private static final int MAXIMUM_RATING = 5;
    private static final int MINIMUM_RATING = 0;

    private final String androidId;
    private int rating = 0;

    public PersonalModel(Context context) {
        androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public void editRating(int rating) {
        if (rating > MINIMUM_RATING &&
                rating <= MAXIMUM_RATING)
            this.rating = rating;
    }
}