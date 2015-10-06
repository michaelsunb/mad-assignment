
package student.rmit.edu.au.s3110401mad_assignment.controller.notification;

import student.rmit.edu.au.s3110401mad_assignment.model.Party;
import student.rmit.edu.au.s3110401mad_assignment.model.PartyModel;
import student.rmit.edu.au.s3110401mad_assignment.model.async_task.DistanceMatrixTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class GeoLocationService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
    return null;
  }
  
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Ayy lmao Class is", this.getClass().getSimpleName());

        List<Party> partyList =
                PartyModel.getSingleton().getAllParties();
    
        for(Party party : partyList){
            new DistanceMatrixTask(
                    getApplicationContext(), party).execute();
        }
        return START_STICKY;
    }
}
