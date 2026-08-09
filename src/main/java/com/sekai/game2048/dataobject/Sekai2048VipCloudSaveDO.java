package com.sekai.game2048.dataobject;

import com.sekai.game2048.model.VipCloudSave;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class Sekai2048VipCloudSaveDO {

    private Long id;
    private Long userId;
    private String slotName;
    private String runData;
    private Integer score;
    private Integer maxTile;
    private Integer moveCount;
    private Integer durationSeconds;
    private String mode;
    private Integer boardSize;
    private Integer targetTile;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public VipCloudSave convertToModel() {
        VipCloudSave cloudSave = new VipCloudSave();
        BeanUtils.copyProperties(this, cloudSave);
        return cloudSave;
    }

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

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getRunData() {
        return runData;
    }

    public void setRunData(String runData) {
        this.runData = runData;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(Integer boardSize) {
        this.boardSize = boardSize;
    }

    public Integer getTargetTile() {
        return targetTile;
    }

    public void setTargetTile(Integer targetTile) {
        this.targetTile = targetTile;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
