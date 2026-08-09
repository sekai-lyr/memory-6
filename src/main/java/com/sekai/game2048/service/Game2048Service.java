package com.sekai.game2048.service;

import com.sekai.game2048.model.GameDashboard;
import com.sekai.game2048.model.GameRecord;
import com.sekai.game2048.model.GameStats;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.SaveGameRequest;

import java.util.List;

public interface Game2048Service {

    GameDashboard buildDashboard(Long userId);

    Result<GameRecord> saveRecord(Long userId, String nickName, SaveGameRequest request);

    List<GameRecord> listLeaderboard();

    GameStats getStats(Long userId);

    GameRecord getBestRecord(Long userId);

    List<GameRecord> listMyRecords(Long userId, int limit);
}
