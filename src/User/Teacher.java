package User;

import static view.Homeview.teachers;

public class Teacher extends User{

    public Teacher() {
    }
    public Teacher(int id, String name,  char[] password) {
        super(id, name,password);
    }

    public static boolean FindTeacherByID(int ID){
        for (int i = 0; i < teachers.size(); i++) {
            if (ID==teachers.get(i).getId()){
                return true;
            }
        }
        return false;
    }
}
