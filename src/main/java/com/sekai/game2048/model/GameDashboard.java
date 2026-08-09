package com.sekai.game2048.model;

import java.util.List;

public class GameDashboard {

    private GameRecord bestRecord;
    private List<GameRecord> myRecords;
    private List<GameRecord> leaderboard;
    private long playCount;

    public GameRecord getBestRecord() {
        return bestRecord;
    }

    public void setBestRecord(GameRecord bestRecord) {
        this.bestRecord = bestRecord;
    }

    public List<GameRecord> getMyRecords() {
        return myRecords;
    }

    public void setMyRecords(List<GameRecord> myRecords) {
        this.myRecords = myRecords;
    }

    public List<GameRecord> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<GameRecord> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(long playCount) {
        this.playCount = playCount;
    }
}
