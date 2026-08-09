package com.sekai.game2048.mapper;

import com.sekai.game2048.dataobject.Sekai2048VipDailyGiftDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface Sekai2048VipDailyGiftMapper {

    int insert(Sekai2048VipDailyGiftDO gift);

    Sekai2048VipDailyGiftDO selectByUserIdAndRewardDate(@Param("userId") Long userId,
                                                        @Param("rewardDate") LocalDate rewardDate);
}
