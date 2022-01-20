package com.tamudatathon.bulletin.service;

import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.data.repository.UserRepository;
import com.tamudatathon.bulletin.middleware.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public User findOrCreateUser(AuthResponse authResponse) {
        User user = this.userRepository.findByAuthId(authResponse.getAuthId());
        if (user == null) {
            user = this.createUser(authResponse);
        }
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public User createUser(AuthResponse authResponse) {
        User user = new User();
        user.setAuthId(authResponse.getAuthId());
        user.setFirstName(authResponse.getFirstName());
        user.setLastName(authResponse.getLastName());
        user.setEmail(authResponse.getEmail());
        user.setIsAdmin(authResponse.getIsAdmin());
        user.setDiscordInfo(authResponse.getDiscordInfo());
        this.userRepository.save(user);
        return user;
    }
}
