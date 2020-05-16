package kr.co.t_woori.good_donation.charity;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.CharityRowBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-14.
 */

public class CharityListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Charity> allCharityList;
    private ArrayList<String> myCharityList;
    private ArrayList<Charity> charityList;
    private HashMap<String, Drawable> imgHashMap;
    private HashSet<String> selectedCharitySet;
    private boolean isProject;
    private boolean showAll;
    private boolean latest;

    public CharityListAdapter(Context context, ArrayList<Charity> allCharityList, ArrayList<String> myCharityList, boolean isProject) {
        this.context = context;
        this.allCharityList = allCharityList;
        this.myCharityList = myCharityList;
        this.isProject = isProject;
        this.selectedCharitySet = new HashSet<>();
        this.charityList = new ArrayList<>();
        this.charityList.addAll(allCharityList);
        this.imgHashMap = new HashMap<>();
        this.showAll = true;
        this.latest = true;
        Utilities.clearGlideCache(context);
    }

    @Override
    public int getCount() {
        return charityList.size();
    }

    @Override
    public Object getItem(int position) {
        return charityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(charityList.get(position).getIdNum());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CharityRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.charity_row, parent, false);

//        if(convertView == null){
//            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.charity_row, parent, false);
//            convertView = binding.getRoot();
//            convertView.setTag(binding);
//        }else{
//            binding = (CharityRowBinding) convertView.getTag();
//        }
        final Charity charity = charityList.get(position);
        binding.nameTextView.setText(charity.getName());
        binding.followTextView.setText(charity.getFollow());
        if(myCharityList.contains(charity.getIdNum())){
            binding.followButton.setChecked(true);
        }else{
            binding.followButton.setChecked(false);
        }
        binding.followButton.setOnClickListener(new OnFollowButtonClickListener(charity));

        if("0".equals(charity.getImgAmount())){
            binding.imageView.setImageResource(R.drawable.img_loading);
        }else{
            if(!imgHashMap.containsKey(charity.getIdNum())){
                Glide.with(context).asDrawable().load(Utilities.getCharityImgDir() + charity.getIdNum() + "_1.jpg")
                        .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.imageView.setImageResource(R.drawable.img_loading);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imgHashMap.put(charity.getIdNum(), resource);
                        return false;
                    }
                }).into(binding.imageView);
            }else{
                binding.imageView.setImageDrawable(imgHashMap.get(charity.getIdNum()));
            }
        }

        if(isProject){
            int achievement = Integer.parseInt(charity.getAccumulation())*100 / Integer.parseInt(charity.getGoal());
            binding.achievementTextView.setText(achievement + "% 달성");
            binding.achievementTextView.setVisibility(View.VISIBLE);
        }
//        binding.introTextView.setText(charity.getIntroduction());
//        binding.registrationTextView.setText(charity.getRegistration());

//
//        if(isProject){
//            int progress = Integer.parseInt(charity.getAccumulation())*100 / Integer.parseInt(charity.getGoal());
//            binding.progressBar.setProgress(progress);
//            binding.progressTextView.setText(charity.getAccumulation() + " / " + charity.getGoal());
//            binding.progressContainer.setVisibility(View.VISIBLE);
//        }

        return binding.getRoot();
    }

    public HashSet<String> getAddedCharityIdNum() {
        HashSet<String> addedCharitySet = new HashSet<>();
        for(String charity : selectedCharitySet){
            if(!myCharityList.contains(charity)){
                addedCharitySet.add(charity);
            }
        }
        return addedCharitySet;
    }

    public HashSet<String> getRemovedCharityIdNum() {
        HashSet<String> removedCharitySet = new HashSet<>();
        for(String charity : myCharityList){
            if(!selectedCharitySet.contains(charity)){
                removedCharitySet.add(charity);
            }
        }
        return removedCharitySet;
    }

    public void latestSort(){
        Collections.sort(charityList, new LatestSort());
        Collections.sort(allCharityList, new LatestSort());
        notifyDataSetChanged();
        latest = true;
    }

    public void popularSort(){
        Collections.sort(charityList, new PopularSort());
        Collections.sort(allCharityList, new PopularSort());
        notifyDataSetChanged();
        latest = false;
    }

    public void showAllCharity(){
        if(!charityList.isEmpty()){
            charityList.clear();
        }
        charityList.addAll(allCharityList);
        notifyDataSetChanged();
        showAll = true;
    }

    public void showMyCharity(){
        if(!charityList.isEmpty()){
            charityList.clear();
        }
        for(Charity charity : allCharityList){
            if(myCharityList.contains(charity.getIdNum())){
                charityList.add(charity);
            }
        }
        notifyDataSetChanged();
        showAll = false;
    }

    private void followCharity(final Charity charity){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                context, APICreator.create(CharityAPIService.class).followCharity(charity.getIdNum())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                myCharityList.add(charity.getIdNum());
                charity.followPlus1();
                notifyDataSetChanged();
                Utilities.showToast(context, "내 기부처에 등록되었습니다.");
            }
        };
        serverCommunicator.execute();
    }

    private void unFollowCharity(final Charity charity){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                context, APICreator.create(CharityAPIService.class).unFollowCharity(charity.getIdNum())
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                myCharityList.remove(charity.getIdNum());
                charity.followMinus1();
                if(!showAll){
                    charityList.remove(charity);
                }
                notifyDataSetChanged();
                Utilities.showToast(context, "내 기부처에서 삭제되었습니다.");
            }
        };
        serverCommunicator.execute();
    }

    private class OnFollowButtonClickListener implements View.OnClickListener{

        private Charity charity;

        public OnFollowButtonClickListener(Charity charity) {
            this.charity = charity;
        }

        @Override
        public void onClick(View v) {
            if(v instanceof ToggleButton){
                ToggleButton followButton = (ToggleButton)v;
                if(followButton.isChecked()){
                    followCharity(charity);
                }else{
                    unFollowCharity(charity);
                }
            }
        }
    }

    private class OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

        private Charity charity;

        public OnCheckedChangeListener(Charity charity) {
            this.charity = charity;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                selectedCharitySet.add(charity.getIdNum());
            }else{
                selectedCharitySet.remove(charity.getIdNum());
            }
        }
    }

    private class LatestSort implements Comparator<Charity>{

        @Override
        public int compare(Charity o1, Charity o2) {
            return o2.getRegistration().compareTo(o1.getRegistration());
        }
    }

    private class PopularSort implements Comparator<Charity>{

        @Override
        public int compare(Charity o1, Charity o2) {
            return o2.getFollow().compareTo(o1.getFollow());
        }
    }
}
