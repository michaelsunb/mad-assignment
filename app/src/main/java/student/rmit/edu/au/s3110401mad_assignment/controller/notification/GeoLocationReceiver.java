package student.rmit.edu.au.s3110401mad_assignment.controller.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class GeoLocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Ayy lmao Class is", this.getClass().getSimpleName());
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null){
            Intent service = new Intent(context, GeoLocationService.class);
            context.startService(service);
        }
    }
}