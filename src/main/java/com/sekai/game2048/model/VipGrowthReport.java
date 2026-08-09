package com.sekai.game2048.model;

import java.util.ArrayList;
import java.util.List;

public class VipGrowthReport {

    private String headline;
    private String levelName;
    private Integer totalRuns;
    private Integer bestScore;
    private Integer bestTile;
    private Integer recentAverageScore;
    private Integer previousAverageScore;
    private Integer trendPercent;
    private Integer consistencyScore;
    private Integer efficiencyScore;
    private Integer nextTargetScore;
    private Integer nextTargetTile;
    private String primaryWeakness;
    private String nextAction;
    private List<String> insights = new ArrayList<>();
    private List<VipTrainingTask> tasks = new ArrayList<>();

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(Integer totalRuns) {
        this.totalRuns = totalRuns;
    }

    public Integer getBestScore() {
        return bestScore;
    }

    public void setBestScore(Integer bestScore) {
        this.bestScore = bestScore;
    }

    public Integer getBestTile() {
        return bestTile;
    }

    public void setBestTile(Integer bestTile) {
        this.bestTile = bestTile;
    }

    public Integer getRecentAverageScore() {
        return recentAverageScore;
    }

    public void setRecentAverageScore(Integer recentAverageScore) {
        this.recentAverageScore = recentAverageScore;
    }

    public Integer getPreviousAverageScore() {
        return previousAverageScore;
    }

    public void setPreviousAverageScore(Integer previousAverageScore) {
        this.previousAverageScore = previousAverageScore;
    }

    public Integer getTrendPercent() {
        return trendPercent;
    }

    public void setTrendPercent(Integer trendPercent) {
        this.trendPercent = trendPercent;
    }

    public Integer getConsistencyScore() {
        return consistencyScore;
    }

    public void setConsistencyScore(Integer consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    public Integer getEfficiencyScore() {
        return efficiencyScore;
    }

    public void setEfficiencyScore(Integer efficiencyScore) {
        this.efficiencyScore = efficiencyScore;
    }

    public Integer getNextTargetScore() {
        return nextTargetScore;
    }

    public void setNextTargetScore(Integer nextTargetScore) {
        this.nextTargetScore = nextTargetScore;
    }

    public Integer getNextTargetTile() {
        return nextTargetTile;
    }

    public void setNextTargetTile(Integer nextTargetTile) {
        this.nextTargetTile = nextTargetTile;
    }

    public String getPrimaryWeakness() {
        return primaryWeakness;
    }

    public void setPrimaryWeakness(String primaryWeakness) {
        this.primaryWeakness = primaryWeakness;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    public List<String> getInsights() {
        return insights;
    }

    public void setInsights(List<String> insights) {
        this.insights = insights;
    }

    public List<VipTrainingTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<VipTrainingTask> tasks) {
        this.tasks = tasks;
    }
}
