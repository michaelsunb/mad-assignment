package student.rmit.edu.au.s3110401mad_assignment.model.async_task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import student.rmit.edu.au.s3110401mad_assignment.R;
import student.rmit.edu.au.s3110401mad_assignment.controller.PartyEditActivity;
import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyStruct;
import student.rmit.edu.au.s3110401mad_assignment.view.PartyMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class DistanceMatrixTask extends AsyncTask<Void, Void, Boolean> {

    private static final String HTTPS_MAPS_GOOGLEAPIS_COM_MAPS_API_DISTANCE_MATRIX_JSON =
            "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final int TIME_THRESHOLD = 15; // wait 15 minutes

    private NotificationManager mNotificationManager;

    private Party event;
    private Context context;

    public DistanceMatrixTask(Context context, Party event) {
        this.event = event;
        this.context = context;
        this.mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private String googleDistanceMatrixUrl(String currLocationLat, String currLocationLong) {
        final String apiKey = context.getString(R.string.google_map_api_key);

        StringBuilder urlAddress;
        urlAddress = new StringBuilder();

        urlAddress.append(HTTPS_MAPS_GOOGLEAPIS_COM_MAPS_API_DISTANCE_MATRIX_JSON);
        urlAddress.append("key=");
        urlAddress.append(apiKey);
        urlAddress.append("&");
        urlAddress.append("origins=");
        urlAddress.append(currLocationLat);
        urlAddress.append(",");
        urlAddress.append(currLocationLong);
        urlAddress.append("&destinations=");
        urlAddress.append(event.getLocation()[PartyStruct.LONGITUDE]);
        urlAddress.append(",");
        urlAddress.append(event.getLocation()[PartyStruct.LATITUDE]);
        return urlAddress.toString();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // get current location
        Criteria criteria = new Criteria();
        LocationManager locationManager =
        (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        String currLocationLat;
        String currLocationLong;
        if(location == null){
            currLocationLat = PartyMapFragment.DEFAULT_LAT + "";
            currLocationLong = PartyMapFragment.DEFAULT_LONG + "";
        }else{
            currLocationLat =
            (String.valueOf(location.getLatitude()));
            currLocationLong =
            (String.valueOf(location.getLongitude()));
        }

        Boolean result = false;

        BufferedReader reader = null;
        HttpURLConnection con = null;
        InputStream inp = null;

        try {
            URL url = new URL(googleDistanceMatrixUrl(currLocationLat, currLocationLong));
            con = (HttpURLConnection) url.openConnection();
            con.connect();

            inp = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inp));

            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }

            int duration;

            Calendar currentDate = Calendar.getInstance();
            Calendar partyDate = Calendar.getInstance();
            partyDate.setTimeInMillis(event.getDate().getTimeInMillis());

            JSONObject gdmResult =
            (JSONObject) new JSONTokener(sb.toString()).nextValue();

            JSONArray rowsArray = gdmResult.getJSONArray("rows");

            if(gdmResult.getString("status").equals("OK")) {
                JSONObject rowsObj =  rowsArray.getJSONObject(0);
                JSONArray elementsArray = rowsObj.getJSONArray("elements");
                JSONObject elementsObj = elementsArray.getJSONObject(0);
                JSONObject durationObj = elementsObj.getJSONObject("duration");
                duration = durationObj.getInt("value");

                partyDate.add(Calendar.SECOND, duration * -1);

                partyDate.add(Calendar.MINUTE, TIME_THRESHOLD * -1);

                if(partyDate.getTimeInMillis() <= currentDate.getTimeInMillis() ||
                        currentDate.getTimeInMillis() >= event.getDate().getTimeInMillis()){
                    result = true;
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inp != null) {
                    inp.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        // Show notifications if it fits the criteria
        if(result){
            Notification notif = new Notification();
            notif.contentIntent = makePartyIntent(event.getId());
            notif.tickerText = context.getString(R.string.notifcation_party) + event.getVenue();
            notif.icon = R.drawable.stat_happy;

            RemoteViews contentView = new RemoteViews(context.getPackageName(),
                    R.layout.status_bar_balloon);
            contentView.setTextViewText(R.id.text, notif.tickerText);
            contentView.setImageViewResource(R.id.icon, notif.icon);
            notif.contentView = contentView;

            mNotificationManager.notify(event.getId() + "", 0, notif);
        }
        super.onPostExecute(result);
    }

    private PendingIntent makePartyIntent(int partyId) {
        Intent intent = new Intent(context, PartyEditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("party_id", partyId);

        return PendingIntent.getActivity (
                context,
                (int)System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_ONE_SHOT
        );
    }
}
