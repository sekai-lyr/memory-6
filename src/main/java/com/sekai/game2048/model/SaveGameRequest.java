package com.sekai.game2048.model;

public class SaveGameRequest {

    private Integer score;
    private Integer maxTile;
    private String maxCharacter;
    private Integer moveCount;
    private Integer durationSeconds;
    private String boardState;

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
}
