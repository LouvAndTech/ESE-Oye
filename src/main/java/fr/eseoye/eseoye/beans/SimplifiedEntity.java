package fr.eseoye.eseoye.beans;

public class SimplifiedEntity {

    private final String secureID;

    private final String name;
    private final String surname;
    private final String phone;
    private final String mail;

    private Boolean isAdmin, isLocked;

    public SimplifiedEntity(String secureID, String name, String surname, String phone, String mail) {
        this.secureID = secureID;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.mail = mail;
    }

    public SimplifiedEntity(String name, String surname, String phone, String mail) {
        this(null, name, surname, phone, mail);
    }

    public SimplifiedEntity(String name, String surname, String secureID, Boolean isAdmin, Boolean isLocked, String phone, String mail) {
        this(secureID, name, surname, phone, mail);
        this.isAdmin = isAdmin;
        this.isLocked = isLocked;
    }


    public boolean isSecureIDPresent() {
        return secureID != null;
    }

    public String getSecureID() {
        return secureID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

}
