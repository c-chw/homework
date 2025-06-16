package Java.bean;

import java.util.*;

public class Student {
    private int id;
    private String name;
    private String password;
    private ArrayList<Score> scores = new ArrayList<>();

    {
        scores.add(new Score("Java", null));
        scores.add(new Score("Math", null));
        scores.add(new Score("English", null));
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

    public ArrayList<Score> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Score> scores) {
        this.scores = scores;
    }

}
