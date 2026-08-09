package com.sekai.game2048.service.impl;

import com.sekai.game2048.dataobject.Sekai2048GameRecordDO;
import com.sekai.game2048.mapper.Sekai2048GameRecordMapper;
import com.sekai.game2048.model.GameDashboard;
import com.sekai.game2048.model.GameRecord;
import com.sekai.game2048.model.GameStats;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.SaveGameRequest;
import com.sekai.game2048.service.Game2048Service;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Game2048ServiceImpl implements Game2048Service {

    @Resource
    private Sekai2048GameRecordMapper gameRecordMapper;

    @Override
    public GameDashboard buildDashboard(Long userId) {
        GameDashboard dashboard = new GameDashboard();
        dashboard.setBestRecord(convert(gameRecordMapper.selectBestByUserId(userId)));
        dashboard.setMyRecords(convertList(gameRecordMapper.selectRecentByUserId(userId, 8)));
        dashboard.setLeaderboard(listLeaderboard());
        dashboard.setPlayCount(gameRecordMapper.countByUserId(userId));
        return dashboard;
    }

    @Override
    @Transactional
    public Result<GameRecord> saveRecord(Long userId, String nickName, SaveGameRequest request) {
        if (userId == null) {
            return Result.fail("请先登录");
        }
        if (request == null || request.getScore() == null || request.getMaxTile() == null) {
            return Result.fail("游戏数据不完整");
        }

        Sekai2048GameRecordDO record = new Sekai2048GameRecordDO();
        record.setUserId(userId);
        record.setNickName(nickName);
        record.setScore(Math.max(0, request.getScore()));
        record.setMaxTile(Math.max(2, request.getMaxTile()));
        record.setMaxCharacter(trimToDefault(request.getMaxCharacter(), "Mystery Character"));
        record.setMoveCount(defaultNumber(request.getMoveCount()));
        record.setDurationSeconds(defaultNumber(request.getDurationSeconds()));
        record.setBoardState(trimToDefault(request.getBoardState(), "[]"));

        int inserted = gameRecordMapper.insert(record);
        if (inserted <= 0 || record.getId() == null) {
            return Result.fail("保存失败");
        }
        return Result.ok(record.convertToModel(), "成绩已保存");
    }

    @Override
    public List<GameRecord> listLeaderboard() {
        return convertList(gameRecordMapper.selectLeaderboard(10));
    }

    @Override
    public GameStats getStats(Long userId) {
        GameStats stats = gameRecordMapper.selectStatsByUserId(userId);
        if (stats == null) {
            stats = new GameStats();
        }
        GameRecord best = convert(gameRecordMapper.selectBestByUserId(userId));
        if (best != null) {
            stats.setBestCharacter(best.getMaxCharacter());
        }
        String favorite = gameRecordMapper.selectFavoriteCharacterByUserId(userId);
        stats.setFavoriteCharacter(trimToDefault(favorite, "None yet"));
        if (stats.getBestCharacter() == null) {
            stats.setBestCharacter("None yet");
        }
        return stats;
    }

    @Override
    public GameRecord getBestRecord(Long userId) {
        return convert(gameRecordMapper.selectBestByUserId(userId));
    }

    @Override
    public List<GameRecord> listMyRecords(Long userId, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 50));
        return convertList(gameRecordMapper.selectRecentByUserId(userId, safeLimit));
    }

    private Integer defaultNumber(Integer value) {
        return value == null ? 0 : Math.max(0, value);
    }

    private String trimToDefault(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? defaultValue : trimmed;
    }

    private GameRecord convert(Sekai2048GameRecordDO recordDO) {
        return recordDO == null ? null : recordDO.convertToModel();
    }

    private List<GameRecord> convertList(List<Sekai2048GameRecordDO> records) {
        return records.stream().map(Sekai2048GameRecordDO::convertToModel).toList();
    }
}
