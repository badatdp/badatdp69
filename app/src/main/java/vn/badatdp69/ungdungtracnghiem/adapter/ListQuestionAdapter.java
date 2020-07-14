package vn.badatdp69.ungdungtracnghiem.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.activity.QuizActivity;
import vn.badatdp69.ungdungtracnghiem.common.Common;
import vn.badatdp69.ungdungtracnghiem.model.Question;

public class ListQuestionAdapter extends ArrayAdapter<Question> {

    public ListQuestionAdapter(@NonNull Context context, ArrayList<Question> arrQuestion) {
        super(context, 0, arrQuestion);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_question, parent, false);
        }

        TextView tvItemQuesNum = convertView.findViewById(R.id.tvItemQuesNum);
        TextView tvItemQuesChoice = convertView.findViewById(R.id.tvItemQuesChoice);
        TextView tvItemQuesTrue = convertView.findViewById(R.id.tvItemQuesTrue);

        final Question question = getItem(position);

        if (question != null) {

            tvItemQuesNum.setText(String.valueOf(position+1));
            tvItemQuesChoice.setText(Common.getChoiceText(question.mChoice));

            if (QuizActivity.checkResult){
                tvItemQuesTrue.setVisibility(View.VISIBLE);
                tvItemQuesTrue.setText(Common.getChoiceText(question.mAnsTrue));
                tvItemQuesChoice.setBackgroundResource(R.drawable.tv_circle_true);
                tvItemQuesTrue.setBackgroundResource(R.drawable.tv_circle_choice);
                if (question.mChoice == question.mAnsTrue){
                    tvItemQuesChoice.setBackgroundResource(R.drawable.tv_circle_choice);
                }
            }
        }

        return convertView;
    }
}
