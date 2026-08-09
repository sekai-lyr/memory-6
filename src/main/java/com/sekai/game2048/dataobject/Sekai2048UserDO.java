package com.sekai.game2048.dataobject;

import com.sekai.game2048.model.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class Sekai2048UserDO {

    private Long id;
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private String vipLevel;
    private LocalDateTime vipExpireTime;
    private LocalDateTime createTime;

    public Sekai2048UserDO() {
    }

    public Sekai2048UserDO(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public User convertToUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
