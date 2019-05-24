package gov.jussantiago.jmulki.autoconsulta;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "Servicio de Mensajes";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message Data Body: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("title").toString();
            String body = remoteMessage.getData().get("body").toString();
            String numero = remoteMessage.getData().get("numero").toString();
            String fuero = remoteMessage.getData().get("fuero").toString();

            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.balanza1;

            Intent i=new Intent(MyFirebaseMessagingService.this, MovimientosActivity.class);

            Log.i("Message Data: ",numero);
            i.putExtra("numero",numero);
            i.putExtra("fuero",fuero);

            int id = createID();
            PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, id, i, 0);

            MediaSessionCompat mMediaSession = new MediaSessionCompat(this,"hola");
            NotificationCompat.MediaStyle mMediaStyle =  new NotificationCompat.MediaStyle();
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.fondo_autoconsulta))
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true)
                    .addAction(R.mipmap.close,"Cerrar",pendingIntent);

            mNotifyMgr.notify(id, mBuilder.build());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.

                //scheduleJob();
            } else {
                // Handle message within 10 seconds

                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmssS",  Locale.US).format(now));
        return id;
    }
}
