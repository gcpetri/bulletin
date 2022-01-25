package com.tamudatathon.bulletin.service;

import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.data.repository.UserRepository;

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
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public User findUser(User user) {
        if (user == null) return null;
        return this.userRepository.findByAuthId(user.getAuthId());
    }
}
