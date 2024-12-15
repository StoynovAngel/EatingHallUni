package dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Grade> grades;

    public User(String username, List<Grade> grades) {
        this.username = username;
        this.grades = grades;
    }

    public User(String username) {
        this(username, new ArrayList<>());
    }

    public String getUsername() {
        return username;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        StringBuilder gradesString = new StringBuilder();
        for (Grade grade : grades) {
            gradesString.append("\t\t").append(grade.toString()).append("\n");
        }

        return "User {\n" +
                "\tusername: '" + username + "',\n" +
                "\tgrades: [\n" + gradesString + "\t]\n" +
                "}";
    }
}
