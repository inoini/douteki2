package model;

public class Post {

    private int id;
    private String name;
    private String message;
    private String snsUrl;
    private String discordName;
    private String snsUrl2;
    private String createdAt;
    private boolean isAdmin;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSnsUrl() {
        return snsUrl;
    }

    public void setSnsUrl(String snsUrl) {
        this.snsUrl = snsUrl;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }
    public String getSnsUrl2() {
        return snsUrl2;
    }

    public void setSnsUrl2(String snsUrl2) {
        this.snsUrl2 = snsUrl2;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}