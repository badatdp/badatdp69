package vn.badatdp69.ungdungtracnghiem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.common.Common;
import vn.badatdp69.ungdungtracnghiem.model.Subject;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ItemHolder> {

    private ArrayList<Subject> subjects;
    private OnItemListener listener;

    public SubjectsAdapter(ArrayList<Subject> subjects, OnItemListener listener) {
        this.subjects = subjects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_subject, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Subject subject = subjects.get(position);
        if (subject != null){
            holder.tvName.setText(subject.mName);
            holder.ivIcon.setImageResource(Common.getId(subject.mIcon, R.drawable.class));
        }
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public interface OnItemListener{
        void onItemClick(View v, int position);
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private ImageView ivIcon;
        private TextView tvName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.itemSjIcon);
            tvName = itemView.findViewById(R.id.itemSjName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
