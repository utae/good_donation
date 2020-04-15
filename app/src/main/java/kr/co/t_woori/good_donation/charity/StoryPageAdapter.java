package kr.co.t_woori.good_donation.charity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.ImagePageBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;
import kr.co.t_woori.good_donation.utilities.ViewPagerIndicator;

/**
 * Created by rladn on 2017-09-08.
 */

public class StoryPageAdapter extends PagerAdapter {

    private Context context;
    private ViewPagerIndicator indicator;
    private HashMap<String, Drawable> imgHashMap;
    private Charity charity;

    public StoryPageAdapter(Context context, Charity charity, ViewPagerIndicator indicator) {
        this.context = context;
        this.charity = charity;
        this.indicator = indicator;
        this.imgHashMap = new HashMap<>();
        this.indicator.createDotPanel(Integer.parseInt(charity.getImgAmount()), R.drawable.ic_indicator_off, R.drawable.ic_indicator_on);
    }

    @Override
    public int getCount() {
        if("0".equals(charity.getImgAmount())){
            return 1;
        }else{
            return Integer.parseInt(charity.getImgAmount());
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImagePageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_page, null, false);
        if("0".equals(charity.getImgAmount())){
            binding.imageView.setImageResource(R.drawable.img_loading);
        }else{
            if(!imgHashMap.containsKey(Integer.toString(position))){
                Glide.with(context).asDrawable().load(Utilities.getCharityImgDir() + charity.getIdNum() + "_" + (position+1) + ".jpg").listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.imageView.setImageResource(R.drawable.img_loading);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imgHashMap.put(Integer.toString(position), resource);
                        return false;
                    }
                }).into(binding.imageView);
            }else{
                binding.imageView.setImageDrawable(imgHashMap.get(Integer.toString(position)));
            }
        }

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public OnPageChangeListener getOnPageChangeListener(){
        return new OnPageChangeListener(indicator);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    private class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener{

        private ViewPagerIndicator indicator;

        public OnPageChangeListener(ViewPagerIndicator indicator) {
            this.indicator = indicator;
        }

        @Override
        public void onPageSelected(int position) {
            indicator.selectDot(position);
        }
    }
}
