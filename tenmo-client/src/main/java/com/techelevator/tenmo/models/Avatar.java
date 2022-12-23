package com.techelevator.tenmo.models;

public class Avatar {
    private int avatarId;
    private String avatarDesc;
    private String avatarLine1;
    private String avatarLine2;
    private String avatarLine3;
    private String avatarLine4;
    private String avatarLine5;

    public Avatar() {
    }

    public Avatar(int avatarId, String avatarDesc, String avatarLine1, String avatarLine2, String avatarLine3, String avatarLine4, String avatarLine5) {
        this.avatarId = avatarId;
        this.avatarDesc = avatarDesc;
        this.avatarLine1 = avatarLine1;
        this.avatarLine2 = avatarLine2;
        this.avatarLine3 = avatarLine3;
        this.avatarLine4 = avatarLine4;
        this.avatarLine5 = avatarLine5;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public String getAvatarDesc() {
        return avatarDesc;
    }

    public void setAvatarDesc(String avatarDesc) {
        this.avatarDesc = avatarDesc;
    }

    public String getAvatarLine1() {
        return avatarLine1;
    }

    public void setAvatarLine1(String avatarLine1) {
        this.avatarLine1 = avatarLine1;
    }

    public String getAvatarLine2() {
        return avatarLine2;
    }

    public void setAvatarLine2(String avatarLine2) {
        this.avatarLine2 = avatarLine2;
    }

    public String getAvatarLine3() {
        return avatarLine3;
    }

    public void setAvatarLine3(String avatarLine3) {
        this.avatarLine3 = avatarLine3;
    }

    public String getAvatarLine4() {
        return avatarLine4;
    }

    public void setAvatarLine4(String avatarLine4) {
        this.avatarLine4 = avatarLine4;
    }

    public String getAvatarLine5() {
        return avatarLine5;
    }

    public void setAvatarLine5(String avatarLine5) {
        this.avatarLine5 = avatarLine5;
    }
}
