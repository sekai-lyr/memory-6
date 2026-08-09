package com.sekai.game2048.mapper;

import com.sekai.game2048.dataobject.Sekai2048MonetizationLeadDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Sekai2048MonetizationLeadMapper {

    int insert(Sekai2048MonetizationLeadDO lead);

    List<Sekai2048MonetizationLeadDO> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    List<Sekai2048MonetizationLeadDO> selectRecent(@Param("limit") int limit);
}
