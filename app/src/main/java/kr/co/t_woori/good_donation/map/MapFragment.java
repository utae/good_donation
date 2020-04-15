package kr.co.t_woori.good_donation.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.MapFragmentBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-01.
 */

public class MapFragment extends NMapFragment{

    private MapFragmentBinding binding;

    private NMapView mapView;
    private NMapLocationManager mapLocationManager;
    private NMapController mapController;
    private NMapMyLocationOverlay mapMyLocationOverlay;
    private NMapViewerResourceProvider mapResourceProvider;
    private NMapOverlayManager mapOverlayManager;
    private NMapCompassManager mapCompassManager;
    private NMapPOIdataOverlay poidataOverlay;
    private ArrayList<Place> placeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        binding = MapFragmentBinding.bind(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initNaverMap();
        startNavigateCurrentLocation();
    }

    private void initNaverMap(){
        mapView = binding.mapView;
        mapView.setClientId(getString(R.string.naver_map_client_id));
        nMapContext.setupMapView(mapView);

        mapLocationManager = new NMapLocationManager(getContext());
        mapController = mapView.getMapController();

        mapLocationManager.setOnLocationChangeListener(new OnLocationChangeListener());

        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        binding.curLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigateCurrentLocation();
            }
        });

        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMap();
            }
        });

        mapResourceProvider = new NMapViewerResourceProvider(getContext());
        mapOverlayManager = new NMapOverlayManager(getContext(), mapView, mapResourceProvider);

        mapMyLocationOverlay = mapOverlayManager.createMyLocationOverlay(mapLocationManager, mapCompassManager);

        placeList = new ArrayList<>();

        getAllPlace();
    }

    private void startNavigateCurrentLocation(){
        if(!mapLocationManager.isMyLocationEnabled()){
            if(!mapLocationManager.enableMyLocation(true)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("위치 서비스를 활성화 해주세요.")
                        .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }else{
            if(mapLocationManager.isMyLocationFixed()){
                mapOverlayManager.addOverlay(mapMyLocationOverlay);
                mapController.setMapCenter(mapLocationManager.getMyLocation(), 14);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(mapLocationManager != null){
            if(hidden){
                if(mapLocationManager.isMyLocationEnabled()){
                    mapLocationManager.disableMyLocation();
                }
            }else{
                if(!mapLocationManager.isMyLocationEnabled()){
                    mapLocationManager.enableMyLocation(true);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mapLocationManager != null){
            if(!mapLocationManager.isMyLocationEnabled()){
                mapLocationManager.enableMyLocation(true);
            }
        }
    }

    @Override
    public void onStop() {
        if(mapLocationManager != null){
            if(mapLocationManager.isMyLocationEnabled()){
                mapLocationManager.disableMyLocation();
            }
        }
        super.onStop();
    }

    private void showPlaceMarker(ArrayList<Place> placeList){
        NMapPOIdata poiData = new NMapPOIdata(placeList.size(), mapResourceProvider);
        poiData.beginPOIdata(placeList.size());
        for(Place place : placeList){
            poiData.addPOIitem(place.getLongitude(), place.getLatitude(), place.getName() + "\n(클릭 시 상세보기)", NMapPOIflagType.PIN, place, Integer.parseInt(place.getIdNum()));
        }
        poiData.endPOIdata();

        poidataOverlay = mapOverlayManager.createPOIdataOverlay(poiData, null);

        poidataOverlay.setOnStateChangeListener(new NMapPOIdataOverlay.OnStateChangeListener() {
            @Override
            public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

            }

            @Override
            public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                PlaceDetailDialog.create((Place)nMapPOIitem.getTag()).show(getFragmentManager(), "place detail dialog");
            }
        });

        Utilities.logD("Test", "overlay : " + poidataOverlay.size());
    }

    private void getAllPlace(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(MapAPIService.class).getAllPlace()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!placeList.isEmpty()){
                    placeList.clear();
                    poidataOverlay.deselectFocusedPOIitem();
                }
                if(Integer.parseInt((String)results.get("count")) > 0){
                    if(results.get("place") instanceof List){
                        for(Object place : (List)results.get("place")){
                            if(place instanceof Map){
                                Map map = (Map)place;
                                placeList.add(new Place((String)map.get("idNum"), (String)map.get("name"), (String)map.get("longitude"), (String)map.get("latitude"), (String)map.get("img")));
                            }
                        }
                        showPlaceMarker(placeList);
                    }
                }
            }
        };
        serverCommunicator.execute();
    }

    private class OnLocationChangeListener implements NMapLocationManager.OnLocationChangeListener{
        @Override
        public boolean onLocationChanged(NMapLocationManager mapLocationManager, NGeoPoint nGeoPoint) {
            mapController.setMapCenter(nGeoPoint, 14);
            return false;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager mapLocationManager) {
            Utilities.showToast(getContext(), "현재 위치 탐색 실패");
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager mapLocationManager, NGeoPoint nGeoPoint) {

        }
    }

    public void refreshMap(){
        if(poidataOverlay != null){
            getAllPlace();
        }
    }
}
