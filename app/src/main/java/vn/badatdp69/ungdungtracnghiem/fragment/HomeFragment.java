package vn.badatdp69.ungdungtracnghiem.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;
import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.activity.ExamActivity;
import vn.badatdp69.ungdungtracnghiem.adapter.SubjectAdapter;
import vn.badatdp69.ungdungtracnghiem.adapter.SubjectsAdapter;
import vn.badatdp69.ungdungtracnghiem.helper.IOHelper;
import vn.badatdp69.ungdungtracnghiem.model.Subject;

public class HomeFragment extends Fragment {

    public static final String EXTRA_RAW_NAME = "extraRawID";
    public static final String EXTRA_STRING_ICON = "extraStringIcon";

    private ArrayList<Subject> mSubjects = new ArrayList<>();
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Hiển thị đồng hồ đếm ngược
        long currentTime = System.currentTimeMillis();
        //long timeInMillis = Common.timeToUnix(1596214800);
        long timeInMillis = 1594918800000L;


        if (getActivity() != null) {
            CountdownView countdownView = getActivity().findViewById(R.id.countdownView);
            countdownView.start(timeInMillis - currentTime);
            recyclerView = getActivity().findViewById(R.id.recycler_view);
        }
        IOHelper helper = new IOHelper(getActivity());
        try {
            mSubjects = helper.getSubject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SubjectsAdapter adapter = new SubjectsAdapter(mSubjects, new SubjectsAdapter.OnItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (mSubjects.get(position).mSrcExam.equals("")){
                    Toast.makeText(getActivity(), "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), ExamActivity.class);
                    intent.putExtra(EXTRA_RAW_NAME, mSubjects.get(position).mSrcExam);
                    intent.putExtra(EXTRA_STRING_ICON, mSubjects.get(position).mIcon);
                    startActivity(intent);
                }
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

    }
}
