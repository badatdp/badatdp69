package vn.badatdp69.ungdungtracnghiem.model;

import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String mName;

    @SerializedName("icon")
    public String mIcon;

    @SerializedName("src_exam")
    public String mSrcExam;

    public Subject(String name, String icon, String srcExam) {
        this.mName = name;
        this.mIcon = icon;
        this.mSrcExam = srcExam;
    }

    public Subject(String id, String mName) {
        this.id = id;
        this.mName = mName;
    }
}
