package kr.co.t_woori.good_donation.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.charity.Charity;
import kr.co.t_woori.good_donation.charity.CharityDetailDialog;
import kr.co.t_woori.good_donation.charity.ProjectDetailDialog;
import kr.co.t_woori.good_donation.databinding.ImagePageBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;
import kr.co.t_woori.good_donation.utilities.ViewPagerIndicator;

/**
 * Created by rladn on 2017-09-08.
 */

public class HomeBannerAdapter extends PagerAdapter {

    private Context context;
    private FragmentManager fragmentManager;
    private ViewPagerIndicator indicator;
    private int imgAmount;
    private HashMap<Integer, Drawable> imgHashMap;
    private View.OnClickListener onHomeBannerClickListener;


    public HomeBannerAdapter(Context context, FragmentManager fragmentManager, ViewPagerIndicator indicator, int imgAmount) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.indicator = indicator;
        this.indicator.createDotPanel(imgAmount, R.drawable.ic_indicator_off, R.drawable.ic_indicator_on);
        this.imgAmount = imgAmount;
        this.imgHashMap = new HashMap<>();
        Utilities.clearGlideCache(context);
    }

    @Override
    public int getCount() {
        return imgAmount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImagePageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_page, null, false);

        if(!imgHashMap.containsKey(position)){
            Glide.with(context).asDrawable().load(Utilities.getHomeBannerDir() + (position+1) + ".jpg").listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    binding.imageView.setImageResource(R.drawable.img_loading);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    imgHashMap.put(position, resource);
                    return false;
                }
            }).into(binding.imageView);
        }else{
            binding.imageView.setImageDrawable(imgHashMap.get(position));
        }
        binding.imageView.setOnClickListener(onHomeBannerClickListener);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    public OnBannerChangeListener getOnBannerChangeListener(){
        return new OnBannerChangeListener(indicator);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class OnBannerChangeListener extends ViewPager.SimpleOnPageChangeListener{

        private ViewPagerIndicator indicator;

        public OnBannerChangeListener(ViewPagerIndicator indicator) {
            this.indicator = indicator;
        }

        @Override
        public void onPageSelected(int position) {
            indicator.selectDot(position);
        }
    }

    public void setOnHomeBannerClickListener(View.OnClickListener onHomeBannerClickListener) {
        this.onHomeBannerClickListener = onHomeBannerClickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
