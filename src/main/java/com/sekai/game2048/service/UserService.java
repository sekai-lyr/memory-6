package com.sekai.game2048.service;

import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.User;

public interface UserService {

    Result<User> register(User user);

    Result<User> login(String userName, String password);
}
