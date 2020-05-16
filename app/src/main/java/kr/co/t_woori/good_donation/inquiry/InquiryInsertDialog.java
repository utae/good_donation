package kr.co.t_woori.good_donation.inquiry;

import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.InquiryInsertDialogBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-09-27.
 */

public class InquiryInsertDialog extends DialogFragment {

    private InquiryInsertDialogBinding binding;

    private OnInsertQuestionListener onInsertQuestionListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.inquiry_insert_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFill(binding.titleTextView)){
                    Utilities.showToast(getContext(), "제목을 입력하세요.");
                }else if(!isFill(binding.questionTextView)){
                    Utilities.showToast(getContext(), "내용을 입력하세요.");
                }else{
                    insertQuestion(binding.titleTextView.getText().toString().trim(), binding.questionTextView.getText().toString().trim());
                }
            }
        });

        return binding.getRoot();
    }

    private boolean isFill(EditText editText){
        return !"".equals(editText.getText().toString().trim());
    }

    private void insertQuestion(String title, String question){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(InquiryAPIService.class).insertQuestion(title, question)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(getContext(), "문의글이 등록되었습니다.");
                if(onInsertQuestionListener != null){
                    onInsertQuestionListener.onInsertQuestion();
                }
                dismiss();
            }
        };
        serverCommunicator.execute();
    }

    public void setOnInsertQuestionListener(OnInsertQuestionListener onInsertQuestionListener) {
        this.onInsertQuestionListener = onInsertQuestionListener;
    }

    public interface OnInsertQuestionListener{
        void onInsertQuestion();
    }
}
