package User;

import java.util.Arrays;

public class User extends Password{
    private int id;
    private String name;
    char[] password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password=" + Arrays.toString(password) +
                '}';
    }

    public User() {
    }

    public User(int id, String name, char[] password) {
        this.id = id;
        this.name = name;
        this.password = password;
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
}
