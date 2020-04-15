package kr.co.t_woori.good_donation.inquiry;

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

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.InquiryDetailDialogBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-09-29.
 */

public class InquiryDetailDialog extends DialogFragment {

    private InquiryDetailDialogBinding binding;

    private String title, question, answer;

    public static InquiryDetailDialog create(Inquiry inquiry){
        InquiryDetailDialog inquiryDetailDialog = new InquiryDetailDialog();
        Bundle args = new Bundle();
        args.putString("title", inquiry.getTitle());
        args.putString("question", inquiry.getQuestion());
        args.putString("answer", inquiry.getAnswer());
        inquiryDetailDialog.setArguments(args);
        return inquiryDetailDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        question = getArguments().getString("question");
        answer = getArguments().getString("answer");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.inquiry_detail_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.titleTextView.setText(title);
        binding.questionTextView.setText(question);
        if(answer == null){
            binding.answerTextView.setText("등록된 답변이 없습니다.");
        }else{
            binding.answerTextView.setText(answer);
        }

        return binding.getRoot();
    }
}
