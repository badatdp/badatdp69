package vn.badatdp69.ungdungtracnghiem.adapter;

import android.content.Context;
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

public class SubjectAdapter extends ArrayAdapter<Subject> {
    public SubjectAdapter(@NonNull Context context, ArrayList<Subject> subjects) {
        super(context, 0, subjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_subject, parent, false);

            TextView tvName = convertView.findViewById(R.id.itemSjName);
            ImageView ivIcon = convertView.findViewById(R.id.itemSjIcon);

            Subject subject = getItem(position);

            if (subject != null){
                tvName.setText(subject.mName);
                ivIcon.setImageResource(Common.getId(subject.mIcon, R.drawable.class));
            }
        }
        return convertView;
    }
}
