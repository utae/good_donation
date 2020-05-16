package kr.co.t_woori.good_donation.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import android.view.Window;

import kr.co.t_woori.good_donation.R;

/**
 * Created by rladn on 2017-09-15.
 */

public class ProgressDialog extends Dialog {

    public ProgressDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setCanceledOnTouchOutside(false);

        setContentView(R.layout.progress_bar);

//        getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
    }
}
