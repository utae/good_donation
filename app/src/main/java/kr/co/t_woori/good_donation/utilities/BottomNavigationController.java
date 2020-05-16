package kr.co.t_woori.good_donation.utilities;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import kr.co.t_woori.good_donation.charity.CharityFragment;
import kr.co.t_woori.good_donation.home.HomeFragment;
import kr.co.t_woori.good_donation.map.MapFragment;
import kr.co.t_woori.good_donation.scan.ScanFragment;

/**
 * Created by rladn on 2017-08-01.
 */

public class BottomNavigationController {

    private FragmentManager fragmentManager;

    @IdRes private int containerId;

    private ArrayList<Fragment> fragments;

    private Menu curMenu;

    private BottomNavigationListenerContainer listenerContainer;

    public BottomNavigationController(FragmentManager fragmentManager, int containerId, BottomNavigationListenerContainer listenerContainer) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listenerContainer = listenerContainer;
        initFragmentList();
    }

    private void initFragmentList(){
        fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnHomeBannerClickListener(listenerContainer.getOnHomeBannerClickListener());
        ScanFragment scanFragment = new ScanFragment();
        MapFragment mapFragment = new MapFragment();
        CharityFragment charityFragment = new CharityFragment();
        fragments.add(homeFragment);
        fragments.add(scanFragment);
        fragments.add(mapFragment);
        fragments.add(charityFragment);
    }

    public void selectMenu(Menu menu){
        if(fragments.size() > menu.ordinal()){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if(curMenu != null){
                transaction.hide(fragmentManager.findFragmentByTag(Integer.toString(curMenu.ordinal())));
            }
            if(fragmentManager.findFragmentByTag(Integer.toString(menu.ordinal())) == null){
                transaction.add(containerId, fragments.get(menu.ordinal()), Integer.toString(menu.ordinal()));
            }else{
                transaction.show(fragmentManager.findFragmentByTag(Integer.toString(menu.ordinal())));
            }
            curMenu = menu;
            transaction.commit();
        }

    }

    public void refreshCurFragment(){
        if(curMenu != null){
            Fragment curFragment = fragmentManager.findFragmentByTag(Integer.toString(curMenu.ordinal()));
            if(curFragment instanceof RefreshFragment){
                ((RefreshFragment)curFragment).refreshFragment();
            }
        }
    }

    public enum Menu{
        home, scan, map, charity
    }

}
