package com.sekai.game2048.model;

import java.time.LocalDate;

public class VipDailyGift {

    private boolean alreadyClaimed;
    private Integer scoreBonus;
    private Integer boostBonus;
    private Integer scanBonus;
    private LocalDate rewardDate;

    public VipDailyGift() {
    }

    public VipDailyGift(boolean alreadyClaimed, Integer scoreBonus, Integer boostBonus,
                        Integer scanBonus, LocalDate rewardDate) {
        this.alreadyClaimed = alreadyClaimed;
        this.scoreBonus = scoreBonus;
        this.boostBonus = boostBonus;
        this.scanBonus = scanBonus;
        this.rewardDate = rewardDate;
    }

    public boolean isAlreadyClaimed() {
        return alreadyClaimed;
    }

    public void setAlreadyClaimed(boolean alreadyClaimed) {
        this.alreadyClaimed = alreadyClaimed;
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

    public LocalDate getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(LocalDate rewardDate) {
        this.rewardDate = rewardDate;
    }
}
