package com.sekai.game2048.model;

import java.time.LocalDateTime;

public class GameRecord {

    private Long id;
    private Long userId;
    private String nickName;
    private Integer score;
    private Integer maxTile;
    private String maxCharacter;
    private Integer moveCount;
    private Integer durationSeconds;
    private String boardState;
    private String vipLevel;
    private LocalDateTime vipExpireTime;
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMaxTile() {
        return maxTile;
    }

    public void setMaxTile(Integer maxTile) {
        this.maxTile = maxTile;
    }

    public String getMaxCharacter() {
        return maxCharacter;
    }

    public void setMaxCharacter(String maxCharacter) {
        this.maxCharacter = maxCharacter;
    }

    public Integer getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(Integer moveCount) {
        this.moveCount = moveCount;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
