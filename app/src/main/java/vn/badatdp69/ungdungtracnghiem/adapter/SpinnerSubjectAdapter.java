package vn.badatdp69.ungdungtracnghiem.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.common.Common;
import vn.badatdp69.ungdungtracnghiem.model.Subject;

public class SpinnerSubjectAdapter extends ArrayAdapter<Subject> {

    public SpinnerSubjectAdapter(Context context, ArrayList<Subject> subjects){
        super(context, 0, subjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.subject_spiner_row, parent, false
            );
        }

        ImageView ivSubjectSpinner = convertView.findViewById(R.id.ivSubjectSpinner);
        TextView tvSubjectSpinner = convertView.findViewById(R.id.tvSubjectSpinner);
        Subject currentSubject = getItem(position);
        if (currentSubject != null) {
            ivSubjectSpinner.setImageResource(Common.getId(currentSubject.mIcon, R.drawable.class));
            ivSubjectSpinner.setColorFilter(Color.BLUE);
            tvSubjectSpinner.setText(currentSubject.mName);
            tvSubjectSpinner.setTextColor(Color.BLUE);
        }
        return convertView;
    }
}
