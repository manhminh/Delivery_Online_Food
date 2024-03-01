package com.backend.service;

import com.backend.model.User;

public interface UserService {

    User createAccount(User user);

    User findUserByEmail(String email);

    User findUserByJwtToken(String token);

}
