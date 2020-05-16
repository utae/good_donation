package kr.co.t_woori.good_donation.map;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nhn.android.maps.NMapContext;

/**
 * Created by rladn on 2017-08-02.
 */

public class NMapFragment extends Fragment {

    protected NMapContext nMapContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nMapContext = new NMapContext(getActivity());
        nMapContext.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        nMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        nMapContext.onResume();
    }

    @Override
    public void onPause() {
        nMapContext.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        nMapContext.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        nMapContext.onDestroy();
        super.onDestroy();
    }
}
