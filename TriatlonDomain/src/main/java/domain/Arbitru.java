package domain;

public class Arbitru extends Entity<Long>{
    private String username;
    private String pasword;
    private String name;

    public Arbitru() { }

    public Arbitru(String username, String pasword, String name) {
        this.username = username;
        this.pasword = pasword;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPasword() {
        return pasword;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
