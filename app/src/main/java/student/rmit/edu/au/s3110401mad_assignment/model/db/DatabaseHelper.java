package student.rmit.edu.au.s3110401mad_assignment.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mad_assignment.sqlite";

    /** Movie Table **/
    public static final String MOVIE_TABLE_NAME = "movie";
    public static final String MOVIE_ID = "_id";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_YEAR = "year";
    public static final String MOVIE_SHORT_PLOT = "shortPlot";
    public static final String MOVIE_FULL_PLOT = "fullPlot";
    public static final String MOVIE_IMAGE_BITMAP = "imageBitmap";
    public static final String MOVIE_RATING = "rating";
    private static final String CREATE_MOVIE_TABLE = "CREATE TABLE " +
          MOVIE_TABLE_NAME +
          " ("+ MOVIE_ID +" VARCHAR, " +
            MOVIE_TITLE + " VARCHAR, " +
            MOVIE_YEAR + " VARCHAR, " +
            MOVIE_SHORT_PLOT + " VARCHAR, " +
            MOVIE_FULL_PLOT + " TEXT, " +
            MOVIE_IMAGE_BITMAP + " BLOB, " +
            MOVIE_RATING + " VARCHAR);";

    public static final String[] MOVIE_SUMMARY_PROJECTION = new String[] {
            MOVIE_ID, // Some reason Cursor looks for double, so i regex "\d+$" to get that double
            MOVIE_ID,
            MOVIE_TITLE,
            MOVIE_YEAR,
            MOVIE_SHORT_PLOT,
            MOVIE_IMAGE_BITMAP,
            MOVIE_RATING};

    /** Party table **/
    public static final String PARTY_TABLE_NAME = "party";
    public static final String PARTY_ID = "_id";
    public static final String PARTY_MOVIE_ID = "movieId";
    public static final String PARTY_MOVIE_TITLE = "movieTitle";
    public static final String PARTY_DATETIME = "datetime";
    public static final String PARTY_VENUE = "place";
    public static final String PARTY_LOCATION = "location";
    private static final String CREATE_PARTY_TABLE = "CREATE TABLE " +
            PARTY_TABLE_NAME +
            " ("+ PARTY_ID + " INTEGER PRIMARY KEY, " +
            PARTY_MOVIE_ID + " VARCHAR, " +
            PARTY_MOVIE_TITLE + " VARCHAR, " +
            PARTY_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            PARTY_VENUE + " VARCHAR, " +
            PARTY_LOCATION + " VARCHAR);";

    /** Party invitees table **/
    public static final String PARTY_INVITEE_TABLE_NAME = "party_invitees";
    public static final String PARTY_INVITEE_ID = "_id";
    public static final String PARTY_INVITEE_PARTY_ID = "partyId";
    public static final String PARTY_INVITEE_CONTACTS_ID = "contactId";
    private static final String CREATE_PARTY_INVITEE_TABLE =
            "CREATE TABLE " + PARTY_INVITEE_TABLE_NAME + " (" +
                    PARTY_INVITEE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PARTY_INVITEE_PARTY_ID + " INTEGER, " +
                    PARTY_INVITEE_CONTACTS_ID + " VARCHAR); ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_PARTY_TABLE);
        db.execSQL(CREATE_PARTY_INVITEE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARTY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARTY_INVITEE_TABLE_NAME);
        onCreate(db);
    }
}
