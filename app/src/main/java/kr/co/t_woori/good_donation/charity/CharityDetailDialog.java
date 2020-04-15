package kr.co.t_woori.good_donation.charity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.CharityDetailDialogBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-25.
 */

public class CharityDetailDialog extends DialogFragment {

    private CharityDetailDialogBinding binding;

    private Charity charity;
    private TabHost tabHost;

    static public CharityDetailDialog create(Charity charity){
        CharityDetailDialog charityDetailDialog = new CharityDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("charity", charity);
        charityDetailDialog.setArguments(args);
        return charityDetailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        charity = (Charity) getArguments().getSerializable("charity");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.charity_detail_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.titleTextView.setText(charity.getName());

        tabHost = binding.tabHost;

        tabHost.setup();

        addTab("story tab", R.id.story_page, "스토리");
        addTab("info tab", R.id.info_page, "기부처 정보");

        for(int i = 0; i < tabHost.getTabWidget().getChildCount(); i++){
            ((TextView)((ViewGroup) tabHost.getTabWidget().getChildTabViewAt(i)).getChildAt(1)).setTextColor(Color.BLACK);
        }

        initStoryPage();

        initInfoPage();

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return binding.getRoot();
    }

    private void initStoryPage(){
        StoryPageAdapter storyAdapter = new StoryPageAdapter(getContext(), charity, binding.storyPage.viewPagerIndicator);
        binding.storyPage.viewPager.setAdapter(storyAdapter);
        binding.storyPage.viewPager.addOnPageChangeListener(storyAdapter.getOnPageChangeListener());
    }

    private void initInfoPage(){
        binding.infoPage.introTextView.setText(charity.getIntroduction());

        binding.infoPage.accumTextView.setText(Utilities.convertStringToNumberFormat(charity.getAccumulation()));

        binding.infoPage.followTextView.setText(charity.getFollow());

        binding.infoPage.todayTextView.setText(charity.getToday());

        binding.infoPage.registrationTextView.setText(Utilities.convertStringToDateFormat(charity.getRegistration()));
    }

    private void addTab(String tag, @IdRes int contentId, String indicator){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setContent(contentId);
        tabSpec.setIndicator(indicator);
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getDialog() == null){
            return;
        }
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
