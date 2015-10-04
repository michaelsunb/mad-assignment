package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by Michaelsun Baluyos on 5/10/2015.
 */
public class CursorToString extends AsyncTask<Void,Void,String> {

    private String columnName;
    private Cursor cursor;

    public CursorToString(Cursor cursor, String columnName) {
        this.cursor = cursor;
        this.columnName = columnName;
    }

    @Override
    protected String doInBackground(Void... params) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }
}
