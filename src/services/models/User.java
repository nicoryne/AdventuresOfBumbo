package services.models;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class User {

    private final int userId;

    private final String firstName;

    private final String lastName;

    private final String username;

    private final Date birthday;

    private final int age;

    public User(int userId, String firstName, String lastName, String username, Date birthday) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthday = birthday;
        this.age = convertBirthdayToAge(birthday);
    }

    private int convertBirthdayToAge(Date birthday) {
        java.util.Date utilDate = new java.util.Date(birthday.getTime());
        LocalDate birthDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public int getAge() {
        return age;
    }

    public int getUserId() {
        return userId;
    }
}
