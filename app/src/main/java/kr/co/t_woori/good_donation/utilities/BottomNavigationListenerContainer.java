package kr.co.t_woori.good_donation.utilities;

import android.view.View;

/**
 * Created by rladn on 2017-09-29.
 */

public class BottomNavigationListenerContainer {

    private View.OnClickListener onHomeBannerClickListener;

    public View.OnClickListener getOnHomeBannerClickListener() {
        return onHomeBannerClickListener;
    }

    public void setOnHomeBannerClickListener(View.OnClickListener onHomeBannerClickListener) {
        this.onHomeBannerClickListener = onHomeBannerClickListener;
    }
}
