package com.sekai.game2048.model;

public class GameStats {

    private long totalRuns;
    private long totalMoves;
    private long totalSeconds;
    private int bestScore;
    private int averageScore;
    private int bestTile;
    private String bestCharacter;
    private String favoriteCharacter;

    public long getTotalRuns() { return totalRuns; }
    public void setTotalRuns(long totalRuns) { this.totalRuns = totalRuns; }
    public long getTotalMoves() { return totalMoves; }
    public void setTotalMoves(long totalMoves) { this.totalMoves = totalMoves; }
    public long getTotalSeconds() { return totalSeconds; }
    public void setTotalSeconds(long totalSeconds) { this.totalSeconds = totalSeconds; }
    public int getBestScore() { return bestScore; }
    public void setBestScore(int bestScore) { this.bestScore = bestScore; }
    public int getAverageScore() { return averageScore; }
    public void setAverageScore(int averageScore) { this.averageScore = averageScore; }
    public int getBestTile() { return bestTile; }
    public void setBestTile(int bestTile) { this.bestTile = bestTile; }
    public String getBestCharacter() { return bestCharacter; }
    public void setBestCharacter(String bestCharacter) { this.bestCharacter = bestCharacter; }
    public String getFavoriteCharacter() { return favoriteCharacter; }
    public void setFavoriteCharacter(String favoriteCharacter) { this.favoriteCharacter = favoriteCharacter; }
}
