package User;

public class Score {
    private double Chinese;
    private double Math;
    private double English;

    public Score() {
    }

    public Score(double chinese, double math, double english) {
        Chinese = chinese;
        Math = math;
        English = english;
    }

    public double getChinese() {
        return Chinese;
    }

    public void setChinese(double chinese) {
        Chinese = chinese;
    }

    public double getMath() {
        return Math;
    }

    public void setMath(double math) {
        Math = math;
    }

    public double getEnglish() {
        return English;
    }

    public void setEnglish(double english) {
        English = english;
    }
}
