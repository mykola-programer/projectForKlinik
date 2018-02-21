package ua.nike.project.struct;

import java.util.Objects;

public class Human {
    private final Integer uid;
    private final String surname;
    private final String firstName;
    private final String secondName;
    private final String allName;
    private final Character sex;
    private final String status;
    private final String telephone;

    public Human(Integer uid, String surname, String firstName, String secondName, Character sex, String status, String telephone) {
        this.uid = uid;
        this.surname = firstUpperCase(surname);
        this.firstName = firstUpperCase(firstName);
        this.secondName = firstUpperCase(secondName);
        this.allName = this.surname + " " + this.firstName + " " + this.secondName;
        this.sex = sex;
        this.status = status.toLowerCase();
        this.telephone = telephone;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getAllName() {
        return allName;
    }

    public Character getSex() {
        return sex;
    }

    public String getStatus() {
        return status;
    }

    public String getTelephone() {
        return telephone;
    }


    /**
     * Create first point to Upper Case. Another case is Lower Case.
     *
     * @param word - це будь яке слово типу String, яке треба вивести з великої букви
     * @return String - повертає теж саме слово, але з великої букви.
     *         Якщо переданий пареметр пуста строка, чи пуста ссилка, то метод повертає пусту строку.
     */
    private String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    /**
     *
     * @param o - обєкт, який потрібно порівняти з поточним.
     * @return Якщо обєкти зсилають на один і той же обєкт,
     *         якщо обєкти є обєктами Human,
     *         якщо у обектів рівні поля(surname,firstName,secondName),
     *         то метод повертає true.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Human)) return false;
        Human human = (Human) o;
        return Objects.equals(surname, human.surname) &&
                Objects.equals(firstName, human.firstName) &&
                Objects.equals(secondName, human.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, firstName, secondName);
    }

    @Override
    public String toString() {
        return "Human{" +
                "Індекс=" + uid +
                ", Прізвище='" + surname + '\'' +
                ", Ім'я='" + firstName + '\'' +
                ", По-батькові='" + secondName + '\'' +
                ", Стать=" + sex +
                ", Статус='" + status + '\'' +
                ", Телефон='" + telephone + '\'' +
                '}';
    }
}
