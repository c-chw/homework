package Java.bean;

public class Score {
    private String Course;
    private Double Score;

    public Score() {
    }

    public Score(String course, Double score) {
        Course = course;
        Score = score;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public Double getScore() {
        return Score;
    }

    public void setScore(Double score) {
        Score = score;
    }
}
