package fiveguys.com.wishcraftapp;

public class Friend {
    private String name;
    private String email;
    private String uid;

    public Friend() {
    }

    public Friend(String name) {
        this.name = name;
    }

    public Friend(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
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

    public String getUid(){return uid;}

    //public void setUid(){this.uid = uid;}

}