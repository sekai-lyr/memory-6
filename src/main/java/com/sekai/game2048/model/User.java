package com.sekai.game2048.model;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private String vipLevel;
    private LocalDateTime vipExpireTime;
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public LocalDateTime getVipExpireTime() {
        return vipExpireTime;
    }

    public void setVipExpireTime(LocalDateTime vipExpireTime) {
        this.vipExpireTime = vipExpireTime;
    }

    public boolean isVipActive() {
        return vipLevel != null && !vipLevel.isBlank()
                && vipExpireTime != null
                && vipExpireTime.isAfter(LocalDateTime.now());
    }

    public String getVipLabel() {
        return isVipActive() ? vipLevel : "FREE";
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
