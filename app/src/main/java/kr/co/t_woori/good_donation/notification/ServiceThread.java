package kr.co.t_woori.good_donation.notification;

/**
 * Created by rladn on 2017-09-26.
 */

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;

public class ServiceThread extends Thread{

    private Context context;
    private Handler handler;
    private boolean isRun = true;
    private LocationManager locationManager;

    public ServiceThread(Context context, Handler handler){
        this.context = context;
        this.handler = handler;
        this.locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkMyLocation(){

    }
}
