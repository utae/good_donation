package kr.co.t_woori.good_donation.utilities;

import android.support.v4.app.Fragment;

/**
 * Created by rladn on 2017-08-01.
 */

public abstract class RefreshFragment extends Fragment {

    private boolean isRefreshing = false;

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public abstract void refreshFragment();

}
