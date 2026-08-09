package com.sekai.game2048.model;

import java.util.List;

public class VipCoachRequest {

    private List<List<Integer>> board;
    private Integer score;
    private Integer moves;
    private Integer boardSize;
    private Integer targetTile;
    private String mode;

    public List<List<Integer>> getBoard() {
        return board;
    }

    public void setBoard(List<List<Integer>> board) {
        this.board = board;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getMoves() {
        return moves;
    }

    public void setMoves(Integer moves) {
        this.moves = moves;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
