package vn.badatdp69.ungdungtracnghiem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.common.Common;
import vn.badatdp69.ungdungtracnghiem.model.Score;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Score> scores;

    public ScoreAdapter(Context context, ArrayList<Score> scores) {
        this.mContext = context;
        this.scores = scores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Score score = scores.get(position);
        holder.tvItemSDate.setText(String.format("%s\n%s",
                Common.unixTimeToDate(score.getTime()),
                Common.unixTimeToTime(score.getTime())));
        holder.tvItemSScore.setText(String.valueOf(score.getScore()));

        holder.layout_item_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.showScoreDialog(mContext, scores, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemSDate, tvItemSScore, tvItemSClassification;
        LinearLayout layout_item_score;

        ViewHolder(View itemView) {
            super(itemView);
            tvItemSDate = itemView.findViewById(R.id.tvItemSDate);
            tvItemSScore = itemView.findViewById(R.id.tvItemSScore);
            tvItemSClassification = itemView.findViewById(R.id.tvItemSClassification);
            layout_item_score = itemView.findViewById(R.id.layout_item_score);
        }
    }
}
