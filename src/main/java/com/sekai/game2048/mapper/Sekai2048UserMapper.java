package com.sekai.game2048.mapper;

import com.sekai.game2048.dataobject.Sekai2048UserDO;
import org.apache.ibatis.annotations.Param;

public interface Sekai2048UserMapper {

    Sekai2048UserDO selectByPrimaryKey(Long id);

    Sekai2048UserDO selectByUserName(String userName);

    Sekai2048UserDO selectByLoginName(@Param("loginName") String loginName);

    int insert(Sekai2048UserDO user);

    int updateByPrimaryKeySelective(Sekai2048UserDO user);
}
