package com.sekai.game2048.model;

import java.util.ArrayList;
import java.util.List;

public class VipCoachReport {

    private Integer risk;
    private Integer emptyCells;
    private Integer mergePairs;
    private Integer maxTile;
    private boolean maxTileInCorner;
    private String bestDirection;
    private String summary;
    private String advice;
    private String planSummary;
    private List<VipCoachMove> moves = new ArrayList<>();
    private List<VipCoachPlan> plans = new ArrayList<>();

    public Integer getRisk() {
        return risk;
    }

    public void setRisk(Integer risk) {
        this.risk = risk;
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

    public boolean isMaxTileInCorner() {
        return maxTileInCorner;
    }

    public void setMaxTileInCorner(boolean maxTileInCorner) {
        this.maxTileInCorner = maxTileInCorner;
    }

    public String getBestDirection() {
        return bestDirection;
    }

    public void setBestDirection(String bestDirection) {
        this.bestDirection = bestDirection;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getPlanSummary() {
        return planSummary;
    }

    public void setPlanSummary(String planSummary) {
        this.planSummary = planSummary;
    }

    public List<VipCoachMove> getMoves() {
        return moves;
    }

    public void setMoves(List<VipCoachMove> moves) {
        this.moves = moves;
    }

    public List<VipCoachPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<VipCoachPlan> plans) {
        this.plans = plans;
    }
}
