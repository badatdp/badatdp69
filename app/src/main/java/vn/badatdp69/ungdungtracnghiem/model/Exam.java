package vn.badatdp69.ungdungtracnghiem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Exam {

    @SerializedName("subject")
    public String mSubject;

    @SerializedName("exam_num")
    public int mExamNum;

    @SerializedName("time")
    public int mTime;

    @SerializedName("questions")
    public ArrayList<Question> mQuestions;

    public Exam(String subject, int examNum, int time, ArrayList<Question> questions) {
        this.mSubject = subject;
        this.mExamNum = examNum;
        this.mTime = time;
        this.mQuestions = questions;
    }
}
