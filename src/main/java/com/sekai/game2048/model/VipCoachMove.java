package com.sekai.game2048.model;

public class VipCoachMove {

    private String direction;
    private boolean legal;
    private Integer gain;
    private Integer emptyCells;
    private Integer mergePairs;
    private Integer maxTile;
    private Integer riskAfter;
    private Integer evaluation;
    private String reason;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isLegal() {
        return legal;
    }

    public void setLegal(boolean legal) {
        this.legal = legal;
    }

    public Integer getGain() {
        return gain;
    }

    public void setGain(Integer gain) {
        this.gain = gain;
    }

    public Integer getEmptyCells() {
        return emptyCells;
    }

    public void setEmptyCells(Integer emptyCells) {
        this.emptyCells = emptyCells;
    }

    public Integer getMergePairs() {
        return mergePairs;
    }

    public void setMergePairs(Integer mergePairs) {
        this.mergePairs = mergePairs;
    }

    public Integer getMaxTile() {
        return maxTile;
    }

    public void setMaxTile(Integer maxTile) {
        this.maxTile = maxTile;
    }

    public Integer getRiskAfter() {
        return riskAfter;
    }

    public void setRiskAfter(Integer riskAfter) {
        this.riskAfter = riskAfter;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
