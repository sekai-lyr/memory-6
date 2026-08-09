package com.sekai.game2048.mapper;

import com.sekai.game2048.dataobject.Sekai2048VipCloudSaveDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Sekai2048VipCloudSaveMapper {

    int upsert(Sekai2048VipCloudSaveDO cloudSave);

    long countByUserId(Long userId);

    Sekai2048VipCloudSaveDO selectByUserIdAndSlotName(@Param("userId") Long userId,
                                                      @Param("slotName") String slotName);

    Sekai2048VipCloudSaveDO selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    List<Sekai2048VipCloudSaveDO> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
