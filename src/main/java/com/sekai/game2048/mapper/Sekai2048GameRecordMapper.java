package com.sekai.game2048.mapper;

import com.sekai.game2048.dataobject.Sekai2048GameRecordDO;
import com.sekai.game2048.model.GameStats;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Sekai2048GameRecordMapper {

    int insert(Sekai2048GameRecordDO record);

    Sekai2048GameRecordDO selectBestByUserId(Long userId);

    List<Sekai2048GameRecordDO> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    List<Sekai2048GameRecordDO> selectLeaderboard(@Param("limit") int limit);

    long countByUserId(Long userId);

    GameStats selectStatsByUserId(Long userId);

    String selectFavoriteCharacterByUserId(Long userId);
}
