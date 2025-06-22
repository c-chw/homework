package Java.bean;

import java.util.*;

public class Student {
    private int id;
    private String name;
    private String password;
    private List<Score> scores = new ArrayList<>();

    {
        scores.add(new Score("Java", 0.0));
        scores.add(new Score("Math", 0.0));
        scores.add(new Score("English", 0.0));
    }

    public Student() {
    }

    public Student(int id, String name, String password, ArrayList<Score> scores) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.scores = scores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }

}
