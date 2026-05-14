package model;



public class DM {

    private int id;
    private String roomId;
    private int senderId;
    private String message;
    private String createdAt;

    // id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // roomId
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    // senderId
    public int getSenderId() {
        return senderId;
    }
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    // message
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    // createdAt
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
}
}