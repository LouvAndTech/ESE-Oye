package fr.eseoye.eseoye.beans;

import java.sql.Date;

public class User extends SimplifiedEntity {

    private final String secureID;

    private final String password;
    private final Date birth;
    private final String address;
    private final int state;

    public User(String secureID, String name, String surname, String password, Date birth, String address, String phone, String mail, int state) {
        super(secureID, name, surname, phone, mail);
        this.secureID = secureID;
        this.password = password;
        this.birth = birth;
        this.address = address;
        this.state = state;
    }

    public String getSecureId() {
        return secureID;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public int getState() {
        return state;
    }
}
