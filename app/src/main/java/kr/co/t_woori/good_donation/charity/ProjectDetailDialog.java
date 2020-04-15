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
import kr.co.t_woori.good_donation.databinding.ProjectDetailDialogBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-25.
 */

public class ProjectDetailDialog extends DialogFragment {

    private ProjectDetailDialogBinding binding;

    private Charity project;
    private TabHost tabHost;

    static public ProjectDetailDialog create(Charity project){
        ProjectDetailDialog charityDetailDialog = new ProjectDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("project", project);
        charityDetailDialog.setArguments(args);
        return charityDetailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project = (Charity) getArguments().getSerializable("project");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_detail_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.titleTextView.setText(project.getName());

        tabHost = binding.tabHost;

        tabHost.setup();

        addTab("story tab", R.id.story_page, "스토리");
        addTab("info tab", R.id.info_page, "프로젝트 정보");

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
        StoryPageAdapter storyAdapter = new StoryPageAdapter(getContext(), project, binding.storyPage.viewPagerIndicator);
        binding.storyPage.viewPager.setAdapter(storyAdapter);
        binding.storyPage.viewPager.addOnPageChangeListener(storyAdapter.getOnPageChangeListener());
    }

    private void initInfoPage(){
        binding.infoPage.introTextView.setText(project.getIntroduction());

        int progress = (Integer.parseInt(project.getAccumulation())*100)/Integer.parseInt(project.getGoal());

        binding.infoPage.progressBar.setProgress(progress);

        SpannableStringBuilder builder = new SpannableStringBuilder(progress+"%");

        builder.setSpan(new AbsoluteSizeSpan(14, true), builder.length()-1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.infoPage.progressTextView.setText(builder);

        binding.infoPage.accumTextView.setText(Utilities.convertStringToNumberFormat(project.getAccumulation()));

        binding.infoPage.goalTextView.setText(Utilities.convertStringToNumberFormat(project.getGoal()));

        binding.infoPage.followTextView.setText(project.getFollow());

        binding.infoPage.todayTextView.setText(project.getToday());

        binding.infoPage.registrationTextView.setText(Utilities.convertStringToDateFormat(project.getRegistration()));
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
