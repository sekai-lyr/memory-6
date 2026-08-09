package com.sekai.game2048.model;

public class VipCloudSaveRequest {

    private String slotName;
    private String runData;
    private Integer score;
    private Integer maxTile;
    private Integer moveCount;
    private Integer durationSeconds;
    private String mode;
    private Integer boardSize;
    private Integer targetTile;

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
}
