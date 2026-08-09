package com.sekai.game2048.model;

public class VipTrainingTask {

    private String title;
    private String description;
    private Integer progress;
    private Integer target;
    private boolean completed;
    private String rewardHint;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getRewardHint() {
        return rewardHint;
    }

    public void setRewardHint(String rewardHint) {
        this.rewardHint = rewardHint;
    }
}
