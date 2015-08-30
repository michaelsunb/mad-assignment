package student.rmit.edu.au.s3110401mad_assignment.controller;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import student.rmit.edu.au.s3110401mad_assignment.R;

/**
 * Created by Michaelsun Baluyos on 30/08/2015.
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[]) pudsBundle.get("pdus");
            SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);

            if (messages.getMessageBody().contains("Hi")) {
                abortBroadcast();
                Toast.makeText(context, "Message sent from contacts."
                        , Toast.LENGTH_SHORT).show();
            }
        }
        /*if (intent.getAction().equals(SMS_RECEIVED)) {
            abortBroadcast();

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // get sms objects
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus.length == 0) {
                    return;
                }
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
                if(messages.getMessageBody().contains("Hi")) {
//                    abortBroadcast();

                    Toast.makeText(context, "Message sent from contacts."
                            , Toast.LENGTH_SHORT).show();
                }
                /*//*
                // large message might be broken into many
                SmsMessage[] messages = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append(messages[i].getMessageBody());
                }
                String sender = messages[0].getOriginatingAddress();
                String message = sb.toString();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(sender, null, message, null, null);//phone number will be your number.
            }
        }*/
    }
}