package vn.badatdp69.ungdungtracnghiem.model;

public class Score {

    private String subject;
    private int exam;
    private int total;
    private int answered;
    private int correct;
    private double score;
    private long time;

    public Score() {
    }

    public Score(String subject, int exam, int total, int answered, int correct, double score, long time) {
        this.subject = subject;
        this.exam = exam;
        this.total = total;
        this.answered = answered;
        this.correct = correct;
        this.score = score;
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
