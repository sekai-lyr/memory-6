package com.sekai.game2048.service.impl;

import com.sekai.game2048.dataobject.Sekai2048UserDO;
import com.sekai.game2048.mapper.Sekai2048UserMapper;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;
import com.sekai.game2048.service.UserService;
import com.sekai.game2048.util.PasswordHashUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private Sekai2048UserMapper userMapper;

    @Override
    @Transactional
    public Result<User> register(User user) {
        if (user == null) {
            return Result.fail("用户信息不能为空");
        }

        user.setUserName(trimToNull(user.getUserName()));
        user.setPassword(trimToNull(user.getPassword()));
        user.setNickName(trimToNull(user.getNickName()));

        if (user.getUserName() == null) {
            return Result.fail("用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return Result.fail("密码至少需要 6 位");
        }
        if (userMapper.selectByUserName(user.getUserName()) != null) {
            return Result.fail("用户名已存在");
        }
        if (user.getNickName() == null) {
            user.setNickName(user.getUserName());
        }
        if (user.getAvatar() == null) {
            user.setAvatar("/images/game2048-login.png");
        }

        user.setPassword(PasswordHashUtil.hash(user.getPassword()));
        Sekai2048UserDO userDO = new Sekai2048UserDO(user);
        int inserted = userMapper.insert(userDO);
        if (inserted <= 0 || userDO.getId() == null) {
            return Result.fail("注册失败，请稍后再试");
        }
        return Result.ok(userMapper.selectByPrimaryKey(userDO.getId()).convertToUser(), "注册成功");
    }

    @Override
    @Transactional
    public Result<User> login(String userName, String password) {
        userName = trimToNull(userName);
        password = trimToNull(password);
        if (userName == null || password == null) {
            return Result.fail("用户名和密码不能为空");
        }

        Sekai2048UserDO dbUser = userMapper.selectByLoginName(userName);
        if (dbUser == null) {
            return Result.fail("用户不存在，请先注册");
        }
        if (!PasswordHashUtil.matches(password, dbUser.getPassword())) {
            return Result.fail("密码不正确");
        }
        if (!PasswordHashUtil.isHashed(dbUser.getPassword())) {
            dbUser.setPassword(PasswordHashUtil.hash(password));
            userMapper.updateByPrimaryKeySelective(dbUser);
        }
        return Result.ok(dbUser.convertToUser(), "登录成功");
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
