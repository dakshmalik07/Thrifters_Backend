package com.thrifters.service;

import com.thrifters.config.JwtProvider;
import com.thrifters.exception.UserException;
import com.thrifters.model.User;
import com.thrifters.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    public User findUserById(Long userId)throws UserException{
        Optional<User>user = userRepository.findById(userId);
        if (user.isPresent()){
            return user.get();
        }

        throw new UserException("User not found with id: "+ userId);
    }

    public User findUserProfileByJwt(String jwt)throws UserException{
        String email = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new UserException("user not found with email "+email);
        }
        return user;
    }
}
