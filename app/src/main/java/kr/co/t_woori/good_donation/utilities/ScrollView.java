package kr.co.t_woori.good_donation.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

/**
 * Created by rladn on 2017-08-09.
 */

public class ScrollView extends android.widget.ScrollView {

    private OnSizeChangeListener onSizeChangeListener;

    public ScrollView(Context context) {
        super(context);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnSizeChangeListener(OnSizeChangeListener onSizeChangeListener) {
        this.onSizeChangeListener = onSizeChangeListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(onSizeChangeListener != null){
            if(w*h > oldw*oldh){
                onSizeChangeListener.onSizeChanged(false);
            }else{
                onSizeChangeListener.onSizeChanged(true);
            }
        }
    }

    public interface OnSizeChangeListener{
        void onSizeChanged(boolean smaller);
    }
}
