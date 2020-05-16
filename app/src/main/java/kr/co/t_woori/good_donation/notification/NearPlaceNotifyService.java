package kr.co.t_woori.good_donation.notification;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.main.MainActivity;

public class NearPlaceNotifyService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private NotificationManager notificationManager;
    private Notification notification;
    private ServerCommunicator serverCommunicator;
    private HashSet<String> nearPlaceSet;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        nearPlaceSet = new HashSet<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            stopSelf();
        }
        locationListener = new LocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);

        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        if(locationManager != null && locationListener != null){
            locationManager.removeUpdates(locationListener);
        }
        if(serverCommunicator != null){
            serverCommunicator.cancel(true);
        }
    }

    private class LocationListener implements android.location.LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            serverCommunicator = new ServerCommunicator(
                    NearPlaceNotifyService.this, APICreator.create(NotificationAPIService.class).checkMyLocation(Double.toString(location.getLongitude()), Double.toString(location.getLatitude())), true
                    ) {
                @Override
                protected void onSuccess(HashMap<String, Object> results) {
                    if(Integer.parseInt((String)results.get("count")) > 0){
                        HashSet<String> newNearPlaceSet = new HashSet<>();
                        for(Object json : (List)results.get("place")){
                            if(json instanceof Map){
                                Map map = (Map)json;
                                String placeId = (String)map.get("placeId");
                                if(!nearPlaceSet.contains(placeId)){
                                    showNotification((String)map.get("name"));
                                }
                                newNearPlaceSet.add(placeId);
                            }
                        }
                        nearPlaceSet = newNearPlaceSet;
                    }
                }
            };
            serverCommunicator.execute();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void showNotification(String placeName){
        Intent intent = new Intent(NearPlaceNotifyService.this, MainActivity.class);
        intent.putExtra("notification", "nearPlace");
        PendingIntent pendingIntent = PendingIntent.getActivity(NearPlaceNotifyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("주변에 기부매장이 있습니다.")
                .setSmallIcon(R.drawable.img_logo)
                .setContentIntent(pendingIntent)
                .setContentText(placeName)
                .build();

        //소리추가
        notification.defaults = Notification.DEFAULT_SOUND;

        //알림 소리를 한번만 내도록
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;

        //확인하면 자동으로 알림이 제거 되도록
        notification.flags = Notification.FLAG_AUTO_CANCEL;


        notificationManager.notify( 777 , notification);
    }
}
