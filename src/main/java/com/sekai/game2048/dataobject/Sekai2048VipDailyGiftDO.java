package com.sekai.game2048.dataobject;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Sekai2048VipDailyGiftDO {

    private Long id;
    private Long userId;
    private LocalDate rewardDate;
    private Integer scoreBonus;
    private Integer boostBonus;
    private Integer scanBonus;
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

    public LocalDate getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(LocalDate rewardDate) {
        this.rewardDate = rewardDate;
    }

    public Integer getScoreBonus() {
        return scoreBonus;
    }

    public void setScoreBonus(Integer scoreBonus) {
        this.scoreBonus = scoreBonus;
    }

    public Integer getBoostBonus() {
        return boostBonus;
    }

    public void setBoostBonus(Integer boostBonus) {
        this.boostBonus = boostBonus;
    }

    public Integer getScanBonus() {
        return scanBonus;
    }

    public void setScanBonus(Integer scanBonus) {
        this.scanBonus = scanBonus;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
