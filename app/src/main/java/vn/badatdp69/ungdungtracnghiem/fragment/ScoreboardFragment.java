package vn.badatdp69.ungdungtracnghiem.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import vn.badatdp69.ungdungtracnghiem.R;
import vn.badatdp69.ungdungtracnghiem.adapter.HighScoreAdapter;
import vn.badatdp69.ungdungtracnghiem.adapter.ScoreAdapter;
import vn.badatdp69.ungdungtracnghiem.adapter.SpinnerSubjectAdapter;
import vn.badatdp69.ungdungtracnghiem.common.Common;
import vn.badatdp69.ungdungtracnghiem.helper.IOHelper;
import vn.badatdp69.ungdungtracnghiem.model.Exam;
import vn.badatdp69.ungdungtracnghiem.model.HighScore;
import vn.badatdp69.ungdungtracnghiem.model.Score;
import vn.badatdp69.ungdungtracnghiem.model.Subject;

public class ScoreboardFragment extends Fragment {

    private LinearLayout layout_data_highscore, layout_hs_max, layout_hs_last;
    private Spinner spinnerSubject, spinnerExam;
    private TextView tvHSMax, tvHSDate, tvHSScoreLast, tvHSLastDate, tvHSStatus;
    private RecyclerView recycler_score;
    private TextView tvExamStatus;

    private ArrayList<Subject> mSubjects = new ArrayList<>();
    private ArrayList<Exam> mExams = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initFirebase();

        if (getActivity() != null) {
            layout_data_highscore = getActivity().findViewById(R.id.layout_data_highscore);
            layout_hs_max = getActivity().findViewById(R.id.layout_hs_max);
            layout_hs_last = getActivity().findViewById(R.id.layout_hs_last);
            tvHSStatus = getActivity().findViewById(R.id.tvHSStatus);
            spinnerSubject = getActivity().findViewById(R.id.spinnerSubject);
            tvHSMax = getActivity().findViewById(R.id.tvHSMax);
            tvHSDate = getActivity().findViewById(R.id.tvHSDate);
            tvHSScoreLast = getActivity().findViewById(R.id.tvHSLast);
            tvHSLastDate = getActivity().findViewById(R.id.tvHSLastDate);
            recycler_score = getActivity().findViewById(R.id.recycler_score);
            spinnerExam = getActivity().findViewById(R.id.spinnerExam);
            tvExamStatus = getActivity().findViewById(R.id.tv_exam_status);
        }

        setDataSpinnerSubject();
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject currentSubject = (Subject) parent.getItemAtPosition(position);
//                setDataHighScore(currentSubject);
                loadFirebaseDataInSubject(currentSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDataSpinnerSubject() {
        IOHelper helper = new IOHelper(getActivity());
        try {
            mSubjects = helper.getSubject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpinnerSubjectAdapter adapter = new SpinnerSubjectAdapter(getActivity(), mSubjects);
        spinnerSubject.setAdapter(adapter);
    }

    private void setDataHighScore(Subject subject) {
        if (subject.mSrcExam.equals("")) {
            setDataNull();
        } else {
            tvHSStatus.setVisibility(View.GONE);
            layout_data_highscore.setVisibility(View.VISIBLE);
            String rawName = subject.mSrcExam;
            SharedPreferences pref = getActivity().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            IOHelper helper = new IOHelper(getActivity());
            try {
                mExams = helper.getExam(Common.getId(rawName, R.raw.class));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ArrayList<ArrayList<HighScore>> mArrHighScores = new ArrayList<>();
            final ArrayList<HighScore> arrHighScoresMax = new ArrayList<>();
            final ArrayList<HighScore> arrHighScoresLast = new ArrayList<>();
            final String[] exams = new String[mExams.size()];
            boolean checkData = false;
            for (int i = 0; i < mExams.size(); i++) {
                exams[i] = "Đề số " + mExams.get(i).mExamNum;

                String key_prefs = rawName + "_" + mExams.get(i).mExamNum;
                Gson gson = new Gson();
                String json = pref.getString(key_prefs, null);
                Type type = new TypeToken<ArrayList<HighScore>>(){}.getType();
                ArrayList<HighScore> mHighScores = gson.fromJson(json, type);
                if (mHighScores == null) {
                    HighScore highScore = new HighScore(null, 0, 0, 0, 0, 0, 0);
                    mHighScores = new ArrayList<>();
                    mHighScores.add(highScore);
                } else {
                    checkData = true;
                }
                mArrHighScores.add(mHighScores);
                arrHighScoresMax.add(mHighScores.get(mHighScores.size()-1));
                arrHighScoresLast.add(mHighScores.get(0));
            }

            if (!checkData){
                setDataNull();
            } else {
                //Set dữ liệu điểm cao nhất của môn học
                Collections.sort(arrHighScoresMax, new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return Double.compare(o2.mDate, o1.mDate);
                    }
                });

                Collections.sort(arrHighScoresMax, new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return Double.compare(o2.mScore, o1.mScore);
                    }
                });
                tvHSMax.setText(String.valueOf(arrHighScoresMax.get(0).mScore));
                tvHSDate.setText(String.format("%s\n%s", Common.unixTimeToDate(arrHighScoresMax.get(0).mDate),
                        Common.unixTimeToTime(arrHighScoresMax.get(0).mDate)));
                layout_hs_max.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.showDialogHighScore(getActivity(), arrHighScoresMax, 0);
                    }
                });

                //Set dữ liệu lần thi gần nhất
                Collections.sort(arrHighScoresLast, new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return Double.compare(o2.mDate, o1.mDate);
                    }
                });
                tvHSScoreLast.setText(String.valueOf(arrHighScoresLast.get(0).mScore));
                tvHSLastDate.setText(String.format("%s\n%s", Common.unixTimeToDate(arrHighScoresLast.get(0).mDate),
                        Common.unixTimeToTime(arrHighScoresLast.get(0).mDate)));
                layout_hs_last.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.showDialogHighScore(getActivity(), arrHighScoresLast, 0);
                    }
                });
            }

            ArrayAdapter<String> adapterExam = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exams);
            adapterExam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerExam.setAdapter(adapterExam);
            spinnerExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<HighScore> arrHighScores = mArrHighScores.get(position);
                    ArrayList<HighScore> arrHighScoresExam = new ArrayList<>();
                    if (arrHighScores.size() > 1) {
                        for (int i = 0; i < arrHighScores.size() - 1; i++) {
                            arrHighScoresExam.add(arrHighScores.get(i));
                        }
                    } else {
                        arrHighScoresExam.addAll(arrHighScores);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recycler_score.setLayoutManager(layoutManager);

                    HighScoreAdapter adapter = new HighScoreAdapter(getActivity(), arrHighScoresExam);
                    recycler_score.setAdapter(adapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void setDataNull(){
        tvHSStatus.setVisibility(View.VISIBLE);
        layout_data_highscore.setVisibility(View.GONE);
        tvHSStatus.setText("Chưa có dữ liệu");
    }

    private ArrayList<Score> scoresSubject;
    private void loadFirebaseDataInSubject(final Subject subject){
        if (mAuth.getUid() != null){
            scoresSubject = new ArrayList<>();
            mRef.child(mAuth.getUid()).child(subject.mSrcExam).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            Score score = ds.getValue(Score.class);
                            scoresSubject.add(score);
                        }

                        Log.d("SCORE_", "Size: " + scoresSubject.size());
                        if (scoresSubject.size() == 0){
                            setDataNull();
                        } else {
                            tvHSStatus.setVisibility(View.GONE);
                            layout_data_highscore.setVisibility(View.VISIBLE);
                            //Hiển thị điểm cao nhất
                            Collections.sort(scoresSubject, new SortScoreByCorrect());
                            tvHSMax.setText(String.valueOf(scoresSubject.get(0).getScore()));
                            tvHSDate.setText(getDateTime(scoresSubject.get(0).getTime()));

                            final ArrayList<Score> scores1 = scoresSubject;
                            layout_hs_max.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Common.showScoreDialog(getActivity(), scores1, 0);
                                }
                            });

                            //Hiển thị lần thi gần nhất
                            Collections.sort(scoresSubject, new SortScoreByTime());
                            tvHSScoreLast.setText(String.valueOf(scoresSubject.get(0).getScore()));
                            tvHSLastDate.setText(getDateTime(scoresSubject.get(0).getTime()));

                            final ArrayList<Score> scores2 = scoresSubject;
                            layout_hs_last.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Common.showScoreDialog(getActivity(), scores2, 0);
                                }
                            });

                            //Hiển thị điểm thi theo đề
                            initExam(subject);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    //Hiển thị danh thị điểm theo đề thi
    private void initExam(final Subject subject){
        IOHelper helper = new IOHelper(getActivity());
        try {
            mExams = helper.getExam(Common.getId(subject.mSrcExam, R.raw.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String[] exams = new String[mExams.size()];
        for (int i = 0; i < mExams.size(); i++) {
            exams[i] = "Đề số " + mExams.get(i).mExamNum;
        }
        ArrayAdapter<String> adapterExam = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exams);
        adapterExam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExam.setAdapter(adapterExam);
        spinnerExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadFirebaseDataInExam(subject, position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private ArrayList<Score> scoresExam;
    private void loadFirebaseDataInExam(Subject subject, final int examNum){
        if (mAuth.getUid() != null){
            scoresExam = new ArrayList<>();
            mRef.child(mAuth.getUid()).child(subject.mSrcExam).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            Score score = ds.getValue(Score.class);

                            if (score.getExam() == examNum) {
                                scoresExam.add(score);
                            }

                            if (scoresExam.size() != 0){
                                tvExamStatus.setVisibility(View.GONE);
                            } else {
                                tvExamStatus.setVisibility(View.VISIBLE);
                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recycler_score.setLayoutManager(layoutManager);
                            ScoreAdapter adapter = new ScoreAdapter(getActivity(), scoresExam);
                            recycler_score.setAdapter(adapter);
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Score");
    }

    private static class SortScoreByCorrect implements Comparator<Score>{
        public int compare(Score a, Score b){
            return -(a.getCorrect() - b.getCorrect());
        }
    }

    private static class SortScoreByTime implements Comparator<Score>{
        public int compare(Score a, Score b){
            return (int) -(a.getTime() - b.getTime());
        }
    }

    public static String getDateTime(long unixTime) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm\ndd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        date.setTime(unixTime);
        return dateFormat.format(date);
    }
}
