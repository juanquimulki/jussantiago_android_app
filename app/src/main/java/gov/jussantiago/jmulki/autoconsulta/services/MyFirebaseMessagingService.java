package gov.jussantiago.jmulki.autoconsulta.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import gov.jussantiago.jmulki.autoconsulta.R;
import gov.jussantiago.jmulki.autoconsulta.activities.CedulaActivity;
import gov.jussantiago.jmulki.autoconsulta.activities.MovimientosActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "Servicio de Mensajes";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Intent i=null;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String numero,fuero;

            Log.d(TAG, "Message Data Body: " + remoteMessage.getData());
            String title = remoteMessage.getData().get("title").toString();
            String body = remoteMessage.getData().get("body").toString();
            String tipo = remoteMessage.getData().get("tipo").toString();
            String ticker = "";

            switch (tipo) {
                case "1":
                    numero = remoteMessage.getData().get("numero").toString();
                    fuero = remoteMessage.getData().get("fuero").toString();

                    i=new Intent(MyFirebaseMessagingService.this, MovimientosActivity.class);
                    Log.i("Message Data: ",numero);
                    i.putExtra("numero",numero);
                    i.putExtra("fuero",fuero);
                    ticker = "Nuevo Expediente";
                    break;
                case "2":
                    numero = remoteMessage.getData().get("numero").toString();

                    i=new Intent(MyFirebaseMessagingService.this,CedulaActivity.class);
                    Log.i("Message Data: ",numero);
                    i.putExtra("numero",numero);
                    ticker = "Nueva Cédula";
                    break;
            }

            int id = createID();
            PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, id, i, 0);

            NotificationCompat.Builder mBuilder;
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.balanza1;
            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.fondo_autoconsulta))
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker(ticker)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);

            mNotifyMgr.notify(1, mBuilder.build());
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
