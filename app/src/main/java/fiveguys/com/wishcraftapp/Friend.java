package fiveguys.com.wishcraftapp;

public class Friend {
    private String name;
    private String email;

    public Friend() {
    }

    public Friend(String name) {
        this.name = name;
    }

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}