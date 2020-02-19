package masters.fmi.uni.besilka;

import java.io.Serializable;

public class Word implements Serializable {

    private int id;
    private String usersender;
    private String userreceiver;
    private String word;
    private String status;

    public Word(String usersender, String userreceiver, String status) {
        this.usersender = usersender;
        this.userreceiver = userreceiver;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsersender() {
        return usersender;
    }

    public void setUsersender(String usersender) {
        this.usersender = usersender;
    }

    public String getUserreceiver() {
        return userreceiver;
    }

    public void setUserreceiver(String userreceiver) {
        this.userreceiver = userreceiver;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
