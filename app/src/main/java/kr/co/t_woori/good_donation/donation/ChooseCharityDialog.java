package kr.co.t_woori.good_donation.donation;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.charity.Charity;
import kr.co.t_woori.good_donation.charity.CharityAPIService;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.ChooseCharityDialogBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-10.
 */

public class ChooseCharityDialog extends DialogFragment {

    private ChooseCharityDialogBinding binding;
    private ArrayList<Charity> myCharityList;
    private ArrayList<Charity> recommendCharityList;
    private SimpleChooseCharityListAdapter charityListAdapter;
    private AdapterView.OnItemClickListener onItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.choose_charity_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        myCharityList = new ArrayList<>();
        recommendCharityList = new ArrayList<>();

        getMyCharity();
        getRecommendCharity();

        return binding.getRoot();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initListView(ListView listView, ArrayList<Charity> charityList){
        if(charityList.isEmpty()){
            binding.myCharityListView.setEmptyView(binding.myEmptyView);
        }
        charityListAdapter = new SimpleChooseCharityListAdapter(getContext(), charityList);
        charityListAdapter.latestSort();
        if(onItemClickListener != null){
            listView.setOnItemClickListener(onItemClickListener);
        }
        listView.setAdapter(charityListAdapter);
    }

    private void getMyCharity(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(CharityAPIService.class).getMyCharity()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    if(results.get("charity") instanceof List){
                        Charity tmpCharity;
                        for(Object charity : (List)results.get("charity")){
                            if(charity instanceof Map){
                                Map map = (Map)charity;
                                tmpCharity = new Charity((String)map.get("idNum"), (String)map.get("name"));
                                tmpCharity.setAppreciationPhrase((String)map.get("appreciation"));
                                myCharityList.add(tmpCharity);
                            }
                        }
                    }
                }
                getMyProject();
            }
        };
        serverCommunicator.execute();
    }

    private void getMyProject(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(CharityAPIService.class).getMyProject()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    if(results.get("project") instanceof List){
                        Charity tmpProject;
                        for(Object charity : (List)results.get("project")){
                            if(charity instanceof Map){
                                Map map = (Map)charity;
                                tmpProject = new Charity((String)map.get("idNum"), (String)map.get("name"));
                                tmpProject.setAppreciationPhrase((String)map.get("appreciation"));
                                myCharityList.add(tmpProject);
                            }
                        }
                    }
                }
                initListView(binding.myCharityListView, myCharityList);
            }
        };
        serverCommunicator.execute();
    }

    private void getRecommendCharity(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(CharityAPIService.class).getRecommendCharity()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    if(results.get("charity") instanceof List){
                        Charity tmpCharity;
                        for(Object charity : (List)results.get("charity")){
                            if(charity instanceof Map){
                                Map map = (Map)charity;
                                tmpCharity = new Charity((String)map.get("idNum"), (String)map.get("name"));
                                tmpCharity.setAppreciationPhrase((String)map.get("appreciation"));
                                recommendCharityList.add(tmpCharity);
                            }
                        }
                    }
                }else{
                    binding.recommendCharityListView.setEmptyView(binding.recommendEmptyView);
                }
                initListView(binding.recommendCharityListView, recommendCharityList);
            }
        };
        serverCommunicator.execute();
    }
}
