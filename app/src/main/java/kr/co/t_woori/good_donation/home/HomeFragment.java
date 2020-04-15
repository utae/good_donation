package kr.co.t_woori.good_donation.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.charity.Charity;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.HomeFragmentBinding;
import kr.co.t_woori.good_donation.donation.DonationAPIService;
import kr.co.t_woori.good_donation.map.Place;
import kr.co.t_woori.good_donation.map.PlaceDetailDialog;
import kr.co.t_woori.good_donation.utilities.RefreshFragment;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-01.
 */

public class HomeFragment extends RefreshFragment {

    private HomeFragmentBinding binding;
    private View.OnClickListener onHomeBannerClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        binding = HomeFragmentBinding.bind(view);

        getHomeBanner();

        getNo1User();
        getNo1Place();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            binding.viewPager.setDuration(2000);
            binding.viewPager.setIndeterminate(true);
            binding.viewPager.setAutoScrollEnabled(true);
        }
    }

    private void initBanner(int imgAmount){
        HomeBannerAdapter bannerAdapter = new HomeBannerAdapter(getContext(), getFragmentManager(), binding.viewPagerIndicator, imgAmount);
        if(onHomeBannerClickListener != null){
            bannerAdapter.setOnHomeBannerClickListener(onHomeBannerClickListener);
        }
        binding.viewPager.setAdapter(bannerAdapter);
        binding.viewPager.addOnPageChangeListener(bannerAdapter.getOnBannerChangeListener());
    }

    private void getHomeBanner(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(HomeAPIService.class).getHomeBanner()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                initBanner(Integer.parseInt((String)results.get("img")));
            }
        };
        serverCommunicator.execute();
    }

//    private void getMyAccumulation(){
//        ServerCommunicator serverCommunicator = new ServerCommunicator(
//                getContext(), APICreator.create(DonationAPIService.class).getMyAccumulation()
//        ) {
//            @Override
//            protected void onSuccess(HashMap<String, Object> results) {
//                String accumulation = (String)results.get("accumulation");
////                binding.myAccumulationTextView.setText("나의 기부금액 : " + accumulation + "원");
//            }
//        };
//        serverCommunicator.execute();
//    }
//
//    private void getAllAccumulation(){
//        ServerCommunicator serverCommunicator = new ServerCommunicator(
//                getContext(), APICreator.create(DonationAPIService.class).getAllAccumulation()
//        ) {
//            @Override
//            protected void onSuccess(HashMap<String, Object> results) {
//                String accumulation = (String)results.get("accumulation");
////                binding.allAccumulationTextView.setText("전체 기부금액 : " + accumulation + "원");
//            }
//        };
//        serverCommunicator.execute();
//    }

    private void getNo1User(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(HomeAPIService.class).getNo1User()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                binding.no1UserNicknameTextView.setText((String)results.get("nickname"));
                binding.no1UserAccumulationTextView.setText((String)results.get("accumulation"));
            }
        };
        serverCommunicator.execute();
    }

    private void getNo1Place(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(HomeAPIService.class).getNo1Place()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                binding.no1PlaceNameTextView.setText((String)results.get("name"));
                binding.no1PlaceAccumulationTextView.setText((String)results.get("accumulation"));
                final Place place = new Place((String)results.get("idNum"), (String)results.get("name"), (String)results.get("longitude"), (String)results.get("latitude"), (String)results.get("img"));
                binding.no1PlaceContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlaceDetailDialog.create(place).show(getFragmentManager(), "no1 place detail dialog");
                    }
                });
            }
        };
        serverCommunicator.execute();
    }

    public void setOnHomeBannerClickListener(View.OnClickListener onHomeBannerClickListener) {
        this.onHomeBannerClickListener = onHomeBannerClickListener;
    }

    @Override
    public void refreshFragment() {

    }
}
