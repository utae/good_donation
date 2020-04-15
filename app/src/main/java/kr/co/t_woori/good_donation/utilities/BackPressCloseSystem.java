package kr.co.t_woori.good_donation.utilities;

import android.app.Activity;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * Created by rladn on 2017-07-31.
 */

public class BackPressCloseSystem {

    private long backKeyPressedTime = 0L;

    private Toast toast;

    private Activity activity;

    private OnBackPressCloseListener onBackPressCloseListener = null;

    public BackPressCloseSystem(Activity activity) {
        this.activity = activity;
    }

    public void setOnBackPressCloseListener(OnBackPressCloseListener onBackPressCloseListener) {
        this.onBackPressCloseListener = onBackPressCloseListener;
    }

    public void onBackPressed() {

        if(backKeyPressedTime != 0 && SystemClock.uptimeMillis() - backKeyPressedTime < 3000){
            if(onBackPressCloseListener != null){
                onBackPressCloseListener.onBackPressClose();
            }
            activity.finishAffinity();
            toast.cancel();
        }else{
            toast = Toast.makeText(activity, "한 번 더 뒤로가기 버튼을 누르면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            backKeyPressedTime = SystemClock.uptimeMillis();
        }
    }

    //권장되는 방법이 아니라서 사용은 안하지만 혹시 몰라서 주석처리
//    private void programShutdown() {
//        activity.moveTaskToBack(true);
//        activity.finish();
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
//    }

    public interface OnBackPressCloseListener{
        void onBackPressClose();
    }
}

