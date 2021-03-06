package kanten.Other;

import android.app.Notification ;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.util.Log;


public class MyNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "com.androidmads.navdraweractivity.Other.MyNotificationPublisher.NOTIFICATION_ID" ;
    public static String NOTIFICATION = "com.androidmads.navdraweractivity.Other.MyNotificationPublisher.NOTIFICATION" ;

    public void onReceive (Context context , Intent intent) {
        Log.d("myTag", "HIER: MyNotificationPublisher");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        Integer Weekday_int = intent.getIntExtra( NOTIFICATION_ID , 0 );
        String Weekday_str = Weekday_int.toString();
        System.out.println("HIER MyNotificationPublisher: "+Weekday_str);

        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel(Weekday_str, "NOTIFICATION_CHANNEL_NAME_", importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }

        System.out.println("Hier: "+Weekday_int);
        assert notificationManager != null;
        notificationManager.notify(Weekday_int, notification);



    }
}