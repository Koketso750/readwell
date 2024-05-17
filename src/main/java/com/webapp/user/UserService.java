package com.webapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired private UserRepository userRepo;

    public void save(User user){
        userRepo.save(user);
    }


    public User getLoginUser(String email, String password){
        User user = userRepo.findByEmailAndPassword(email, password);

        return user;
    }

    public User getUserAttibutesAfterLogIn(String email){
        User user = userRepo.findByEmail(email);

        return user;
    }

    public void deleteAccount(User user){
        userRepo.delete(user);
    }
}
