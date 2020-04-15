package kr.co.t_woori.good_donation.charity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.CharityFragmentBinding;
import kr.co.t_woori.good_donation.utilities.RefreshFragment;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-01.
 */

public class CharityFragment extends RefreshFragment {

    private CharityFragmentBinding binding;
    private TabHost tabHost;
    private ArrayList<Charity> allCharityList;
    private ArrayList<Charity> allProjectList;
    private ArrayList<String> myCharityList;
    private ArrayList<String> myProjectList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.charity_fragment, container, false);
        binding = CharityFragmentBinding.bind(view);

        tabHost = binding.tabHost;

        tabHost.setup();

        addTab("charity tab", R.id.charity_page, "재단후원");
        addTab("project tab", R.id.project_page, "특별 모금 프로젝트");

        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
            ((TextView)((ViewGroup) tabHost.getTabWidget().getChildTabViewAt(i)).getChildAt(1)).setTextColor(Color.BLACK);
        }

        charityPageInit();
        projectPageInit();

        return view;
    }

    private void addTab(String tag, @IdRes int contentId, String indicator){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setContent(contentId);
        tabSpec.setIndicator(indicator);
        tabHost.addTab(tabSpec);
    }

    public void setPage(int index){
        tabHost.setCurrentTab(index);
    }

    private void initListView(ListView listView, ArrayList<Charity> charityList, ArrayList<String> myList, boolean isProject){
        CharityListAdapter listAdapter;
        listAdapter = new CharityListAdapter(getContext(), charityList, myList, isProject);
        listView.setOnItemClickListener(new OnItemClickListener(isProject));
        if(isProject){
            if(binding.projectPage.sortBtn.isChecked()){
                listAdapter.popularSort();
            }
            if(binding.projectPage.filterBtn.isChecked()){
                listAdapter.showMyCharity();
            }
        }else{
            if(binding.charityPage.sortBtn.isChecked()){
                listAdapter.popularSort();
            }
            if(binding.charityPage.filterBtn.isChecked()){
                listAdapter.showMyCharity();
            }
        }
        listView.setAdapter(listAdapter);
    }

    private void charityPageInit(){
        allCharityList = new ArrayList<>();
        myCharityList = new ArrayList<>();
        String intro = "기부액 목표 제한이 없는 기부재단 입니다.";
        binding.charityPage.introTextView.setText(intro);
        binding.charityPage.sortBtn.setOnCheckedChangeListener(new OnSortButtonToggleListener(binding.charityPage.listView));
        binding.charityPage.filterBtn.setOnCheckedChangeListener(new OnFilterButtonToggleListener(binding.charityPage.listView));
        binding.charityPage.saveBtn.setOnClickListener(new OnSaveButtonClickListener(binding.charityPage.listView, false));
        getAllCharity();
    }

    private void projectPageInit(){
        allProjectList = new ArrayList<>();
        myProjectList = new ArrayList<>();
        String intro = "달성 목표 기부액이 있는 기부재단 입니다.";
        binding.projectPage.introTextView.setText(intro);
        binding.projectPage.sortBtn.setOnCheckedChangeListener(new OnSortButtonToggleListener(binding.projectPage.listView));
        binding.projectPage.filterBtn.setOnCheckedChangeListener(new OnFilterButtonToggleListener(binding.projectPage.listView));
        binding.projectPage.saveBtn.setOnClickListener(new OnSaveButtonClickListener(binding.projectPage.listView, true));
        getAllProject();
    }

    private void getAllCharity(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(CharityAPIService.class).getAllCharity()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!allCharityList.isEmpty()){
                    allCharityList.clear();
                }
                if(!myCharityList.isEmpty()){
                    myCharityList.clear();
                }
                if(results.get("allCharity") instanceof List && results.get("myCharity") instanceof List){
                    Charity charity;
                    for(Object json : (List)results.get("allCharity")){
                        if(json instanceof Map){
                            Map map = (Map)json;
                            String idNum = (String)map.get("idNum");
                            String name = (String)map.get("name");
                            String introduction = (String)map.get("introduction");
                            String appreciation = (String)map.get("appreciation");
                            String registration = (String)map.get("registration");
                            String follow = (String)map.get("follow");
                            String today = (String)map.get("today");
                            String accumulation = (String)map.get("accumulation");
                            String imgAmount = (String)map.get("img");
                            charity = new Charity(idNum, name, introduction, appreciation, registration, follow, today, accumulation, imgAmount);

                            allCharityList.add(charity);
                        }
                    }
                    if(Integer.parseInt((String)results.get("myCount")) > 0){
                        for(Object json : (List)results.get("myCharity")){
                            if(json instanceof Map){
                                Map map = (Map)json;
                                myCharityList.add((String)map.get("idNum"));
                            }
                        }
                    }
                    initListView(binding.charityPage.listView, allCharityList, myCharityList, false);
                }
            }
        };
        serverCommunicator.execute();
    }

    private void getAllProject(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(CharityAPIService.class).getAllProject()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!allProjectList.isEmpty()){
                    allProjectList.clear();
                }
                if(!myProjectList.isEmpty()){
                    myProjectList.clear();
                }
                if(results.get("allProject") instanceof List && results.get("myProject") instanceof List){
                    if(Integer.parseInt((String)results.get("allCount")) > 0){
                        Charity charity;
                        for(Object json : (List)results.get("allProject")){
                            if(json instanceof Map){
                                Map map = (Map)json;
                                String idNum = (String)map.get("idNum");
                                String name = (String)map.get("name");
                                String introduction = (String)map.get("introduction");
                                String appreciation = (String)map.get("appreciation");
                                String registration = (String)map.get("registration");
                                String follow = (String)map.get("follow");
                                String today = (String)map.get("today");
                                String goal = (String)map.get("goal");
                                String accumulation = (String)map.get("accumulation");
                                String imgAmount = (String)map.get("img");
                                charity = new Charity(idNum, name, introduction, appreciation, registration, follow, today, accumulation, imgAmount, goal);
                                allProjectList.add(charity);
                            }
                        }
                    }
                    if(Integer.parseInt((String)results.get("myCount")) > 0){
                        for(Object json : (List)results.get("myProject")){
                            if(json instanceof Map){
                                Map map = (Map)json;
                                String idNum = (String)map.get("idNum");
                                myProjectList.add(idNum);
                            }
                        }
                    }
                    initListView(binding.projectPage.listView, allProjectList, myProjectList, true);
                }
            }
        };
        serverCommunicator.execute();
    }

    private void modMyCharity(HashSet<String> addedList, HashSet<String> removedList, final boolean isProject){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(CharityAPIService.class).modMyCharity(addedList.toString(), removedList.toString())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "저장되었습니다.");
                if(isProject){
                    getAllProject();
                }else{
                    getAllCharity();
                }
            }
        };
        serverCommunicator.execute();
    }

    @Override
    public void refreshFragment() {
        getAllCharity();
        getAllProject();
    }

    private class OnSortButtonToggleListener implements CompoundButton.OnCheckedChangeListener{

        private ListView listView;

        public OnSortButtonToggleListener(ListView listView) {
            this.listView = listView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(listView.getAdapter() != null){
                if(isChecked){
                    if(listView.getAdapter() instanceof CharityListAdapter){
                        ((CharityListAdapter)listView.getAdapter()).popularSort();
                    }
                }else{
                    if(listView.getAdapter() instanceof CharityListAdapter){
                        ((CharityListAdapter)listView.getAdapter()).latestSort();
                    }
                }
            }
        }
    }

    private class OnFilterButtonToggleListener implements CompoundButton.OnCheckedChangeListener{

        private ListView listView;

        public OnFilterButtonToggleListener(ListView listView) {
            this.listView = listView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(listView.getAdapter() != null){
                if(isChecked){
                    if(listView.getAdapter() instanceof CharityListAdapter){
                        ((CharityListAdapter)listView.getAdapter()).showMyCharity();
                    }
                }else{
                    if(listView.getAdapter() instanceof CharityListAdapter){
                        ((CharityListAdapter)listView.getAdapter()).showAllCharity();
                    }
                }
            }
        }
    }

    private class OnSaveButtonClickListener implements View.OnClickListener{

        private ListView listView;
        boolean isProject;

        public OnSaveButtonClickListener(ListView listView, boolean isProject) {
            this.listView = listView;
            this.isProject = isProject;
        }

        @Override
        public void onClick(View v) {
            if(listView.getAdapter() instanceof CharityListAdapter){
                modMyCharity(((CharityListAdapter)listView.getAdapter()).getAddedCharityIdNum(), ((CharityListAdapter)listView.getAdapter()).getRemovedCharityIdNum(), isProject);
            }
        }
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener{

        private boolean isProject;

        public OnItemClickListener(boolean isProject) {
            this.isProject = isProject;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getAdapter() != null && parent.getAdapter().getItem(position) instanceof Charity){
                if(isProject){
                    ProjectDetailDialog.create((Charity)parent.getAdapter().getItem(position)).show(getFragmentManager(), "ProjectDetailDialog");
                }else{
                    CharityDetailDialog.create((Charity)parent.getAdapter().getItem(position)).show(getFragmentManager(), "CharityDetailDialog");
                }
            }
        }
    }
}
