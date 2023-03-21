package fr.eseoye.eseoye.beans;

public class Admin extends SimplifiedEntity {

    private final String password;
    private final int rank;

    public Admin(String secureID, String name, String surname, String password, int rank) {
        super(secureID, name, surname, null, null);
        this.password = password;
        this.rank = rank;
    }

    public String getPassword() {
        return password;
    }

    public int getRank() {
        return rank;
    }

}
