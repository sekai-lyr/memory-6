package com.sekai.game2048.model;

import java.util.ArrayList;
import java.util.List;

public class VipCoachPlan {

    private List<String> steps = new ArrayList<>();
    private String route;
    private Integer totalGain;
    private Integer finalRisk;
    private Integer finalEmptyCells;
    private Integer finalMergePairs;
    private Integer finalMaxTile;
    private Integer evaluation;
    private String reason;

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Integer getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(Integer totalGain) {
        this.totalGain = totalGain;
    }

    public Integer getFinalRisk() {
        return finalRisk;
    }

    public void setFinalRisk(Integer finalRisk) {
        this.finalRisk = finalRisk;
    }

    public Integer getFinalEmptyCells() {
        return finalEmptyCells;
    }

    public void setFinalEmptyCells(Integer finalEmptyCells) {
        this.finalEmptyCells = finalEmptyCells;
    }

    public Integer getFinalMergePairs() {
        return finalMergePairs;
    }

    public void setFinalMergePairs(Integer finalMergePairs) {
        this.finalMergePairs = finalMergePairs;
    }

    public Integer getFinalMaxTile() {
        return finalMaxTile;
    }

    public void setFinalMaxTile(Integer finalMaxTile) {
        this.finalMaxTile = finalMaxTile;
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
