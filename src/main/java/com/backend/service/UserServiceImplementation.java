package com.backend.service;

import com.backend.config.jwt.JwtProvider;
import com.backend.exception.UserException;
import com.backend.model.Cart;
import com.backend.model.User;
import com.backend.repository.CartRepository;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User createAccount(User user) {
        User isExistedUser = findUserByEmail(user.getEmail());
        if(isExistedUser != null) {
            throw new UserException("This is email is already used with another account");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());

        Cart cart = new Cart();
        cart.setCustomer(newUser);
        cartRepository.save(cart);

        return userRepository.save(newUser);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByJwtToken(String token) {
        String email = jwtProvider.getEmailFromJwt(token);
        User user = findUserByEmail(email);
        if(user == null) {
            throw new UserException("User not found");
        }
        return user;
    }
}
